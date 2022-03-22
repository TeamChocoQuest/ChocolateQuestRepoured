package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.network.AbstractPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SPacketArmorCooldownSync extends AbstractPacket<SPacketArmorCooldownSync> {

	private Map<Item, Integer> itemCooldownMap = new HashMap<>();

	public SPacketArmorCooldownSync() {

	}

	public SPacketArmorCooldownSync(Map<Item, Integer> itemCooldownMap) {
		this.itemCooldownMap = itemCooldownMap;
	}

	@Override
	public SPacketArmorCooldownSync fromBytes(PacketBuffer buf) {
		SPacketArmorCooldownSync result = new SPacketArmorCooldownSync();
		
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			//Item item = ByteBufUtils.readRegistryEntry(buf, ForgeRegistries.ITEMS);
			Item item = buf.readRegistryIdSafe(ForgeRegistries.ITEMS.getRegistrySuperType());
			int cooldown = buf.readInt();
			result.itemCooldownMap.put(item, cooldown);
		}
		
		return result;
	}

	@Override
	public void toBytes(SPacketArmorCooldownSync packet, PacketBuffer buf) {
		buf.writeInt(packet.itemCooldownMap.size());
		for (Entry<Item, Integer> entry : packet.itemCooldownMap.entrySet()) {
			//ByteBufUtils.writeRegistryEntry(buf, entry.getKey());
			buf.writeRegistryId(entry.getKey());
			buf.writeInt(entry.getValue());
		}
	}

	public Map<Item, Integer> getItemCooldownMap() {
		return this.itemCooldownMap;
	}

	@Override
	public Class<SPacketArmorCooldownSync> getPacketClass() {
		return SPacketArmorCooldownSync.class;
	}

}
