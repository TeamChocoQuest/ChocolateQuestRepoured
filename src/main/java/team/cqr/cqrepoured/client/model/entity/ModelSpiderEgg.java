package team.cqr.cqrepoured.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.entity.misc.EntitySpiderEgg;

/**
 * SpiderEgg - DerToaster Created using Tabula 7.0.1
 */
public class ModelSpiderEgg extends EntityModel<EntitySpiderEgg> {
	public ModelRenderer shape1;
	public ModelRenderer shape2;
	public ModelRenderer shape3;

	public ModelSpiderEgg() {
		/*this.textureWidth = 64;
		this.textureHeight = 48;
		this.shape1 = new ModelRenderer(this, 0, 0);
		this.shape1.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.shape1.addBox(-4.0F, -2.0F, -4.0F, 8, 2, 8, 0.0F);
		this.shape2 = new ModelRenderer(this, 0, 10);
		this.shape2.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.shape2.addBox(-6.0F, -10.0F, -6.0F, 12, 10, 12, 0.0F);
		this.shape3 = new ModelRenderer(this, 0, 32);
		this.shape3.setRotationPoint(0.0F, -10.0F, 0.0F);
		this.shape3.addBox(-4.0F, -4.0F, -4.0F, 8, 4, 8, 0.0F);
		this.shape1.addChild(this.shape2);
		this.shape2.addChild(this.shape3);*/
	}

	/*@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.shape1.render(f5);
	}*/

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		/*modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;*/
	}

	@Override
	public void setupAnim(EntitySpiderEgg pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		// TODO Auto-generated method stub
		
	}
}
