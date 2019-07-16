package com.teamcqr.chocolatequestrepoured.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CQSaveStructureRequestPacket implements IMessage {

	private BlockPos startPos;
	private BlockPos endPos;
	private String author;
	private String structureName;
	private boolean hasShield;
	
	public CQSaveStructureRequestPacket(BlockPos startPos, BlockPos endPos, String authorName, String name, boolean hasShield) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.author = authorName;
		this.structureName = name;
		this.hasShield = hasShield;
	}
	
	public CQSaveStructureRequestPacket() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int x,y,z;
		//StartPos
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		startPos = new BlockPos(x,y,z);
		
		//EndPos
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		endPos = new BlockPos(x,y,z);
		
		//Author
		author = ByteBufUtils.readUTF8String(buf);
		
		//Name
		structureName = ByteBufUtils.readUTF8String(buf);
		
		//Shield Bool
		hasShield = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//StartPos XYZ
		buf.writeInt(startPos.getX());
		buf.writeInt(startPos.getY());
		buf.writeInt(startPos.getZ());
		
		//EndPos XYZ
		buf.writeInt(endPos.getX());
		buf.writeInt(endPos.getY());
		buf.writeInt(endPos.getZ());
		
		//Author
		ByteBufUtils.writeUTF8String(buf, author);
		
		//Name
		ByteBufUtils.writeUTF8String(buf, structureName);
		
		//Shield bool
		buf.writeBoolean(hasShield);
	}

}
