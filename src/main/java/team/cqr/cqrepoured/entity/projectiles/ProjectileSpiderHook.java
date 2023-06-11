package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.item.ItemHookshotBase;

public class ProjectileSpiderHook extends ProjectileHookShotHook {

	public ProjectileSpiderHook(Level worldIn) {
		super(worldIn);
	}

	public ProjectileSpiderHook(Level worldIn, LivingEntity shooter, ItemHookshotBase hookshot, ItemStack stack) {
		super(worldIn, shooter, hookshot, stack);
	}

}
