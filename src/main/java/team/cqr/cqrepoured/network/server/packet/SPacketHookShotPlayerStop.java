package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.network.FriendlyByteBuf;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketHookShotPlayerStop extends AbstractPacket<SPacketHookShotPlayerStop> {

	public SPacketHookShotPlayerStop() {

	}

	@Override
	public Class<SPacketHookShotPlayerStop> getPacketClass() {
		return SPacketHookShotPlayerStop.class;
	}

	@Override
	public SPacketHookShotPlayerStop fromBytes(FriendlyByteBuf buffer) {
		return new SPacketHookShotPlayerStop();
	}

	@Override
	public void toBytes(SPacketHookShotPlayerStop packet, FriendlyByteBuf buffer) {
		
	}

}
