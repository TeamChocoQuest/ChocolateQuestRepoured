package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import com.teamcqr.chocolatequestrepoured.objects.items.ItemHookshotBase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ProjectileSpiderHook extends ProjectileHookShotHook {

	public ProjectileSpiderHook(World worldIn) {
		super(worldIn);
	}
	
	public ProjectileSpiderHook(World worldIn, double x, double y, double z, double range) {
		super(worldIn, x, y, z, range);
	}
	
	public ProjectileSpiderHook(World worldIn, EntityLivingBase shooter, ItemHookshotBase hookshot, ItemStack stack) {
		super(worldIn, shooter, hookshot, stack);
	}

}
