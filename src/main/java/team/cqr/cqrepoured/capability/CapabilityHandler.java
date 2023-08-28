package team.cqr.cqrepoured.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.capability.faction.FactionRelationCapabilityProvider;
import team.cqr.cqrepoured.capability.protectedregions.CapabilityProtectedRegionDataProvider;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

@EventBusSubscriber(modid = CQRConstants.MODID)
public class CapabilityHandler {

	@SubscribeEvent
	public static void onEntityAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(CapabilityCooldownHandlerProvider.REGISTRY_NAME, CapabilityCooldownHandlerProvider.createProvider());
		}
		if (event.getObject() instanceof AbstractEntityCQR) {
			event.addCapability(CapabilityExtraItemHandlerProvider.REGISTRY_NAME, CapabilityExtraItemHandlerProvider.createProvider(3));
		}

		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(CapabilityElectricShockProvider.REGISTRY_NAME, CapabilityElectricShockProvider.createProvider((LivingEntity) event.getObject()));
			event.addCapability(FactionRelationCapabilityProvider.IDENTIFIER, new FactionRelationCapabilityProvider());
		}
		
		if (event.getObject() instanceof Player) {
			event.addCapability(FactionRelationCapabilityProvider.IDENTIFIER, new FactionRelationCapabilityProvider());
		}
	}

	@SubscribeEvent
	public static void onChunkAttachCapabilitiesEvent(AttachCapabilitiesEvent<LevelChunk> event) {
		event.addCapability(CapabilityProtectedRegionDataProvider.LOCATION, CapabilityProtectedRegionDataProvider.createProvider(event.getObject()));
	}

	public static void writeToItemStackNBT(ItemStack stack, String key, CompoundTag compound) {
		CompoundTag stackCompound = stack.getTag();

		if (stackCompound == null) {
			stackCompound = new CompoundTag();
			stack.setTag(stackCompound);
		}

		stackCompound.put(key, compound);
	}

	public static CompoundTag readFromItemStackNBT(ItemStack stack, String key) {
		CompoundTag stackCompound = stack.getTag();
		return stackCompound != null ? stackCompound.getCompound(key) : new CompoundTag();
	}

}
