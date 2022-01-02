package team.cqr.cqrepoured.item.armor;

import javax.annotation.Nullable;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import team.cqr.cqrepoured.client.init.CQRArmorModels;

public class ItemArmorInquisition extends ArmorItem {

	public ItemArmorInquisition(ArmorMaterial materialIn, int renderIndexIn, EquipmentSlotType equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}

	@Override
	@Dist(OnlyIn.CLIENT)
	@Nullable
	public ModelBiped getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, ModelBiped _default) {
		return armorSlot == EquipmentSlotType.LEGS ? CQRArmorModels.inquisitionArmorLegs : CQRArmorModels.inquisitionArmor;
	}

}
