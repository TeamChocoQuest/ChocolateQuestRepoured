package team.cqr.cqrepoured.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Vector3f;
import net.minecraftforge.client.ForgeHooksClient;
import team.cqr.cqrepoured.tileentity.TileEntityTable;

public class TileEntityTableRenderer extends BlockEntityRenderer<TileEntityTable> {
	public TileEntityTableRenderer(BlockEntityRenderDispatcher p_i226006_1_) {
		super(p_i226006_1_);
	}

	@Override
	public void render(TileEntityTable te, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
		ItemStack stack = te.getInventory().getItem(0);
		float rotation = te.getRotationInDegree();

		if (!stack.isEmpty()) {
			BakedModel itemModel = Minecraft.getInstance().getItemRenderer().getModel(stack, te.getLevel(), null);
			itemModel = ForgeHooksClient.handleCameraTransforms(pMatrixStack, itemModel, TransformType.NONE, false);
			
			pMatrixStack.pushPose();
			
			if(itemModel.isGui3d()) {
				pMatrixStack.translate(0.5, 1.25, 0.5);
				pMatrixStack.scale(0.5F, 0.5F, 0.5F);
				pMatrixStack.mulPose(Vector3f.YN.rotationDegrees(rotation));
			} else {
				pMatrixStack.translate(0.5, 1.02, 0.5);
				pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
				pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
				pMatrixStack.scale(0.7F, 0.7F, 0.7F);
			}
			
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, pCombinedLight, pCombinedOverlay, pMatrixStack, pBuffer);
			
			pMatrixStack.popPose();
		}
	}
}
