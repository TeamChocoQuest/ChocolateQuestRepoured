package team.cqr.cqrepoured.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.gui.GuiMapPlaceholder.GuiButtonOrientation;
import team.cqr.cqrepoured.network.client.packet.CPacketCloseMapPlaceholderGuiSimple;

@OnlyIn(Dist.CLIENT)
public class GuiMapPlaceholderSimple extends Screen {

	private final BlockPos pos;
	private final Direction facing;
	private TextFieldWidget scale;
	private GuiButtonOrientation orientation;
	private CheckboxButton lockOrientation;
	private TextFieldWidget sizeUp;
	private TextFieldWidget sizeDown;
	private TextFieldWidget sizeRight;
	private TextFieldWidget sizeLeft;
	private CheckboxButton fillMap;
	private TextFieldWidget fillRadius;

	public GuiMapPlaceholderSimple(BlockPos pos, Direction facing) {
		super(new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.title"));
		this.pos = pos;
		this.facing = facing;
	}

	@Override
	public void init() {
		super.init();

		this.scale = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72, 38, 12, new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.scale"));
		this.orientation = this.addButton(new GuiButtonOrientation(this.width / 2 - 38, this.height / 2 - 72 + 16, 40, 14, Direction.NORTH, new TranslationTextComponent("")));
		this.lockOrientation = this.addButton(new CheckboxButton(this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 32,  20, 20, new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.lock_orientation"), false));
		this.sizeUp = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 48, 38, 12, new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.size_up"));
		this.sizeDown = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 64, 38, 12, new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.size_down"));
		this.sizeRight = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 80, 38, 12, new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.size_right"));
		this.sizeLeft = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 96, 38, 12, new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.size_left"));
		this.fillMap = this.addButton(new CheckboxButton(this.width / 2 - 1 - 38, this.height / 2 + 1 - 72 + 112, 20, 20, new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.fill_map"), false));
		this.fillRadius = new TextFieldWidget(this.font, this.width / 2 + 1 - 38, this.height / 2 + 1 - 72 + 128, 38, 12, new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.fill_radius"));

		this.scale.setValue("0");
		this.sizeUp.setValue("0");
		this.sizeDown.setValue("0");
		this.sizeRight.setValue("0");
		this.sizeLeft.setValue("0");
		this.fillRadius.setValue("64");

		this.children.add(this.scale);
		this.children.add(this.sizeUp);
		this.children.add(this.sizeDown);
		this.children.add(this.sizeRight);
		this.children.add(this.sizeLeft);
		this.children.add(this.fillRadius);

	}

	@Override
	public void onClose() {
		onDone();
		super.onClose();
	}

	private void onDone() {
		int scale = this.parseInt(this.scale.getValue(), 0, 4, 0, "Invalid argument: scale");
		Direction orientation = this.orientation.getDirection();
		boolean lockOrientation = this.lockOrientation.selected();
		int sizeUp = this.parseInt(this.sizeUp.getValue(), 0, 16, 0, "Invalid argument: sizeUp");
		int sizeDown = this.parseInt(this.sizeDown.getValue(), 0, 16, 0, "Invalid argument: sizeDown");
		int sizeRight = this.parseInt(this.sizeRight.getValue(), 0, 16, 0, "Invalid argument: sizeRight");
		int sizeLeft = this.parseInt(this.sizeLeft.getValue(), 0, 16, 0, "Invalid argument: sizeLeft");
		boolean fillMap = this.fillMap.selected();
		int fillRadius = this.parseInt(this.fillRadius.getValue(), 0, 1024, 64, "Invalid argument: fillRadius");

		CQRMain.NETWORK.sendToServer(new CPacketCloseMapPlaceholderGuiSimple(this.pos, this.facing, scale, orientation, lockOrientation, sizeUp, sizeDown, sizeRight, sizeLeft, fillMap, fillRadius));
	}

	private int parseInt(String s, int min, int max, int defaultValue, String warning) {
		try {
			return Mth.clamp(Integer.parseInt(s), min, max);
		} catch (Exception e) {
			this.minecraft.player.sendMessage(new TextComponent(warning), this.minecraft.player.getUUID());
		}
		return defaultValue;
	}

	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
			return true;
		} else if (pKeyCode != 257 && pKeyCode != 335) {
			return false;
		} else {
			this.onDone();
			return true;
		}
	}


	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.scale.mouseClicked(mouseX, mouseY, mouseButton);
		this.sizeUp.mouseClicked(mouseX, mouseY, mouseButton);
		this.sizeDown.mouseClicked(mouseX, mouseY, mouseButton);
		this.sizeRight.mouseClicked(mouseX, mouseY, mouseButton);
		this.sizeLeft.mouseClicked(mouseX, mouseY, mouseButton);
		this.fillRadius.mouseClicked(mouseX, mouseY, mouseButton);

		if (this.orientation.isMouseOver(mouseX, mouseY) && (mouseButton == 0 || mouseButton == 1)) {
			if (mouseButton == 1) {
				this.orientation.playDownSound(this.minecraft.getSoundManager());
			}
			this.orientation.onMouseClick(mouseButton == 0);
		}
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		this.scale.tick();
		this.sizeUp.tick();
		this.sizeDown.tick();
		this.sizeRight.tick();
		this.sizeLeft.tick();
		this.fillRadius.tick();
	}

	@Override
	public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);


		drawString(pMatrixStack, this.font, "Scale", this.width / 2 + 4, this.height / 2 - 72 + 3, 0xE0E0E0);
		this.scale.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Orientation", this.width / 2 + 4, this.height / 2 - 72 + 3 + 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Size Up", this.width / 2 + 4, this.height / 2 - 72 + 3 + 48, 0xE0E0E0);
		this.sizeUp.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Size Down", this.width / 2 + 4, this.height / 2 - 72 + 3 + 64, 0xE0E0E0);
		this.sizeDown.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Size Right", this.width / 2 + 4, this.height / 2 - 72 + 3 + 80, 0xE0E0E0);
		this.sizeRight.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Size Left", this.width / 2 + 4, this.height / 2 - 72 + 3 + 96, 0xE0E0E0);
		this.sizeLeft.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Fill Radius", this.width / 2 + 4, this.height / 2 - 72 + 3 + 128, 0xE0E0E0);
		this.fillRadius.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		drawString(pMatrixStack, this.font, "Map", this.width / 2, this.height / 2 - 96, 0xFFFFFF);
		drawString(pMatrixStack, this.font, "Simple Mode", this.width / 2, this.height / 2 - 86, 0x0FFF0F);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
