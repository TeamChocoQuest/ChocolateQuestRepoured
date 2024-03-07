package team.cqr.cqrepoured.common.datapack;

import java.util.Optional;

import commoble.databuddy.codec.RegistryDispatcher;
import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.entity.profile.EntityProfile;
import team.cqr.cqrepoured.common.entity.profile.variant.extradata.IVariantExtraData;

public class EntityProfileDatapackLoaders implements DatapackLoaderHelper {
	
	public static final CodecJsonDataManager<EntityProfile> ENTITY_PROFILES = new CodecJsonDataManager<>("entity/profile", EntityProfile.CODEC);
	public static final RegistryDispatcher<IVariantExtraData<?>> VARIANT_EXTRA_DATA_DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
			FMLJavaModLoadingContext.get().getModEventBus(), 
			CQRepoured.prefix("registry/dispatcher/variant/extradata"), 
			data -> data.getType(),
			builder -> {}
	); 
	
	public static Optional<EntityProfile> getProfile(EntityType<?> entityType) {
		return getProfile(ForgeRegistries.ENTITY_TYPES.getKey(entityType));
	}
	
	public static Optional<EntityProfile> getProfile(ResourceLocation profileId) {
		return DatapackLoaderHelper.getValueGeneral(ENTITY_PROFILES, profileId);
	}

}
