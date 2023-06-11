package team.cqr.cqrepoured.client.util;

import java.awt.TextField;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class GuiHelper {

	public static void drawTexture(double x, double y, double u, double v, double width, double height, double texWidth, double texHeight) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(u, v + texHeight);
		GL11.glVertex2d(x, y + height);

		GL11.glTexCoord2d(u + texWidth, v + texHeight);
		GL11.glVertex2d(x + width, y + height);

		GL11.glTexCoord2d(u + texWidth, v);
		GL11.glVertex2d(x + width, y);

		GL11.glTexCoord2d(u, v);
		GL11.glVertex2d(x, y);
		GL11.glEnd();
	}

	public static void drawString(FontRenderer fontRenderer, MatrixStack matrixStack, String text, int x, int y, int color, boolean centered, boolean withShadow) {
		if (centered) {
			x -= fontRenderer.width(text) / 2;
		}
		if (withShadow) {
			fontRenderer.drawShadow(matrixStack, text, x, y, color);
		} else {
			fontRenderer.draw(matrixStack, text, x, y, color);
		}
	}

	public static boolean isMouseOver(int mouseX, int mouseY, TextField textField) {
		if (mouseX < textField.getX()) {
			return false;
		}
		if (mouseX > textField.getX() + textField.getWidth()) {
			return false;
		}
		if (mouseY < textField.getY()) {
			return false;
		}
		return mouseY <= textField.getY() + textField.getHeight();
	}

	public static boolean isValidCharForNumberTextField(char typedChar, int keyCode, boolean allowNegative, boolean allowDouble) {
		return Character.isDigit(typedChar) || (allowNegative && typedChar == '-') || (allowDouble && typedChar == '.') || keyCode == 14 || keyCode == 211 || keyCode == 203 || keyCode == 205 || keyCode == 199 || keyCode == 207;
	}


	public static boolean isMouseOver(int mouseX, int mouseY, TextFieldWidget inStockTextField) {
		return inStockTextField.isMouseOver(mouseX, mouseY);
	}

}
