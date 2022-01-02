package team.cqr.cqrepoured.item.staff;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.input.Keyboard;

import net.minecraft.block.TorchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.item.IRangedWeapon;

public class ItemStaffFire extends Item implements IRangedWeapon {

	public ItemStaffFire() {
		this.setMaxStackSize(1);
		this.setMaxDamage(2048);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean flag = super.hitEntity(stack, target, attacker);

		if (flag && itemRand.nextInt(5) == 0) {
			if (target.getRidingEntity() != null) {
				target.dismountRidingEntity();
			}
		}
		return flag;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.swingArm(handIn);
		this.shootFromEntity(playerIn);
		this.changeTorch(worldIn, playerIn);
		stack.damageItem(1, playerIn);
		playerIn.getCooldownTracker().setCooldown(stack.getItem(), 20);
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	public void changeTorch(World worldIn, PlayerEntity player) {
		Vector3d start = player.getPositionEyes(1.0F);
		Vector3d end = start.add(player.getLookVec().scale(10.0D));
		RayTraceResult result = worldIn.rayTraceBlocks(start, end);

		if (result != null && !worldIn.isRemote) {
			BlockPos pos = new BlockPos(result.hitVec);
			BlockState blockStateLookingAt = worldIn.getBlockState(pos);

			if (blockStateLookingAt.getBlock() == CQRBlocks.UNLIT_TORCH) {
				worldIn.setBlockState(pos, Blocks.TORCH.getDefaultState().withProperty(TorchBlock.FACING, blockStateLookingAt.getValue(TorchBlock.FACING)));
			}
		}
	}

	public void shootFromEntity(LivingEntity shooter) {
		World world = shooter.world;

		if (!world.isRemote) {
			Random r = shooter.getRNG();
			for (int i = 0; i < 20; i++) {
				// TODO don't send 20 packets
				Vector3d v = shooter.getLookVec();
				v = v.add((r.nextFloat() - 0.5D) / 3.0D, (r.nextFloat() - 0.5D) / 3.0D, (r.nextFloat() - 0.5D) / 3.0D);
				((ServerWorld) world).spawnParticle(ParticleTypes.FLAME, shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, 0, v.x, v.y, v.z, r.nextFloat() + 0.2D);
			}

			world.getEntitiesWithinAABB(LivingEntity.class, shooter.getEntityBoundingBox().grow(8.0D), entity -> {
				if (TargetUtil.isAllyCheckingLeaders(shooter, entity)) {
					return false;
				}

				double x = MathHelper.clamp(shooter.posX, entity.posX - entity.width * 0.5D, entity.posX + entity.width * 0.5D) - shooter.posX;
				double y = MathHelper.clamp(shooter.posY + shooter.getEyeHeight(), entity.posY, entity.posY + entity.height) - (shooter.posY + shooter.getEyeHeight());
				double z = MathHelper.clamp(shooter.posZ, entity.posZ - entity.width * 0.5D, entity.posZ + entity.width * 0.5D) - shooter.posZ;
				if (x * x + y * y + z * z > 8.0D * 8.0D) {
					return false;
				}

				Vector3d a = shooter.getLookVec();
				Vector3d b = new Vector3d(x, y, z);
				if (Math.toDegrees(Math.acos(a.dotProduct(b) / (a.length() * b.length()))) > 40.0D) {
					return false;
				}

				return shooter.canEntityBeSeen(entity);
			}).forEach(target -> {
				if (target.attackEntityFrom(DamageSource.causeMobDamage(shooter).setFireDamage(), 3.0F)) {
					target.setFire(5);
				}
			});
		}
	}

	@Override
	@Dist(OnlyIn.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.staff_fire.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		this.shootFromEntity(shooter);
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundEvents.ENTITY_GHAST_SHOOT;
	}

	@Override
	public double getRange() {
		return 7.5D;
	}

	@Override
	public int getCooldown() {
		return 60;
	}

	@Override
	public int getChargeTicks() {
		return 0;
	}

}
