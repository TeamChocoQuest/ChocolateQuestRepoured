package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketItemStackSync extends AbstractPacket<SPacketItemStackSync> {

	private int entityId;
	private int slotIndex;
	private ItemStack stack;

	public SPacketItemStackSync() {

	}

	public SPacketItemStackSync(int entityId, int slotIndex, ItemStack stack) {
		this.entityId = entityId;
		this.slotIndex = slotIndex;
		this.stack = stack;
	}

	@Override
	public SPacketItemStackSync fromBytes(PacketBuffer buf) {
		SPacketItemStackSync res = new SPacketItemStackSync();
		res.entityId = buf.readInt();
		res.slotIndex = buf.readInt();
		res.stack = buf.readItem();
		return res;
	}

	@Override
	public void toBytes(SPacketItemStackSync packet, PacketBuffer buf) {
		buf.writeInt(packet.entityId);
		buf.writeInt(packet.slotIndex);
		buf.writeItem(packet.stack);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public int getSlotIndex() {
		return this.slotIndex;
	}

	public ItemStack getStack() {
		return this.stack;
	}

	@Override
	public Class<SPacketItemStackSync> getPacketClass() {
		return SPacketItemStackSync.class;
	}

}
