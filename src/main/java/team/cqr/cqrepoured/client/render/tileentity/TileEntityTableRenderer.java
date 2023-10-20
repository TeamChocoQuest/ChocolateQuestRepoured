package team.cqr.cqrepoured.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import team.cqr.cqrepoured.tileentity.TileEntityTable;

public class TileEntityTableRenderer implements BlockEntityRenderer<TileEntityTable> {
	
	private final ItemRenderer itemRenderer;
	
	public TileEntityTableRenderer(BlockEntityRendererProvider.Context pContext) {
		this.itemRenderer = pContext.getItemRenderer();
	}

	@Override
	public void render(TileEntityTable te, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
		ItemStack stack = te.getItem(0);
		float rotation = te.getRotationInDegree();

		if (!stack.isEmpty()) {
			BakedModel itemModel = Minecraft.getInstance().getItemRenderer().getModel(stack, te.getLevel(), null, 0);
			itemModel = ForgeHooksClient.handleCameraTransforms(pMatrixStack, itemModel, ItemDisplayContext.NONE, false);
			
			pMatrixStack.pushPose();
			
			if(itemModel.isGui3d()) {
				pMatrixStack.translate(0.5, 1.25, 0.5);
				pMatrixStack.scale(0.5F, 0.5F, 0.5F);
				pMatrixStack.mulPose(Axis.YN.rotationDegrees(rotation));
			} else {
				pMatrixStack.translate(0.5, 1.02, 0.5);
				pMatrixStack.mulPose(Axis.YP.rotationDegrees(rotation));
				pMatrixStack.mulPose(Axis.XP.rotationDegrees(90));
				pMatrixStack.scale(0.7F, 0.7F, 0.7F);
			}
			
			this.itemRenderer.renderStatic(stack, ItemDisplayContext.NONE, pCombinedLight, pCombinedOverlay, pMatrixStack, pBuffer, te.getLevel(), 0);
			
			pMatrixStack.popPose();
		}
	}
}
