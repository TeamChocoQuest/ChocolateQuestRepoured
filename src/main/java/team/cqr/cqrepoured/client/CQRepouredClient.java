package team.cqr.cqrepoured.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.cqr.cqrepoured.client.gui.*;
import team.cqr.cqrepoured.client.init.CQREntityRenderers;
import team.cqr.cqrepoured.init.CQRContainerTypes;

public class CQRepouredClient {

	public static void setupClient(FMLClientSetupEvent event)
	{
		//ScreenManager.register(CQRContainerTypes.SPAWNER.get(), GuiSpawner::new);
		ScreenManager.register(CQRContainerTypes.BOSS_BLOCK.get(), ScreenBossBlock::new);
		ScreenManager.register(CQRContainerTypes.BACKPACK.get(), ScreenBackpack::new);
		ScreenManager.register(CQRContainerTypes.ALCHEMY_BAG.get(), ScreenAlchemyBag::new);
		ScreenManager.register(CQRContainerTypes.BADGE.get(), ScreenBadge::new);

		CQREntityRenderers.registerRenderers();
	}

}
