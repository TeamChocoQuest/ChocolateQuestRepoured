package com.teamcqr.chocolatequestrepoured.capability.armor;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ArmorCooldownSyncPacket;
import com.teamcqr.chocolatequestrepoured.util.ItemUtil;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@EventBusSubscriber(modid = Reference.MODID)
public class CapabilitySpecialArmorHandler {

	public static List<Capability<? extends CapabilitySpecialArmor>> capabilities = new ArrayList<Capability<? extends CapabilitySpecialArmor>>();

	public static void addCapability(Capability<? extends CapabilitySpecialArmor> capability) {
		if (!capabilities.contains(capability)) {
			capabilities.add(capability);
		}
	}

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		CapabilitySpecialArmor icapability;
		for (Capability<? extends CapabilitySpecialArmor> capability : capabilities) {
			icapability = entity.getCapability(capability, null);
			icapability.reduceCooldown();
			if (ItemUtil.hasFullSet(entity, icapability.getItemClass())) {
				icapability.onLivingUpdateEvent(event);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		CapabilitySpecialArmor icapability;
		for (Capability<? extends CapabilitySpecialArmor> capability : capabilities) {
			icapability = entity.getCapability(capability, null);
			if (ItemUtil.hasFullSet(entity, icapability.getItemClass())) {
				icapability.onLivingDamageEvent(event);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			CQRMain.NETWORK.sendTo(new ArmorCooldownSyncPacket(event.player, capabilities), (EntityPlayerMP) event.player);
		}
	}

}
