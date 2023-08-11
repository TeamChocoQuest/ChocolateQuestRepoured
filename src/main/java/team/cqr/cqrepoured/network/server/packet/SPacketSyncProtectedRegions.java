package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;

import java.util.Collection;

public class SPacketSyncProtectedRegions extends AbstractPacket<SPacketSyncProtectedRegions> {

	private FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

	public SPacketSyncProtectedRegions() {

	}

	public SPacketSyncProtectedRegions(Collection<ProtectedRegion> protectedRegions, boolean clearExisting) {
		this.buffer.writeBoolean(clearExisting);
		this.buffer.writeShort(protectedRegions.size());
		for (ProtectedRegion protectedRegion : protectedRegions) {
			protectedRegion.writeToByteBuf(this.buffer);
		}
	}

	@Override
	public SPacketSyncProtectedRegions fromBytes(FriendlyByteBuf buf) {
		SPacketSyncProtectedRegions result = new SPacketSyncProtectedRegions();
		
		result.buffer.writeBytes(buf);
		
		return result;
	}

	@Override
	public void toBytes(SPacketSyncProtectedRegions packet, FriendlyByteBuf buf) {
		buf.writeBytes(packet.buffer);
	}

	public FriendlyByteBuf getBuffer() {
		return this.buffer;
	}

	@Override
	public Class<SPacketSyncProtectedRegions> getPacketClass() {
		return SPacketSyncProtectedRegions.class;
	}

}
