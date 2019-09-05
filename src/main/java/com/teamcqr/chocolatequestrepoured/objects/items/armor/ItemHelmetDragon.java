package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import com.google.common.collect.Multimap;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHelmetDragon extends ItemArmor {

	private AttributeModifier attackDamage;
	private AttributeModifier health;

	public ItemHelmetDragon(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		this.health = new AttributeModifier("DragonHelmetHealthModifier", 10D, 0);
		this.attackDamage = new AttributeModifier("DragonHelmetDamageModifier", 1D, 2);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityLiving.getSlotForItemStack(stack)) {
			multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), this.health);
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), this.attackDamage);
		}

		return multimap;
	}

	/*
	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot,
			ModelBiped _default) {
		if (itemStack != null) {
			if (itemStack.getItem() instanceof ItemArmor) {
				ModelHelmetDragon model = new ModelHelmetDragon();

				model.bipedHead.showModel = armorSlot == EntityEquipmentSlot.HEAD;
				model.bipedHeadwear.showModel = armorSlot == EntityEquipmentSlot.HEAD;

				model.isSneak = _default.isSneak;
				model.isRiding = _default.isRiding;
				model.isChild = _default.isChild;
				model.rightArmPose = _default.rightArmPose;
				model.leftArmPose = _default.leftArmPose;

				return model;
			}
		}
		return null;
	}
	*/

}