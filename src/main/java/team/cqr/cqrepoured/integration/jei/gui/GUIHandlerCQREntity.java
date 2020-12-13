package team.cqr.cqrepoured.integration.jei.gui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import team.cqr.cqrepoured.client.gui.npceditor.GuiCQREntity;

public class GUIHandlerCQREntity<T extends GuiCQREntity> implements IAdvancedGuiHandler<T> {

	private final Class<T> GUI_CLASS;

	public GUIHandlerCQREntity(Class<T> guiClass) {
		this.GUI_CLASS = guiClass;
	}

	@Override
	public Class<T> getGuiContainerClass() {
		return GUI_CLASS;
	}

	@Override
	public List<Rectangle> getGuiExtraAreas(T guiContainer) {
		GuiCQREntity gui = guiContainer;
		List<Rectangle> rects = new ArrayList<>();
		// Main GUI
		rects.add(new Rectangle(gui.getGuiLeft(), gui.getGuiTop(), gui.width, gui.height));
		// Entity (Sizes are guessed values...)
		rects.add(new Rectangle(gui.getGuiLeft() + 225, gui.getGuiTop() + 100, 100, 100));
		// Sliders
		rects.add(new Rectangle(5, 5, 108, 8 * 16 /* 8 sliders with height of 16 */));

		return rects;
	}

}
