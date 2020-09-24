package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHookShotHook;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderHook;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSpiderHook extends ItemHookshotBase {

	public ItemSpiderHook() {
		super("spiderhook");
	}

	@Override
	public String getTranslationKey() {
		return "description.spiderhook.name";
	}

	@Override
	public double getHookRange() {
		return 40;
	}
	
	@Override
	public int getCooldown() {
		return 50;
	}

	@Override
	public ProjectileHookShotHook getNewHookEntity(World worldIn, EntityLivingBase shooter, ItemStack stack) {
		return new ProjectileSpiderHook(worldIn, shooter, this, stack);
	}

}
