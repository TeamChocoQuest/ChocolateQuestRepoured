package team.cqr.cqrepoured.event.capability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.armor.CapabilityArmorCooldown;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;

@EventBusSubscriber(modid = CQRepoured.MODID)
public class ItemCooldownEventHandler {

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		LazyOptional<CapabilityArmorCooldown> lOpCap = entity.getCapability(CQRCapabilities.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		lOpCap.ifPresent(capa -> capa.tick());
	}

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getEntity().level().isClientSide()) {
			LazyOptional<CapabilityArmorCooldown> lOpCap = event.getEntity().getCapability(CQRCapabilities.CAPABILITY_ITEM_COOLDOWN_CQR, null);
			
			if(event.getEntity() instanceof ServerPlayer) {
				lOpCap.ifPresent(capa -> {
					CQRMain.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SPacketArmorCooldownSync(capa.getItemCooldownMap()));
				});
			}
		}
	}

}
