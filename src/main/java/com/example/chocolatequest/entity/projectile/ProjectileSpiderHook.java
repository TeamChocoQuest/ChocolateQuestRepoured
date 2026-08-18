package com.example.chocolatequest.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.example.chocolatequest.item.ItemHookshotBase;
import com.example.chocolatequest.registry.ModEntities;

public class ProjectileSpiderHook extends ProjectileHookShotHook {
	public ProjectileSpiderHook(Level worldIn) {
		super(ModEntities.PROJECTILE_SPIDER_HOOK.get(), worldIn);
	}

	public ProjectileSpiderHook(EntityType<? extends ProjectileSpiderHook> type, Level worldIn) {
		super(type, worldIn);
	}

	public ProjectileSpiderHook(Level worldIn, LivingEntity shooter, ItemHookshotBase hookshot, ItemStack stack) {
		super(ModEntities.PROJECTILE_SPIDER_HOOK.get(), shooter, worldIn, hookshot, stack);
	}
}
