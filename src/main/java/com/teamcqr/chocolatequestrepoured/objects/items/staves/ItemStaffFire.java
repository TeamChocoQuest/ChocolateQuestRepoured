package com.teamcqr.chocolatequestrepoured.objects.items.staves;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockUnlitTorch;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		this.changeTorch(worldIn);
		stack.damageItem(1, playerIn);
		playerIn.getCooldownTracker().setCooldown(stack.getItem(), 20);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	public void changeTorch(World worldIn) {
		RayTraceResult result = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(10D, 1.0F);

		if (result != null && !worldIn.isRemote) {
			BlockPos pos = new BlockPos(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ());
			IBlockState blockStateLookingAt = worldIn.getBlockState(pos);

			if (blockStateLookingAt.getBlock() == ModBlocks.UNLIT_TORCH) {
				worldIn.setBlockState(pos, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, blockStateLookingAt.getValue(BlockUnlitTorch.FACING)));
			}
		}
	}

	public void shootFromEntity(EntityLivingBase shooter) {
		World world = shooter.world;

		float x = (float) -Math.sin(Math.toRadians(shooter.rotationYaw));
		float z = (float) Math.cos(Math.toRadians(shooter.rotationYaw));
		float y = (float) -Math.sin(Math.toRadians(shooter.rotationPitch));
		x *= 1.0F - Math.abs(y);
		z *= 1.0F - Math.abs(y);

		if(world.isRemote) {
			for (int i = 0; i < 50; i++) {
				double flameRandomMotion = itemRand.nextDouble() + 0.2D;
				float height = shooter.height;
				world.spawnParticle(EnumParticleTypes.FLAME, true, shooter.posX, shooter.posY + height, shooter.posZ, (x + (itemRand.nextDouble() - 0.5D) / 3.0D) * flameRandomMotion,
						(y + (itemRand.nextDouble() - 0.5D) / 3.0D) * flameRandomMotion, (z + (itemRand.nextDouble() - 0.5D) / 3.0D) * flameRandomMotion);
			}
		}

		if (!world.isRemote) {
			int dist = 15;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(shooter, shooter.getEntityBoundingBox().grow(shooter.getLookVec().x * dist, shooter.getLookVec().y * dist, shooter.getLookVec().z * dist).expand(1.0D, 1.0D, 1.0D));

			for (Entity e : list) {
				if (e instanceof EntityLivingBase) {
					double rotDiff = Math.abs(this.getAngleBetweenEntities(shooter, e));
					double rot = rotDiff - Math.abs(MathHelper.wrapDegrees(shooter.rotationYaw));
					rot = Math.abs(rot);

					if (rot < 10.0D) {
						if (shooter.canEntityBeSeen(e)) {
							e.setFire(6);
							e.attackEntityFrom(DamageSource.IN_FIRE, 4.0F);
						}
					}
				}
			}
		}
	}

	public double getAngleBetweenEntities(Entity attacker, Entity target) {
		double d = attacker.posX - target.posX;
		double d2 = attacker.posZ - target.posZ;
		double angle = Math.atan2(d, d2);
		angle = angle * 180.0D / 3.141592D;

		angle = -MathHelper.wrapDegrees(angle - 180.0D);

		return angle;
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

}