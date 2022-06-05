package team.cqr.cqrepoured.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.entity.misc.AbstractEntityLaser;

public class ModelLaser extends EntityModel<AbstractEntityLaser> {

	private final ModelRenderer bone;

	public ModelLaser() {
		super(CQRRenderTypes::emissive);
		this.texWidth = 64;
		this.texHeight = 32;

		this.bone = new ModelRenderer(this);
		bone.setPos(0.0F, 24.0F, 0.0F);
		bone.texOffs(0, 0).addBox(-4.0F, -28.0F, -16.0F, 8.0F, 8.0F, 16.0F, 0.0F, false);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bone.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void setupAnim(AbstractEntityLaser pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		
	}
}
