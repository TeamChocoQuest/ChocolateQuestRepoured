package com.teamcqr.chocolatequestrepoured.gui;

import java.io.IOException;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiExporter extends GuiScreen 
{
	private String authorName;
	private World world;
	private boolean saveStructOnExit = false;
	@SuppressWarnings("unused")
	private boolean usePartsMode = false;
	private BlockPos structureEndPos = null;
	private BlockPos structureStartPos = null;
	private TileEntityExporter exporter;

	private GuiButtonExt btnExport;
	private GuiTextField edtName, edtEndX, edtEndY, edtEndZ, edtStartX, edtStartY, edtStartZ;
	private GuiCheckBox chbxPartsMode;

	public GuiExporter(World worldIn, EntityPlayer player, TileEntityExporter exporter) 
	{
		this.world = worldIn;
		this.authorName = player.getName();
		this.exporter = exporter;
		if(this.exporter != null) 
		{
			this.exporter.setUser(player);
		}
	}

	@Override
	public void initGui() 
	{
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

		chbxPartsMode = new GuiCheckBox(5, width / 2 -70, height /2 +40, "Use Part Mode", false);

		btnExport = new GuiButtonExt(4, width / 2 -70, height / 2 +60, 140, 20, "Export");

		buttonList.add(chbxPartsMode);
		buttonList.add(btnExport);
	}

	@Override
	public void onGuiClosed() 
	{
		int sX = 0;
		int eX = 0;
		int sY = 0;
		int eY = 0;
		int sZ = 0;
		int eZ = 0;
		boolean useParts = false;
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

		if(!fail) 
		{
			structName = edtName.getText();
			structName = structName.replaceAll(" ", "_");

			if(structName.isEmpty() || structName.equalsIgnoreCase("")) 
			{
				structName = "dungeon_export";
			}

			useParts = chbxPartsMode.isChecked();
			exporter.setValues(sX, sY, sZ, eX, eY, eZ, structName, useParts);
			useParts = chbxPartsMode.isChecked();

			if(this.saveStructOnExit) 
			{
				this.exporter.saveStructure(this.world, this.structureStartPos, this.structureEndPos, this.authorName);
				System.out.println("Saving structure...");
			}
		}
		super.onGuiClosed();

	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException 
	{
		super.keyTyped(typedChar, keyCode);

		edtName.textboxKeyTyped(typedChar, keyCode);

		//if(Character.isDigit(typedChar) || typedChar == '-' || keyCode == 37 || keyCode == 39) {
		if(!Character.isAlphabetic(typedChar)) 
		{
			edtEndX.textboxKeyTyped(typedChar, keyCode);
			edtEndY.textboxKeyTyped(typedChar, keyCode);
			edtEndZ.textboxKeyTyped(typedChar, keyCode);

			edtStartX.textboxKeyTyped(typedChar, keyCode);
			edtStartY.textboxKeyTyped(typedChar, keyCode);
			edtStartZ.textboxKeyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
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
	public void updateScreen() 
	{
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
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, I18n.format("tile.exporter.name"), this.width / 2, 20, 16777215);
		
		edtName.drawTextBox();

		edtEndX.drawTextBox();
		edtEndY.drawTextBox();
		edtEndZ.drawTextBox();

		edtStartX.drawTextBox();
		edtStartY.drawTextBox();
		edtStartZ.drawTextBox();

		drawString(this.fontRenderer, "Structure Name", width / 2 -70, height / 2 -80, 10526880);

		drawString(this.fontRenderer, "Start X", width / 2 -70, height / 2 -40, 10526880);
		drawString(this.fontRenderer, "Start Y", width / 2 -20, height / 2 -40, 10526880);
		drawString(this.fontRenderer, "Start Z", width / 2 +30, height / 2 -40, 10526880);

		drawString(this.fontRenderer, "End X", width / 2 -70, height / 2, 10526880);
		drawString(this.fontRenderer, "End Y", width / 2 -20, height / 2, 10526880);
		drawString(this.fontRenderer, "End Z", width / 2 +30, height / 2, 10526880);

		if(this.chbxPartsMode.isMouseOver())
		{
			this.drawHoveringText(I18n.format("description.gui_exporter.name"), width / 2 - 170, height / 2 + 30);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}


	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException 
	{
		int sX = 0;
		int eX = 0;
		int sY = 0;
		int eY = 0;
		int sZ = 0;
		boolean useParts = false;
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

		if(!fail) 
		{
			structName = edtName.getText();
			structName = structName.replaceAll(" ", "_");

			if(structName.isEmpty() || structName.equalsIgnoreCase("")) 
			{
				structName = "dungeon_export";
			}

			structName = "export-" + structName;
			useParts = chbxPartsMode.isChecked();
		}

		if(!fail) 
		{
			if(button == btnExport) 
			{
				BlockPos startPos = new BlockPos(sX, sY, sZ);
				BlockPos endPos = new BlockPos(eX, eY, eZ);

				//Solution: move saving  a w a y  from GUI, move it into the tile entity section
				exporter.setValues(sX, sY, sZ, eX, eY, eZ, structName, useParts);

				this.saveStructOnExit = true;
				this.usePartsMode = useParts;
				this.structureEndPos = endPos;
				this.structureStartPos = startPos;
			}
		}
	}
} 