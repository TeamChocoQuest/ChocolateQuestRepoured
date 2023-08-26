package team.cqr.cqrepoured.init;

import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonElement;

import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.customtextures.TextureSetNew;
import team.cqr.cqrepoured.entity.profile.EntityProfile;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncEntityProfiles;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncTextureSet;

@Mod.EventBusSubscriber(modid = CQRConstants.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CQRDatapackLoaders {

	public static final CodecJsonDataManager<TextureSetNew> TEXTURE_SETS = new CodecJsonDataManager<>("cqr/texture_sets", TextureSetNew.CODEC) {
		protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
			super.apply(jsons, resourceManager, profiler);
			
			this.data.entrySet().forEach(entry -> {
				entry.getValue().setId(entry.getKey());
			});
		};
	};
	public static final CodecJsonDataManager<EntityProfile> ENTITY_PROFILES = new CodecJsonDataManager<>("entity/profile", EntityProfile.CODEC);
	
	public static void init() {
		TEXTURE_SETS.subscribeAsSyncable(CQRMain.NETWORK, SPacketSyncTextureSet::new);
		ENTITY_PROFILES.subscribeAsSyncable(CQRMain.NETWORK, SPacketSyncEntityProfiles::new);
	}
	
	public static <T> Optional<T> getValueGeneral(final CodecJsonDataManager<T> manager, final ResourceLocation id) {
		return Optional.ofNullable(manager.getData().getOrDefault(id, null));
	}
	
	public static Optional<TextureSetNew> getTextureSet(ResourceLocation textureSetId) {
		return getValueGeneral(TEXTURE_SETS, textureSetId);
	}
	
	public static Optional<TextureSetNew> getTextureSet(EntityType<?> entityType) {
		return getTextureSet(ForgeRegistries.ENTITY_TYPES.getKey(entityType));
	}
	
}
