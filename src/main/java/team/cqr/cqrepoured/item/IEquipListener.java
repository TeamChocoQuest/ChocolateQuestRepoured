package team.cqr.cqrepoured.item;

import net.minecraft.item.ItemStack;

public interface IEquipListener {

	void onEquip(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot);

	void onUnequip(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot);

}
