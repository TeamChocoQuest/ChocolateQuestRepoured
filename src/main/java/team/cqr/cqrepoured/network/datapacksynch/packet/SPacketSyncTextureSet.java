package team.cqr.cqrepoured.network.datapacksynch.packet;

import java.util.Map;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;

import de.dertoaster.multihitboxlib.api.network.AbstractSPacketSyncDatapackContent;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.customtextures.TextureSetNew;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;

public class SPacketSyncTextureSet extends AbstractSPacketSyncDatapackContent<TextureSetNew, SPacketSyncTextureSet> {

	private static final Codec<Map<ResourceLocation, TextureSetNew>> _MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, TextureSetNew.CODEC);
	
	public SPacketSyncTextureSet() {
		super();
	}
	
	public SPacketSyncTextureSet(Map<ResourceLocation, TextureSetNew> data) {
		super(data);
	}
	
	@Override
	public Class<SPacketSyncTextureSet> getPacketClass() {
		return SPacketSyncTextureSet.class;
	}

	@Override
	public BiConsumer<ResourceLocation, TextureSetNew> consumer() {
		return CQRDatapackLoaders.TEXTURE_SETS.getData()::putIfAbsent;
	}

	@Override
	protected SPacketSyncTextureSet createFromPacket(Map<ResourceLocation, TextureSetNew> var1) {
		return new SPacketSyncTextureSet(var1);
	}

	@Override
	protected Codec<Map<ResourceLocation, TextureSetNew>> createMapper() {
		return _MAPPER;
	}

	@Override
	protected Codec<TextureSetNew> getCodec() {
		return TextureSetNew.CODEC;
	}

}
