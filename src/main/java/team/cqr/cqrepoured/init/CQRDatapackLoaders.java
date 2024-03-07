package team.cqr.cqrepoured.init;

import java.util.Optional;

import commoble.databuddy.codec.RegistryDispatcher;
import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.datapack.CQRCodecJsonDataManager;
import team.cqr.cqrepoured.common.datapack.DatapackLoaderHelper;
import team.cqr.cqrepoured.entity.profile.EntityProfile;
import team.cqr.cqrepoured.entity.profile.variant.extradata.IVariantExtraData;
import team.cqr.cqrepoured.entity.trade.TradeProfile;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonInhabitant;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncEntityProfiles;

@Mod.EventBusSubscriber(modid = CQRepoured.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CQRDatapackLoaders implements DatapackLoaderHelper {
	
	// TODO: Create reload listener that forces all holders to reload
	
	public static final CQRCodecJsonDataManager<DungeonInhabitant> DUNGEON_INHABITANTS = new CQRCodecJsonDataManager<>("cqr/dungeon/inhabitant", DungeonInhabitant.CODEC);
	
	// TODO: Maybe make this synched for JEI integration?
	public static final CodecJsonDataManager<TradeProfile> TRADE_PROFILES = new CodecJsonDataManager<>("cqr/trades", TradeProfile.CODEC);
	
	public static void init() {
		ENTITY_PROFILES.subscribeAsSyncable(CQRMain.NETWORK, SPacketSyncEntityProfiles::new);
	}
	
	public static Optional<TradeProfile> getTradeProfile(ResourceLocation tradeProfileId) {
		return getValueGeneral(TRADE_PROFILES, tradeProfileId);
	}
	
	public static Optional<DungeonInhabitant> getDungeonInhabitant(ResourceLocation inhabitantId) {
		return getValueGeneral(DUNGEON_INHABITANTS, inhabitantId);
	}
	

	
}
