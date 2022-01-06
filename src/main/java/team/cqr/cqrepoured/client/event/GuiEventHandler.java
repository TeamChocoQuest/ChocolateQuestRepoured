package team.cqr.cqrepoured.client.event;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.gui.GuiDungeonMapTool;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;

@EventBusSubscriber(modid = CQRMain.MODID, value = Dist.CLIENT)
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

		Button buttonReloadDungeons = new Button(0, scaled.getScaledWidth() - 102, 2, 100, 20, "Reload Dungeons") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				DungeonRegistry.getInstance().loadDungeonFiles();
				return true;
			}

			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
				super.drawButton(mc, mouseX, mouseY, partialTicks);
				if (this.isMouseOver()) {
					gui.drawToolTip(Arrays.asList("Reloads all dungeon files located in config/CQR/dungeons."), mouseX, mouseY);
				}
			}
		};
		buttonReloadDungeons.enabled = mc.world == null || mc.isGamePaused();
		event.getButtonList().add(buttonReloadDungeons);

		Button buttonMapTool = new Button(1, 2, 2, 100, 20, "Map Tool") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				mc.displayGuiScreen(new GuiDungeonMapTool(gui));
				return true;
			}
		};
		buttonMapTool.enabled = mc.world == null;
		event.getButtonList().add(buttonMapTool);
	}

}
