package team.cqr.cqrepoured.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.Reference;

@EventBusSubscriber(modid = Reference.MODID)
public class CapabilityHandler {

	@SubscribeEvent
	public static void attachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityLivingBase) {
			event.addCapability(CapabilityCooldownHandlerProvider.REGISTRY_NAME, CapabilityCooldownHandlerProvider.createProvider());
		}
		if (event.getObject() instanceof AbstractEntityCQR) {
			event.addCapability(CapabilityExtraItemHandlerProvider.REGISTRY_NAME, CapabilityExtraItemHandlerProvider.createProvider(3));
		}
	}

	public static void writeToItemStackNBT(ItemStack stack, String key, NBTTagCompound compound) {
		NBTTagCompound stackCompound = stack.getTagCompound();

		if (stackCompound == null) {
			stackCompound = new NBTTagCompound();
			stack.setTagCompound(stackCompound);
		}

		stackCompound.setTag(key, compound);
	}

	public static NBTTagCompound readFromItemStackNBT(ItemStack stack, String key) {
		NBTTagCompound stackCompound = stack.getTagCompound();
		return stackCompound != null ? stackCompound.getCompoundTag(key) : new NBTTagCompound();
	}

}
