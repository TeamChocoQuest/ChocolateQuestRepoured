package team.cqr.cqrepoured.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.tileentity.TileEntityMap;

@SideOnly(Side.CLIENT)
public class GuiMapPlaceholder extends GuiScreen {

	private final TileEntityMap tileEntity;

	private GuiTextField scale;
	private GuiButtonOrientation orientation;
	private GuiCheckBox lockOrientation;
	private GuiTextField originX;
	private GuiTextField originZ;
	private GuiTextField offsetX;
	private GuiTextField offsetZ;
	private GuiCheckBox fillMap;
	private GuiTextField fillRadius;

	public GuiMapPlaceholder(TileEntityMap tileEntity) {
		this.mc = Minecraft.getMinecraft();
		this.tileEntity = tileEntity;
	}

	public void sync() {
		this.scale.setText(String.valueOf(this.tileEntity.getScale()));
		this.orientation.setDisplayString(this.tileEntity.getOrientation());
		this.lockOrientation.setIsChecked(this.tileEntity.lockOrientation());
		this.originX.setText(String.valueOf(this.tileEntity.getOriginX()));
		this.originZ.setText(String.valueOf(this.tileEntity.getOriginZ()));
		this.offsetX.setText(String.valueOf(this.tileEntity.getOffsetX()));
		this.offsetZ.setText(String.valueOf(this.tileEntity.getOffsetZ()));
		this.fillMap.setIsChecked(this.tileEntity.fillMap());
		this.fillRadius.setText(String.valueOf(this.tileEntity.getFillRadius()));
	}

	@Override
	public void initGui() {
		this.scale = new GuiTextField(0, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72, 38, 12);
		this.orientation = new GuiButtonOrientation(1, this.width / 2 - 38, this.height / 2 - 72 + 16, 40, 14, this.tileEntity.getOrientation());
		this.lockOrientation = new GuiCheckBox(2, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 32, "Lock Orientation", this.tileEntity.lockOrientation());
		this.originX = new GuiTextField(3, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 48, 38, 12);
		this.originZ = new GuiTextField(4, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 64, 38, 12);
		this.offsetX = new GuiTextField(5, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 80, 38, 12);
		this.offsetZ = new GuiTextField(6, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 96, 38, 12);
		this.fillMap = new GuiCheckBox(7, this.width / 2 - 1 - 38, this.height / 2 + 1 - 72 + 112, "Fill Map", this.tileEntity.fillMap());
		this.fillRadius = new GuiTextField(8, this.fontRenderer, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 128, 38, 12);

		this.sync();

		this.buttonList.add(this.orientation);
		this.buttonList.add(this.lockOrientation);
		this.buttonList.add(this.fillMap);
	}

	@Override
	public void onGuiClosed() {
		try {
			int scale = Integer.parseInt(this.scale.getText());
			EnumFacing orientation = this.orientation.getDirection();
			boolean lockOrientation = this.lockOrientation.isChecked();
			int originX = Integer.parseInt(this.originX.getText());
			int originZ = Integer.parseInt(this.originZ.getText());
			int offsetX = Integer.parseInt(this.offsetX.getText());
			int offsetZ = Integer.parseInt(this.offsetZ.getText());
			boolean fillMap = this.fillMap.isChecked();
			int fillRadius = Integer.parseInt(this.fillRadius.getText());

			this.tileEntity.set(scale, orientation, lockOrientation, originX, originZ, offsetX, offsetZ, fillMap, fillRadius);
		} catch (IllegalArgumentException e) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Invalid arguments"));
		}

		super.onGuiClosed();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.scale.isFocused() || this.originX.isFocused() || this.originZ.isFocused() || this.offsetX.isFocused() || this.offsetZ.isFocused() || this.fillRadius.isFocused()) {
			if (keyCode == 1) {
				this.scale.setFocused(false);
				this.originX.setFocused(false);
				this.originZ.setFocused(false);
				this.offsetX.setFocused(false);
				this.offsetZ.setFocused(false);
				this.fillRadius.setFocused(false);
			} else if (GuiHelper.isValidCharForNumberTextField(typedChar, keyCode, true, false)) {
				this.scale.textboxKeyTyped(typedChar, keyCode);
				this.originX.textboxKeyTyped(typedChar, keyCode);
				this.originZ.textboxKeyTyped(typedChar, keyCode);
				this.offsetX.textboxKeyTyped(typedChar, keyCode);
				this.offsetZ.textboxKeyTyped(typedChar, keyCode);
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
		this.originX.mouseClicked(mouseX, mouseY, mouseButton);
		this.originZ.mouseClicked(mouseX, mouseY, mouseButton);
		this.offsetX.mouseClicked(mouseX, mouseY, mouseButton);
		this.offsetZ.mouseClicked(mouseX, mouseY, mouseButton);
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
		this.originX.updateCursorCounter();
		this.originZ.updateCursorCounter();
		this.offsetX.updateCursorCounter();
		this.offsetZ.updateCursorCounter();
		this.fillRadius.updateCursorCounter();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.scale.drawTextBox();
		this.originX.drawTextBox();
		this.originZ.drawTextBox();
		this.offsetX.drawTextBox();
		this.offsetZ.drawTextBox();
		this.fillRadius.drawTextBox();

		GuiHelper.drawString(this.fontRenderer, "Scale", this.width / 2 + 4, this.height / 2 - 72 + 3, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Orientation", this.width / 2 + 4, this.height / 2 - 72 + 3 + 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Origin X", this.width / 2 + 4, this.height / 2 - 72 + 3 + 48, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Origin Z", this.width / 2 + 4, this.height / 2 - 72 + 3 + 64, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Offset X", this.width / 2 + 4, this.height / 2 - 72 + 3 + 80, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Offset Z", this.width / 2 + 4, this.height / 2 - 72 + 3 + 96, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Fill Radius", this.width / 2 + 4, this.height / 2 - 72 + 3 + 128, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Map", this.width / 2, this.height / 2 - 96, 0xFFFFFF, true, true);
		GuiHelper.drawString(this.fontRenderer, "Advanced Mode", this.width / 2, this.height / 2 - 86, 0xFF0F0F, true, true);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public static class GuiButtonOrientation extends GuiButtonCustom {
		private final String[] displayStrings = { "North", "East", "South", "West" };
		private int index = 0;

		public GuiButtonOrientation(int buttonId, int x, int y, int width, int height, EnumFacing orientation) {
			super(buttonId, x, y, width, height, "");
			this.setDisplayString(orientation);
		}

		public void onMouseClick(boolean leftClick) {
			if (leftClick) {
				this.index = this.index < this.displayStrings.length - 1 ? this.index + 1 : 0;
			} else {
				this.index = this.index > 0 ? this.index - 1 : this.displayStrings.length - 1;
			}
			this.displayString = this.displayStrings[this.index];
		}

		public void setDisplayString(EnumFacing orientation) {
			switch (orientation) {
			case EAST:
				this.index = 1;
				break;
			case SOUTH:
				this.index = 2;
				break;
			case WEST:
				this.index = 3;
				break;
			default:
				this.index = 0;
				break;
			}
			this.displayString = this.displayStrings[this.index];
		}

		public EnumFacing getDirection() {
			switch (this.index) {
			case 1:
				return EnumFacing.EAST;
			case 2:
				return EnumFacing.SOUTH;
			case 3:
				return EnumFacing.WEST;
			default:
				return EnumFacing.NORTH;
			}
		}
	}

}
