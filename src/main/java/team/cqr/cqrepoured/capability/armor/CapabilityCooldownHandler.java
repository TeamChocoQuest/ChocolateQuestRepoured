package team.cqr.cqrepoured.capability.armor;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntMap.FastEntrySet;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public class CapabilityCooldownHandler {

	private final Object2IntMap<Item> itemCooldownMap = new Object2IntOpenHashMap<>();

	public void tick() {
		// workaround for the following code, issue is fixed in fastutil 8.5.12
//		this.itemCooldownMap.object2IntEntrySet().forEach(e -> {
//			if (e.getIntValue() > 0) {
//				e.setValue(e.getIntValue() - 1);
//			}
//		});
		ObjectSet<Entry<Item>> entries = this.itemCooldownMap.object2IntEntrySet();
		(entries instanceof FastEntrySet ? ((FastEntrySet<Item>) entries).fastIterator() : entries.iterator()).forEachRemaining(e -> {
			if (e.getIntValue() > 0) {
				e.setValue(e.getIntValue() - 1);
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
