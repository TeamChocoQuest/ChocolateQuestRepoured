package com.teamcqr.chocolatequestrepoured.objects.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GUIExporter extends GuiScreen {

	public static int GUIID = 1;
	
	/*private int dimX, dimY, dimZ;
	private String structureName;
	private String authorName;*/
	
	
	private GuiButtonExt exportButton;
	//private GuiLabel lblDimX, lblDimY, lblDimZ, lblName;
	private GuiTextField edtName, edtX, edtY, edtZ, edtOffX, edtOffY, edtOffZ;
	
	public GUIExporter() {
		//lblName = new GuiLabel(this.fontRenderer, p_i45540_2_, p_i45540_3_, p_i45540_4_, p_i45540_5_, p_i45540_6_, p_i45540_7_)
	}

	@Override
	public void initGui() {
		//TODO: remake this thing, it doesnt look nice enough
		//TODO: add "Offset" fields
		//Has inputs for start and end locations
		//setGuiSize(180, 220);
		
		edtName = new GuiTextField(0, this.fontRenderer, width / 2 -70, height / 2 -70, 140, 20);
		
		edtX = new GuiTextField(1, this.fontRenderer, width / 2 -70, height / 2 +10, 40, 20);
		edtX.setText("0");
		edtY = new GuiTextField(2, this.fontRenderer, width / 2 -70 + 50, height / 2 +10, 40, 20);
		edtY.setText("0");
		edtZ = new GuiTextField(3, this.fontRenderer, width / 2 -70 +50 +50, height / 2 +10, 40, 20);
		edtZ.setText("0");
		
		edtOffX = new GuiTextField(1, this.fontRenderer, width / 2 -70, height / 2 -30, 40, 20);
		edtOffX.setText("0");
		edtOffY = new GuiTextField(2, this.fontRenderer, width / 2 -70 + 50, height / 2 -30, 40, 20);
		edtOffY.setText("0");
		edtOffZ = new GuiTextField(3, this.fontRenderer, width / 2 -70 +50 +50, height / 2 -30, 40, 20);
		edtOffZ.setText("0");
		
		exportButton = new GuiButtonExt(4, width / 2 -70, height / 2 +40, 140, 20, "Export");
		buttonList.add(exportButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		
		edtName.textboxKeyTyped(typedChar, keyCode);
		
		//if(Character.isDigit(typedChar) || typedChar == '-' || keyCode == 37 || keyCode == 39) {
		if(!Character.isAlphabetic(typedChar)) {
			edtX.textboxKeyTyped(typedChar, keyCode);
			edtY.textboxKeyTyped(typedChar, keyCode);
			edtZ.textboxKeyTyped(typedChar, keyCode);
			
			edtOffX.textboxKeyTyped(typedChar, keyCode);
			edtOffY.textboxKeyTyped(typedChar, keyCode);
			edtOffZ.textboxKeyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		edtName.mouseClicked(mouseX, mouseY, mouseButton);
		
		edtX.mouseClicked(mouseX, mouseY, mouseButton);
		edtY.mouseClicked(mouseX, mouseY, mouseButton);
		edtZ.mouseClicked(mouseX, mouseY, mouseButton);
		
		edtOffX.mouseClicked(mouseX, mouseY, mouseButton);
		edtOffY.mouseClicked(mouseX, mouseY, mouseButton);
		edtOffZ.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void updateScreen() {
		edtName.updateCursorCounter();
		
		edtX.updateCursorCounter();
		edtY.updateCursorCounter();
		edtZ.updateCursorCounter();
		
		edtOffX.updateCursorCounter();
		edtOffY.updateCursorCounter();
		edtOffZ.updateCursorCounter();
		
		super.updateScreen();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		edtName.drawTextBox();
		
		edtX.drawTextBox();
		edtY.drawTextBox();
		edtZ.drawTextBox();
		
		edtOffX.drawTextBox();
		edtOffY.drawTextBox();
		edtOffZ.drawTextBox();
		
		drawString(this.fontRenderer, "Structure Name", width / 2 -70, height / 2 -80, -1);
		
		drawString(this.fontRenderer, "Start X", width / 2 -70, height / 2 -40, -1);
		drawString(this.fontRenderer, "Start Y", width / 2 -20, height / 2 -40, -1);
		drawString(this.fontRenderer, "Start Z", width / 2 +30, height / 2 -40, -1);
		
		drawString(this.fontRenderer, "End X", width / 2 -70, height / 2, -1);
		drawString(this.fontRenderer, "End Y", width / 2 -20, height / 2, -1);
		drawString(this.fontRenderer, "End Z", width / 2 +30, height / 2, -1);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button == exportButton) {
			mc.displayInGameMenu();
		}
	}
}
