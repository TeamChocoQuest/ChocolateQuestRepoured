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
import team.cqr.cqrepoured.entity.projectiles.ProjectileVampiricSpell;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaffVampiric extends ItemLore implements IRangedWeapon {

	public ItemStaffVampiric(Properties properties)
	{
		super(properties.durability(2048));
		//this.setMaxDamage(2048);
		//this.setMaxStackSize(1);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		this.shoot(stack, worldIn, playerIn, handIn);
		return InteractionResultHolder.success(stack);
	}

	public void shoot(ItemStack stack, Level worldIn, Player player, InteractionHand handIn) {
		worldIn.playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.ENDER_PEARL_THROW, SoundSource.MASTER, 4.0F, (1.0F + (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F) * 0.7F, false);
		player.swing(handIn);

		if (!worldIn.isClientSide) {
			ProjectileVampiricSpell spell = new ProjectileVampiricSpell(player, worldIn);
			spell.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 0F);
			worldIn.addFreshEntity(spell);
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(handIn));
			player.getCooldowns().addCooldown(stack.getItem(), 20);
		}
	}

	@Override
	public void shoot(Level worldIn, LivingEntity shooter, Entity target, InteractionHand handIn) {
		shooter.swing(handIn);

		if (!worldIn.isClientSide) {
			ProjectileVampiricSpell spell = new ProjectileVampiricSpell(shooter, worldIn);
			Vec3 v = target.position().subtract(shooter.position());
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
