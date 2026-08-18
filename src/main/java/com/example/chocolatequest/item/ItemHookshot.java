package com.example.chocolatequest.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.example.chocolatequest.entity.projectile.ProjectileHookShotHook;

public class ItemHookshot extends ItemHookshotBase {
	public ItemHookshot(Properties properties) {
		super(properties);
	}
	@Override
	public double getHookRange() { return 20.0; }
	@Override
	public int getCooldown() { return 30; }
	@Override
	public ProjectileHookShotHook getNewHookEntity(Level worldIn, LivingEntity player, ItemStack stack) {
		return new ProjectileHookShotHook(worldIn, player, this, stack);
	}
}
