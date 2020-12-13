package team.cqr.cqrepoured.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketStructureSelector implements IMessage {

	private EnumHand hand;

	public CPacketStructureSelector() {

	}

	public CPacketStructureSelector(EnumHand hand) {
		this.hand = hand;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.hand = buf.readBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.hand == EnumHand.MAIN_HAND);
	}

	public EnumHand getHand() {
		return this.hand;
	}

}
