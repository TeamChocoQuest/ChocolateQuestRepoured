package team.cqr.cqrepoured.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import team.cqr.cqrepoured.tileentity.TileEntityTable;

public class TileEntityTableRenderer extends TileEntityRenderer<TileEntityTable> {
	public TileEntityTableRenderer(TileEntityRendererDispatcher p_i226006_1_) {
		super(p_i226006_1_);
	}

	@Override
	public void render(TileEntityTable te, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
		ItemStack stack = te.getInventory().getItem(0);
		float rotation = te.getRotationInDegree();

		if (!stack.isEmpty()) {
			pMatrixStack.pushPose();
			
			pMatrixStack.translate(0, 1.25, 0);
			pMatrixStack.scale(0.75F, 0.75F, 0.75F);
			pMatrixStack.mulPose(Vector3f.YN.rotationDegrees(rotation));
			
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, pCombinedLight, pCombinedOverlay, pMatrixStack, pBuffer);
			
			pMatrixStack.popPose();
		}
			/*IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(), null);
			model = ForgeHooksClient.handleCameraTransforms(model, TransformType.NONE, false);

			if (model.isGui3d()) {
				GlStateManager.enableRescaleNormal();
				GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
				GlStateManager.enableBlend();
				RenderHelper.enableStandardItemLighting();
				GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 0.5, y + 1.25, z + 0.5);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(rotation, 0, -1, 0);

				Minecraft.getMinecraft().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

				GlStateManager.popMatrix();
				GlStateManager.disableRescaleNormal();
				GlStateManager.disableBlend();
			}

			else {
				GlStateManager.enableRescaleNormal();
				GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
				GlStateManager.enableBlend();
				RenderHelper.enableStandardItemLighting();
				GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 0.5, y + 1.02, z + 0.5);
				GlStateManager.rotate(90F, 1, 0, 0);
				GlStateManager.rotate(rotation, 0, 0, 1);
				GlStateManager.scale(0.7F, 0.7F, 0.7F);

				Minecraft.getMinecraft().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

				GlStateManager.popMatrix();
				GlStateManager.disableRescaleNormal();
				GlStateManager.disableBlend();
			}*/
	}
}
