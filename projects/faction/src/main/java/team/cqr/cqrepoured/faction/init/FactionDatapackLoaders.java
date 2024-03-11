package team.cqr.cqrepoured.faction.init;

import java.util.Optional;

import javax.annotation.Nullable;

import de.dertoaster.multihitboxlib.api.DatapackRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.datapack.DatapackLoaderHelper;
import team.cqr.cqrepoured.faction.EntityFactionInformation;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.textureset.TextureSetNew;

@Mod.EventBusSubscriber(modid = CQRepoured.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FactionDatapackLoaders implements DatapackLoaderHelper {
	
	// TODO: Create reload listener that forces all holders to reload
	
	public static final DatapackRegistry<TextureSetNew> TEXTURE_SETS = new DatapackRegistry<>(CQRepoured.prefix("cqr/texture_sets"), TextureSetNew.CODEC);
	public static final DatapackRegistry<Faction> FACTIONS = new DatapackRegistry<>(CQRepoured.prefix("cqr/factions"), Faction.CODEC);
	public static final DatapackRegistry<EntityFactionInformation> ENTITY_FACTION_INFORMATIONS = new DatapackRegistry<>(CQRepoured.prefix("entity/cqr_faction_information"), EntityFactionInformation.CODEC);
	
	public static void init() {
		//TEXTURE_SETS.subscribeAsSyncable(CQRServices.NETWORK.network(), SPacketSyncTextureSet::new);
		//FACTIONS.subscribeAsSyncable(CQRServices.NETWORK.network(), SPacketSyncFaction::new);
	}
	
	/* Access methods */
	public static <T> Optional<T> getValueGeneral(final DatapackRegistry<T> manager, final ResourceLocation id, RegistryAccess registryAccess) {
		return Optional.ofNullable(manager.get(id, registryAccess));
	}
	
	/* Type specific */
	public static Optional<TextureSetNew> getTextureSet(ResourceLocation textureSetId, RegistryAccess registryAccess) {
		return getValueGeneral(TEXTURE_SETS, textureSetId, registryAccess);
	}
	
	public static Optional<TextureSetNew> getTextureSet(EntityType<?> entityType, RegistryAccess registryAccess) {
		return getTextureSet(ForgeRegistries.ENTITY_TYPES.getKey(entityType), registryAccess);
	}
	
	public static Optional<Faction> getFaction(ResourceLocation factionId, RegistryAccess registryAccess) {
		return getValueGeneral(FACTIONS, factionId, registryAccess);
	}
	
	@Nullable
	public static EntityFactionInformation getEntityFactionInformation(EntityType<?> entityType, RegistryAccess registryAccess) {
		return ENTITY_FACTION_INFORMATIONS.get(ForgeRegistries.ENTITY_TYPES.getKey(entityType), registryAccess);
	}

	@SubscribeEvent
	public static void register(DataPackRegistryEvent.NewRegistry event) {
		TEXTURE_SETS.registerSynchable(event);
		FACTIONS.registerSynchable(event);
		ENTITY_FACTION_INFORMATIONS.register(event);
	}
	
}
