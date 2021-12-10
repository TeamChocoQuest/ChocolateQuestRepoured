package team.cqr.cqrepoured.event.capability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandler;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;

@EventBusSubscriber(modid = CQRMain.MODID)
public class ItemCooldownEventHandler {

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		CapabilityCooldownHandler capabilityCooldown = entity.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		capabilityCooldown.tick();
	}

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			CapabilityCooldownHandler capabilityCooldown = event.player.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
			CQRMain.NETWORK.sendTo(new SPacketArmorCooldownSync(capabilityCooldown.getItemCooldownMap()), (EntityPlayerMP) event.player);
		}
	}

}
