package team.cqr.cqrepoured.client.render.entity.layers;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerCQRPirateParrotHeldItem extends LayerHeldItem {

	public LayerCQRPirateParrotHeldItem(RenderParrot renderCQREntity) {
		super(renderCQREntity);
	}

	@Override
	protected void translateToHand(EnumHandSide handSide) {
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
