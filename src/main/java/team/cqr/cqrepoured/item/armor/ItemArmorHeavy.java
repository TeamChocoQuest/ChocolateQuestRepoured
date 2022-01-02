package team.cqr.cqrepoured.item.armor;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.init.CQRMaterials;

public class ItemArmorHeavy extends ArmorItem {

	private AttributeModifier movementSpeed;
	private AttributeModifier knockbackResistance;

	public ItemArmorHeavy(ArmorMaterial materialIn, int renderIndexIn, EquipmentSlotType equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		this.movementSpeed = new AttributeModifier("HeavySpeedModifier", -0.05D, 2);
		this.knockbackResistance = new AttributeModifier("HeavyKnockbackModifier", 0.1D, 0);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == MobEntity.getSlotForItemStack(stack)) {
			multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), this.knockbackResistance);
			multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.movementSpeed);
		}

		return multimap;
	}

	@Override
	public void onArmorTick(World world, PlayerEntity player, ItemStack itemStack) {
		player.jumpMovementFactor *= 0.95F;
	}

	@Override
	@Dist(OnlyIn.CLIENT)
	@Nullable
	public ModelBiped getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, ModelBiped _default) {
		ArmorMaterial armorMaterial = ((ArmorItem) itemStack.getItem()).getArmorMaterial();

		if (armorMaterial == CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND) {
			return armorSlot == EquipmentSlotType.LEGS ? CQRArmorModels.heavyDiamondArmorLegs : CQRArmorModels.heavyDiamondArmor;
		} else if (armorMaterial == CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON) {
			return armorSlot == EquipmentSlotType.LEGS ? CQRArmorModels.heavyIronArmorLegs : CQRArmorModels.heavyIronArmor;
		}

		return null;

	}

}
