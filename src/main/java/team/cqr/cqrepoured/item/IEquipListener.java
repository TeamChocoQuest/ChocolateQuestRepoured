package team.cqr.cqrepoured.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IEquipListener {

	void onEquip(LivingEntity entity, ItemStack stack, EquipmentSlot slot);

	void onUnequip(LivingEntity entity, ItemStack stack, EquipmentSlot slot);

}
