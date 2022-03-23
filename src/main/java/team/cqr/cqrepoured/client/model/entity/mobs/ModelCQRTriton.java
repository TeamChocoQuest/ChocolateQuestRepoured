package team.cqr.cqrepoured.client.model.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRTriton;

/**
 * ModelTriton - DerToaster98 Created using Tabula 7.0.1
 */
public class ModelCQRTriton extends ModelCQRBiped<EntityCQRTriton> {

	public ModelRenderer mouthTentacle1;
	public ModelRenderer mouthTentacle2;
	public ModelRenderer mouthTentacle3;
	public ModelRenderer mouthTentacle4;
	public ModelRenderer tail1;
	public ModelRenderer tail2;
	public ModelRenderer tail3;
	public ModelRenderer tailEnd1;
	public ModelRenderer tailEnd2;
	public ModelRenderer tailEnd3;

	public ModelCQRTriton() {
		super(64, 64, false);

		this.disableLegs();

		this.tailEnd1 = new ModelRenderer(this, 42, 57);
		this.tailEnd1.setPos(0.0F, 1.0F, 3.4F);
		this.tailEnd1.addBox(-1.5F, 0.0F, 0.5F, 3, 3, 4, 0.0F);

		this.tail2 = new ModelRenderer(this, 0, 48);
		this.tail2.setPos(0.0F, 4.0F, 1.0F);
		this.tail2.addBox(-3.0F, 0.0F, 0.0F, 6, 4, 4, 0.0F);

		this.tail1 = new ModelRenderer(this, 0, 56);
		this.tail1.setPos(0.0F, 12.0F, -1.55F);
		this.tail1.addBox(-4.0F, 0.0F, 0.0F, 8, 4, 4, 0.0F);

		this.body = new ModelRenderer(this, 0, 16);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);

		this.mouthTentacle1 = new ModelRenderer(this, 32, 0);
		this.mouthTentacle1.setPos(-2.0F, -3.5F, -3.0F);
		this.mouthTentacle1.addBox(0.0F, 1.5F, 0.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.mouthTentacle1, -0.6981317007977318F, 0.0F, 0.0F);

		this.mouthTentacle3 = new ModelRenderer(this, 32, 0);
		this.mouthTentacle3.setPos(0.0F, -3.5F, -3.0F);
		this.mouthTentacle3.addBox(0.0F, 1.5F, 0.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.mouthTentacle3, -0.6981317007977318F, 0.0F, 0.0F);

		this.tailEnd2 = new ModelRenderer(this, 42, 57);
		this.tailEnd2.setPos(0.0F, 0.0F, 4.5F);
		this.tailEnd2.addBox(-1.5F, 0.0F, 0.0F, 3, 3, 4, 0.0F);

		this.mouthTentacle2 = new ModelRenderer(this, 32, 0);
		this.mouthTentacle2.setPos(-1.0F, -3.5F, -3.0F);
		this.mouthTentacle2.addBox(0.0F, 1.5F, 0.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.mouthTentacle2, -0.6981317007977318F, 0.0F, 0.0F);

		this.leftArm = new ModelRenderer(this, 24, 16);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);

		this.rightArm = new ModelRenderer(this, 40, 16);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);

		this.tailEnd3 = new ModelRenderer(this, 42, 57);
		this.tailEnd3.setPos(0.0F, 0.0F, 4.0F);
		this.tailEnd3.addBox(-1.5F, 0.0F, 0.0F, 3, 3, 4, 0.0F);

		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);

		this.mouthTentacle4 = new ModelRenderer(this, 32, 0);
		this.mouthTentacle4.setPos(1.0F, -3.5F, -3.0F);
		this.mouthTentacle4.addBox(0.0F, 1.5F, 0.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.mouthTentacle4, -0.6981317007977318F, 0.0F, 0.0F);

		this.tail3 = new ModelRenderer(this, 24, 56);
		this.tail3.setPos(0.0F, 4.0F, 1.5F);
		this.tail3.addBox(-2.5F, 0.0F, 0.0F, 5, 4, 4, 0.0F);

		// Children relations
		this.tail3.addChild(this.tailEnd1);
		this.tail1.addChild(this.tail2);
		this.body.addChild(this.tail1);
		this.head.addChild(this.mouthTentacle1);
		this.head.addChild(this.mouthTentacle3);
		this.tailEnd1.addChild(this.tailEnd2);
		this.head.addChild(this.mouthTentacle2);
		this.tailEnd2.addChild(this.tailEnd3);
		this.head.addChild(this.mouthTentacle4);
		this.tail2.addChild(this.tail3);
	}

	private void disableLegs() {
		this.leftLeg.visible = true;
		this.leftLeg.setTexSize(0, 0);
		this.bipedLeftLegwear.visible = true;
		this.bipedLeftLegwear.setTexSize(0, 0);

		this.rightLeg.visible = true;
		this.rightLeg.setTexSize(0, 0);
		this.bipedRightLegwear.visible = true;
		this.bipedRightLegwear.setTexSize(0, 0);
	}

	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		this.body.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.leftArm.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.rightArm.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.head.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}

	@Override
	public void setupAnim(EntityCQRTriton pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		if (Math.abs(pLimbSwingAmount) > 0.1) {
			this.renderSlitheringTail(pAgeInTicks, 1.5F);
			this.renderSlitheringTailEnd(pAgeInTicks, 1.0F, 1.5F);
		} else {
			this.renderSlitheringTailEnd(pAgeInTicks, 0.5F, 1.0F);
		}

		this.renderFaceTentaclesAnimation(pAgeInTicks);
	}

	protected void renderSlitheringTail(float ageInTicks, float multiplierForAngle) {
		float speedMultiplier = 10;
		float sine = new Float(Math.sin(((2F * Math.PI) / speedMultiplier) * ageInTicks)) / 1.5F;
		sine *= multiplierForAngle;
		float sineTmp = sine;
		this.tail1.x = sine;
		sine *= 0.85;
		this.tail2.x = -sine - sineTmp;
		sineTmp = sine;
		sine *= 0.8;
		this.tail3.x = sine + sineTmp;
	}

	protected void renderSlitheringTailEnd(float ageInTicks, float multiplierForAngle, float speedMultiplier) {
		float angleY = (float) (Math.sin(((2F * Math.PI) / (50 / speedMultiplier)) * ageInTicks)) / 4F;
		angleY *= multiplierForAngle;

		this.tailEnd1.yRot = angleY;
		angleY /= 1.125F;
		this.tailEnd2.yRot = angleY;
		angleY /= 1.125F;
		this.tailEnd3.yRot = angleY;
	}

	protected void renderFaceTentaclesAnimation(float ageInTicks) {
		float yAngle = (float) (Math.sin(((2F * Math.PI) / 75) * ageInTicks) + 1);
		yAngle /= 2.5F;

		this.mouthTentacle1.yRot = yAngle;
		this.mouthTentacle2.yRot = yAngle / 2.5F;
		this.mouthTentacle3.yRot = -(yAngle / 2.5F);
		this.mouthTentacle4.yRot = -yAngle;

		float xAngle = (float) (Math.sin(((2F * Math.PI) / 50) * ageInTicks + (Math.PI / 8)));
		xAngle /= 6F;
		this.mouthTentacle1.xRot = xAngle - 0.6981317007977318F;

		xAngle = (float) (Math.sin(((2F * Math.PI) / 50) * ageInTicks + 3 * (Math.PI / 8)));
		xAngle /= 6F;
		this.mouthTentacle2.xRot = xAngle - 0.6981317007977318F;

		xAngle = (float) (Math.sin(((2F * Math.PI) / 50) * ageInTicks + 5 * (Math.PI / 8)));
		xAngle /= 6F;
		this.mouthTentacle3.xRot = xAngle - 0.6981317007977318F;

		xAngle = (float) (Math.sin(((2F * Math.PI) / 50) * ageInTicks + 7 * (Math.PI / 8)));
		xAngle /= 6F;
		this.mouthTentacle4.xRot = xAngle - 0.6981317007977318F;
	}
}
