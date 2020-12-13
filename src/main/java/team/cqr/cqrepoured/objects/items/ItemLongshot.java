package team.cqr.cqrepoured.objects.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileHookShotHook;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ItemLongshot extends ItemHookshotBase {
	public ItemLongshot() {
		super("longshot");
	}

	@Override
	public String getTranslationKey() {
		return "description.longshot.name";
	}

	@Override
	public double getHookRange() {
		return 30.0;
	}

	@Override
	public int getCooldown() {
		return 30;
	}

	@Override
	public ProjectileHookShotHook getNewHookEntity(World worldIn, EntityLivingBase shooter, ItemStack stack) {
		return new ProjectileHookShotHook(worldIn, shooter, this, stack);
	}
}
