package team.cqr.cqrepoured.client.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

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

	public static void drawString(FontRenderer fontRenderer, String text, int x, int y, int color, boolean centered, boolean withShadow) {
		if (centered) {
			x -= fontRenderer.getStringWidth(text) / 2;
		}
		if (withShadow) {
			fontRenderer.drawStringWithShadow(text, x, y, color);
		} else {
			fontRenderer.drawString(text, x, y, color);
		}
	}

	public static boolean isMouseOver(int mouseX, int mouseY, GuiTextField textField) {
		if (mouseX < textField.x) {
			return false;
		}
		if (mouseX > textField.x + textField.width) {
			return false;
		}
		if (mouseY < textField.y) {
			return false;
		}
		return mouseY <= textField.y + textField.height;
	}

	public static boolean isValidCharForNumberTextField(char typedChar, int keyCode, boolean allowNegative, boolean allowDouble) {
		return Character.isDigit(typedChar) || (allowNegative && typedChar == '-') || (allowDouble && typedChar == '.') || keyCode == 14 || keyCode == 211 || keyCode == 203 || keyCode == 205 || keyCode == 199 || keyCode == 207;
	}

}
