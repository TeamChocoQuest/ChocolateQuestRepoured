package team.cqr.cqrepoured.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.gui.button.GuiButtonMapTool;
import team.cqr.cqrepoured.client.gui.button.GuiButtonMigrateStructures;
import team.cqr.cqrepoured.client.gui.button.GuiButtonReload;

@EventBusSubscriber(modid = CQRMain.MODID, value = Side.CLIENT)
public class GuiEventHandler {

	@SubscribeEvent
	public static void onInitGuiEvent(GuiScreenEvent.InitGuiEvent.Post event) {
		if (!(event.getGui() instanceof GuiConfig)) {
			return;
		}

		GuiConfig gui = (GuiConfig) event.getGui();
		if (!gui.modID.equals(CQRMain.MODID) || !(gui.parentScreen instanceof GuiModList)) {
			return;
		}

		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaled = new ScaledResolution(mc);

		GuiButton buttonReloadDungeons = new GuiButtonReload(gui, 0, scaled.getScaledWidth() - 102, 2, 100, 20,
				"Reload Dungeons");
		buttonReloadDungeons.enabled = mc.world == null || mc.isGamePaused();
		event.getButtonList().add(buttonReloadDungeons);

		GuiButton buttonMapTool = new GuiButtonMapTool(gui, 1, 2, 2, 100, 20, "Map Tool");
		buttonMapTool.enabled = mc.world == null;
		event.getButtonList().add(buttonMapTool);

		GuiButton buttonMigrateDungeons = new GuiButtonMigrateStructures(gui, 2, 2 + 10 + 100, 2, 150, 20,
				"Migrate Structure Files");
		buttonMigrateDungeons.enabled = mc.world == null;
		event.getButtonList().add(buttonMigrateDungeons);
	}

}
