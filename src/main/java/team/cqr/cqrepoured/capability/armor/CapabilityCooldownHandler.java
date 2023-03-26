package team.cqr.cqrepoured.capability.armor;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.Item;

public class CapabilityCooldownHandler {

	private final Object2IntMap<Item> itemCooldownMap = new Object2IntOpenHashMap<>();

	public void tick() {
		this.itemCooldownMap.object2IntEntrySet().forEach(e -> {
			if (e.getIntValue() > 0) {
				//e.setValue(e.getIntValue() - 1);
			}
		});
	}

	public int getCooldown(Item item) {
		return this.itemCooldownMap.getInt(item);
	}

	public void setCooldown(Item item, int cooldown) {
		this.itemCooldownMap.put(item, cooldown);
	}

	public boolean isOnCooldown(Item item) {
		return this.getCooldown(item) > 0;
	}

	public Object2IntMap<Item> getItemCooldownMap() {
		return this.itemCooldownMap;
	}

}
