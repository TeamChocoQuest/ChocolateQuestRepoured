package com.teamcqr.chocolatequestrepoured.capability.armor;

import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityCooldownHandlerStorage implements IStorage<CapabilityCooldownHandler> {

	@Override
	public NBTBase writeNBT(Capability<CapabilityCooldownHandler> capability, CapabilityCooldownHandler instance, EnumFacing side) {
		NBTTagList nbtTagList = new NBTTagList();

		for (Map.Entry<Item, Integer> entry : instance.getItemCooldownMap().entrySet()) {
			NBTTagCompound compound = new NBTTagCompound();

			compound.setString("item", entry.getKey().getRegistryName().toString());
			compound.setInteger("cooldown", entry.getValue());
			nbtTagList.appendTag(compound);
		}

		return nbtTagList;
	}

	@Override
	public void readNBT(Capability<CapabilityCooldownHandler> capability, CapabilityCooldownHandler instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagList) {
			NBTTagList nbtTagList = (NBTTagList) nbt;

			for (int i = 0; i < nbtTagList.tagCount(); i++) {
				NBTTagCompound compound = nbtTagList.getCompoundTagAt(i);
				Item item = Item.REGISTRY.getObject(new ResourceLocation(compound.getString("item")));

				if (item != null) {
					instance.setCooldown(item, compound.getInteger("cooldown"));
				}
			}
		}
	}

}
