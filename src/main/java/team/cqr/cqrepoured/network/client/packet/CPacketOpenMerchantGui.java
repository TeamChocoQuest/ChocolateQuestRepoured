package team.cqr.cqrepoured.network.client.packet;

import net.minecraft.network.FriendlyByteBuf;
import team.cqr.cqrepoured.network.AbstractPacket;

public class CPacketOpenMerchantGui extends AbstractPacket<CPacketOpenMerchantGui> {

	private int entityId;

	public CPacketOpenMerchantGui() {

	}

	public CPacketOpenMerchantGui(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public CPacketOpenMerchantGui fromBytes(FriendlyByteBuf buf) {
		CPacketOpenMerchantGui result = new CPacketOpenMerchantGui();
		result.entityId = buf.readInt();
		return result;
	}

	@Override
	public void toBytes(CPacketOpenMerchantGui packet, FriendlyByteBuf buf) {
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
