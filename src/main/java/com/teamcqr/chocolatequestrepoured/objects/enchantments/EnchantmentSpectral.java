package com.teamcqr.chocolatequestrepoured.objects.enchantments;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentSpectral extends Enchantment {

	public EnchantmentSpectral() {
		this(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
	}
	
	private EnchantmentSpectral(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.setName("spectral");
		this.setRegistryName(Reference.MODID, "spectral");
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}
	
}
