package team.cqr.cqrepoured.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.gui.GuiDungeonMapTool;

@SideOnly(Side.CLIENT)
public class GuiButtonMapTool extends GuiButtonConfig {

	public GuiButtonMapTool(GuiConfig configGui, int buttonId, int x, int y, int widthIn, int heightIn,
			String buttonText) {
		super(configGui, buttonId, x, y, widthIn, heightIn, buttonText);
	}

	@Override
	protected void onButtonPressed(Minecraft mc, int mouseX, int mouseY) {
		mc.displayGuiScreen(new GuiDungeonMapTool(configGui));
	}

}
