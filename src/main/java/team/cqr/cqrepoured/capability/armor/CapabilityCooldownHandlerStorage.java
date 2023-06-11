package team.cqr.cqrepoured.capability.armor;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.registries.ForgeRegistries;

public class CapabilityCooldownHandlerStorage implements IStorage<CapabilityCooldownHandler> {

	@Override
	public INBT writeNBT(Capability<CapabilityCooldownHandler> capability, CapabilityCooldownHandler instance, Direction side) {
		ListTag nbtTagList = new ListTag();

		for (Object2IntMap.Entry<Item> entry : instance.getItemCooldownMap().object2IntEntrySet()) {
			CompoundTag compound = new CompoundTag();

			compound.putString("item", entry.getKey().getRegistryName().toString());
			compound.putInt("cooldown", entry.getIntValue());
			nbtTagList.add(compound);
		}

		return nbtTagList;
	}

	@Override
	public void readNBT(Capability<CapabilityCooldownHandler> capability, CapabilityCooldownHandler instance, Direction side, INBT nbt) {
		if (nbt instanceof ListTag) {
			ListTag nbtTagList = (ListTag) nbt;

			for (int i = 0; i < nbtTagList.size(); i++) {
				CompoundTag compound = nbtTagList.getCompound(i);
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(compound.getString("item")));

				if (item != null) {
					instance.setCooldown(item, compound.getInt("cooldown"));
				}
			}
		}
	}

}
