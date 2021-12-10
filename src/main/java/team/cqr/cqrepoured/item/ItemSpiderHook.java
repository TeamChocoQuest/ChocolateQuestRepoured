package team.cqr.cqrepoured.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHookShotHook;
import team.cqr.cqrepoured.entity.projectiles.ProjectileSpiderHook;

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
