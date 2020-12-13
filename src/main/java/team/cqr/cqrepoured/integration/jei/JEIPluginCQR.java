package team.cqr.cqrepoured.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import team.cqr.cqrepoured.client.gui.npceditor.GuiCQREntity;
import team.cqr.cqrepoured.client.gui.npceditor.GuiMerchant;
import team.cqr.cqrepoured.client.gui.npceditor.GuiMerchantEditTrade;
import team.cqr.cqrepoured.integration.jei.gui.GUIHandlerCQREntity;
import team.cqr.cqrepoured.integration.jei.gui.GUIHandlerMerchant;
import team.cqr.cqrepoured.integration.jei.gui.GUIHandlerMerchantEditTrade;

@JEIPlugin
public class JEIPluginCQR implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {
		registry.addAdvancedGuiHandlers(new GUIHandlerMerchant<>(GuiMerchant.class));
		registry.addAdvancedGuiHandlers(new GUIHandlerMerchantEditTrade<>(GuiMerchantEditTrade.class));
		registry.addAdvancedGuiHandlers(new GUIHandlerCQREntity<>(GuiCQREntity.class));
	}
}
