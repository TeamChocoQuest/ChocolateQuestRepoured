package com.teamcqr.chocolatequestrepoured.network.packets.toServer;

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

	public SaveStructureRequestPacket(BlockPos startPos, BlockPos endPos, String authorName, String name, boolean hasShield, boolean partMode) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.author = authorName;
		this.structureName = name;
		this.usePartMode = partMode;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.startPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.endPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.author = ByteBufUtils.readUTF8String(buf);
		this.structureName = ByteBufUtils.readUTF8String(buf);
		this.usePartMode = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.startPos.getX());
		buf.writeInt(this.startPos.getY());
		buf.writeInt(this.startPos.getZ());

		buf.writeInt(this.endPos.getX());
		buf.writeInt(this.endPos.getY());
		buf.writeInt(this.endPos.getZ());

		ByteBufUtils.writeUTF8String(buf, this.author);
		ByteBufUtils.writeUTF8String(buf, this.structureName);
		buf.writeBoolean(this.usePartMode);
	}

	public BlockPos getStartPos() {
		return this.startPos;
	}

	public BlockPos getEndPos() {
		return this.endPos;
	}

	public String getAuthor() {
		return this.author;
	}

	public String getName() {
		return this.structureName;
	}

	public Boolean usePartMode() {
		return this.usePartMode;
	}

}
