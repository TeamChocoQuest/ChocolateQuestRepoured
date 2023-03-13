package team.cqr.cqrepoured.client.gui.button;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;

@SideOnly(Side.CLIENT)
public class GuiButtonReload extends GuiButtonConfig {

	public GuiButtonReload(GuiConfig configGui, int buttonId, int x, int y, int widthIn, int heightIn,
			String buttonText) {
		super(configGui, buttonId, x, y, widthIn, heightIn, buttonText);
	}

	@Override
	protected void onButtonPressed(Minecraft mc, int mouseX, int mouseY) {
		DungeonRegistry.getInstance().loadDungeonFiles();
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.drawButton(mc, mouseX, mouseY, partialTicks);
		if (this.isMouseOver()) {
			configGui.drawToolTip(Arrays.asList("Reloads all dungeon files located in config/CQR/dungeons."), mouseX,
					mouseY);
		}
	}

}
