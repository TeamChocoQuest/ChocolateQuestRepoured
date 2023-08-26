package team.cqr.cqrepoured.mhlib;

import de.dertoaster.multihitboxlib.api.event.server.AssetEnforcementManagerRegistrationEvent;
import de.dertoaster.multihitboxlib.api.event.server.SynchAssetFinderRegistrationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.mhlib.assetsynch.enforcers.CQRAssetEnforcer;
import team.cqr.cqrepoured.mhlib.assetsynch.finders.CQRAssetFinder;

@EventBusSubscriber(modid = CQRConstants.MODID, bus = Bus.MOD)
public class MHLibEvents {
	
	@SubscribeEvent
	public static void onAssetEnforcementRegistration(AssetEnforcementManagerRegistrationEvent event) {
		if (!event.tryAdd(CQRMain.prefixAssesEnforcementManager("cts-enforcer"), new CQRAssetEnforcer())) {
			//TODO: Log error
		}
	}
	
	@SubscribeEvent
	public static void onAssetFinderRegistration(SynchAssetFinderRegistrationEvent event) {
		if (!event.tryAdd(CQRMain.prefixAssetFinder("cts-asset-finder"), new CQRAssetFinder())) {
			//TODO: Log error
		}
	}

}
