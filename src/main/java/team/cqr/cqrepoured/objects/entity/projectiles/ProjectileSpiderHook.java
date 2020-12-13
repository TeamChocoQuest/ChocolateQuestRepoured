package team.cqr.cqrepoured.objects.entity.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.items.ItemHookshotBase;

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
