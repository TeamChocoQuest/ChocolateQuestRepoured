package team.cqr.cqrepoured.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.util.tool.DungeonMapTool;

public class GuiDungeonMapTool extends GuiScreen {

	private final GuiScreen parent;
	private final List<GuiTextField> textFieldList = new ArrayList<>();
	private GuiNumberTextField textFieldRadius;
	private GuiNumberTextField textFieldSeed;
	private GuiNumberTextField textFieldDistance;
	private GuiNumberTextField textFieldSpread;
	private GuiNumberTextField textFieldRarityDivisor;
	private GuiButton buttonCancel;
	private GuiButton buttonCreateMap;
	private boolean canExit = true;

	public GuiDungeonMapTool(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	public void initGui() {
		super.initGui();
		int id = 0;

		this.textFieldRadius = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 2, id * 30, 150,
				20, false, false);
		this.textFieldSeed = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 2, id * 30, 150, 20,
				false, false);
		this.textFieldDistance = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 2, id * 30, 150, 20,
				false, false);
		this.textFieldSpread = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 2, id * 30, 150, 20,
				false, false);
		this.textFieldRarityDivisor = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 2, id * 30, 150, 20,
				false, true);

		this.textFieldRadius.setText("100");
		this.textFieldSeed.setText("0");
		this.textFieldDistance.setText("20");
		this.textFieldSpread.setText("0");
		this.textFieldRarityDivisor.setText("1.0");

		this.textFieldList.add(this.textFieldRadius);
		this.textFieldList.add(this.textFieldSeed);
		this.textFieldList.add(this.textFieldDistance);
		this.textFieldList.add(this.textFieldSpread);
		this.textFieldList.add(this.textFieldRarityDivisor);

		this.buttonCancel = new GuiButton(id++, this.width / 2 - 102, this.height - 24, 100, 20, "Cancel") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				if (this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height) {
					if (GuiDungeonMapTool.this.canExit) {
						GuiDungeonMapTool.this.mc.displayGuiScreen(GuiDungeonMapTool.this.parent);
					}
				}
			}
		};
		this.buttonCreateMap = new GuiButton(id++, this.width / 2 + 2, this.height - 24, 100, 20, "Create Map") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				if (this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height) {
					GuiDungeonMapTool.this.canExit = false;
					GuiDungeonMapTool.this.buttonCancel.enabled = false;
					GuiDungeonMapTool.this.buttonCreateMap.enabled = false;
					try {
						int radius = GuiDungeonMapTool.this.textFieldRadius.getInt();
						if (GuiDungeonMapTool.this.textFieldSeed.getLong() == 0) {
							GuiDungeonMapTool.this.textFieldSeed.setText(Long.toString(new Random().nextLong()));
						}
						long seed = GuiDungeonMapTool.this.textFieldSeed.getLong();
						int distance = GuiDungeonMapTool.this.textFieldDistance.getInt();
						int spread = GuiDungeonMapTool.this.textFieldSpread.getInt();
						double rarityDivisor = GuiDungeonMapTool.this.textFieldRarityDivisor.getDouble();

						new Thread(() -> {
							DungeonMapTool.run(radius, seed, distance, spread, rarityDivisor);

							GuiDungeonMapTool.this.canExit = true;
							GuiDungeonMapTool.this.buttonCancel.enabled = true;
							GuiDungeonMapTool.this.buttonCreateMap.enabled = true;
						}).start();
					} catch (Exception e) {
						e.printStackTrace();
						GuiDungeonMapTool.this.canExit = true;
						GuiDungeonMapTool.this.buttonCancel.enabled = true;
						GuiDungeonMapTool.this.buttonCreateMap.enabled = true;
					}
				}
			}
		};
		
		this.buttonList.add(this.buttonCancel);
		this.buttonList.add(this.buttonCreateMap);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		Optional<GuiTextField> focusedTextField = this.textFieldList.stream().filter(GuiTextField::isFocused)
				.findFirst();
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
		this.textFieldList.forEach(GuiTextField::updateCursorCounter);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.textFieldList.forEach(GuiTextField::drawTextBox);
		int i = 0;
		GuiHelper.drawString(this.fontRenderer, "Radius", this.width / 2 - 42, ++i * 30 + 6, 0xF0F0F0, true, false);
		GuiHelper.drawString(this.fontRenderer, "Seed", this.width / 2 - 42, ++i * 30 + 6, 0xF0F0F0, true, false);
		GuiHelper.drawString(this.fontRenderer, "Distance", this.width / 2 - 42, ++i * 30 + 6, 0xF0F0F0, true, false);
		GuiHelper.drawString(this.fontRenderer, "Spread", this.width / 2 - 42, ++i * 30 + 6, 0xF0F0F0, true, false);
		GuiHelper.drawString(this.fontRenderer, "Rarity Divisor", this.width / 2 - 42, ++i * 30 + 6, 0xF0F0F0, true, false);
	}

}
