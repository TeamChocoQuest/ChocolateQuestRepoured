package team.cqr.cqrepoured.item.staff;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaffGun extends ItemLore implements IRangedWeapon {

	public ItemStaffGun(Properties properties)
	{
		super(properties.durability(2048));
		//this.setMaxDamage(2048);
		//this.setMaxStackSize(1);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		this.shootStaff(worldIn, playerIn, stack, handIn);
		return InteractionResultHolder.success(stack);
	}

	public void shootStaff(Level worldIn, Player player, ItemStack stack, InteractionHand handIn) {
		worldIn.playLocalSound(player.position().x, player.position().y, player.position().z, CQRSounds.GUN_SHOOT, SoundSource.MASTER, 4.0F, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F, false);

		if (!worldIn.isClientSide) {
			ProjectileCannonBall ball = new ProjectileCannonBall(player, worldIn, false);
			ball.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 3.5F, 0F);
			worldIn.addFreshEntity(ball);
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(handIn));
			player.getCooldowns().addCooldown(stack.getItem(), 20);
		}
	}

	@Override
	public void shoot(Level worldIn, LivingEntity shooter, Entity target, InteractionHand handIn) {
		if (!worldIn.isClientSide) {
			ProjectileCannonBall ball = new ProjectileCannonBall(shooter, worldIn, false);
			Vec3 v = target.position().subtract(shooter.position());
			v = v.normalize();
			v = v.scale(3.5D);
			ball.setDeltaMovement(v);
			//ball.setDeltaMovement();
			// ball.setVelocity(v.x, v.y, v.z);
			//ball.set
			//ball.getDeltaMovement(). = v.x;
			//ball.motionY = v.y;
			//ball.motionZ = v.z;
			//ball.velocityChanged = true; //Dont need I guess
			worldIn.addFreshEntity(ball);
		}
	}

	@Override
	public SoundEvent getShootSound() {
		return CQRSounds.GUN_SHOOT;
	}

	@Override
	public double getRange() {
		return 32.0D;
	}

	@Override
	public int getCooldown() {
		return 50;
	}

	@Override
	public int getChargeTicks() {
		return 0;
	}

}
