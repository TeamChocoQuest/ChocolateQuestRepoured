package team.cqr.cqrepoured.event.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandler;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;

@EventBusSubscriber(modid = CQRMain.MODID)
public class ItemCooldownEventHandler {

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		LazyOptional<CapabilityCooldownHandler> lOpCap = entity.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		lOpCap.ifPresent(capa -> capa.tick());
	}

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getPlayer().level.isClientSide) {
			LazyOptional<CapabilityCooldownHandler> lOpCap = event.getPlayer().getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
			
			if(event.getPlayer() instanceof ServerPlayerEntity) {
				lOpCap.ifPresent(capa -> {
					CQRMain.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)event.getPlayer()), new SPacketArmorCooldownSync(capa.getItemCooldownMap()));
				});
			}
		}
	}

}
