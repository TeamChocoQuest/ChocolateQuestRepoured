package team.cqr.cqrepoured.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHookShotHook;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ItemLongshot extends ItemHookshotBase {
	public ItemLongshot(Properties properties) {
		super("longshot", properties);
	}

	@Override
	public String getDescriptionId() {
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
	public ProjectileHookShotHook getNewHookEntity(Level worldIn, LivingEntity shooter, ItemStack stack) {
		return new ProjectileHookShotHook(worldIn, shooter, this, stack);
	}
}
