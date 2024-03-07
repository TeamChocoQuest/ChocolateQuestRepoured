package team.cqr.cqrepoured.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.capability.armor.attachment.CapabilityArmorAttachmentProvider;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

@EventBusSubscriber(modid = CQRepoured.MODID)
public class CapabilityHandler {

	@SubscribeEvent
	public static void onEntityAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(CapabilityCooldownHandlerProvider.REGISTRY_NAME, CapabilityCooldownHandlerProvider.createProvider());
		}
		if (event.getObject() instanceof AbstractEntityCQR) {
			event.addCapability(CapabilityExtraItemHandlerProvider.REGISTRY_NAME, CapabilityExtraItemHandlerProvider.createProvider(3));
		}

	}
	
	public static void onItemAttachCapabilitiesEvent(AttachCapabilitiesEvent<Item> event) {
		if (event.getObject() instanceof ArmorItem) {
			event.addCapability(CapabilityArmorAttachmentProvider.IDENTIFIER, CapabilityArmorAttachmentProvider.createProvider());
		}
		
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
