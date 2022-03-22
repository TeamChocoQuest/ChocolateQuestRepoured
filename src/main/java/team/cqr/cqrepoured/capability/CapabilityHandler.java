package team.cqr.cqrepoured.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.capability.protectedregions.CapabilityProtectedRegionDataProvider;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

//@EventBusSubscriber(modid = CQRMain.MODID)
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
		}
	}

	@SubscribeEvent
	public static void onChunkAttachCapabilitiesEvent(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(CapabilityProtectedRegionDataProvider.LOCATION, CapabilityProtectedRegionDataProvider.createProvider(event.getObject()));
	}

	public static void writeToItemStackNBT(ItemStack stack, String key, CompoundNBT compound) {
		CompoundNBT stackCompound = stack.getTag();

		if (stackCompound == null) {
			stackCompound = new CompoundNBT();
			stack.setTag(stackCompound);
		}

		stackCompound.put(key, compound);
	}

	public static CompoundNBT readFromItemStackNBT(ItemStack stack, String key) {
		CompoundNBT stackCompound = stack.getTag();
		return stackCompound != null ? stackCompound.getCompound(key) : new CompoundNBT();
	}

}
