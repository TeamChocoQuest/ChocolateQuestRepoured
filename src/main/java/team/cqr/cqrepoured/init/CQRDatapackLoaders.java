package team.cqr.cqrepoured.init;

import java.util.Optional;

import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.customtextures.TextureSet;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncTextureSet;

@Mod.EventBusSubscriber(modid = CQRMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CQRDatapackLoaders {

	public static final CodecJsonDataManager<TextureSet> TEXTURE_SETS = new CodecJsonDataManager<>("cqr/texture_sets", TextureSet.CODEC);
	
	public static void init() {
		TEXTURE_SETS.subscribeAsSyncable(CQRMain.NETWORK, SPacketSyncTextureSet::new);
	}
	
	public static <T> Optional<T> getValueGeneral(final CodecJsonDataManager<T> manager, final ResourceLocation id) {
		return Optional.ofNullable(manager.getData().getOrDefault(id, null));
	}
	
	public static Optional<TextureSet> getTextureSet(ResourceLocation entityID) {
		return getValueGeneral(TEXTURE_SETS, entityID);
	}
	
	public static Optional<TextureSet> getTextureSet(EntityType<?> entityType) {
		return getTextureSet(ForgeRegistries.ENTITY_TYPES.getKey(entityType));
	}
	
}
