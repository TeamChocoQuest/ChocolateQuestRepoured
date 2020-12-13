package team.cqr.cqrepoured.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import team.cqr.cqrepoured.client.util.GuiHelper;

public class GuiNumberTextField extends GuiTextField {

	private final boolean allowNegative;
	private final boolean allowDouble;

	public GuiNumberTextField(int componentId, FontRenderer fontrendererIn, int x, int y, int widthIn, int heightIn, boolean allowNegative, boolean allowDouble) {
		super(componentId, fontrendererIn, x, y, widthIn, heightIn);
		this.allowNegative = allowNegative;
		this.allowDouble = allowDouble;
	}

	@Override
	public boolean textboxKeyTyped(char typedChar, int keyCode) {
		if (!GuiHelper.isValidCharForNumberTextField(typedChar, keyCode, this.allowNegative, this.allowDouble)) {
			return false;
		}
		return super.textboxKeyTyped(typedChar, keyCode);
	}

	public int getInt() throws NumberFormatException {
		return Integer.parseInt(this.getText());
	}

	public double getDouble() throws NumberFormatException {
		return Double.parseDouble(this.getText());
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
	}

}
