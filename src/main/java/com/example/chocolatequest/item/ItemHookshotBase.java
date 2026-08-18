package com.example.chocolatequest.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import com.example.chocolatequest.entity.projectile.ProjectileHookShotHook;
import com.example.chocolatequest.registry.ModSounds;

public abstract class ItemHookshotBase extends Item {

	public ItemHookshotBase(Properties props) {
		super(props.stacksTo(1));
	}

	public boolean canLatchToBlock(BlockState state) {
		return !state.isAir() && state.getFluidState().isEmpty();
	}

	public abstract double getHookRange();

	public abstract ProjectileHookShotHook getNewHookEntity(Level worldIn, LivingEntity shooter, ItemStack stack);

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		this.shoot(stack, worldIn, playerIn);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	public void shoot(ItemStack stack, Level worldIn, Player player) {
		if (!worldIn.isClientSide) {
			ProjectileHookShotHook hookEntity = this.getNewHookEntity(worldIn, player, stack);
			hookEntity.shootHook(player, this.getHookRange(), 1.8D);
			worldIn.addFreshEntity(hookEntity);
			com.example.chocolatequest.registry.ModItems.applySharedCooldown(player, this, getCooldown());
			stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
			worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), getShootSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
		}
	}

	public ProjectileHookShotHook entityAIshoot(Level worldIn, LivingEntity shooter, Entity target, InteractionHand handIn) {
		if (!worldIn.isClientSide) {
			ProjectileHookShotHook hookEntity = this.getNewHookEntity(worldIn, shooter, shooter.getItemInHand(handIn));
			Vec3 v = target.position().subtract(shooter.position());
			hookEntity.shootHook(shooter, v.x, v.y, v.z, this.getHookRange(), 1.8D);
			worldIn.addFreshEntity(hookEntity);
			return hookEntity;
		}
		return null;
	}

	public SoundEvent getShootSound() {
		return ModSounds.GUN_SHOOT.get(); // fallback
	}

	public double getRange() {
		return 16.0D;
	}

	public int getCooldown() {
		return 30;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
}
