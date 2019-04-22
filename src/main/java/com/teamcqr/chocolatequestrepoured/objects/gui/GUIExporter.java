package com.teamcqr.chocolatequestrepoured.objects.gui;

import java.io.IOException;

import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GUIExporter extends GuiScreen {

	public static int GUIID = 1;
	
	
	private String authorName;
	private World world;
	private TileEntityExporter exporter;
	
	private GuiButtonExt btnExport;
	private GuiTextField edtName, edtEndX, edtEndY, edtEndZ, edtStartX, edtStartY, edtStartZ;
	
	public GUIExporter(World worldIn, EntityPlayer player, TileEntityExporter exporter) {
		this.world = worldIn;
		this.authorName = player.getName();
		this.exporter = exporter;
	}

	@Override
	public void initGui() {
		//Has inputs for start and end locations
		
		edtName = new GuiTextField(0, this.fontRenderer, width / 2 -70, height / 2 -70, 140, 20);
		edtName.setText(exporter.structureName);
		
		edtEndX = new GuiTextField(1, this.fontRenderer, width / 2 -70, height / 2 +10, 40, 20);
		edtEndX.setText(String.valueOf(exporter.endX));
		edtEndY = new GuiTextField(2, this.fontRenderer, width / 2 -70 + 50, height / 2 +10, 40, 20);
		edtEndY.setText(String.valueOf(exporter.endY));
		edtEndZ = new GuiTextField(3, this.fontRenderer, width / 2 -70 +50 +50, height / 2 +10, 40, 20);
		edtEndZ.setText(String.valueOf(exporter.endZ));
		
		edtStartX = new GuiTextField(1, this.fontRenderer, width / 2 -70, height / 2 -30, 40, 20);
		edtStartX.setText(String.valueOf(exporter.startX));
		edtStartY = new GuiTextField(2, this.fontRenderer, width / 2 -70 + 50, height / 2 -30, 40, 20);
		edtStartY.setText(String.valueOf(exporter.startY));
		edtStartZ = new GuiTextField(3, this.fontRenderer, width / 2 -70 +50 +50, height / 2 -30, 40, 20);
		edtStartZ.setText(String.valueOf(exporter.startZ));
		
		btnExport = new GuiButtonExt(4, width / 2 -70, height / 2 +40, 140, 20, "Export");
		buttonList.add(btnExport);
	}
	
	@Override
	public void onGuiClosed() {
		int sX = 0;
		int eX = 0;
		int sY = 0;
		int eY = 0;
		int sZ = 0;
		int eZ = 0;
		String structName = "";
		
		boolean fail = false;
		try {
			eX = Integer.parseInt(edtEndX.getText());
			sX = Integer.parseInt(edtStartX.getText());
			eY = Integer.parseInt(edtEndY.getText());
			sY = Integer.parseInt(edtStartY.getText());
			eZ = Integer.parseInt(edtEndZ.getText());
			sZ = Integer.parseInt(edtStartZ.getText());
		} catch(NumberFormatException ex) {
			fail = true;
		}
		if(!fail) {
			structName = edtName.getText();
			structName = structName.replaceAll(" ", "_");
			if(structName.isEmpty() || structName.equalsIgnoreCase("")) {
				structName = "dungeon_export";
			}
			structName = "export-" + structName;
			exporter.setValues(sX, sY, sZ, eX, eY, eZ, structName);
		}
		super.onGuiClosed();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		
		edtName.textboxKeyTyped(typedChar, keyCode);
		
		//if(Character.isDigit(typedChar) || typedChar == '-' || keyCode == 37 || keyCode == 39) {
		if(!Character.isAlphabetic(typedChar)) {
			edtEndX.textboxKeyTyped(typedChar, keyCode);
			edtEndY.textboxKeyTyped(typedChar, keyCode);
			edtEndZ.textboxKeyTyped(typedChar, keyCode);
			
			edtStartX.textboxKeyTyped(typedChar, keyCode);
			edtStartY.textboxKeyTyped(typedChar, keyCode);
			edtStartZ.textboxKeyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		edtName.mouseClicked(mouseX, mouseY, mouseButton);
		
		edtEndX.mouseClicked(mouseX, mouseY, mouseButton);
		edtEndY.mouseClicked(mouseX, mouseY, mouseButton);
		edtEndZ.mouseClicked(mouseX, mouseY, mouseButton);
		
		edtStartX.mouseClicked(mouseX, mouseY, mouseButton);
		edtStartY.mouseClicked(mouseX, mouseY, mouseButton);
		edtStartZ.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void updateScreen() {
		edtName.updateCursorCounter();
		
		edtEndX.updateCursorCounter();
		edtEndY.updateCursorCounter();
		edtEndZ.updateCursorCounter();
		
		edtStartX.updateCursorCounter();
		edtStartY.updateCursorCounter();
		edtStartZ.updateCursorCounter();
		
		super.updateScreen();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		edtName.drawTextBox();
		
		edtEndX.drawTextBox();
		edtEndY.drawTextBox();
		edtEndZ.drawTextBox();
		
		edtStartX.drawTextBox();
		edtStartY.drawTextBox();
		edtStartZ.drawTextBox();
		
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
		int sX = 0;
		int eX = 0;
		int sY = 0;
		int eY = 0;
		int sZ = 0;
		int eZ = 0;
		String structName = "";
		
		boolean fail = false;
		try {
			eX = Integer.parseInt(edtEndX.getText());
			sX = Integer.parseInt(edtStartX.getText());
			eY = Integer.parseInt(edtEndY.getText());
			sY = Integer.parseInt(edtStartY.getText());
			eZ = Integer.parseInt(edtEndZ.getText());
			sZ = Integer.parseInt(edtStartZ.getText());
		} catch(NumberFormatException ex) {
			fail = true;
		}
		if(!fail) {
			structName = edtName.getText();
			structName = structName.replaceAll(" ", "_");
			if(structName.isEmpty() || structName.equalsIgnoreCase("")) {
				structName = "dungeon_export";
			}
			structName = "export-" + structName;
		}
		
		if(!fail) {
			
			if(button == btnExport) {
				BlockPos startPos = new BlockPos(sX, sY, sZ);
				BlockPos endPos = new BlockPos(eX, eY, eZ);
				
				//Solution: move saving  a w a y  from GUI, move it into the tile entity section
				exporter.setValues(sX, sY, sZ, eX, eY, eZ, structName);
				
				CQStructure structure = new CQStructure(structName);
				structure.setAuthor(this.authorName);
				
				structure.save(this.world, startPos, endPos);
			}
		}
	}
	
}
