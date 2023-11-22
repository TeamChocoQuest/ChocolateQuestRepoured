package team.cqr.cqrepoured.init;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import commoble.databuddy.codec.RegistryDispatcher;
import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.customtextures.TextureSetNew;
import team.cqr.cqrepoured.entity.profile.EntityProfile;
import team.cqr.cqrepoured.entity.profile.variant.extradata.IVariantExtraData;
import team.cqr.cqrepoured.entity.trade.TradeProfile;
import team.cqr.cqrepoured.faction.EntityFactionInformation;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncEntityProfiles;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncFaction;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncTextureSet;
import team.cqr.cqrepoured.util.CQRCodecJsonDataManager;
import team.cqr.cqrepoured.util.registration.RegistrationIDSupplier;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;

@Mod.EventBusSubscriber(modid = CQRConstants.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CQRDatapackLoaders {
	
	// TODO: Create reload listener that forces all holders to reload
	
	public static final CQRCodecJsonDataManager<TextureSetNew> TEXTURE_SETS = new CQRCodecJsonDataManager<>("cqr/texture_sets", TextureSetNew.CODEC);
	public static final CQRCodecJsonDataManager<Faction> FACTIONS = new CQRCodecJsonDataManager<>("cqr/factions", Faction.CODEC);
	public static final CQRCodecJsonDataManager<EntityFactionInformation> ENTITY_FACTION_INFORMATIONS = new CQRCodecJsonDataManager<>("entity/cqr_faction_information", EntityFactionInformation.CODEC);
	public static final CQRCodecJsonDataManager<DungeonInhabitant> DUNGEON_INHABITANTS = new CQRCodecJsonDataManager<>("cqr/dungeon/inhabitant", DungeonInhabitant.CODEC);
	
	// TODO: Maybe make this synched for JEI integration?
	public static final CodecJsonDataManager<TradeProfile> TRADE_PROFILES = new CodecJsonDataManager<>("cqr/trades", TradeProfile.CODEC);
	public static final CodecJsonDataManager<EntityProfile> ENTITY_PROFILES = new CodecJsonDataManager<>("entity/profile", EntityProfile.CODEC);
	public static final RegistryDispatcher<IVariantExtraData<?>> VARIANT_EXTRA_DATA_DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
			FMLJavaModLoadingContext.get().getModEventBus(), 
			CQRMain.prefix("registry/dispatcher/variant/extradata"), 
			data -> data.getType(),
			builder -> {}
	); 
	
	public static void init() {
		TEXTURE_SETS.subscribeAsSyncable(CQRMain.NETWORK, SPacketSyncTextureSet::new);
		FACTIONS.subscribeAsSyncable(CQRMain.NETWORK, SPacketSyncFaction::new);
		ENTITY_PROFILES.subscribeAsSyncable(CQRMain.NETWORK, SPacketSyncEntityProfiles::new);
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
	
	public static Optional<TradeProfile> getTradeProfile(ResourceLocation tradeProfileId) {
		return getValueGeneral(TRADE_PROFILES, tradeProfileId);
	}
	
	public static Optional<DungeonInhabitant> getDungeonInhabitant(ResourceLocation inhabitantId) {
		return getValueGeneral(DUNGEON_INHABITANTS, inhabitantId);
	}
	
	public static Optional<EntityProfile> getProfile(EntityType<?> entityType) {
		return getProfile(ForgeRegistries.ENTITY_TYPES.getKey(entityType));
	}
	
	public static Optional<EntityProfile> getProfile(ResourceLocation profileId) {
		return getValueGeneral(ENTITY_PROFILES, profileId);
	}
	
	@Nullable
	public static EntityFactionInformation getEntityFactionInformation(EntityType<?> entityType) {
		return ENTITY_FACTION_INFORMATIONS.getData().getOrDefault(ForgeRegistries.ENTITY_TYPES.getKey(entityType), null);
	}
	
	/* Access codecs */
	public static <T extends RegistrationIDSupplier> Codec<T> byNameCodec(final Function<ResourceLocation, T> retrievalFunction) {
		return byNameCodecFor(T::getId, retrievalFunction);
	}
	
	public static <T> Codec<T> byNameCodecFor(final Function<T, ResourceLocation> idRetrievalFunction, final Function<ResourceLocation, T> retrievalFunction) {
		return ResourceLocation.CODEC.flatXmap((rs) -> {
			return Optional.ofNullable(retrievalFunction.apply(rs)).map(DataResult::success).orElseGet(() -> {
				return DataResult.error(() -> {
					return "Unknown element id:" + rs.toString();
				});
			});
		}, (obj) -> {
			return Optional.ofNullable(idRetrievalFunction.apply(obj)).map(DataResult::success).orElseGet(() -> {
				return DataResult.error(() -> {
					return "Element with unknown id:" + obj.toString();
				});
			});
		});
	}
	
}
