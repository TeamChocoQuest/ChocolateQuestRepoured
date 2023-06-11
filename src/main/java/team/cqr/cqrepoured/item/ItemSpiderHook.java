package team.cqr.cqrepoured.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHookShotHook;
import team.cqr.cqrepoured.entity.projectiles.ProjectileSpiderHook;

public class ItemSpiderHook extends ItemHookshotBase {

	public ItemSpiderHook(Properties properties) {
		super("spiderhook", properties);
	}

	@Override
	public String getDescriptionId() {
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
	public ProjectileHookShotHook getNewHookEntity(Level worldIn, LivingEntity shooter, ItemStack stack) {
		return new ProjectileSpiderHook(worldIn, shooter, this, stack);
	}

}
