package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;

public class SPacketUpdateProtectedRegion implements IMessage {

	private ByteBuf buffer = Unpooled.buffer();

	public SPacketUpdateProtectedRegion() {

	}

	public SPacketUpdateProtectedRegion(ProtectedRegion protectedRegion) {
		protectedRegion.writeToByteBuf(this.buffer);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.buffer.writeBytes(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBytes(this.buffer);
	}

	public ByteBuf getBuffer() {
		return this.buffer;
	}

}
