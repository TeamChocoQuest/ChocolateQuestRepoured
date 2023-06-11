package team.cqr.cqrepoured.item.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.init.CQRArmorModels;

public class ItemArmorInquisition extends ArmorItem {

	public ItemArmorInquisition(IArmorMaterial materialIn, EquipmentSlot equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	@Override
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		return armorSlot == EquipmentSlot.LEGS ? (A) CQRArmorModels.INQUISITION_ARMOR_LEGS : (A) CQRArmorModels.INQUISITION_ARMOR;
	}

}
