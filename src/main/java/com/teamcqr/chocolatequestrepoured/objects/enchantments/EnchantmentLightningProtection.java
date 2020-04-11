package com.teamcqr.chocolatequestrepoured.objects.enchantments;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModEnchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EnchantmentLightningProtection extends Enchantment {

	public EnchantmentLightningProtection() {
		this(Rarity.RARE, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD});
	}
	
	private EnchantmentLightningProtection(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}
	

	@Override
	public int getMaxLevel() {
		return 8;
	}
	
	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}
	
	@Override
	public int getMinLevel() {
		return 1;
	}


	@SubscribeEvent
	public static void onStruckByLightning(EntityStruckByLightningEvent event) {
		if(event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) event.getEntity();
			ItemStack helmet = living.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(helmet != null && helmet.getItem() != null && (EnchantmentHelper.getEnchantments(helmet)).containsKey(ModEnchantments.LIGHTNING_PROTECTION)) {
				int lvl = (EnchantmentHelper.getEnchantments(helmet)).get(ModEnchantments.LIGHTNING_PROTECTION);
				Random rdm = living.getRNG();
				if(lvl >= rdm.nextInt(ModEnchantments.LIGHTNING_PROTECTION.getMaxLevel() +1)) {
					event.setCanceled(true);
				}
			}
		}
	}
	
}
