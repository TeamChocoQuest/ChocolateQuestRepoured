package team.cqr.cqrepoured.network.server.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketSyncProtectionWhitelists implements IMessage {

	private List<Block> breakableBlocks = new ArrayList<>();
	private List<Block> placeableBlocks = new ArrayList<>();

	public SPacketSyncProtectionWhitelists() {

	}

	public SPacketSyncProtectionWhitelists(Collection<Block> breakableBlocks, Collection<Block> placeableBlocks) {
		this.breakableBlocks.addAll(breakableBlocks);
		this.placeableBlocks.addAll(placeableBlocks);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		for (int i = buf.readInt(); i > 0; i--) {
			this.breakableBlocks.add(Block.REGISTRY.getObjectById(buf.readInt()));
		}
		for (int i = buf.readInt(); i > 0; i--) {
			this.placeableBlocks.add(Block.REGISTRY.getObjectById(buf.readInt()));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.breakableBlocks.size());
		for (Block b : this.breakableBlocks) {
			buf.writeInt(Block.REGISTRY.getIDForObject(b));
		}
		buf.writeInt(this.placeableBlocks.size());
		for (Block b : this.placeableBlocks) {
			buf.writeInt(Block.REGISTRY.getIDForObject(b));
		}
	}

	public List<Block> getBreakableBlocks() {
		return breakableBlocks;
	}

	public List<Block> getPlaceableBlocks() {
		return placeableBlocks;
	}

}
