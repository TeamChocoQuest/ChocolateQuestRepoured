package com.teamcqr.chocolatequestrepoured.objects.enchantments;

import com.teamcqr.chocolatequestrepoured.init.CQREnchantments;
import com.teamcqr.chocolatequestrepoured.util.Reference;

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
		this(Rarity.RARE, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD });
	}

	private EnchantmentLightningProtection(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.setName("lightning_protection");
		this.setRegistryName(Reference.MODID, "lightning_protection");
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
		if (event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) event.getEntity();
			ItemStack helmet = living.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

			int lvl = EnchantmentHelper.getEnchantmentLevel(CQREnchantments.LIGHTNING_PROTECTION, helmet);
			if (lvl > 0 && lvl > living.getRNG().nextInt(10)) {
				event.setCanceled(true);
			}
		}
	}

}
