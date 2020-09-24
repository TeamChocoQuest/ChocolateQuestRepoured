package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHookShotHook;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ItemHookshot extends ItemHookshotBase {
	public ItemHookshot() {
		super("hookshot");
	}

	@Override
	public String getTranslationKey() {
		return "description.hookshot.name";
	}

	@Override
	public double getHookRange() {
		return 20.0;
	}

	@Override
	public int getCooldown() {
		return 30;
	}

	@Override
	public ProjectileHookShotHook getNewHookEntity(World worldIn, EntityLivingBase player, ItemStack stack) {
		return new ProjectileHookShotHook(worldIn, player, this, stack);
	}
}
