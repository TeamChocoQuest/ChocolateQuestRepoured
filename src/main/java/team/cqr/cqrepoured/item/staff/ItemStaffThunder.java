package team.cqr.cqrepoured.item.staff;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (this.isNotAirBlock(worldIn, playerIn)) {
			if (!worldIn.isClientSide) {
				playerIn.swing(handIn);
				this.spawnLightningBolt(playerIn, worldIn);
				stack.hurtAndBreak(1, playerIn, p -> p.broadcastBreakEvent(handIn));
				playerIn.getCooldowns().addCooldown(stack.getItem(), 20);
			}
			return InteractionResultHolder.success(stack);
		}

		return InteractionResultHolder.fail(stack);
	}

	//#TODO RayTraceResult needs tests
	public void spawnLightningBolt(Player player, Level worldIn) {
		if (!worldIn.isClientSide) {
			Vec3 start = player.getEyePosition(1.0F);
			Vec3 end = start.add(player.getLookAngle().scale(20.0D));
			HitResult result = worldIn.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

			if (result != null) {
				EntityColoredLightningBolt entity = new EntityColoredLightningBolt(worldIn, result.getLocation().x, result.getLocation().y, result.getLocation().z, true, false, 0.0F, 0.0F, 0.75F, 0.25F);
				worldIn.addFreshEntity(entity);
			}
		}
	}

	public boolean isNotAirBlock(Level worldIn, Player player) {
		Vec3 start = player.getEyePosition(1.0F);
		Vec3 end = start.add(player.getLookAngle().scale(20.0D));
		HitResult result = worldIn.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

		return result != null && !worldIn.isEmptyBlock(BlockPos.containing(result.getLocation()));
	}

	@Override
	public void shoot(Level worldIn, LivingEntity shooter, Entity target, InteractionHand handIn) {
		Vec3 v = target.position().subtract(shooter.position());
		Vec3 pos = target.position();
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
