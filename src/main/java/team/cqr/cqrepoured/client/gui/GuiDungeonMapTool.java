package team.cqr.cqrepoured.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.util.tool.DungeonMapTask;

public class GuiDungeonMapTool extends Screen {

	private static int lastRadius = 100;
	private static long lastSeed = 0L;
	private static int lastDistance = 20;
	private static int lastSpread = 0;
	private static double lastRarityDivisor = 0.0D;
	private static boolean lastGenerateBiomes = false;

	private final Screen parent;
	private final List<TextFieldWidget> textFieldList = new ArrayList<>();
	private GuiNumberTextField textFieldRadius;
	private GuiNumberTextField textFieldSeed;
	private Button buttonRandSeed;
	private Button buttonWorldSeed;
	private GuiNumberTextField textFieldDistance;
	private GuiNumberTextField textFieldSpread;
	private GuiNumberTextField textFieldRarityDivisor;
	private GuiCheckBox checkBoxGenerateBiomes;
	private Button buttonExit;
	private Button buttonCancel;
	private Button buttonCreateMap;
	private boolean canExit = true;
	@Nullable
	private DungeonMapTask task;

	public GuiDungeonMapTool(Screen parent) {
		this.parent = parent;
	}

	@Override
	public void initGui() {
		super.initGui();
		int id = 0;

		this.textFieldRadius = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 - 20, 30, 150, 20, false, false) {
			@Override
			public boolean textboxKeyTyped(char typedChar, int keyCode) {
				if (!super.textboxKeyTyped(typedChar, keyCode)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastRadius = Integer.parseInt(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setText(String textIn) {
				super.setText(textIn);
				try {
					GuiDungeonMapTool.lastRadius = Integer.parseInt(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldRadius.setText(Integer.toString(lastRadius));
		this.textFieldSeed = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 - 20, 60, 150, 20, false, false) {
			@Override
			public boolean textboxKeyTyped(char typedChar, int keyCode) {
				if (!super.textboxKeyTyped(typedChar, keyCode)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastSeed = Long.parseLong(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setText(String textIn) {
				super.setText(textIn);
				try {
					GuiDungeonMapTool.lastSeed = Long.parseLong(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldSeed.setText(Long.toString(lastSeed));
		this.textFieldDistance = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 - 20, 90, 150, 20, false, false) {
			@Override
			public boolean textboxKeyTyped(char typedChar, int keyCode) {
				if (!super.textboxKeyTyped(typedChar, keyCode)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastDistance = Integer.parseInt(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setText(String textIn) {
				super.setText(textIn);
				try {
					GuiDungeonMapTool.lastDistance = Integer.parseInt(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldDistance.setText(Integer.toString(lastDistance));
		this.textFieldDistance.setEnabled(false);
		this.textFieldSpread = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 - 20, 120, 150, 20, false, false) {
			@Override
			public boolean textboxKeyTyped(char typedChar, int keyCode) {
				if (!super.textboxKeyTyped(typedChar, keyCode)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastSpread = Integer.parseInt(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setText(String textIn) {
				super.setText(textIn);
				try {
					GuiDungeonMapTool.lastSpread = Integer.parseInt(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldSpread.setText(Integer.toString(lastSpread));
		this.textFieldSpread.setEnabled(false);
		this.textFieldRarityDivisor = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 - 20, 150, 150, 20, false, true) {
			@Override
			public boolean textboxKeyTyped(char typedChar, int keyCode) {
				if (!super.textboxKeyTyped(typedChar, keyCode)) {
					return false;
				}
				try {
					GuiDungeonMapTool.lastRarityDivisor = Double.parseDouble(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
				return true;
			}

			@Override
			public void setText(String textIn) {
				super.setText(textIn);
				try {
					GuiDungeonMapTool.lastRarityDivisor = Double.parseDouble(this.getText());
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		};
		this.textFieldRarityDivisor.setText(Double.toString(lastRarityDivisor));
		this.textFieldRarityDivisor.setEnabled(false);

		this.buttonRandSeed = new Button(id++, this.width / 2 + 140, 60, 20, 20, "R") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				GuiDungeonMapTool.this.textFieldSeed.setText(Long.toString(ThreadLocalRandom.current().nextLong()));
				return true;
			}
		};
		this.buttonWorldSeed = new Button(id++, this.width / 2 + 170, 60, 20, 20, "W") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				GuiDungeonMapTool.this.textFieldSeed.setText(Long.toString(GuiDungeonMapTool.this.mc.getIntegratedServer().getWorld(0).getSeed()));
				return true;
			}
		};
		this.buttonWorldSeed.enabled = this.mc.isIntegratedServerRunning();
		this.buttonExit = new Button(id++, 5, 5, 20, 20, "X") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				if (!GuiDungeonMapTool.this.canExit) {
					return false;
				}
				GuiDungeonMapTool.this.mc.displayGuiScreen(GuiDungeonMapTool.this.parent);
				return true;
			}
		};
		this.buttonCancel = new Button(id++, this.width / 2 - 102, this.height - 24, 100, 20, "Cancel") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				DungeonMapTask task1 = GuiDungeonMapTool.this.task;
				if (task1 != null) {
					task1.cancel();
				}
				return true;
			}
		};
		this.buttonCancel.enabled = false;
		this.buttonCreateMap = new Button(id++, this.width / 2 + 2, this.height - 24, 100, 20, "Create Map") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				int radius = GuiDungeonMapTool.this.textFieldRadius.getInt();
				long seed = GuiDungeonMapTool.this.textFieldSeed.getLong();
				boolean generateBiomes = GuiDungeonMapTool.this.checkBoxGenerateBiomes.isChecked();

				GuiDungeonMapTool.this.task = new DungeonMapTask(radius, seed, generateBiomes);
				GuiDungeonMapTool.this.canExit = false;
				GuiDungeonMapTool.this.buttonExit.enabled = false;
				GuiDungeonMapTool.this.buttonCancel.enabled = true;
				GuiDungeonMapTool.this.buttonCreateMap.enabled = false;

				GuiDungeonMapTool.this.task.run().handleAsync((v, t) -> {
					GuiDungeonMapTool.this.canExit = true;
					GuiDungeonMapTool.this.buttonExit.enabled = true;
					GuiDungeonMapTool.this.buttonCancel.enabled = false;
					GuiDungeonMapTool.this.buttonCreateMap.enabled = true;
					if (t != null) {
						if (t instanceof Exception) {
							CQRMain.logger.error("Failed creating dungeon map", t);
						} else {
							mc.crashed(new CrashReport("Failed generating dungeon map", t));
						}
					}
					return null;
				});

				return true;
			}
		};

		this.checkBoxGenerateBiomes = new GuiCheckBox(id++, this.width / 2 - 20, 180, "Generate Biomes", lastGenerateBiomes) {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				GuiDungeonMapTool.lastGenerateBiomes = this.isChecked();
				return true;
			}

			@Override
			public void setIsChecked(boolean isChecked) {
				super.setIsChecked(isChecked);
				GuiDungeonMapTool.lastGenerateBiomes = this.isChecked();
			}
		};

		this.textFieldList.add(this.textFieldRadius);
		this.textFieldList.add(this.textFieldSeed);
		this.buttonList.add(this.buttonRandSeed);
		this.buttonList.add(this.buttonWorldSeed);
		this.textFieldList.add(this.textFieldDistance);
		this.textFieldList.add(this.textFieldSpread);
		this.textFieldList.add(this.textFieldRarityDivisor);
		this.buttonList.add(this.checkBoxGenerateBiomes);
		this.buttonList.add(this.buttonExit);
		this.buttonList.add(this.buttonCancel);
		this.buttonList.add(this.buttonCreateMap);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		Optional<TextFieldWidget> focusedTextField = this.textFieldList.stream().filter(TextFieldWidget::isFocused).findFirst();
		if (focusedTextField.isPresent()) {
			if (keyCode == 1) {
				focusedTextField.get().setFocused(false);
			} else {
				focusedTextField.get().textboxKeyTyped(typedChar, keyCode);
			}
		} else if (keyCode == 1) {
			if (this.canExit) {
				this.mc.displayGuiScreen(this.parent);
			}
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.textFieldList.forEach(tf -> tf.mouseClicked(mouseX, mouseY, mouseButton));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.textFieldList.forEach(TextFieldWidget::updateCursorCounter);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.textFieldList.forEach(TextFieldWidget::drawTextBox);
		int i = 0;
		GuiHelper.drawString(this.fontRenderer, "Radius", this.width / 2 - 75, ++i * 30 + 6, 0xF0F0F0, true, false);
		GuiHelper.drawString(this.fontRenderer, "Seed", this.width / 2 - 75, ++i * 30 + 6, 0xF0F0F0, true, false);
		GuiHelper.drawString(this.fontRenderer, "Distance (WIP)", this.width / 2 - 70, ++i * 30 + 6, 0xF0F0F0, true, false);
		GuiHelper.drawString(this.fontRenderer, "Spread (WIP)", this.width / 2 - 75, ++i * 30 + 6, 0xF0F0F0, true, false);
		GuiHelper.drawString(this.fontRenderer, "Rarity Divisor (WIP)", this.width / 2 - 75, ++i * 30 + 6, 0xF0F0F0, true, false);

		DungeonMapTask task1 = this.task;
		if (task1 != null) {
			GuiHelper.drawString(this.fontRenderer, this.task.getProgress().toString(), this.width / 2 + 120, this.height - 18, 0xF0F0F0, false, false);
		}
	}

}
