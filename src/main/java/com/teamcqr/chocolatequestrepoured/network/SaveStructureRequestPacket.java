package com.teamcqr.chocolatequestrepoured.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SaveStructureRequestPacket implements IMessage {

	private BlockPos startPos;
	private BlockPos endPos;
	private String author;
	private String structureName;
	private boolean usePartMode;

	public SaveStructureRequestPacket() {

	}

	public SaveStructureRequestPacket(BlockPos startPos, BlockPos endPos, String authorName, String name,
			boolean hasShield, boolean partMode) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.author = authorName;
		this.structureName = name;
		this.usePartMode = partMode;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		startPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		endPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		author = ByteBufUtils.readUTF8String(buf);
		structureName = ByteBufUtils.readUTF8String(buf);
		usePartMode = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(startPos.getX());
		buf.writeInt(startPos.getY());
		buf.writeInt(startPos.getZ());

		buf.writeInt(endPos.getX());
		buf.writeInt(endPos.getY());
		buf.writeInt(endPos.getZ());

		ByteBufUtils.writeUTF8String(buf, author);
		ByteBufUtils.writeUTF8String(buf, structureName);
		buf.writeBoolean(usePartMode);
	}

	public BlockPos getStartPos() {
		return startPos;
	}

	public BlockPos getEndPos() {
		return endPos;
	}

	public String getAuthor() {
		return author;
	}

	public String getName() {
		return structureName;
	}

	public Boolean usePartMode() {
		return usePartMode;
	}

}
