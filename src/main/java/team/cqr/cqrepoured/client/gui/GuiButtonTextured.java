package team.cqr.cqrepoured.client.gui;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiButtonTextured extends ImageButton implements INumericIDButton {

	protected final int id;
	protected final ResourceLocation icon;
	private ResourceLocation resourceLocation;
	
	public GuiButtonTextured(final int id, int pX, int pY, int pWidth, int pHeight, int pXTexStart, int pYTexStart, int pYDiffTex, ResourceLocation pResourceLocation, @Nullable ResourceLocation icon, IPressable pOnPress, ITextComponent pMessage) {
		super(pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pYDiffTex, pResourceLocation, 256, 256, pOnPress, pMessage);
		this.id = id;
		this.icon = icon;
		this.resourceLocation = pResourceLocation;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		if(this.icon != null) {
			ResourceLocation origResLoc = this.resourceLocation;
			this.resourceLocation = this.icon;
			
			super.renderButton(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
			
			this.resourceLocation = origResLoc;
		}
		super.renderButton(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
	}
	

}
