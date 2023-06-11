package team.cqr.cqrepoured.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IEquipListener {

	void onEquip(LivingEntity entity, ItemStack stack, EquipmentSlotType slot);

	void onUnequip(LivingEntity entity, ItemStack stack, EquipmentSlotType slot);

}
