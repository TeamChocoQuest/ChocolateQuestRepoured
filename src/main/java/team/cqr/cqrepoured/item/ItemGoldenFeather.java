package team.cqr.cqrepoured.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ItemGoldenFeather extends ItemLore {

	public ItemGoldenFeather(Properties props) {
		super(props);
		//Move to creation of item
		//this.setMaxStackSize(1);
		//this.setMaxDamage(385);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// mainhand or offhand
		if (!isSelected && itemSlot != 0) {
			return;
		}
		if (entityIn.fallDistance <= 0.0F) {
			return;
		}
		worldIn.addParticle(ParticleTypes.CLOUD, entityIn.position().x(), entityIn.position().y(), entityIn.position().z(), (random.nextFloat() - 0.5F) / 2.0F, -0.5D, (random.nextFloat() - 0.5F) / 2.0F);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

}
