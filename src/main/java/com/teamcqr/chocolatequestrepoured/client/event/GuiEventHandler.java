package com.teamcqr.chocolatequestrepoured.client.event;

import java.util.Arrays;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonRegistry;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public class GuiEventHandler {

	@SubscribeEvent
	public static void onInitGuiEvent(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof GuiConfig) {
			GuiConfig gui = (GuiConfig) event.getGui();
			if (gui.modID.equals(Reference.MODID) && gui.parentScreen instanceof GuiModList) {
				Minecraft mc = Minecraft.getMinecraft();
				ScaledResolution scaled = new ScaledResolution(mc);
				event.getButtonList().add(new GuiButtonReloadDungeons(0, scaled.getScaledWidth() - 102, 2, 100, 20, "Reload Dungeons", gui, mc.world == null));
			}
		}
	}

	@SubscribeEvent
	public static void onActionPerformedEvent(GuiScreenEvent.ActionPerformedEvent.Pre event) {
		if (event.getButton() instanceof GuiButtonReloadDungeons && Minecraft.getMinecraft().world == null) {
			DungeonRegistry.getInstance().reloadDungeonFiles();
			event.setCanceled(true);
		}
	}

	private static class GuiButtonReloadDungeons extends GuiButton {

		private GuiConfig gui;

		public GuiButtonReloadDungeons(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, GuiConfig gui, boolean enabled) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
			this.gui = gui;
			this.enabled = enabled;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			super.drawButton(mc, mouseX, mouseY, partialTicks);
			if (this.isMouseOver()) {
				this.gui.drawToolTip(Arrays.asList("Reloads all dungeon files located in config/CQR/dungeons.", "Only works while in the main menu."), mouseX, mouseY);
			}
		}

	}

}
