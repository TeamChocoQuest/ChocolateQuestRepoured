package com.teamcqr.chocolatequestrepoured.network.packets.toClient;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketSyncProtectedRegions implements IMessage {

	private ByteBuf buf;
	private List<ProtectedRegion> protectedRegions;

	public SPacketSyncProtectedRegions() {

	}

	public SPacketSyncProtectedRegions(List<ProtectedRegion> protectedRegions) {
		this.protectedRegions = protectedRegions;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.buf = buf.copy();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeShort(this.protectedRegions.size());
		for (ProtectedRegion protectedRegion : this.protectedRegions) {
			ByteBufUtil.writeUuid(buf, protectedRegion.getUuid());
			ByteBufUtil.writeBlockPos(buf, protectedRegion.getStartPos());
			ByteBufUtil.writeBlockPos(buf, protectedRegion.getEndPos());

			byte flags = 0;
			flags |= protectedRegion.preventBlockBreaking() ? 1 : 0;
			flags |= protectedRegion.preventBlockPlacing() ? (1 << 1) : 0;
			flags |= protectedRegion.preventExplosionsTNT() ? (1 << 2) : 0;
			flags |= protectedRegion.preventExplosionsOther() ? (1 << 3) : 0;
			flags |= protectedRegion.preventFireSpreading() ? (1 << 4) : 0;
			flags |= protectedRegion.preventEntitySpawning() ? (1 << 5) : 0;
			flags |= protectedRegion.ignoreNoBossOrNexus() ? (1 << 6) : 0;
			flags |= protectedRegion.isGenerating() ? (1 << 7) : 0;
			buf.writeByte(flags);

			Set<UUID> entityDependencies = protectedRegion.getEntityDependencies();
			buf.writeShort(entityDependencies.size());
			for (UUID uuid : entityDependencies) {
				ByteBufUtil.writeUuid(buf, uuid);
			}

			Set<BlockPos> blockDependencies = protectedRegion.getBlockDependencies();
			buf.writeShort(blockDependencies.size());
			for (BlockPos pos : blockDependencies) {
				ByteBufUtil.writeBlockPos(buf, pos);
			}
		}
	}

	public ByteBuf getProtectedRegions() {
		return this.buf;
	}

}
