package team.cqr.cqrepoured.network.server.packet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.customtextures.TextureSet;

public class SPacketCustomTextures implements IMessage {

	private Map<String, byte[]> entries = new HashMap<>();
	private Map<String, Map<ResourceLocation, Set<ResourceLocation>>> textureSets = new HashMap<>();

	public SPacketCustomTextures() {

	}

	public void addPair(byte[] file, String path) {
		this.entries.put(path, file);
	}

	public void addTextureSet(TextureSet ts) {
		this.textureSets.put(ts.getName(), ts.getMappings());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int keys = buf.readInt();
		int tscount = buf.readInt();
		while (keys > 0) {
			String k = ByteBufUtils.readUTF8String(buf);
			byte[] v = new byte[buf.readInt()];
			buf.readBytes(v);
			this.entries.put(k, v);
			keys--;
		}

		while (tscount > 0) {
			String tsname = ByteBufUtils.readUTF8String(buf);
			Map<ResourceLocation, Set<ResourceLocation>> entityTextureMap = this.textureSets.getOrDefault(tsname, new HashMap<>());
			int tskeys = buf.readInt();
			while (tskeys > 0) {
				String key = ByteBufUtils.readUTF8String(buf);
				Set<ResourceLocation> values = new HashSet<>();
				int vals = buf.readInt();
				while (vals > 0) {
					String val = ByteBufUtils.readUTF8String(buf);
					values.add(new ResourceLocation(val));
					vals--;
				}
				entityTextureMap.put(new ResourceLocation(key), values);
				tskeys--;
			}
			this.textureSets.put(tsname, entityTextureMap);
			tscount--;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entries.size());
		buf.writeInt(this.textureSets.size());
		// Textures
		for (Map.Entry<String, byte[]> entry : this.entries.entrySet()) {
			ByteBufUtils.writeUTF8String(buf, entry.getKey());
			buf.writeInt(entry.getValue().length);
			buf.writeBytes(entry.getValue());
		}
		// Texture sets
		for (Map.Entry<String, Map<ResourceLocation, Set<ResourceLocation>>> entry : this.textureSets.entrySet()) {
			ByteBufUtils.writeUTF8String(buf, entry.getKey());
			Map<ResourceLocation, Set<ResourceLocation>> entityTextureMap = entry.getValue();
			buf.writeInt(entityTextureMap.size());

			for (ResourceLocation key : entityTextureMap.keySet()) {
				ByteBufUtils.writeUTF8String(buf, key.toString());
				buf.writeInt(entityTextureMap.get(key).size());
				for (ResourceLocation val : entityTextureMap.get(key)) {
					ByteBufUtils.writeUTF8String(buf, val.toString());
				}
			}
		}

	}

	public Map<String, byte[]> getTextureMap() {
		return this.entries;
	}

	public Map<String, Map<ResourceLocation, Set<ResourceLocation>>> getTextureSets() {
		return this.textureSets;
	}

}
