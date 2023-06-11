package team.cqr.cqrepoured.item.staff;

import org.joml.Vector3d;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		this.shoot(worldIn, playerIn, stack, handIn);
		return ActionResult.success(playerIn.getItemInHand(handIn));
	}

	public void shoot(World worldIn, PlayerEntity playerIn, ItemStack stack, Hand handIn) {
		worldIn.playLocalSound(playerIn.position().x, playerIn.position().y, playerIn.position().z, SoundEvents.SLIME_SQUISH, SoundCategory.MASTER, 4.0F, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F, false);
		playerIn.swing(handIn);

		if (!worldIn.isClientSide) {
			ProjectileSpiderBall ball = new ProjectileSpiderBall(playerIn, worldIn);
			ball.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 1.5F, 0F);
			worldIn.addFreshEntity(ball);
			stack.hurtAndBreak(1, playerIn, p -> p.broadcastBreakEvent(handIn));
			playerIn.getCooldowns().addCooldown(stack.getItem(), 20);
		}
	}

	@Override
	public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		shooter.swing(handIn);

		if (!worldIn.isClientSide) {
			ProjectileSpiderBall ball = new ProjectileSpiderBall(shooter, worldIn);
			Vector3d v = target.position().subtract(shooter.position());
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
