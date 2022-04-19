package team.cqr.cqrepoured.client.render.entity.layer.special;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ModelRenderer.ModelBox;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerCQRPirateParrotHeldItem extends HeldItemLayer {

	public LayerCQRPirateParrotHeldItem(ParrotRenderer renderCQREntity) {
		super(renderCQREntity);
	}

	@Override
	protected void translateToHand(HandSide handSide) {
		// super.translateToHand(handSide);
		if (this.livingEntityRenderer.getMainModel() instanceof ModelParrot) {
			ModelParrot model = (ModelParrot) this.livingEntityRenderer.getMainModel();
			ModelRenderer armRenderer = model.boxList.get(0);
			if (!armRenderer.cubeList.isEmpty()) {
				ModelBox armBox = armRenderer.cubeList.get(0);
				float x = 0.125F - 0.03125F * (armBox.posX2 - armBox.posX1);
				float z = 0.125F;
				float sizeY = Math.abs(Math.max(armBox.posY1, armBox.posY2) - Math.min(armBox.posY1, armBox.posY2));
				float y = 0.0625F * (armBox.posY2 - armBox.posY1 + (sizeY * 4)); // 6F is the height of the body

				GlStateManager.translate(x, y, z + 0.295);
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			}
		}
	}

}
