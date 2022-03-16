package team.cqr.cqrepoured.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.inventory.ContainerBackpack;

@OnlyIn(Dist.CLIENT)
public class ScreenBackpack extends ContainerScreen<ContainerBackpack> {

	private static final ResourceLocation SCREEN_BACKPACK = new ResourceLocation("textures/gui/container/shulker_box.png");

	public ScreenBackpack(ContainerBackpack containerBackpack, PlayerInventory inventory, ITextComponent titleIn)
	{
		super(containerBackpack, inventory, titleIn);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		this.font.draw(matrixStack, this.title, 8, 6, 0x404040);
		this.font.draw(matrixStack, this.inventory.getDisplayName(), 8, 74, 0x404040);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.getTextureManager().bind(SCREEN_BACKPACK);
		int x = (this.width - this.imageWidth) / 2;
		int y = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, x, y, 0, 0, this.imageWidth, this.imageHeight);

		//this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}
