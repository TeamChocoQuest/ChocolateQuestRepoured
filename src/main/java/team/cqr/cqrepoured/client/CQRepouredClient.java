package team.cqr.cqrepoured.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.cqr.cqrepoured.client.gui.GuiSpawner;
import team.cqr.cqrepoured.client.gui.ScreenBossBlock;
import team.cqr.cqrepoured.init.CQRContainerTypes;

public class CQRepouredClient {

	public static void setupClient(FMLClientSetupEvent event)
	{
		ScreenManager.register(CQRContainerTypes.SPAWNER.get(), GuiSpawner::new);
		ScreenManager.register(CQRContainerTypes.BOSS_BLOCK.get(), ScreenBossBlock::new);

		//CQREntityRenderers.registerRenderers();
	}

}
