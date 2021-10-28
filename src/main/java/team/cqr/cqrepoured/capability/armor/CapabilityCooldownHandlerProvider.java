package team.cqr.cqrepoured.capability.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;
import team.cqr.cqrepoured.util.Reference;

@EventBusSubscriber(modid = Reference.MODID)
public class CapabilityCooldownHandlerProvider extends SerializableCapabilityProvider<CapabilityCooldownHandler> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Reference.MODID, "item_cooldown_handler");

	@CapabilityInject(CapabilityCooldownHandler.class)
	public static final Capability<CapabilityCooldownHandler> CAPABILITY_ITEM_COOLDOWN_CQR = null;

	public CapabilityCooldownHandlerProvider(Capability<CapabilityCooldownHandler> capability, CapabilityCooldownHandler instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityCooldownHandler.class, new CapabilityCooldownHandlerStorage(), CapabilityCooldownHandler::new);
	}

	public static CapabilityCooldownHandlerProvider createProvider() {
		return new CapabilityCooldownHandlerProvider(CAPABILITY_ITEM_COOLDOWN_CQR, new CapabilityCooldownHandler());
	}

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		CapabilityCooldownHandler capabilityCooldown = entity.getCapability(CAPABILITY_ITEM_COOLDOWN_CQR, null);
		capabilityCooldown.tick();
	}

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			CapabilityCooldownHandler capabilityCooldown = event.player.getCapability(CAPABILITY_ITEM_COOLDOWN_CQR, null);
			CQRMain.NETWORK.sendTo(new SPacketArmorCooldownSync(capabilityCooldown.getItemCooldownMap()), (EntityPlayerMP) event.player);
		}
	}

}
