package team.cqr.cqrepoured.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiButtonConfig extends GuiButton {

	protected final GuiConfig configGui;

	public GuiButtonConfig(GuiConfig configGui, int buttonId, int x, int y, int widthIn, int heightIn,
			String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.configGui = configGui;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (!super.mousePressed(mc, mouseX, mouseY)) {
			return false;
		}
		this.onButtonPressed(mc, mouseX, mouseY);
		return true;
	}

	protected abstract void onButtonPressed(Minecraft mc, int mouseX, int mouseY);

}
