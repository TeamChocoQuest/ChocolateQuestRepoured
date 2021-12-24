package team.cqr.cqrepoured.client.render.entity.layer.special;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layer.AbstractLayerCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerCQRLeaderFeather<R extends RenderCQREntity<E>, E extends AbstractEntityCQR> extends AbstractLayerCQR<R, E> {

	private static final ItemStack STACK = new ItemStack(Items.FEATHER);

	private ModelRenderer bipedHead;

	public LayerCQRLeaderFeather(R renderer, ModelRenderer bipedHead) {
		super(renderer);
		this.bipedHead = bipedHead;
	}

	@Override
	public void doRenderLayer(E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
			float scale) {
		if (!entity.isLeader()) {
			return;
		}
		GlStateManager.pushMatrix();

		GlStateManager.rotate(180, 0, 0, 1);
		GlStateManager.scale(scale + 0.4, scale + 0.4, scale + 0.4);
		float yaw = netHeadYaw + 90F;
		GlStateManager.rotate(-headPitch, 1, 0, 0);
		GlStateManager.rotate(yaw, 0, 1, 0);
		float height = entity.getEyeHeight();
		GlStateManager.translate(this.bipedHead.offsetX, height, this.bipedHead.offsetZ);
		Minecraft.getMinecraft().getRenderItem().renderItem(STACK, ItemCameraTransforms.TransformType.FIXED);

		GlStateManager.popMatrix();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
