package team.cqr.cqrepoured.protection.network.server.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import team.cqr.cqrepoured.protection.ProtectedRegion;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;

public class SPacketUpdateProtectedRegion extends AbstractPacket<SPacketUpdateProtectedRegion> {

	private FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

	public SPacketUpdateProtectedRegion() {

	}

	public SPacketUpdateProtectedRegion(ProtectedRegion protectedRegion) {
		this.buffer.writeJsonWithCodec(ProtectedRegion.CODEC, protectedRegion);
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
