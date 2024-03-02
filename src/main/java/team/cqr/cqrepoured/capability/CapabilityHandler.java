package team.cqr.cqrepoured.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.capability.armor.attachment.CapabilityArmorAttachmentProvider;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.capability.faction.FactionRelationCapabilityProvider;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.protection.capability.ProtectionReferencesProvider;

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
			event.addCapability(FactionRelationCapabilityProvider.IDENTIFIER, new FactionRelationCapabilityProvider());
		}
		
		if (event.getObject() instanceof Player) {
			event.addCapability(FactionRelationCapabilityProvider.IDENTIFIER, new FactionRelationCapabilityProvider());
		}
	}
	
	public static void onItemAttachCapabilitiesEvent(AttachCapabilitiesEvent<Item> event) {
		if (event.getObject() instanceof ArmorItem) {
			event.addCapability(CapabilityArmorAttachmentProvider.IDENTIFIER, CapabilityArmorAttachmentProvider.createProvider());
		}
		
	}

	@SubscribeEvent
	public static void onChunkAttachCapabilitiesEvent(AttachCapabilitiesEvent<LevelChunk> event) {
		event.addCapability(ProtectionReferencesProvider.LOCATION, ProtectionReferencesProvider.createProvider(event.getObject()));
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
