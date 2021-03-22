package team.cqr.cqrepoured.integration.jei.gui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import team.cqr.cqrepoured.client.gui.npceditor.GuiMerchantEditTrade;

public class GUIHandlerMerchantEditTrade<T extends GuiMerchantEditTrade> implements IAdvancedGuiHandler<T> {

	private final Class<T> GUI_CLASS;

	public GUIHandlerMerchantEditTrade(Class<T> guiClass) {
		this.GUI_CLASS = guiClass;
	}

	@Override
	public Class<T> getGuiContainerClass() {
		return this.GUI_CLASS;
	}

	@Override
	public List<Rectangle> getGuiExtraAreas(T guiContainer) {
		GuiMerchantEditTrade gui = guiContainer;
		List<Rectangle> rects = new ArrayList<>();
		rects.add(new Rectangle(gui.getGuiLeft(), gui.getGuiTop(), gui.width, gui.height));

		return rects;
	}

}
