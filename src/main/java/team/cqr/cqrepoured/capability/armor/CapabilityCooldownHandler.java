package team.cqr.cqrepoured.capability.armor;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class CapabilityCooldownHandler implements CapabilityArmorCooldown {

	private final Object2IntMap<Item> itemCooldownMap = new Object2IntOpenHashMap<>();

	public Object2IntMap<Item> getItemCooldownMap() {
		return this.itemCooldownMap;
	}

	@Override
	public CompoundTag serializeNBT() {
		ListTag nbtTagList = new ListTag();

		for (Object2IntMap.Entry<Item> entry : this.getItemCooldownMap().object2IntEntrySet()) {
			CompoundTag compound = new CompoundTag();

			compound.putString("item", ForgeRegistries.ITEMS.getResourceKey(entry.getKey()).toString());
			compound.putInt("cooldown", entry.getIntValue());
			nbtTagList.add(compound);
		}

		CompoundTag result = new CompoundTag();
		result.put("cooldowns", nbtTagList);
		return result;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		Tag tag = nbt.get("cooldowns");
		if (tag instanceof ListTag) {
			ListTag nbtTagList = (ListTag) tag;

			for (int i = 0; i < nbtTagList.size(); i++) {
				CompoundTag compound = nbtTagList.getCompound(i);
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(compound.getString("item")));

				if (item != null) {
					this.setCooldown(item, compound.getInt("cooldown"));
				}
			}
		}
		
	}

}
