package team.cqr.cqrepoured.network.server.packet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.customtextures.TextureSet;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketCustomTextures extends AbstractPacket<SPacketCustomTextures> {

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
	public SPacketCustomTextures fromBytes(PacketBuffer buf) {
		SPacketCustomTextures result = new SPacketCustomTextures();
		
		int keys = buf.readInt();
		int tscount = buf.readInt();
		while (keys > 0) {
			String k = buf.readUtf();
			byte[] v = new byte[buf.readInt()];
			buf.readBytes(v);
			result.entries.put(k, v);
			keys--;
		}

		while (tscount > 0) {
			String tsname = buf.readUtf();
			Map<ResourceLocation, Set<ResourceLocation>> entityTextureMap = result.textureSets.getOrDefault(tsname, new HashMap<>());
			int tskeys = buf.readInt();
			while (tskeys > 0) {
				String key = buf.readUtf();
				Set<ResourceLocation> values = new HashSet<>();
				int vals = buf.readInt();
				while (vals > 0) {
					String val = buf.readUtf();
					values.add(new ResourceLocation(val));
					vals--;
				}
				entityTextureMap.put(new ResourceLocation(key), values);
				tskeys--;
			}
			result.textureSets.put(tsname, entityTextureMap);
			tscount--;
		}
		
		return result;
	}

	@Override
	public void toBytes(SPacketCustomTextures packet, PacketBuffer buf) {
		buf.writeInt(packet.entries.size());
		buf.writeInt(packet.textureSets.size());
		// Textures
		for (Map.Entry<String, byte[]> entry : packet.entries.entrySet()) {
			buf.writeUtf(entry.getKey());
			buf.writeInt(entry.getValue().length);
			buf.writeBytes(entry.getValue());
		}
		// Texture sets
		for (Map.Entry<String, Map<ResourceLocation, Set<ResourceLocation>>> entry : packet.textureSets.entrySet()) {
			buf.writeUtf(entry.getKey());
			Map<ResourceLocation, Set<ResourceLocation>> entityTextureMap = entry.getValue();
			buf.writeInt(entityTextureMap.size());

			entityTextureMap.forEach((k, v) -> {
				buf.writeUtf(k.toString());
				buf.writeInt(v.size());
				for (ResourceLocation val : v) {
					buf.writeUtf(val.toString());
				}
			});
		}

	}

	public Map<String, byte[]> getTextureMap() {
		return this.entries;
	}

	public Map<String, Map<ResourceLocation, Set<ResourceLocation>>> getTextureSets() {
		return this.textureSets;
	}

	@Override
	public Class<SPacketCustomTextures> getPacketClass() {
		return SPacketCustomTextures.class;
	}

}
