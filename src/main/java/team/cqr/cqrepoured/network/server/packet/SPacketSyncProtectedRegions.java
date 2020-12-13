package team.cqr.cqrepoured.network.server.packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;

public class SPacketSyncProtectedRegions implements IMessage {

	private ByteBuf buffer = Unpooled.buffer();

	public SPacketSyncProtectedRegions() {

	}

	public SPacketSyncProtectedRegions(List<ProtectedRegion> protectedRegions) {
		this.buffer.writeShort(protectedRegions.size());
		for (ProtectedRegion protectedRegion : protectedRegions) {
			protectedRegion.writeToByteBuf(this.buffer);
		}
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
