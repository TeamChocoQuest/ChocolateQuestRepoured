package team.cqr.cqrepoured.network.datapacksynch.packet;

import java.util.Map;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;

import de.dertoaster.multihitboxlib.api.network.AbstractSPacketSyncDatapackContent;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.customtextures.TextureSet;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;

public class SPacketSyncTextureSet extends AbstractSPacketSyncDatapackContent<TextureSet, SPacketSyncTextureSet> {

	private static final Codec<Map<ResourceLocation, TextureSet>> _MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, TextureSet.CODEC);
	
	public SPacketSyncTextureSet() {
		super();
	}
	
	public SPacketSyncTextureSet(Map<ResourceLocation, TextureSet> data) {
		super(data);
	}
	
	@Override
	public Class<SPacketSyncTextureSet> getPacketClass() {
		return SPacketSyncTextureSet.class;
	}

	@Override
	public BiConsumer<ResourceLocation, TextureSet> consumer() {
		return CQRDatapackLoaders.TEXTURE_SETS.getData()::putIfAbsent;
	}

	@Override
	protected SPacketSyncTextureSet createFromPacket(Map<ResourceLocation, TextureSet> var1) {
		return new SPacketSyncTextureSet(var1);
	}

	@Override
	protected Codec<Map<ResourceLocation, TextureSet>> createMapper() {
		return _MAPPER;
	}

	@Override
	protected Codec<TextureSet> getCodec() {
		return TextureSet.CODEC;
	}

}
