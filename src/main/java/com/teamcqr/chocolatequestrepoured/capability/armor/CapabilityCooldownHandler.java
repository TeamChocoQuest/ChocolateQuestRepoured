package com.teamcqr.chocolatequestrepoured.capability.armor;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;

public class CapabilityCooldownHandler {

	private final Map<Item, Integer> itemCooldownMap = new HashMap<>();

	public int getCooldown(Item item) {
		if (this.itemCooldownMap.containsKey(item)) {
			return this.itemCooldownMap.get(item);
		}

		return 0;
	}

	public void setCooldown(Item item, int cooldown) {
		this.itemCooldownMap.put(item, cooldown);
	}

	public void reduceCooldown(Item item) {
		if (this.itemCooldownMap.containsKey(item)) {
			this.itemCooldownMap.put(item, this.itemCooldownMap.get(item) - 1);
		}
	}

	public boolean onCooldown(Item item) {
		return this.getCooldown(item) > 0;
	}

	public Map<Item, Integer> getItemCooldownMap() {
		return this.itemCooldownMap;
	}

}
