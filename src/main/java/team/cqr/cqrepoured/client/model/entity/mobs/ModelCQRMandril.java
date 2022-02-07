package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMandril;

/**
 * ModelMonkey - Created by DerToaster Created using Tabula 7.0.1
 */
public class ModelCQRMandril extends ModelCQRBiped<EntityCQRMandril> {

	public ModelRenderer tail1;
	public ModelRenderer snout;
	public ModelRenderer tail2;
	public ModelRenderer tail3;

	public ModelCQRMandril() {
		super(96, 96, true);

		this.snout = new ModelRenderer(this, 64, 0);
		this.snout.setPos(-2.0F, 5.0F, -7.0F);
		this.snout.addBox(0.0F, 0.0F, 0.0F, 4, 3, 3, 0.0F);

		this.tail1 = new ModelRenderer(this, 64, 16);
		this.tail1.setPos(-0.5F, 10.0F, -2.5F);
		this.tail1.addBox(0.0F, -0.2F, -1.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.tail1, 1.7453292519943295F, 0.31869712141416456F, 0.003490658503988659F);

		this.body = new ModelRenderer(this, 16, 16);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.addBox(-4.0F, -0.4F, -6.5F, 8, 12, 4, 0.0F);
		this.setRotateAngle(this.body, 0.39269908169872414F, 0.0F, 0.0F);

		this.tail3 = new ModelRenderer(this, 64, 32);
		this.tail3.setPos(0.0F, 3.5F, 0.0F);
		this.tail3.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.tail3, -0.36425021489121656F, 0.0F, 0.5918411493512771F);

		this.tail2 = new ModelRenderer(this, 64, 24);
		this.tail2.setPos(0.0F, 3.5F, -1.0F);
		this.tail2.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.tail2, 0.36425021489121656F, 0.0F, 0.5009094953223726F);

		this.leftLeg = new ModelRenderer(this, 16, 48);
		this.leftLeg.setPos(2.2F, 12.0F, 0.0F);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);

		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, -6.0F, -5.0F);
		this.head.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8, 0.0F);

		this.rightArm = new ModelRenderer(this, 40, 16);
		// this.bipedRightArm.mirror = true;
		this.rightArm.setPos(-5.0F, 1.5F, -3.0F);
		this.rightArm.addBox(-3.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.setRotateAngle(this.rightArm, -0.2617993877991494F, 0.0F, 0.0F);

		this.leftArm = new ModelRenderer(this, 32, 48);
		this.leftArm.setPos(5.0F, 1.5F, -3.0F);
		this.leftArm.addBox(-1.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.setRotateAngle(this.leftArm, -0.2617993877991494F, 0.0F, 0.0F);

		this.rightLeg = new ModelRenderer(this, 0, 16);
		// this.bipedRightLeg.mirror = true;
		this.rightLeg.setPos(-2.2F, 12.0F, 0.0F);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);

		// Children
		this.head.addChild(this.snout);
		// Tail
		this.body.addChild(this.tail1);
		this.tail2.addChild(this.tail3);
		this.tail1.addChild(this.tail2);

		// Copying angles
		copyModelAngles(this.body, this.bipedBodyWear);
		copyModelAngles(this.head, this.hat);
		copyModelAngles(this.leftArm, this.bipedLeftArmwear);
		copyModelAngles(this.rightArm, this.bipedRightArmwear);
		copyModelAngles(this.leftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.rightLeg, this.bipedRightLegwear);

		// copyModelRotationPoint(bipedHead, bipedHeadwear);
	}

	@Override
	protected void initExtraLayer() {
		// Clothing layer
		this.hat = new ModelRenderer(this, 32, 0);
		this.hat.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8, 0.5F);
		this.hat.setPos(0.0F, -6.0F, -5.0F);

		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.addBox(-4.0F, -0.4F, -6.5F, 8, 12, 4, 0.25F);
		this.bipedBodyWear.setPos(0.0F, 0.0F, 0.0F);

		this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
		this.bipedLeftArmwear.addBox(-1.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftArmwear.setPos(5.0F, 1.5F, -3.0F);

		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.addBox(-3.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightArmwear.setPos(-5.0F, 1.5F, -3.0F);

		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftLegwear.setPos(2.2F, 12.0F, 0.0F);

		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightLegwear.setPos(-2.2F, 12.0F, 0.0F);
	}

	@Override
	public void setupAnim(EntityCQRMandril pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

		this.setRotateAngle(this.body, 0.39269908169872414F, 0.0F, 0.0F);

		this.rightArm.setPos(-5.0F, 1.5F, -3.0F);
		this.head.setPos(0.0F, -6.0F, -5.0F);
		this.leftArm.setPos(5.0F, 1.5F, -3.0F);

		copyModelAngles(this.body, this.bipedBodyWear);
		copyModelAngles(this.head, this.hat);
		copyModelAngles(this.leftArm, this.bipedLeftArmwear);
		copyModelAngles(this.rightArm, this.bipedRightArmwear);
		copyModelAngles(this.leftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.rightLeg, this.bipedRightLegwear);

		if (Math.abs(pLimbSwingAmount) > 0.1) {
			this.renderTailAnimation(pEntity.tickCount, 1.5);
		} else {
			this.renderTailAnimation(pEntity.tickCount, 1.0);
		}

	}

	protected void renderTailAnimation(float ageInTicks, double speedMultiplier) {
		float angleY = new Float(Math.sin(((2F * Math.PI) / (50 / speedMultiplier)) * ageInTicks)) / 3F;
		float angleX = new Float(Math.cos(((2F * Math.PI) / (80 / speedMultiplier)) * ageInTicks)) / 4F;
		float angleZ = new Float(Math.cos(((2F * Math.PI) / (100 / speedMultiplier)) * ageInTicks)) / 3F;
		this.tail1.yRot = angleY;
		this.tail2.xRot = angleX * 0.75F;
		this.tail2.zRot = -angleX * 0.5F;
		this.tail3.xRot = angleZ;
	}

}
