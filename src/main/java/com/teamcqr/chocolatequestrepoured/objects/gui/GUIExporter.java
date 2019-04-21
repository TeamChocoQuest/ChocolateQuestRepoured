package com.teamcqr.chocolatequestrepoured.objects.gui;

import java.io.IOException;

import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GUIExporter extends GuiScreen {

	public static int GUIID = 1;
	
	/*private int dimX, dimY, dimZ;
	private String structureName;
	private String authorName;*/
	private World world;
	
	private GuiButtonExt exportButton;
	//private GuiLabel lblDimX, lblDimY, lblDimZ, lblName;
	private GuiTextField edtName, edtEndX, edtEndY, edtEndZ, edtStartX, edtStartY, edtStartZ;
	
	public GUIExporter(World worldIn) {
		this.world = worldIn;
		//lblName = new GuiLabel(this.fontRenderer, p_i45540_2_, p_i45540_3_, p_i45540_4_, p_i45540_5_, p_i45540_6_, p_i45540_7_)
	}

	@Override
	public void initGui() {
		//TODO: remake this thing, it doesnt look nice enough
		//TODO: add "Offset" fields
		//Has inputs for start and end locations
		//setGuiSize(180, 220);
		
		edtName = new GuiTextField(0, this.fontRenderer, width / 2 -70, height / 2 -70, 140, 20);
		
		edtEndX = new GuiTextField(1, this.fontRenderer, width / 2 -70, height / 2 +10, 40, 20);
		edtEndX.setText("0");
		edtEndY = new GuiTextField(2, this.fontRenderer, width / 2 -70 + 50, height / 2 +10, 40, 20);
		edtEndY.setText("0");
		edtEndZ = new GuiTextField(3, this.fontRenderer, width / 2 -70 +50 +50, height / 2 +10, 40, 20);
		edtEndZ.setText("0");
		
		edtStartX = new GuiTextField(1, this.fontRenderer, width / 2 -70, height / 2 -30, 40, 20);
		edtStartX.setText("0");
		edtStartY = new GuiTextField(2, this.fontRenderer, width / 2 -70 + 50, height / 2 -30, 40, 20);
		edtStartY.setText("0");
		edtStartZ = new GuiTextField(3, this.fontRenderer, width / 2 -70 +50 +50, height / 2 -30, 40, 20);
		edtStartZ.setText("0");
		
		exportButton = new GuiButtonExt(4, width / 2 -70, height / 2 +40, 140, 20, "Export");
		buttonList.add(exportButton);
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
		if(button == exportButton) {
			int sX = 0;
			int eX = 0;
			int sY = 0;
			int eY = 0;
			int sZ = 0;
			int eZ = 0;
			
			boolean fail = false;
			try {
				eX = Integer.parseInt(edtEndX.getText());
				sX = Integer.parseInt(edtStartX.getText());
				eY = Integer.parseInt(edtEndY.getText());
				sY = Integer.parseInt(edtStartY.getText());
				eZ = Integer.parseInt(edtEndZ.getText());
				sZ = Integer.parseInt(edtStartZ.getText());
			} catch(NumberFormatException ex) {
				//ex.printStackTrace();
				fail = true;
			}
			if(!fail) {
				String structName = edtName.getText();
				structName = structName.replaceAll(" ", "_");
				if(structName.isEmpty() || structName.equalsIgnoreCase("")) {
					structName = "dungeon_export";
				}
				structName = "exports-" + structName;
				BlockPos startPos = new BlockPos(sX, sY, sZ);
				BlockPos endPos = new BlockPos(eX, eY, eZ);
				
				CQStructure structure = new CQStructure(structName);
				structure.save(this.world, startPos, endPos);
			}
		}
	}
	
}
