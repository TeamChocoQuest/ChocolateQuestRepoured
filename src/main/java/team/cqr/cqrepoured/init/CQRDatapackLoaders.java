package team.cqr.cqrepoured.init;

import java.util.Optional;

import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.datapack.CQRCodecJsonDataManager;
import team.cqr.cqrepoured.common.datapack.DatapackLoaderHelper;
import team.cqr.cqrepoured.entity.trade.TradeProfile;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.DungeonInhabitant;

@Mod.EventBusSubscriber(modid = CQRepoured.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CQRDatapackLoaders implements DatapackLoaderHelper {
	
	// TODO: Create reload listener that forces all holders to reload
	
	public static final CQRCodecJsonDataManager<DungeonInhabitant> DUNGEON_INHABITANTS = new CQRCodecJsonDataManager<>("cqr/dungeon/inhabitant", DungeonInhabitant.CODEC);
	
	// TODO: Maybe make this synched for JEI integration?
	public static final CodecJsonDataManager<TradeProfile> TRADE_PROFILES = new CodecJsonDataManager<>("cqr/trades", TradeProfile.CODEC);
	
	public static Optional<TradeProfile> getTradeProfile(ResourceLocation tradeProfileId) {
		return DatapackLoaderHelper.getValueGeneral(TRADE_PROFILES, tradeProfileId);
	}
	
	public static Optional<DungeonInhabitant> getDungeonInhabitant(ResourceLocation inhabitantId) {
		return DatapackLoaderHelper.getValueGeneral(DUNGEON_INHABITANTS, inhabitantId);
	}
	

	
}
