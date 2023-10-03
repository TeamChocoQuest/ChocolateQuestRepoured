package team.cqr.cqrepoured.capability.armor;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface CapabilityArmorCooldown extends INBTSerializable<CompoundTag> {

	public default int getCooldown(Item item) {
		return this.getItemCooldownMap().getOrDefault(item, -1);
	}
	public default void setCooldown(Item item, int value) {
		this.getItemCooldownMap().put(item, value);
	}
	public default boolean isOnCooldow(Item item) {
		return this.getCooldown(item) > 0;
	}
	public Object2IntMap<Item> getItemCooldownMap();
	
	public default void tick() {
		this.getItemCooldownMap().object2IntEntrySet().forEach(e -> {
			if (e.getIntValue() > 0) {
				e.setValue(e.getIntValue() - 1);
			}
		});
	}

}
