package team.cqr.cqrepoured.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiDungeonMapTool extends Screen {

	private static int lastRadius = 100;
	private static long lastSeed = 0L;
	private static int lastDistance = 20;
	private static int lastSpread = 0;
	private static double lastRarityDivisor = 0.0D;
	private static boolean lastGenerateBiomes = false;

	private final Screen parent;
	private final List<TextFieldWidget> textFieldList = new ArrayList<>();
	private TextFieldWidget textFieldRadius;
	private TextFieldWidget textFieldSeed;
	private Button buttonRandSeed;
	private Button buttonWorldSeed;
	private TextFieldWidget textFieldDistance;
	private TextFieldWidget textFieldSpread;
	private TextFieldWidget textFieldRarityDivisor;
	private CheckboxButton checkBoxGenerateBiomes;
	private Button buttonExit;
	private Button buttonCancel;
	private Button buttonCreateMap;
	private boolean canExit = true;
	@Nullable
	//private DungeonMapTask task;

	public GuiDungeonMapTool(Screen parent) {
		super(new TranslationTextComponent("gui.cqrepoured.dungeon_map_tool.title"));
		this.parent = parent;
	}

	@Override
	public void init() {
		super.init();

		this.textFieldRadius = new TextFieldWidget(this.font, this.width / 2 - 20, 30, 150, 20, new TranslationTextComponent("gui.cqrepoured.dungeon_map_tool.radius")){
			@Override
			public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
				if (!super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastRadius = Integer.parseInt(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setValue(String textIn) {
				super.setValue(textIn);
				try {
					GuiDungeonMapTool.lastRadius = Integer.parseInt(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldRadius.setValue(Integer.toString(lastRadius));
		this.textFieldSeed = new TextFieldWidget(this.font, this.width / 2 - 20, 60, 150, 20,  new TranslationTextComponent("gui.cqrepoured.dungeon_map_tool.seed")){
			@Override
			public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
				if (!super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastSeed = Long.parseLong(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setValue(String textIn) {
				super.setValue(textIn);
				try {
					GuiDungeonMapTool.lastSeed = Long.parseLong(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldSeed.setValue(Long.toString(lastSeed));
		this.textFieldDistance = new TextFieldWidget(this.font, this.width / 2 - 20, 90, 150, 20,  new TranslationTextComponent("gui.cqrepoured.dungeon_map_tool.distance")){
			@Override
			public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
				if (!super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastDistance = Integer.parseInt(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setValue(String textIn) {
				super.setValue(textIn);
				try {
					GuiDungeonMapTool.lastDistance = Integer.parseInt(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldDistance.setValue(Integer.toString(lastDistance));
		this.textFieldDistance.setVisible(false);
		this.textFieldSpread = new TextFieldWidget(this.font, this.width / 2 - 20, 120, 150, 20, new TranslationTextComponent("gui.cqrepoured.dungeon_map_tool.spread")){
			@Override
			public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
				if (!super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastSpread = Integer.parseInt(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setValue(String textIn) {
				super.setValue(textIn);
				try {
					GuiDungeonMapTool.lastSpread = Integer.parseInt(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldSpread.setValue(Integer.toString(lastSpread));
		this.textFieldSpread.setVisible(false);
		this.textFieldRarityDivisor = new TextFieldWidget(this.font, this.width / 2 - 20, 150, 150, 20,  new TranslationTextComponent("gui.cqrepoured.dungeon_map_tool.rarity_divisor")){
			@Override
			public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
				if (!super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastRarityDivisor = Double.parseDouble(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setValue(String textIn) {
				super.setValue(textIn);
				try {
					GuiDungeonMapTool.lastRarityDivisor = Double.parseDouble(this.getValue());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldRarityDivisor.setValue(Double.toString(lastRarityDivisor));
		this.textFieldRarityDivisor.setVisible(false);

		this.buttonRandSeed = this.addButton(new Button(this.width / 2 + 140, 60, 20, 20, new StringTextComponent("R"), (button) -> {
				GuiDungeonMapTool.this.textFieldSeed.setValue(Long.toString(ThreadLocalRandom.current().nextLong()));
		}));
		this.buttonWorldSeed = this.addButton(new Button(this.width / 2 + 170, 60, 20, 20, new StringTextComponent("W"), (button) -> {
			if(this.minecraft.isLocalServer()) {
				GuiDungeonMapTool.this.textFieldSeed.setValue(Long.toString(GuiDungeonMapTool.this.minecraft.getSingleplayerServer().getWorldData().worldGenSettings().seed()));
			}
		}));
		this.buttonWorldSeed.visible = this.minecraft.isLocalServer();
		this.buttonExit = this.addButton(new Button(5, 5, 20, 20, new StringTextComponent("X"), (button) -> {
				if (GuiDungeonMapTool.this.canExit) {
					GuiDungeonMapTool.this.minecraft.setScreen(GuiDungeonMapTool.this.parent);
				}
		}));
		this.buttonCancel = this.addButton(new Button(this.width / 2 - 102, this.height - 24, 100, 20, new StringTextComponent("Cancel"), (button) -> {
			/*DungeonMapTask task1 = GuiDungeonMapTool.this.task;
			if (task1 != null) {
				task1.cancel();
			}*/
		}));
		this.buttonCancel.visible = false;
		this.buttonCreateMap = this.addButton(new Button(this.width / 2 + 2, this.height - 24, 100, 20, new StringTextComponent("Create Map"), (button) -> {
			int radius = Integer.parseInt(GuiDungeonMapTool.this.textFieldRadius.getValue());
			long seed = Long.parseLong(GuiDungeonMapTool.this.textFieldSeed.getValue());
			boolean generateBiomes = GuiDungeonMapTool.this.checkBoxGenerateBiomes.selected();

			//GuiDungeonMapTool.this.task = new DungeonMapTask(radius, seed, generateBiomes);
			GuiDungeonMapTool.this.canExit = false;
			GuiDungeonMapTool.this.buttonExit.visible = false;
			GuiDungeonMapTool.this.buttonCancel.visible = true;
			GuiDungeonMapTool.this.buttonCreateMap.visible = false;

			/*GuiDungeonMapTool.this.task.run().handleAsync((v, t) -> {
				GuiDungeonMapTool.this.canExit = true;
				GuiDungeonMapTool.this.buttonExit.visible = true;
				GuiDungeonMapTool.this.buttonCancel.visible = false;
				GuiDungeonMapTool.this.buttonCreateMap.visible = true;
				if (t != null) {
					if (t instanceof Exception) {
						CQRMain.logger.error("Failed creating dungeon map", t);
					} else {
						GuiDungeonMapTool.this.minecraft.delayCrash(new CrashReport("Failed generating dungeon map", t));
					}
				}
				return null;
			});*/
		}));

		this.checkBoxGenerateBiomes = this.addButton(new CheckboxButton(this.width / 2 - 20, 180, 20, 20, new StringTextComponent("Generate Biomes"), false));

		this.children.add(this.textFieldRadius);
		this.children.add(this.textFieldSeed);
		this.children.add(this.textFieldDistance);
		this.children.add(this.textFieldSpread);
		this.children.add(this.textFieldRarityDivisor);
	}

	@Override
	public void tick() {
		super.tick();
		this.textFieldRadius.tick();
		this.textFieldSeed.tick();
		this.textFieldDistance.tick();
		this.textFieldSpread.tick();
		this.textFieldRarityDivisor.tick();
	}

	@Override
	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		int i = 0;
		drawString(pMatrixStack, this.font, "Radius", this.width / 2 - 75, ++i * 30 + 6, 0xF0F0F0);
		this.textFieldRadius.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		drawString(pMatrixStack, this.font, "Seed", this.width / 2 - 75, ++i * 30 + 6, 0xF0F0F0);
		this.textFieldSeed.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		drawString(pMatrixStack, this.font, "Distance (WIP)", this.width / 2 - 70, ++i * 30 + 6, 0xF0F0F0);
		this.textFieldDistance.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		drawString(pMatrixStack, this.font, "Spread (WIP)", this.width / 2 - 75, ++i * 30 + 6, 0xF0F0F0);
		this.textFieldSpread.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		drawString(pMatrixStack, this.font, "Rarity Divisor (WIP)", this.width / 2 - 75, ++i * 30 + 6, 0xF0F0F0);
		this.textFieldRarityDivisor.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

		/*DungeonMapTask task1 = this.task;
		if (task1 != null) {
			drawString(pMatrixStack, this.font, this.task.getProgress().toString(), this.width / 2 + 120, this.height - 18, 0xF0F0F0);
		}*/
	}

}
