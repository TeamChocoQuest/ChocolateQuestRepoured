package team.cqr.cqrepoured.faction.network.datapacksynch.packet;

import java.util.Map;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;

import de.dertoaster.multihitboxlib.api.network.AbstractSPacketSyncDatapackContent;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.init.FactionDatapackLoaders;

public class SPacketSyncFaction extends AbstractSPacketSyncDatapackContent<Faction, SPacketSyncFaction> {

	private static final Codec<Map<ResourceLocation, Faction>> _MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, Faction.CODEC);
	
	public SPacketSyncFaction() {
		super();
	}
	
	public SPacketSyncFaction(Map<ResourceLocation, Faction> data) {
		super(data);
	}
	
	@Override
	public Class<SPacketSyncFaction> getPacketClass() {
		return SPacketSyncFaction.class;
	}

	@Override
	public BiConsumer<ResourceLocation, Faction> consumer() {
		return FactionDatapackLoaders.FACTIONS.getData()::putIfAbsent;
	}

	@Override
	protected SPacketSyncFaction createFromPacket(Map<ResourceLocation, Faction> var1) {
		return new SPacketSyncFaction(var1);
	}

	@Override
	protected Codec<Map<ResourceLocation, Faction>> createMapper() {
		return _MAPPER;
	}

	@Override
	protected Codec<Faction> getCodec() {
		return Faction.CODEC;
	}
}
