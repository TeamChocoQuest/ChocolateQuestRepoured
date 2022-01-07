package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketHookShotPlayerStop extends AbstractPacket<SPacketHookShotPlayerStop> {

	public SPacketHookShotPlayerStop() {

	}

	@Override
	public Class<SPacketHookShotPlayerStop> getPacketClass() {
		return SPacketHookShotPlayerStop.class;
	}

	@Override
	public SPacketHookShotPlayerStop fromBytes(PacketBuffer buffer) {
		return new SPacketHookShotPlayerStop();
	}

	@Override
	public void toBytes(SPacketHookShotPlayerStop packet, PacketBuffer buffer) {
		
	}

}
