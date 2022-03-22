package team.cqr.cqrepoured.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiNumberTextField extends GuiTextField {

	private final boolean allowNegative;
	private final boolean allowDouble;

	public GuiNumberTextField(int componentId, FontRenderer fontrendererIn, int x, int y, int widthIn, int heightIn,
			boolean allowNegative, boolean allowDouble) {
		super(componentId, fontrendererIn, x, y, widthIn, heightIn);
		this.allowNegative = allowNegative;
		this.allowDouble = allowDouble;
	}

	@Override
	public boolean textboxKeyTyped(char typedChar, int keyCode) {
		String oldText = this.getText();
		int oldPos = this.getCursorPosition();
		boolean flag = super.textboxKeyTyped(typedChar, keyCode);
		if (this.getText().isEmpty() || this.getText().equals("-")) {
			this.setText("0");
			return true;
		}
		try {
			if (!allowNegative && typedChar == '-') {
				throw new NumberFormatException();
			}
			if (allowDouble) {
				Double.parseDouble(this.getText());
			} else {
				Integer.parseInt(this.getText());
			}
		} catch (NumberFormatException e) {
			this.setText(oldText);
			this.setCursorPosition(oldPos);
			return false;
		}
		return flag;
	}

	public int getInt() throws NumberFormatException {
		return Integer.parseInt(this.getText());
	}

	public long getLong() throws NumberFormatException {
		return Long.parseLong(this.getText());
	}

	public double getDouble() throws NumberFormatException {
		return Double.parseDouble(this.getText());
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
	}

}
