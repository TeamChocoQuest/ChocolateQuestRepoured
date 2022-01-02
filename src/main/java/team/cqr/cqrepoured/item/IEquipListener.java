package team.cqr.cqrepoured.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public interface IEquipListener {

	void onEquip(LivingEntity entity, ItemStack stack, EquipmentSlotType slot);

	void onUnequip(LivingEntity entity, ItemStack stack, EquipmentSlotType slot);

}
