package com.teamcqr.chocolatequestrepoured.network.packets.toClient;

import java.util.HashMap;

import org.apache.commons.lang3.SerializationUtils;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CustomTexturesPacket implements IMessage {

	private HashMap<String, String> entries = new HashMap<>();
	
	public CustomTexturesPacket() {
		
	}
	
	public void addPair(String file, String path) {
		this.entries.put(file,path);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		//this.entries = SerializationUtils.deserialize(buf.read)
		int byteCount = buf.readInt();
		byte[] bytes = new byte[byteCount];
		buf.readBytes(bytes);
		this.entries = SerializationUtils.deserialize(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		byte[] bytes = SerializationUtils.serialize(this.entries);
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}

}
