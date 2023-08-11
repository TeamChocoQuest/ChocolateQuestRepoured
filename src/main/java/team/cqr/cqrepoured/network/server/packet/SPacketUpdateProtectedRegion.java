package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;

public class SPacketUpdateProtectedRegion extends AbstractPacket<SPacketUpdateProtectedRegion> {

	private FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

	public SPacketUpdateProtectedRegion() {

	}

	public SPacketUpdateProtectedRegion(ProtectedRegion protectedRegion) {
		protectedRegion.writeToByteBuf(this.buffer);
	}

	@Override
	public SPacketUpdateProtectedRegion fromBytes(FriendlyByteBuf buf) {
		SPacketUpdateProtectedRegion result = new SPacketUpdateProtectedRegion();
		
		result.buffer.writeBytes(buf);
		
		return result;
	}

	@Override
	public void toBytes(SPacketUpdateProtectedRegion packet, FriendlyByteBuf buf) {
		buf.writeBytes(packet.buffer);
	}

	public FriendlyByteBuf getBuffer() {
		return this.buffer;
	}

	@Override
	public Class<SPacketUpdateProtectedRegion> getPacketClass() {
		return SPacketUpdateProtectedRegion.class;
	}

}
