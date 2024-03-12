package team.cqr.cqrepoured.common.datapack;

import java.util.Optional;

import commoble.databuddy.codec.RegistryDispatcher;
import de.dertoaster.multihitboxlib.api.DatapackRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.entity.profile.EntityProfile;
import team.cqr.cqrepoured.common.entity.profile.variant.extradata.IVariantExtraData;

//@EventBusSubscriber(modid = CQRepoured.MODID, bus = Bus.MOD)
public class EntityProfileDatapackRegistries implements DatapackLoaderHelper {
	
	public static final DatapackRegistry<EntityProfile> ENTITY_PROFILES = new DatapackRegistry<>(CQRepoured.prefix("entity/profile"), EntityProfile.CODEC);
	public static final RegistryDispatcher<IVariantExtraData<?>> VARIANT_EXTRA_DATA_DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
			FMLJavaModLoadingContext.get().getModEventBus(), 
			CQRepoured.prefix("registry/dispatcher/variant/extradata"), 
			data -> data.getType(),
			builder -> {}
	); 
	
	public static Optional<EntityProfile> getProfile(EntityType<?> entityType, RegistryAccess registryAccess) {
		return getProfile(ForgeRegistries.ENTITY_TYPES.getKey(entityType), registryAccess);
	}
	
	public static void init() {
		//ENTITY_PROFILES.subscribeAsSyncable(CQRServices.NETWORK.network(), SPacketSyncEntityProfiles::new);
	}
	
	public static Optional<EntityProfile> getProfile(ResourceLocation profileId, RegistryAccess registryAccess) {
		return Optional.ofNullable(ENTITY_PROFILES.get(profileId, registryAccess));
	}
	
	@SubscribeEvent
	public static void register(DataPackRegistryEvent.NewRegistry event) {
		ENTITY_PROFILES.registerSynchable(event);
	}

}
