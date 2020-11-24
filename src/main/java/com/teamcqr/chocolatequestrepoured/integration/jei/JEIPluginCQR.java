package com.teamcqr.chocolatequestrepoured.integration.jei;

import com.teamcqr.chocolatequestrepoured.client.gui.npceditor.GuiCQREntity;
import com.teamcqr.chocolatequestrepoured.client.gui.npceditor.GuiMerchant;
import com.teamcqr.chocolatequestrepoured.client.gui.npceditor.GuiMerchantEditTrade;
import com.teamcqr.chocolatequestrepoured.integration.jei.gui.GUIHandlerCQREntity;
import com.teamcqr.chocolatequestrepoured.integration.jei.gui.GUIHandlerMerchant;
import com.teamcqr.chocolatequestrepoured.integration.jei.gui.GUIHandlerMerchantEditTrade;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JEIPluginCQR implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {
		registry.addAdvancedGuiHandlers(new GUIHandlerMerchant<>(GuiMerchant.class));
		registry.addAdvancedGuiHandlers(new GUIHandlerMerchantEditTrade<>(GuiMerchantEditTrade.class));
		registry.addAdvancedGuiHandlers(new GUIHandlerCQREntity<>(GuiCQREntity.class));
	}
}
