package com.example.chocolatequest.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.example.chocolatequest.entity.projectile.ProjectileHookShotHook;
import com.example.chocolatequest.entity.projectile.ProjectileSpiderHook;

public class ItemSpiderHook extends ItemHookshotBase {
	public ItemSpiderHook(Properties properties) {
		super(properties);
	}
	@Override
	public double getHookRange() { return 42.0; }
	@Override
	public int getCooldown() { return 20; }
	@Override
	public ProjectileHookShotHook getNewHookEntity(Level worldIn, LivingEntity player, ItemStack stack) {
		return new ProjectileSpiderHook(worldIn, player, this, stack);
	}
}
