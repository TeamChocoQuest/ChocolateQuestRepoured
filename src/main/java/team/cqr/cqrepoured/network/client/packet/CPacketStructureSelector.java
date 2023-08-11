package team.cqr.cqrepoured.network.client.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;

public class CPacketStructureSelector extends AbstractPacket<CPacketStructureSelector> {

	private InteractionHand hand;

	public CPacketStructureSelector() {

	}

	public CPacketStructureSelector(InteractionHand hand) {
		this.hand = hand;
	}

	@Override
	public CPacketStructureSelector fromBytes(FriendlyByteBuf buf) {
		CPacketStructureSelector result = new CPacketStructureSelector();
		result.hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return result;
	}

	@Override
	public void toBytes(CPacketStructureSelector packet, FriendlyByteBuf buf) {
		buf.writeBoolean(packet.hand == InteractionHand.MAIN_HAND);
	}

	public InteractionHand getHand() {
		return this.hand;
	}

	@Override
	public Class<CPacketStructureSelector> getPacketClass() {
		return CPacketStructureSelector.class;
	}

}
