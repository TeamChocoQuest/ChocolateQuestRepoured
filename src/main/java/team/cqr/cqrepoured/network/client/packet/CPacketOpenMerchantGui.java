package team.cqr.cqrepoured.network.client.packet;

import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.network.AbstractPacket;

public class CPacketOpenMerchantGui extends AbstractPacket<CPacketOpenMerchantGui> {

	private int entityId;

	public CPacketOpenMerchantGui() {

	}

	public CPacketOpenMerchantGui(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public CPacketOpenMerchantGui fromBytes(PacketBuffer buf) {
		CPacketOpenMerchantGui result = new CPacketOpenMerchantGui();
		result.entityId = buf.readInt();
		return result;
	}

	@Override
	public void toBytes(CPacketOpenMerchantGui packet, PacketBuffer buf) {
		buf.writeInt(packet.entityId);
	}

	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public Class<CPacketOpenMerchantGui> getPacketClass() {
		return CPacketOpenMerchantGui.class;
	}

}
