package team.cqr.cqrepoured.init;

import de.dertoaster.multihitboxlib.shadow.commoble.databuddy.data.CodecJsonDataManager;
import net.minecraftforge.fml.common.Mod;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.customtextures.TextureSet;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncTextureSet;

@Mod.EventBusSubscriber(modid = CQRMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CQRDatapackLoaders {

	public static final CodecJsonDataManager<TextureSet> TEXTURE_SETS = new CodecJsonDataManager<>("cqr/texture_sets", TextureSet.CODEC);
	
	public static void init() {
		TEXTURE_SETS.subscribeAsSyncable(CQRMain.NETWORK, SPacketSyncTextureSet::new);
	}
	
}
