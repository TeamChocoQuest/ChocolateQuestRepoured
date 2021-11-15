package team.cqr.cqrepoured.objects.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemGoldenFeather extends ItemLore implements INonEnchantable {

	public ItemGoldenFeather() {
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(385);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// mainhand or offhand
		if (!isSelected && itemSlot != 0) {
			return;
		}
		if (entityIn.fallDistance <= 0.0F) {
			return;
		}
		worldIn.spawnParticle(EnumParticleTypes.CLOUD, entityIn.posX, entityIn.posY, entityIn.posZ, (itemRand.nextFloat() - 0.5F) / 2.0F, -0.5D, (itemRand.nextFloat() - 0.5F) / 2.0F);
	}

	// INonEnchantable stuff
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return INonEnchantable.super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return INonEnchantable.super.isEnchantable(stack);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return INonEnchantable.super.isBookEnchantable(stack, book);
	}

}
