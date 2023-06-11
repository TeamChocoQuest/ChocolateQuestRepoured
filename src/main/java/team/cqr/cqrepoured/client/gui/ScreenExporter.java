package team.cqr.cqrepoured.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

@OnlyIn(Dist.CLIENT)
public class ScreenExporter extends Screen {

	// TODO update unprotected positions list

	private final TileEntityExporter tileEntity;
	// private final DummyListElement unprotectedBlocksConfig;

	private Button btnExport;
	private Button btnUnprotectedBlocks;
	private TextFieldWidget edtName;
	private NumberTextField<Integer> edtEndX;
	private NumberTextField<Integer> edtEndY;
	private NumberTextField<Integer> edtEndZ;
	private NumberTextField<Integer> edtStartX;
	private NumberTextField<Integer> edtStartY;
	private NumberTextField<Integer> edtStartZ;
	private CheckboxButton chbxRelativeMode;
	private CheckboxButton chbxIgnoreEntities;

	public ScreenExporter(TextComponent title, TileEntityExporter tileEntity) {
		super(title);
		this.tileEntity = tileEntity;
		/*
		String[] unprotectedBlocks = Arrays.stream(this.tileEntity.getUnprotectedBlocks()).map(p -> String.format("%d %d %d", p.getX(), p.getY(), p.getZ())).toArray(String[]::new);
		this.unprotectedBlocksConfig = new DummyListElement("test", unprotectedBlocks, ConfigGuiType.STRING, "test");
		*/
	}

	@Override
	public void init() {
		super.init();

		this.edtName = this.addButton(new TextFieldWidget(this.font, this.width / 2 - 200, this.height / 2 - 70, 400, 20, null));
		this.edtName.setMaxLength(1024);
		this.edtName.setValue(this.tileEntity.getStructureName());

		this.edtEndX = this.addButton(NumberTextField.integerTextField(this.font, this.width / 2 - 70, this.height / 2 + 10, 40, 20, this.tileEntity.getEndX()));
		this.edtEndY = this.addButton(NumberTextField.integerTextField(this.font, this.width / 2 - 70 + 50, this.height / 2 + 10, 40, 20, this.tileEntity.getEndY()));
		this.edtEndZ = this.addButton(NumberTextField.integerTextField(this.font, this.width / 2 - 70 + 50 + 50, this.height / 2 + 10, 40, 20, this.tileEntity.getEndZ()));

		this.edtStartX = this.addButton(NumberTextField.integerTextField(this.font, this.width / 2 - 70, this.height / 2 - 30, 40, 20, this.tileEntity.getStartX()));
		this.edtStartY = this.addButton(NumberTextField.integerTextField(this.font, this.width / 2 - 70 + 50, this.height / 2 - 30, 40, 20, this.tileEntity.getStartY()));
		this.edtStartZ = this.addButton(NumberTextField.integerTextField(this.font, this.width / 2 - 70 + 50 + 50, this.height / 2 - 30, 40, 20, this.tileEntity.getStartZ()));

		this.chbxRelativeMode = this.addButton(new CheckboxButton(this.width / 2 + 30, this.height / 2 + 40, 20, 20, new TextComponent("Use Relative Mode"), this.tileEntity.isRelativeMode()) {
			@Override
			public void onPress() {
				super.onPress();
				boolean flag = chbxRelativeMode.selected();
				BlockPos pos = tileEntity.getBlockPos();
				edtStartX.setNumber(edtStartX.getNumber() + (flag ? -pos.getX() : pos.getX()));
				edtStartY.setNumber(edtStartY.getNumber() + (flag ? -pos.getY() : pos.getY()));
				edtStartZ.setNumber(edtStartZ.getNumber() + (flag ? -pos.getZ() : pos.getZ()));
				edtEndX.setNumber(edtEndX.getNumber() + (flag ? -pos.getX() : pos.getX()));
				edtEndY.setNumber(edtEndY.getNumber() + (flag ? -pos.getY() : pos.getY()));
				edtEndZ.setNumber(edtEndZ.getNumber() + (flag ? -pos.getZ() : pos.getZ()));
			}
		});
		this.chbxIgnoreEntities = this.addButton(new CheckboxButton(this.width / 2 - 70, this.height / 2 + 40, 20, 20, new TextComponent("Ignore Entities"), this.tileEntity.isIgnoreEntities()));

		this.btnExport = this.addButton(new Button(this.width / 2 - 70, this.height / 2 + 90, 140, 20, new TextComponent("Export"), button -> {
			this.minecraft.setScreen(null);
			this.tileEntity.saveStructure(this.minecraft.player);
		}));

		this.btnUnprotectedBlocks = this.addButton(new Button(this.width / 2 - 70, this.height / 2 + 65, 140, 20, new TextComponent("Unprotected Blocks (WIP)"), button -> {

		}));
		this.btnUnprotectedBlocks.active = false;
	}

	@Override
	public void onClose() {
		super.onClose();

		String structName = this.edtName.getValue();
		if (structName.isEmpty()) {
			structName = "Unnamed";
		}
		int startX = this.edtStartX.getNumber();
		int startY = this.edtStartY.getNumber();
		int startZ = this.edtStartZ.getNumber();
		int endX = this.edtEndX.getNumber();
		int endY = this.edtEndY.getNumber();
		int endZ = this.edtEndZ.getNumber();
		/*
		BlockPos[] unprotectedBlocks = Arrays.stream(this.unprotectedBlocksConfig.getDefaults()).map(obj -> {
			String[] arr = ((String) obj).split(" ");
			return new BlockPos(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
		}).toArray(BlockPos[]::new);
		 */
		boolean relativeMode = this.chbxRelativeMode.selected();
		boolean ignoreEntities = this.chbxIgnoreEntities.selected();

		this.tileEntity.setValues(structName, startX, startY, startZ, endX, endY, endZ, relativeMode, ignoreEntities, this.tileEntity.getUnprotectedBlocks());
	}

	@Override
	public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

		AbstractGui.drawCenteredString(pMatrixStack, this.font, this.title, this.width / 2, 20, 0xFFFFFF);

		AbstractGui.drawString(pMatrixStack, this.font, "Start X", this.width / 2 - 70, this.height / 2 - 40, 0xA0A0A0);
		AbstractGui.drawString(pMatrixStack, this.font, "Start Y", this.width / 2 - 20, this.height / 2 - 40, 0xA0A0A0);
		AbstractGui.drawString(pMatrixStack, this.font, "Start Z", this.width / 2 + 30, this.height / 2 - 40, 0xA0A0A0);

		AbstractGui.drawString(pMatrixStack, this.font, "End X", this.width / 2 - 70, this.height / 2, 0xA0A0A0);
		AbstractGui.drawString(pMatrixStack, this.font, "End Y", this.width / 2 - 20, this.height / 2, 0xA0A0A0);
		AbstractGui.drawString(pMatrixStack, this.font, "End Z", this.width / 2 + 30, this.height / 2, 0xA0A0A0);

		if (this.chbxRelativeMode.isMouseOver(pMouseX, pMouseY)) {
			this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_exporter_relative_mode.name"), pMouseX, pMouseY);
		} else if (this.chbxIgnoreEntities.isMouseOver(pMouseX, pMouseY)) {
			this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_exporter_ignore_entities.name"), pMouseX, pMouseY);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
