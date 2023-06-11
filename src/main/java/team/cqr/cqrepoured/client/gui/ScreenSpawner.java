package team.cqr.cqrepoured.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.inventory.ContainerSpawner;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

@OnlyIn(Dist.CLIENT)
public class ScreenSpawner extends ContainerScreen<ContainerSpawner> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");

	private final TileEntitySpawner tileEntity;
	private CheckboxButton vanillaSpawnerCheckBox;
	private NumberTextField<Integer> minSpawnDelayTextField;
	private NumberTextField<Integer> maxSpawnDelayTextField;
	private NumberTextField<Integer> spawnCountTextField;
	private NumberTextField<Integer> maxNearbyEntitiesTextField;
	private NumberTextField<Integer> activatingRangeFromPlayerTextField;
	private NumberTextField<Integer> spawnRangeTextField;

	public ScreenSpawner(ContainerSpawner container, PlayerInventory playerInv, ITextComponent title) {
		super(container, playerInv, title);
		this.tileEntity = container.getTileEntity();
	}

	@Override
	public void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;

		this.vanillaSpawnerCheckBox = this.addButton(new CheckboxButton(this.width / 2 + 90, this.height / 2 - 80, 20,
				20, new StringTextComponent("Vanilla Spawner"), this.tileEntity.isVanillaSpawner()) {
			@Override
			public void onPress() {
				super.onPress();
				boolean flag = vanillaSpawnerCheckBox.selected();
				minSpawnDelayTextField.setFocus(false);
				minSpawnDelayTextField.setEditable(flag);
				minSpawnDelayTextField.setCanLoseFocus(flag);
				maxSpawnDelayTextField.setFocus(false);
				maxSpawnDelayTextField.setEditable(flag);
				maxSpawnDelayTextField.setCanLoseFocus(flag);
				spawnCountTextField.setFocus(false);
				spawnCountTextField.setEditable(flag);
				spawnCountTextField.setCanLoseFocus(flag);
				maxNearbyEntitiesTextField.setFocus(false);
				maxNearbyEntitiesTextField.setEditable(flag);
				maxNearbyEntitiesTextField.setCanLoseFocus(flag);
				activatingRangeFromPlayerTextField.setFocus(false);
				activatingRangeFromPlayerTextField.setEditable(flag);
				activatingRangeFromPlayerTextField.setCanLoseFocus(flag);
				spawnRangeTextField.setFocus(false);
				spawnRangeTextField.setEditable(flag);
				spawnRangeTextField.setCanLoseFocus(flag);
			}
		});
		this.minSpawnDelayTextField = this.addButton(NumberTextField.positiveIntegerTextField(this.font,
				this.width / 2 + 91, this.height / 2 - 56, 32, 10, this.tileEntity.getMinSpawnDelay()));
		this.maxSpawnDelayTextField = this.addButton(NumberTextField.positiveIntegerTextField(this.font,
				this.width / 2 + 91, this.height / 2 - 42, 32, 10, this.tileEntity.getMaxSpawnDelay()));
		this.spawnCountTextField = this.addButton(NumberTextField.positiveIntegerTextField(this.font,
				this.width / 2 + 91, this.height / 2 - 28, 32, 10, this.tileEntity.getSpawnCount()));
		this.maxNearbyEntitiesTextField = this.addButton(NumberTextField.positiveIntegerTextField(this.font,
				this.width / 2 + 91, this.height / 2 - 14, 32, 10, this.tileEntity.getMaxNearbyEntities()));
		this.activatingRangeFromPlayerTextField = this.addButton(NumberTextField.positiveIntegerTextField(this.font,
				this.width / 2 + 91, this.height / 2, 32, 10, this.tileEntity.getActivatingRangeFromPlayer()));
		this.spawnRangeTextField = this.addButton(NumberTextField.positiveIntegerTextField(this.font,
				this.width / 2 + 91, this.height / 2 + 14, 32, 10, this.tileEntity.getSpawnRange()));

		if (!this.vanillaSpawnerCheckBox.selected()) {
			minSpawnDelayTextField.setEditable(false);
			minSpawnDelayTextField.setCanLoseFocus(false);
			maxSpawnDelayTextField.setEditable(false);
			maxSpawnDelayTextField.setCanLoseFocus(false);
			spawnCountTextField.setEditable(false);
			spawnCountTextField.setCanLoseFocus(false);
			maxNearbyEntitiesTextField.setEditable(false);
			maxNearbyEntitiesTextField.setCanLoseFocus(false);
			activatingRangeFromPlayerTextField.setEditable(false);
			activatingRangeFromPlayerTextField.setCanLoseFocus(false);
			spawnRangeTextField.setEditable(false);
			spawnRangeTextField.setCanLoseFocus(false);
		}
	}

	@Override
	public void onClose() {
		super.onClose();

		if (this.vanillaSpawnerCheckBox.selected()) {
			int minSpawnDelay = this.minSpawnDelayTextField.getNumber();
			int maxSpawnDelay = this.maxSpawnDelayTextField.getNumber();
			int spawnCount = this.spawnCountTextField.getNumber();
			int maxNearbyEntities = this.maxNearbyEntitiesTextField.getNumber();
			int activatingRangeFromPlayer = this.activatingRangeFromPlayerTextField.getNumber();
			int spawnRange = this.spawnRangeTextField.getNumber();
			this.tileEntity.setVanillaSpawner(minSpawnDelay, maxSpawnDelay, spawnCount, maxNearbyEntities,
					activatingRangeFromPlayer, spawnRange);
		} else {
			this.tileEntity.setCQRSpawner();
		}
	}

	@Override
	protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
		super.renderLabels(pMatrixStack, pX, pY);

		this.font.drawShadow(pMatrixStack, "Min Spawn Delay", 214, 28, 0xE0E0E0);
		this.font.drawShadow(pMatrixStack, "Max Spawn Delay", 214, 42, 0xE0E0E0);
		this.font.drawShadow(pMatrixStack, "Spawn Count", 214, 56, 0xE0E0E0);
		this.font.drawShadow(pMatrixStack, "Max Nearby Entities", 214, 70, 0xE0E0E0);
		this.font.drawShadow(pMatrixStack, "Activation Range", 214, 84, 0xE0E0E0);
		this.font.drawShadow(pMatrixStack, "Spawn Range", 214, 98, 0xE0E0E0);
	}

	@Override
	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(pMatrixStack, pMouseX, pMouseY);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(TEXTURE);
		this.blit(pMatrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

}
