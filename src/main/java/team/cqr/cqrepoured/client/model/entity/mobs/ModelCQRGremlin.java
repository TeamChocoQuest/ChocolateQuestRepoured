package team.cqr.cqrepoured.client.model.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGremlin;

public class ModelCQRGremlin extends ModelCQRBiped<EntityCQRGremlin> {

	public ModelRenderer hornR1;
	public ModelRenderer hornL1;
	public ModelRenderer hornR2;
	public ModelRenderer hornL2;
	public ModelRenderer nose;

	public ModelCQRGremlin() {
		super(96, 96, false);

		this.leftLeg = new ModelRenderer(this, 0, 22);
		this.leftLeg.mirror = true;
		this.leftLeg.setPos(2.0F, 18.0F, 6.0F);
		this.leftLeg.addBox(0.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.hornR2 = new ModelRenderer(this, 56, 4);
		this.hornR2.setPos(0.0F, -0.7F, 0.0F);
		this.hornR2.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
		this.setRotateAngle(this.hornR2, -0.7853981633974483F, 0.7853981633974483F, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 10.0F, -4.0F);
		this.head.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F + 0.125F);
		this.hat = new ModelRenderer(this, 32, 0);
		this.hat.setPos(0.0F, 10.0F, -4.0F);
		this.hat.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F + 0.25F);
		this.rightArm = new ModelRenderer(this, 40, 16);
		this.rightArm.setPos(-4.0F, 14.5F, -2.5F);
		this.rightArm.addBox(-4.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.setRotateAngle(this.rightArm, -0.39269908169872414F, 0.0F, 0.0F);
		this.rightLeg = new ModelRenderer(this, 0, 22);
		this.rightLeg.setPos(-2.0F, 18.0F, 6.0F);
		this.rightLeg.addBox(-4.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leftArm = new ModelRenderer(this, 40, 16);
		this.leftArm.mirror = true;
		this.leftArm.setPos(4.0F, 14.5F, -1.5F);
		this.leftArm.addBox(0.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.setRotateAngle(this.leftArm, -0.3839724354387525F, 0.0F, 0.0F);
		this.body = new ModelRenderer(this, 16, 16);
		this.body.setPos(0.0F, 13.0F, -4.0F);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.setRotateAngle(this.body, 1.0471975511965976F, 0.0F, 0.0F);
		this.hornR1 = new ModelRenderer(this, 56, 0);
		this.hornR1.setPos(-3.5F, -3.5F, -3.5F);
		this.hornR1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
		this.setRotateAngle(this.hornR1, 0.7853981633974483F, 0.7853981633974483F, 0.0F);
		this.hornL2 = new ModelRenderer(this, 56, 4);
		this.hornL2.setPos(0.0F, -0.7F, 0.0F);
		this.hornL2.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
		this.setRotateAngle(this.hornL2, -0.6829473363053812F, 0.091106186954104F, -0.7853981633974483F);
		this.hornL1 = new ModelRenderer(this, 56, 0);
		this.hornL1.mirror = true;
		this.hornL1.setPos(3.5F, -3.5F, -3.5F);
		this.hornL1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
		this.setRotateAngle(this.hornL1, 0.7853981633974483F, -0.7853981633974483F, 0.0F);
		this.nose = new ModelRenderer(this, 64, 0);
		this.nose.setPos(0.0F, -1.0F, -4.0F);
		this.nose.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(this.nose, -0.4363323129985824F, 0.0F, 0.0F);
		this.hornR1.addChild(this.hornR2);
		this.head.addChild(this.hornR1);
		this.hornL1.addChild(this.hornL2);
		this.head.addChild(this.hornL1);
		this.head.addChild(this.nose);
	}

	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		this.riding = false;
		super.renderToBuffer(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}
	
	@Override
	public void setupAnim(EntityCQRGremlin pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		this.riding = false;

		this.leftArm.xRot += MathHelper.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount * 0.5F;
		this.rightArm.xRot += MathHelper.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount * 0.5F;

		this.body.setPos(0.0F, 13.0F, -4.0F);
		this.rightArm.setPos(-4.0F, 14.5F, -2.5F);
		this.leftArm.setPos(4.0F, 14.5F, -1.5F);
		this.rightLeg.setPos(-2.0F, 18.0F, 6.0F);
		this.leftLeg.setPos(2.0F, 18.0F, 6.0F);
		this.head.setPos(0.0F, 10.0F, -4.0F);
		this.hat.setPos(0.0F, 10.0F, -4.0F);
		this.setRotateAngle(this.body, 1.0471975511965976F, 0.0F, 0.0F);

		copyModelAngles(this.head, this.hat);
	}

}
