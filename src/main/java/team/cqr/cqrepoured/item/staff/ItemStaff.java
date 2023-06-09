package team.cqr.cqrepoured.item.staff;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaff extends ItemLore {

	public ItemStaff(Properties properties)
	{
		super(properties.stacksTo(1));
		//this.setMaxStackSize(1);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		target.knockback(1.0F, attacker.position().x - target.position().x, attacker.position().z - target.position().z);
		return false;
	}
}
