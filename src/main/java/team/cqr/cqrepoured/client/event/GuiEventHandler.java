package team.cqr.cqrepoured.client.event;

import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//@EventBusSubscriber(modid = CQRConstants.MODID, value = Dist.CLIENT)
public class GuiEventHandler {

	@SubscribeEvent
	public static void onInitGuiEvent(ScreenEvent.Init.Post event) {
//		if (!(event.getGui() instanceof GuiConfig)) {
//			return;
//		}
//
//		GuiConfig gui = (GuiConfig) event.getGui();
//		if (!gui.modID.equals(CQRConstants.MODID) || !(gui.parentScreen instanceof ModListScreen)) {
//			return;
//		}
//
//		Minecraft mc = Minecraft.getInstance();
//		ScaledResolution scaled = new ScaledResolution(mc);
//
//		Button buttonReloadDungeons = new Button(0, scaled.getScaledWidth() - 102, 2, 100, 20, "Reload Dungeons") {
//			@Override
//			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
//				if (!super.mousePressed(mc, mouseX, mouseY)) {
//					return false;
//				}
//				DungeonRegistry.getInstance().loadDungeonFiles();
//				return true;
//			}
//
//			@Override
//			public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//				super.drawButton(mc, mouseX, mouseY, partialTicks);
//				if (this.isMouseOver()) {
//					gui.drawToolTip(Arrays.asList("Reloads all dungeon files located in config/CQR/dungeons."), mouseX, mouseY);
//				}
//			}
//		};
//		buttonReloadDungeons.enabled = mc.world == null || mc.isGamePaused();
//		event.getButtonList().add(buttonReloadDungeons);
//
//		Button buttonMapTool = new Button(1, 2, 2, 100, 20, "Map Tool") {
//			@Override
//			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
//				if (!super.mousePressed(mc, mouseX, mouseY)) {
//					return false;
//				}
//				mc.displayGuiScreen(new GuiDungeonMapTool(gui));
//				return true;
//			}
//		};
//		buttonMapTool.enabled = mc.world == null;
//		event.getButtonList().add(buttonMapTool);
	}

}
