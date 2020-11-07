package com.teamcqr.chocolatequestrepoured.network.server.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SPacketArmorCooldownSync implements IMessage {

	private Map<Item, Integer> itemCooldownMap = new HashMap<>();

	public SPacketArmorCooldownSync() {

	}

	public SPacketArmorCooldownSync(Map<Item, Integer> itemCooldownMap) {
		this.itemCooldownMap = itemCooldownMap;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			Item item = ByteBufUtils.readRegistryEntry(buf, ForgeRegistries.ITEMS);
			int cooldown = buf.readInt();
			this.itemCooldownMap.put(item, cooldown);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.itemCooldownMap.size());
		for (Entry<Item, Integer> entry : this.itemCooldownMap.entrySet()) {
			ByteBufUtils.writeRegistryEntry(buf, entry.getKey());
			buf.writeInt(entry.getValue());
		}
	}

	public Map<Item, Integer> getItemCooldownMap() {
		return this.itemCooldownMap;
	}

}
