package team.cqr.cqrepoured.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.world.entity.Entity;

public class ModelChain extends EntityModel {

	private final ModelRenderer bone;

	public ModelChain() {
		this.texWidth = 16;
		this.texHeight = 16;

		this.bone = new ModelRenderer(this);
		this.bone.setPos(0.0F, 0.0F, 0.0F);
		this.bone.texOffs(0, 0).addBox(-1.5F, -0.5F, -1.0F, 1, 1, 8, 0.0F, false);
		this.bone.texOffs(0, 0).addBox(0.5F, -0.5F, -1.0F, 1, 1, 8, 0.0F, false);
		this.bone.texOffs(0, 0).addBox(-0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F, false);
		this.bone.texOffs(0, 0).addBox(-0.5F, -0.5F, 6.0F, 1, 1, 1, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bone.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
