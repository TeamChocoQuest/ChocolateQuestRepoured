package com.teamcqr.chocolatequestrepoured.client.gui;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiReputation extends GuiScreen {

	// Textures
	protected static final ResourceLocation TEXTURE_BG = new ResourceLocation(Reference.MODID, "textures/gui/repu/gui_reputation.png");
	protected static final ResourceLocation TEXTURE_REPU_BAR = new ResourceLocation(Reference.MODID, "textures/gui/repu/repubar.png");
	protected ResourceLocation imgPlayerHead;

	// GUI Elements
	protected GuiButtonExt btnCycleFaction;

	// Lang keys
	protected final String lblFactionButtonLangKey = "gui.repu.faction";
	protected final String lblReputationBarLangKey = "gui.repu.reputation";
	protected final String lblMembersKilledLangKey = "gui.repu.memberskilled";
	protected final String lblDungeonsConqueredLangKey = "gui.repu.dungeonsconquered";

	// Coordinates
	protected int PLAYER_HEAD_X;
	protected int PLAYER_HEAD_Y;
	protected int REPU_BAR_X;
	protected int REPU_BAR_Y;

	// Data
	protected String[] factionNames = new String[] { "missingNo" };

	// Player texture: Player object -> getTexture or similar...

	public GuiReputation(EntityPlayerSP player) {
		super();
		this.imgPlayerHead = player.getLocationSkin();

		this.setGuiSize(this.width / 4, this.height / 3);
	}

	@Override
	public void initGui() {
		super.initGui();

		this.btnCycleFaction = new GuiButtonExt(0, this.width / 2 - 65, this.height / 2 - 65, 150, 20, "missingNo");

		this.buttonList.add(this.btnCycleFaction);
		this.adjustComponentsToFaction(null);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.drawHoveringText(I18n.format(this.lblFactionButtonLangKey), this.width / 2 - 70, this.height / 2 - 15);
		this.drawHoveringText(I18n.format(this.lblReputationBarLangKey), this.width / 2 - 70, this.height / 2 + 5);
		this.drawHoveringText(I18n.format(this.lblMembersKilledLangKey) + ": missingNo", this.width / 2 - 70, this.height / 2 + 25);
		this.drawHoveringText(I18n.format(this.lblDungeonsConqueredLangKey) + ": missingNo", this.width / 2 - 70, this.height / 2 + 45);
		this.REPU_BAR_X = this.width / 2 - 70;
		this.REPU_BAR_Y = this.height / 2 + 65;
	}

	@Override
	public void drawBackground(int tint) {
		super.drawBackground(tint);

		// Draw images
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_REPU_BAR);
		// Reputation Bar
		this.drawTexturedModalRect(this.REPU_BAR_X, this.REPU_BAR_Y, 0, 0, 128, 128);

		// TODO: Calculate coordinates for head -> Recalculate X coordinate

		// Draw player head
		// Minecraft.getMinecraft().renderEngine.bindTexture(imgPlayerHead);
		// Face
		// this.drawTexturedModalRect(PLAYER_HEAD_X, PLAYER_HEAD_Y, 8, 8, 8, 8);
		// Headwear
		// this.drawTexturedModalRect(PLAYER_HEAD_X, PLAYER_HEAD_Y, 72, 8, 8, 8);
	}

	protected void adjustComponentsToFaction(String newFaction) {
		if (newFaction != null) {
			this.btnCycleFaction.displayString = newFaction;
		} else {
			this.btnCycleFaction.displayString = "None";
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
