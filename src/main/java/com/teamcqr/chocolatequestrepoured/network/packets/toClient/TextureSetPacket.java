package com.teamcqr.chocolatequestrepoured.network.packets.toClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TextureSetPacket implements IMessage {
	
	private String setName;
	private final Map<ResourceLocation, Set<ResourceLocation>> entityTextureMap;
	
	public TextureSetPacket() {
		entityTextureMap = new HashMap<>();
	}
	
	public TextureSetPacket(String setName, Map<ResourceLocation, Set<ResourceLocation>> entityTextureMap) {
		this.setName = setName;
		this.entityTextureMap = entityTextureMap;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.setName = ByteBufUtils.readUTF8String(buf);
		entityTextureMap.clear();
		int keys = buf.readInt();
		while(keys > 0) {
			String key = ByteBufUtils.readUTF8String(buf);
			Set<ResourceLocation> values = new HashSet<>();
			int vals = buf.readInt();
			while(vals > 0) {
				String val = ByteBufUtils.readUTF8String(buf);
				values.add(new ResourceLocation(val));
				vals--;
			}
			entityTextureMap.put(new ResourceLocation(key), values);
			keys--;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.setName);
		buf.writeInt(entityTextureMap.keySet().size());
		for(ResourceLocation key : entityTextureMap.keySet()) {
			ByteBufUtils.writeUTF8String(buf, key.toString());
			buf.writeInt(entityTextureMap.get(key).size());
			for(ResourceLocation val : entityTextureMap.get(key)) {
				ByteBufUtils.writeUTF8String(buf, val.toString());
			}
		}
	}

}
