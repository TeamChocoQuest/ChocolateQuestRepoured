package team.cqr.cqrepoured.network.datapacksynch.packet;

import java.util.Map;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;

import de.dertoaster.multihitboxlib.api.network.AbstractSPacketSyncDatapackContent;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.entity.profile.EntityProfile;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;

public class SPacketSyncEntityProfiles extends AbstractSPacketSyncDatapackContent<EntityProfile, SPacketSyncEntityProfiles> {
	
	private static final Codec<Map<ResourceLocation, EntityProfile>> _MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, EntityProfile.CODEC);
	
	public SPacketSyncEntityProfiles() {
		super();
	}
	
	public SPacketSyncEntityProfiles(Map<ResourceLocation, EntityProfile> data) {
		super(data);
	}

	@Override
	public Class<SPacketSyncEntityProfiles> getPacketClass() {
		return SPacketSyncEntityProfiles.class;
	}

	@Override
	public BiConsumer<ResourceLocation, EntityProfile> consumer() {
		return CQRDatapackLoaders.ENTITY_PROFILES.getData()::putIfAbsent;
	}

	@Override
	protected SPacketSyncEntityProfiles createFromPacket(Map<ResourceLocation, EntityProfile> arg0) {
		return new SPacketSyncEntityProfiles(arg0);
	}

	@Override
	protected Codec<Map<ResourceLocation, EntityProfile>> createMapper() {
		return _MAPPER;
	}

	@Override
	protected Codec<EntityProfile> getCodec() {
		return EntityProfile.CODEC;
	}

}
