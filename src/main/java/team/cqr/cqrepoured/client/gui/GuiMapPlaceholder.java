package team.cqr.cqrepoured.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.tileentity.TileEntityMap;

@OnlyIn(Dist.CLIENT)
public class GuiMapPlaceholder extends Screen {

	private final TileEntityMap tileEntity;

	private TextFieldWidget scale;
	private GuiButtonOrientation orientation;
	private CheckboxButton lockOrientation;
	private TextFieldWidget originX;
	private TextFieldWidget originZ;
	private TextFieldWidget offsetX;
	private TextFieldWidget offsetZ;
	private CheckboxButton fillMap;
	private TextFieldWidget fillRadius;

	public GuiMapPlaceholder(TileEntityMap tileEntity) {
		super(new StringTextComponent("Map Placeholder"));
		this.minecraft = Minecraft.getInstance();
		this.tileEntity = tileEntity;
	}

	@Override
	public void init() {
		this.scale = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72, 38, 12, new TranslationTextComponent("map_placeholder.scale"));
		this.scale.setValue(Integer.toString(this.tileEntity.getScale()));
		this.children.add(this.scale);
		this.orientation = this.addButton(new GuiButtonOrientation(this.width / 2 - 38, this.height / 2 - 72 + 16, 40, 14, this.tileEntity.getOrientation(), new TranslationTextComponent("")));
		this.orientation.setDisplayString(this.tileEntity.getOrientation());
		this.lockOrientation = this.addButton(new CheckboxButton(this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 32, 20, 20,new TranslationTextComponent("map_placeholder.lock_orientation"), this.tileEntity.lockOrientation()));
		this.originX = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 48, 38, 12, new TranslationTextComponent("map_placeholder.origin_x"));
		this.originX.setValue(Integer.toString(this.tileEntity.getOriginX()));
		this.children.add(this.originX);
		this.originZ = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 64, 38, 12, new TranslationTextComponent("map_placeholder.origin_z"));
		this.originZ.setValue(Integer.toString(this.tileEntity.getOriginZ()));
		this.children.add(this.originZ);
		this.offsetX = new TextFieldWidget( this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 80, 38, 12, new TranslationTextComponent("map_placeholder.offset_x"));
		this.offsetX.setValue(Integer.toString(this.tileEntity.getOffsetX()));
		this.children.add(this.offsetX);
		this.offsetZ = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 96, 38, 12, new TranslationTextComponent("map_placeholder.offset_z"));
		this.offsetZ.setValue(Integer.toString(this.tileEntity.getOffsetZ()));
		this.children.add(this.offsetZ);
		this.fillMap = this.addButton(new CheckboxButton(this.width / 2 - 1 - 38, this.height / 2 + 1 - 72 + 112, 20, 20, new TranslationTextComponent("map_placeholder.fill_map"), this.tileEntity.fillMap()));
		this.fillRadius = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 128, 38, 12, new TranslationTextComponent("map_placeholder.fill_radius"));
		this.fillRadius.setValue(Integer.toString(this.tileEntity.getFillRadius()));
		this.children.add(this.fillRadius);
	}



	@Override
	public void onClose() {
		try {
			int scale = Integer.parseInt(this.scale.getMessage().getString());
			Direction orientation = this.orientation.getDirection();
			boolean lockOrientation = this.lockOrientation.selected();
			int originX = Integer.parseInt(this.originX.getMessage().getString());
			int originZ = Integer.parseInt(this.originZ.getMessage().getString());
			int offsetX = Integer.parseInt(this.offsetX.getMessage().getString());
			int offsetZ = Integer.parseInt(this.offsetZ.getMessage().getString());
			boolean fillMap = this.fillMap.selected();
			int fillRadius = Integer.parseInt(this.fillRadius.getMessage().getString());

			this.tileEntity.set(scale, orientation, lockOrientation, originX, originZ, offsetX, offsetZ, fillMap, fillRadius);
		} catch (IllegalArgumentException e) {
			Minecraft.getInstance().player.sendMessage(new StringTextComponent("Invalid arguments"), Minecraft.getInstance().player.getUUID());
		}

		super.onClose();
	}



	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		InputMappings.Input keyInput = InputMappings.getKey(pKeyCode, pScanCode);
		if (this.scale.isFocused() || this.originX.isFocused() || this.originZ.isFocused() || this.offsetX.isFocused() || this.offsetZ.isFocused() || this.fillRadius.isFocused()) {
			if (pKeyCode == 0) {
				this.scale.setFocus(false);
				this.originX.setFocus(false);
				this.originZ.setFocus(false);
				this.offsetX.setFocus(false);
				this.offsetZ.setFocus(false);
				this.fillRadius.setFocus(false);
			} else if (pKeyCode == 14 || pKeyCode == 211 || pKeyCode == 203 || pKeyCode == 205 || pKeyCode == 199 || pKeyCode == 207) {
				this.scale.keyPressed(pKeyCode, pScanCode, pModifiers);
				this.originX.keyPressed(pKeyCode, pScanCode, pModifiers);
				this.originZ.keyPressed(pKeyCode, pScanCode, pModifiers);
				this.offsetX.keyPressed(pKeyCode, pScanCode, pModifiers);
				this.offsetZ.keyPressed(pKeyCode, pScanCode, pModifiers);
				this.fillRadius.keyPressed(pKeyCode, pScanCode, pModifiers);
			}
		} else if (pKeyCode == 1 || this.minecraft.options.keyInventory.isActiveAndMatches(keyInput)) {
			this.minecraft.player.closeContainer();
		} else {
			if (!super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		boolean sr = super.mouseClicked(mouseX, mouseY, mouseButton);

		this.scale.mouseClicked(mouseX, mouseY, mouseButton);
		this.originX.mouseClicked(mouseX, mouseY, mouseButton);
		this.originZ.mouseClicked(mouseX, mouseY, mouseButton);
		this.offsetX.mouseClicked(mouseX, mouseY, mouseButton);
		this.offsetZ.mouseClicked(mouseX, mouseY, mouseButton);
		this.fillRadius.mouseClicked(mouseX, mouseY, mouseButton);

		if (this.orientation.isMouseOver(mouseX, mouseY) && (mouseButton == 0 || mouseButton == 1)) {
			if (mouseButton == 1) {
				this.orientation.playDownSound(this.minecraft.getSoundManager());
			}
			this.orientation.onMouseClick(mouseButton == 0);
		}

		return sr;
	}

	@Override
	public void tick() {
		super.tick();

		this.scale.tick();
		this.originX.tick();
		this.originZ.tick();
		this.offsetX.tick();
		this.offsetZ.tick();
		this.fillRadius.tick();
	}

	@Override
	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		drawString(pMatrixStack, this.font, "Scale", this.width / 2 + 4, this.height / 2 - 72 + 3, 0xE0E0E0);
		this.scale.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Orientation", this.width / 2 + 4, this.height / 2 - 72 + 3 + 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Origin X", this.width / 2 + 4, this.height / 2 - 72 + 3 + 48, 0xE0E0E0);
		this.originX.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Origin Z", this.width / 2 + 4, this.height / 2 - 72 + 3 + 64, 0xE0E0E0);
		this.originZ.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Offset X", this.width / 2 + 4, this.height / 2 - 72 + 3 + 80, 0xE0E0E0);
		this.offsetX.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Offset Z", this.width / 2 + 4, this.height / 2 - 72 + 3 + 96, 0xE0E0E0);
		this.offsetZ.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Fill Radius", this.width / 2 + 4, this.height / 2 - 72 + 3 + 128, 0xE0E0E0);
		this.fillRadius.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Map", this.width / 2, this.height / 2 - 96, 0xFFFFFF);
		drawString(pMatrixStack, this.font, "Advanced Mode", this.width / 2, this.height / 2 - 86, 0xFF0F0F);

	}


	@Override
	public boolean isPauseScreen() {
		return false;
	}

	public static class GuiButtonOrientation extends Button {
		private final String[] displayStrings = { "North", "East", "South", "West" };
		private int index = 0;

		public GuiButtonOrientation(int x, int y, int width, int height, Direction orientation, TranslationTextComponent pMessage) {
			super(x, y, width, height, pMessage, (p_214283_1_) -> {
			});
			this.setDisplayString(orientation);
		}

		public void onMouseClick(boolean leftClick) {
			if (leftClick) {
				this.index = this.index < this.displayStrings.length - 1 ? this.index + 1 : 0;
			} else {
				this.index = this.index > 0 ? this.index - 1 : this.displayStrings.length - 1;
			}
			this.setMessage(new StringTextComponent(this.displayStrings[this.index]));
		}

		public void setDisplayString(Direction orientation) {
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
			this.setMessage(new StringTextComponent(this.displayStrings[this.index]));
		}

		public Direction getDirection() {
			switch (this.index) {
			case 1:
				return Direction.EAST;
			case 2:
				return Direction.SOUTH;
			case 3:
				return Direction.WEST;
			default:
				return Direction.NORTH;
			}
		}
	}

}
