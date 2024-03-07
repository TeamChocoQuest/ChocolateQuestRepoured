package team.cqr.cqrepoured.faction.init;

import java.util.Optional;

import javax.annotation.Nullable;

import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.datapack.CQRCodecJsonDataManager;
import team.cqr.cqrepoured.common.datapack.DatapackLoaderHelper;
import team.cqr.cqrepoured.faction.EntityFactionInformation;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.network.datapacksynch.packet.SPacketSyncFaction;
import team.cqr.cqrepoured.faction.network.datapacksynch.packet.SPacketSyncTextureSet;
import team.cqr.cqrepoured.faction.textureset.TextureSetNew;

@Mod.EventBusSubscriber(modid = CQRepoured.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FactionDatapackLoaders implements DatapackLoaderHelper {
	
	// TODO: Create reload listener that forces all holders to reload
	
	public static final CQRCodecJsonDataManager<TextureSetNew> TEXTURE_SETS = new CQRCodecJsonDataManager<>("cqr/texture_sets", TextureSetNew.CODEC);
	public static final CQRCodecJsonDataManager<Faction> FACTIONS = new CQRCodecJsonDataManager<>("cqr/factions", Faction.CODEC);
	public static final CQRCodecJsonDataManager<EntityFactionInformation> ENTITY_FACTION_INFORMATIONS = new CQRCodecJsonDataManager<>("entity/cqr_faction_information", EntityFactionInformation.CODEC);
	
	public static void init() {
		TEXTURE_SETS.subscribeAsSyncable(CQRepoured.NETWORK, SPacketSyncTextureSet::new);
		FACTIONS.subscribeAsSyncable(CQRepoured.NETWORK, SPacketSyncFaction::new);
	}
	
	/* Access methods */
	public static <T> Optional<T> getValueGeneral(final CodecJsonDataManager<T> manager, final ResourceLocation id) {
		return Optional.ofNullable(manager.getData().getOrDefault(id, null));
	}
	
	/* Type specific */
	public static Optional<TextureSetNew> getTextureSet(ResourceLocation textureSetId) {
		return getValueGeneral(TEXTURE_SETS, textureSetId);
	}
	
	public static Optional<TextureSetNew> getTextureSet(EntityType<?> entityType) {
		return getTextureSet(ForgeRegistries.ENTITY_TYPES.getKey(entityType));
	}
	
	public static Optional<Faction> getFaction(ResourceLocation factionId) {
		return getValueGeneral(FACTIONS, factionId);
	}
	
	@Nullable
	public static EntityFactionInformation getEntityFactionInformation(EntityType<?> entityType) {
		return ENTITY_FACTION_INFORMATIONS.getData().getOrDefault(ForgeRegistries.ENTITY_TYPES.getKey(entityType), null);
	}
	
}
