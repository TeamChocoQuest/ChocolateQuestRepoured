package team.cqr.cqrepoured.item.staff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.projectiles.ProjectileVampiricSpell;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaffVampiric extends ItemLore implements IRangedWeapon {

	public ItemStaffVampiric(Properties properties)
	{
		super(properties.durability(2048).stacksTo(1));
		//this.setMaxDamage(2048);
		//this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		this.shoot(stack, worldIn, playerIn, handIn);
		return ActionResult.success(stack);
	}

	public void shoot(ItemStack stack, World worldIn, PlayerEntity player, Hand handIn) {
		worldIn.playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.ENDER_PEARL_THROW, SoundCategory.MASTER, 4.0F, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F, false);
		player.swing(handIn);

		if (!worldIn.isClientSide) {
			ProjectileVampiricSpell spell = new ProjectileVampiricSpell(worldIn, player);
			spell.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 2.0F, 0F);
			worldIn.addFreshEntity(spell);
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(handIn));
			player.getCooldowns().addCooldown(stack.getItem(), 20);
		}
	}

	@Override
	public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		shooter.swing(handIn);

		if (!worldIn.isClientSide) {
			ProjectileVampiricSpell spell = new ProjectileVampiricSpell(worldIn, shooter);
			Vector3d v = target.position().subtract(shooter.position());
			v = v.normalize();
			v = v.scale(0.75D);
			spell.setDeltaMovement(v);
			// spell.setVelocity(v.x, v.y, v.z);
			//spell.motionX = v.x;
			//spell.motionY = v.y;
			//spell.motionZ = v.z;
			//spell.velocityChanged = true;
			worldIn.addFreshEntity(spell);
		}
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundEvents.ENDER_PEARL_THROW;
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
