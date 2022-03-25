package team.cqr.cqrepoured.item.staff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaffGun extends ItemLore implements IRangedWeapon {

	public ItemStaffGun(Properties properties)
	{
		super(properties.durability(2048).stacksTo(1));
		//this.setMaxDamage(2048);
		//this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		this.shootStaff(worldIn, playerIn, stack, handIn);
		return ActionResult.success(stack);
	}

	public void shootStaff(World worldIn, PlayerEntity player, ItemStack stack, Hand handIn) {
		worldIn.playLocalSound(player.position().x, player.position().y, player.position().z, CQRSounds.GUN_SHOOT, SoundCategory.MASTER, 4.0F, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F, false);

		if (!worldIn.isClientSide) {
			ProjectileCannonBall ball = new ProjectileCannonBall(player, worldIn, false);
			ball.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 3.5F, 0F);
			worldIn.addFreshEntity(ball);
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(handIn));
			player.getCooldowns().addCooldown(stack.getItem(), 20);
		}
	}

	@Override
	public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		if (!worldIn.isClientSide) {
			ProjectileCannonBall ball = new ProjectileCannonBall(shooter, worldIn, false);
			Vector3d v = target.position().subtract(shooter.position());
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
