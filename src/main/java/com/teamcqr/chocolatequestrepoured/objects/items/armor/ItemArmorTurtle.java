package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmor;
import com.teamcqr.chocolatequestrepoured.capability.armor.turtle.CapabilityTurtleArmorProvider;
import com.teamcqr.chocolatequestrepoured.client.init.ModArmorModels;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.util.ItemUtil;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorTurtle extends ItemArmor {

	private AttributeModifier health;

	public ItemArmorTurtle(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		this.health = new AttributeModifier("TurtleHealthModifier", 2.0D, 0);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityLiving.getSlotForItemStack(stack)) {
			multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), this.health);
		}

		return multimap;
	}
	
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot,
			ModelBiped _default) {
		//return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
		return ModArmorModels.turtleArmor;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null) {
			CapabilitySpecialArmor icapability = player.getCapability(CapabilityTurtleArmorProvider.CAPABILITY_TURTLE_ARMOR, null);
			if (icapability != null && icapability.onCooldown()) {
				tooltip.add(TextFormatting.RED + I18n.format("description.turtle_armor_charging.name") + convertCooldown(icapability.getCooldown()));
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.turtle_armor.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	private String convertCooldown(int cd) {
		int i = cd / 20;
		int minutes = i / 60;
		int seconds = i % 60;

		if (seconds < 10) {
			return minutes + ":" + "0" + seconds;
		}

		return minutes + ":" + seconds;
	}

	@EventBusSubscriber(modid = Reference.MODID)
	public static class EventHandler {

		@SubscribeEvent
		public static void onLivingHurtEvent(LivingAttackEvent event) {
			if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST)
					.getItem() == ModItems.CHESTPLATE_TURTLE) {
				if (event.getSource().getImmediateSource() != null && ItemUtil.compareRotations(event.getEntity().rotationYaw, event.getSource().getImmediateSource().rotationYaw, 50.0D)) {
					event.setCanceled(true);
				}
			}
		}

	}

}
