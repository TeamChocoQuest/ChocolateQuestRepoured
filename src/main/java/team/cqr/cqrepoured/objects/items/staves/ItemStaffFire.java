package team.cqr.cqrepoured.objects.items.staves;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.util.IRangedWeapon;

public class ItemStaffFire extends Item implements IRangedWeapon {

	public ItemStaffFire() {
		this.setMaxStackSize(1);
		this.setMaxDamage(2048);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		boolean flag = super.hitEntity(stack, target, attacker);

		if (flag && itemRand.nextInt(5) == 0) {
			if (target.getRidingEntity() != null) {
				target.dismountRidingEntity();
			}
		}
		return flag;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.swingArm(handIn);
		this.shootFromEntity(playerIn);
		this.changeTorch(worldIn, playerIn);
		stack.damageItem(1, playerIn);
		playerIn.getCooldownTracker().setCooldown(stack.getItem(), 20);
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	public void changeTorch(World worldIn, EntityPlayer player) {
		Vec3d start = player.getPositionEyes(1.0F);
		Vec3d end = start.add(player.getLookVec().scale(10.0D));
		RayTraceResult result = worldIn.rayTraceBlocks(start, end);

		if (result != null && !worldIn.isRemote) {
			BlockPos pos = new BlockPos(result.hitVec);
			IBlockState blockStateLookingAt = worldIn.getBlockState(pos);

			if (blockStateLookingAt.getBlock() == CQRBlocks.UNLIT_TORCH) {
				worldIn.setBlockState(pos, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, blockStateLookingAt.getValue(BlockTorch.FACING)));
			}
		}
	}

	public void shootFromEntity(EntityLivingBase shooter) {
		World world = shooter.world;

		if (!world.isRemote) {
			Random r = shooter.getRNG();
			for (int i = 0; i < 20; i++) {
				// TODO don't send 20 packets
				Vec3d v = shooter.getLookVec();
				v = v.add((r.nextFloat() - 0.5D) / 3.0D, (r.nextFloat() - 0.5D) / 3.0D, (r.nextFloat() - 0.5D) / 3.0D);
				((WorldServer) world).spawnParticle(EnumParticleTypes.FLAME, shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, 0, v.x, v.y,
						v.z, r.nextFloat() + 0.2D);
			}

			world.getEntitiesWithinAABB(EntityLivingBase.class, shooter.getEntityBoundingBox().grow(8.0D), entity -> {
				if (TargetUtil.isAllyCheckingLeaders(shooter, entity)) {
					return false;
				}

				double x = MathHelper.clamp(shooter.posX, entity.posX - entity.width * 0.5D, entity.posX + entity.width * 0.5D) - shooter.posX;
				double y = MathHelper.clamp(shooter.posY + shooter.getEyeHeight(), entity.posY, entity.posY + entity.height)
						- (shooter.posY + shooter.getEyeHeight());
				double z = MathHelper.clamp(shooter.posZ, entity.posZ - entity.width * 0.5D, entity.posZ + entity.width * 0.5D) - shooter.posZ;
				if (x * x + y * y + z * z > 8.0D * 8.0D) {
					return false;
				}

				Vec3d a = shooter.getLookVec();
				Vec3d b = new Vec3d(x, y, z);
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.staff_fire.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public void shoot(World worldIn, EntityLivingBase shooter, Entity target, EnumHand handIn) {
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
