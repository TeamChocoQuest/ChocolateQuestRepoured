package team.cqr.cqrepoured.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.gui.GuiMapPlaceholder.GuiButtonOrientation;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.network.client.packet.CPacketCloseMapPlaceholderGuiSimple;

@SideOnly(Side.CLIENT)
public class GuiMapPlaceholderSimple extends GuiScreen {

	private final BlockPos pos;
	private final EnumFacing facing;
	private GuiTextField scale;
	private GuiButtonOrientation orientation;
	private GuiCheckBox lockOrientation;
	private GuiTextField sizeUp;
	private GuiTextField sizeDown;
	private GuiTextField sizeRight;
	private GuiTextField sizeLeft;
	private GuiCheckBox fillMap;
	private GuiTextField fillRadius;

	public GuiMapPlaceholderSimple(BlockPos pos, EnumFacing facing) {
		this.pos = pos;
		this.facing = facing;
	}

	@Override
	public void initGui() {
		super.initGui();

		this.scale = new GuiTextField(0, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72, 38, 12);
		this.orientation = new GuiButtonOrientation(1, this.width / 2 - 38, this.height / 2 - 72 + 16, 40, 14, EnumFacing.NORTH);
		this.lockOrientation = new GuiCheckBox(2, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 32, "Lock Orientation", false);
		this.sizeUp = new GuiTextField(3, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 48, 38, 12);
		this.sizeDown = new GuiTextField(4, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 64, 38, 12);
		this.sizeRight = new GuiTextField(5, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 80, 38, 12);
		this.sizeLeft = new GuiTextField(6, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 96, 38, 12);
		this.fillMap = new GuiCheckBox(7, this.width / 2 - 1 - 38, this.height / 2 + 1 - 72 + 112, "Fill Map", false);
		this.fillRadius = new GuiTextField(8, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 128, 38, 12);

		this.scale.setText("0");
		this.sizeUp.setText("0");
		this.sizeDown.setText("0");
		this.sizeRight.setText("0");
		this.sizeLeft.setText("0");
		this.fillRadius.setText("64");

		this.buttonList.add(this.orientation);
		this.buttonList.add(this.lockOrientation);
		this.buttonList.add(this.fillMap);
	}

	@Override
	public void onGuiClosed() {
		int scale = this.parseInt(this.scale.getText(), 0, 4, 0, "Invalid argument: scale");
		EnumFacing orientation = this.orientation.getDirection();
		boolean lockOrientation = this.lockOrientation.isChecked();
		int sizeUp = this.parseInt(this.sizeUp.getText(), 0, 16, 0, "Invalid argument: sizeUp");
		int sizeDown = this.parseInt(this.sizeDown.getText(), 0, 16, 0, "Invalid argument: sizeDown");
		int sizeRight = this.parseInt(this.sizeRight.getText(), 0, 16, 0, "Invalid argument: sizeRight");
		int sizeLeft = this.parseInt(this.sizeLeft.getText(), 0, 16, 0, "Invalid argument: sizeLeft");
		boolean fillMap = this.fillMap.isChecked();
		int fillRadius = this.parseInt(this.fillRadius.getText(), 0, 1024, 64, "Invalid argument: fillRadius");

		CQRMain.NETWORK.sendToServer(new CPacketCloseMapPlaceholderGuiSimple(this.pos, this.facing, scale, orientation, lockOrientation, sizeUp, sizeDown, sizeRight, sizeLeft, fillMap, fillRadius));
		super.onGuiClosed();
	}

	private int parseInt(String s, int min, int max, int defaultValue, String warning) {
		try {
			return MathHelper.clamp(Integer.parseInt(s), min, max);
		} catch (Exception e) {
			this.mc.player.sendMessage(new TextComponentString(warning));
		}
		return defaultValue;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.scale.isFocused() || this.sizeUp.isFocused() || this.sizeDown.isFocused() || this.sizeRight.isFocused() || this.sizeLeft.isFocused() || this.fillRadius.isFocused()) {
			if (keyCode == 1) {
				this.scale.setFocused(false);
				this.sizeUp.setFocused(false);
				this.sizeDown.setFocused(false);
				this.sizeRight.setFocused(false);
				this.sizeLeft.setFocused(false);
				this.fillRadius.setFocused(false);
			} else if (GuiHelper.isValidCharForNumberTextField(typedChar, keyCode, true, false)) {
				this.scale.textboxKeyTyped(typedChar, keyCode);
				this.sizeUp.textboxKeyTyped(typedChar, keyCode);
				this.sizeDown.textboxKeyTyped(typedChar, keyCode);
				this.sizeRight.textboxKeyTyped(typedChar, keyCode);
				this.sizeLeft.textboxKeyTyped(typedChar, keyCode);
				this.fillRadius.textboxKeyTyped(typedChar, keyCode);
			}
		} else if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.scale.mouseClicked(mouseX, mouseY, mouseButton);
		this.sizeUp.mouseClicked(mouseX, mouseY, mouseButton);
		this.sizeDown.mouseClicked(mouseX, mouseY, mouseButton);
		this.sizeRight.mouseClicked(mouseX, mouseY, mouseButton);
		this.sizeLeft.mouseClicked(mouseX, mouseY, mouseButton);
		this.fillRadius.mouseClicked(mouseX, mouseY, mouseButton);

		if (this.orientation.isMouseOver() && (mouseButton == 0 || mouseButton == 1)) {
			if (mouseButton == 1) {
				this.orientation.playPressSound(this.mc.getSoundHandler());
			}
			this.orientation.onMouseClick(mouseButton == 0);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		this.scale.updateCursorCounter();
		this.sizeUp.updateCursorCounter();
		this.sizeDown.updateCursorCounter();
		this.sizeRight.updateCursorCounter();
		this.sizeLeft.updateCursorCounter();
		this.fillRadius.updateCursorCounter();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.scale.drawTextBox();
		this.sizeUp.drawTextBox();
		this.sizeDown.drawTextBox();
		this.sizeRight.drawTextBox();
		this.sizeLeft.drawTextBox();
		this.fillRadius.drawTextBox();

		GuiHelper.drawString(this.fontRenderer, "Scale", this.width / 2 + 4, this.height / 2 - 72 + 3, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Orientation", this.width / 2 + 4, this.height / 2 - 72 + 3 + 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Size Up", this.width / 2 + 4, this.height / 2 - 72 + 3 + 48, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Size Down", this.width / 2 + 4, this.height / 2 - 72 + 3 + 64, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Size Right", this.width / 2 + 4, this.height / 2 - 72 + 3 + 80, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Size Left", this.width / 2 + 4, this.height / 2 - 72 + 3 + 96, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Fill Radius", this.width / 2 + 4, this.height / 2 - 72 + 3 + 128, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Map", this.width / 2, this.height / 2 - 96, 0xFFFFFF, true, true);
		GuiHelper.drawString(this.fontRenderer, "Simple Mode", this.width / 2, this.height / 2 - 86, 0x0FFF0F, true, true);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
