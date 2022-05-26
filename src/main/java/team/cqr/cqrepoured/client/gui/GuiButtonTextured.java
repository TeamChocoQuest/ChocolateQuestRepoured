package team.cqr.cqrepoured.client.gui;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiButtonTextured extends ImageButton implements INumericIDButton {

	protected final int id;
	protected final ResourceLocation icon;
	
	public GuiButtonTextured(final int id, int pX, int pY, int pWidth, int pHeight, int pXTexStart, int pYTexStart, int pYDiffTex, ResourceLocation pResourceLocation, @Nullable ResourceLocation icon, IPressable pOnPress, ITextComponent pMessage) {
		super(pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pYDiffTex, pResourceLocation, 256, 256, pOnPress, pMessage);
		this.id = id;
		this.icon = icon;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		super.renderButton(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		if(this.icon != null) {
			ResourceLocation origResLoc = this.resourceLocation;
			this.resourceLocation = this.icon;
			
			super.renderButton(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
			
			this.resourceLocation = origResLoc;
		}
	}
	

}
