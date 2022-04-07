package team.cqr.cqrepoured.item.staff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaffThunder extends ItemLore implements IRangedWeapon {

	public ItemStaffThunder(Properties properties)
	{
		super(properties.durability(2048));
		//this.setMaxDamage(2048);
		//this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (this.isNotAirBlock(worldIn, playerIn)) {
			if (!worldIn.isClientSide) {
				playerIn.swing(handIn);
				this.spawnLightningBolt(playerIn, worldIn);
				stack.hurtAndBreak(1, playerIn, p -> p.broadcastBreakEvent(handIn));
				playerIn.getCooldowns().addCooldown(stack.getItem(), 20);
			}
			return ActionResult.success(stack);
		}

		return ActionResult.fail(stack);
	}

	//#TODO RayTraceResult needs tests
	public void spawnLightningBolt(PlayerEntity player, World worldIn) {
		if (!worldIn.isClientSide) {
			Vector3d start = player.getEyePosition(1.0F);
			Vector3d end = start.add(player.getLookAngle().scale(20.0D));
			RayTraceResult result = worldIn.clip(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));

			if (result != null) {
				EntityColoredLightningBolt entity = new EntityColoredLightningBolt(worldIn, result.getLocation().x, result.getLocation().y, result.getLocation().z, true, false, 0.0F, 0.0F, 0.75F, 0.25F);
				worldIn.addFreshEntity(entity);
			}
		}
	}

	public boolean isNotAirBlock(World worldIn, PlayerEntity player) {
		Vector3d start = player.getEyePosition(1.0F);
		Vector3d end = start.add(player.getLookAngle().scale(20.0D));
		RayTraceResult result = worldIn.clip(new RayTraceContext(start, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));

		return result != null && !worldIn.isEmptyBlock(new BlockPos(result.getLocation().x, result.getLocation().y, result.getLocation().z));
	}

	@Override
	public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		Vector3d v = target.position().subtract(shooter.position());
		Vector3d pos = target.position();
		if (v.length() > 20) {
			v = v.normalize();
			v = v.scale(20D);
			pos = shooter.position().add(v);
		}
		EntityColoredLightningBolt entity = new EntityColoredLightningBolt(worldIn, pos.x, pos.y, pos.z, true, false);
		worldIn.addFreshEntity(entity);
	}

	@Override
	public SoundEvent getShootSound() {
		return CQRSounds.MAGIC;
	}

	@Override
	public double getRange() {
		return 32.0D;
	}

	@Override
	public int getCooldown() {
		return 80;
	}

	@Override
	public int getChargeTicks() {
		return 0;
	}

}
