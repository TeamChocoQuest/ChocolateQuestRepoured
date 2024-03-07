package team.cqr.cqrepoured.faction.eventhandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.faction.capability.FactionRelationCapabilityProvider;

@EventBusSubscriber(modid = CQRepoured.MODID)
public class CapabilityHandler {

	@SubscribeEvent
	public static void onEntityAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(FactionRelationCapabilityProvider.IDENTIFIER, new FactionRelationCapabilityProvider());
		}
		
		if (event.getObject() instanceof Player) {
			event.addCapability(FactionRelationCapabilityProvider.IDENTIFIER, new FactionRelationCapabilityProvider());
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
