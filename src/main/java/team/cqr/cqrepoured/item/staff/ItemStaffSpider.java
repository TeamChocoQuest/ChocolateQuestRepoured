package team.cqr.cqrepoured.item.staff;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.projectiles.ProjectileSpiderBall;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaffSpider extends ItemLore implements IRangedWeapon {

	public ItemStaffSpider(Properties properties)
	{
		super(properties.durability(2048));
		//this.setMaxDamage(2048);
		//this.setMaxStackSize(1);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		this.shoot(worldIn, playerIn, stack, handIn);
		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}

	public void shoot(Level worldIn, Player playerIn, ItemStack stack, InteractionHand handIn) {
		worldIn.playLocalSound(playerIn.position().x, playerIn.position().y, playerIn.position().z, SoundEvents.SLIME_SQUISH, SoundSource.MASTER, 4.0F, (1.0F + (playerIn.getRandom().nextFloat() - playerIn.getRandom().nextFloat()) * 0.2F) * 0.7F, false);
		playerIn.swing(handIn);

		if (!worldIn.isClientSide) {
			ProjectileSpiderBall ball = new ProjectileSpiderBall(playerIn, worldIn);
			ball.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.5F, 0F);
			worldIn.addFreshEntity(ball);
			stack.hurtAndBreak(1, playerIn, p -> p.broadcastBreakEvent(handIn));
			playerIn.getCooldowns().addCooldown(stack.getItem(), 20);
		}
	}

	@Override
	public void shoot(Level worldIn, LivingEntity shooter, Entity target, InteractionHand handIn) {
		shooter.swing(handIn);

		if (!worldIn.isClientSide) {
			ProjectileSpiderBall ball = new ProjectileSpiderBall(shooter, worldIn);
			Vec3 v = target.position().subtract(shooter.position());
			v = v.normalize();
			v = v.scale(0.5D);
			ball.setDeltaMovement(v);
			// ball.setVelocity(v.x, v.y, v.z);
			//ball.motionX = v.x;
			//ball.motionY = v.y;
			//ball.motionZ = v.z;
			///ball.velocityChanged = true;
			worldIn.addFreshEntity(ball);
		}
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundEvents.SLIME_SQUISH;
	}

	@Override
	public double getRange() {
		return 32.0D;
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
