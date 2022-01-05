package team.cqr.cqrepoured.network.client.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import team.cqr.cqrepoured.network.AbstractPacket;

public class CPacketStructureSelector extends AbstractPacket<CPacketStructureSelector> {

	private Hand hand;

	public CPacketStructureSelector() {

	}

	public CPacketStructureSelector(Hand hand) {
		this.hand = hand;
	}

	@Override
	public CPacketStructureSelector fromBytes(PacketBuffer buf) {
		CPacketStructureSelector result = new CPacketStructureSelector();
		result.hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
		return result;
	}

	@Override
	public void toBytes(CPacketStructureSelector packet, PacketBuffer buf) {
		buf.writeBoolean(packet.hand == Hand.MAIN_HAND);
	}

	public Hand getHand() {
		return this.hand;
	}

	@Override
	public Class<CPacketStructureSelector> getPacketClass() {
		return CPacketStructureSelector.class;
	}

}
