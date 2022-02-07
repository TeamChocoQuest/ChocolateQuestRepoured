package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGoblin;

public class ModelCQRGoblin extends ModelCQRBiped<EntityCQRGoblin> {

	public ModelRenderer Nose1;
	public ModelRenderer Nose2;
	public ModelRenderer EarRight1;
	public ModelRenderer EarLeft1;
	public ModelRenderer Body2;
	public ModelRenderer LegOver1;

	public ModelCQRGoblin() {
		super(74, 64, true);

		this.bipedBodyWear = new ModelRenderer(this, 16, 33);
		this.bipedBodyWear.setPos(-0.5F, 8.0F, -0.5F);
		this.bipedBodyWear.addBox(-3.5F, 0.0F, -1.5F, 8, 8, 3, 0.1F);
		this.body = new ModelRenderer(this, 16, 17);
		this.body.setPos(0.0F, 8.0F, -0.5F);
		this.body.addBox(-4.0F, 0.0F, -1.5F, 8, 4, 3, 0.0F);
		this.leftLeg = new ModelRenderer(this, 16, 49);
		this.leftLeg.mirror = true;
		this.leftLeg.setPos(2.0F, 16.0F, -0.5F);
		this.leftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
		this.rightLeg = new ModelRenderer(this, 0, 17);
		this.rightLeg.setPos(-2.0F, 16.0F, -0.5F);
		this.rightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.setPos(-4.0F, 10.0F, -0.5F);
		this.bipedRightArmwear.addBox(-3.0F, -2.0F, -1.5F, 3, 12, 3, 0.1F);
		this.EarLeft1 = new ModelRenderer(this, 68, 7);
		this.EarLeft1.setPos(3.5F, -3.5F, -1.1F);
		this.EarLeft1.addBox(0.0F, -3.0F, 0.0F, 0, 4, 3, 0.0F);
		this.setRotateAngle(this.EarLeft1, 0.0F, 0.4363323129985824F, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 8.0F, -0.5F);
		this.head.addBox(-3.5F, -6.0F, -3.5F, 7, 6, 7, 0.0F);
		this.Nose2 = new ModelRenderer(this, 66, 4);
		this.Nose2.setPos(0.0F, -2.5F, -3.5F);
		this.Nose2.addBox(-0.5F, -1.0F, -1.0F, 1, 1, 1, 0.0F);
		this.Body2 = new ModelRenderer(this, 16, 24);
		this.Body2.setPos(0.0F, 4.0F, 0.0F);
		this.Body2.addBox(-3.5F, 0.0F, -1.5F, 7, 4, 3, 0.0F);
		this.LegOver1 = new ModelRenderer(this, 52, 21);
		this.LegOver1.setPos(0.0F, 16.0F, -0.5F);
		this.LegOver1.addBox(-4.0F, 0.0F, -1.5F, 8, 8, 3, 0.2F);
		this.leftArm = new ModelRenderer(this, 32, 49);
		this.leftArm.mirror = true;
		this.leftArm.setPos(4.0F, 10.0F, -0.5F);
		this.leftArm.addBox(0.0F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
		this.EarRight1 = new ModelRenderer(this, 68, 3);
		this.EarRight1.setPos(-3.5F, -3.5F, -1.1F);
		this.EarRight1.addBox(0.0F, -3.0F, 0.0F, 0, 4, 3, 0.0F);
		this.setRotateAngle(this.EarRight1, 0.0F, -0.4363323129985824F, 0.0F);
		this.rightArm = new ModelRenderer(this, 40, 17);
		this.rightArm.setPos(-4.0F, 10.0F, -0.5F);
		this.rightArm.addBox(-3.0F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
		this.hat = new ModelRenderer(this, 32, 1);
		this.hat.setPos(0.0F, 8.0F, -0.5F);
		this.hat.addBox(-3.5F, -6.0F, -3.5F, 7, 6, 7, 0.1F);
		this.bipedLeftArmwear = new ModelRenderer(this, 48, 49);
		this.bipedLeftArmwear.mirror = true;
		this.bipedLeftArmwear.setPos(4.0F, 10.0F, -0.5F);
		this.bipedLeftArmwear.addBox(0.0F, -2.0F, -1.5F, 3, 12, 3, 0.1F);
		this.bipedLeftLegwear = new ModelRenderer(this, 0, 49);
		this.bipedLeftLegwear.mirror = true;
		this.bipedLeftLegwear.setPos(2.0F, 16.0F, -0.5F);
		this.bipedLeftLegwear.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.1F);
		this.Nose1 = new ModelRenderer(this, 66, 0);
		this.Nose1.setPos(0.0F, -2.5F, -3.5F);
		this.Nose1.addBox(-0.5F, 0.0F, -3.0F, 1, 1, 3, 0.0F);
		this.bipedRightLegwear = new ModelRenderer(this, 0, 33);
		this.bipedRightLegwear.setPos(-2.0F, 16.0F, -0.5F);
		this.bipedRightLegwear.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.1F);

		this.head.addChild(this.EarLeft1);
		this.head.addChild(this.Nose2);
		this.body.addChild(this.Body2);
		this.head.addChild(this.EarRight1);
		this.head.addChild(this.Nose1);
	}

	@Override
	public void setupAnim(EntityCQRGoblin pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

		this.head.setPos(0.0F, 8.0F, -0.5F);
		this.body.setPos(0.0F, 8.0F, -0.5F);
		this.leftArm.setPos(4.0F, 10.0F, -0.5F);
		this.leftLeg.setPos(2.0F, 16.0F, -0.5F);
		this.rightLeg.setPos(-2.0F, 16.0F, -0.5F);
		this.rightArm.setPos(-4.0F, 10.0F, -0.5F);

		this.hat.setPos(0.0F, 8.0F, -0.5F);
		this.bipedBodyWear.setPos(-0.5F, 8.0F, -0.5F);
		this.bipedRightArmwear.setPos(-4.0F, 10.0F, -0.5F);
		this.bipedLeftArmwear.setPos(4.0F, 10.0F, -0.5F);
		this.bipedRightLegwear.setPos(-2.0F, 16.0F, -0.5F);
		this.bipedLeftLegwear.setPos(2.0F, 16.0F, -0.5F);

		copyModelAngles(this.body, this.bipedBodyWear);
		copyModelAngles(this.head, this.hat);
		copyModelAngles(this.leftArm, this.bipedLeftArmwear);
		copyModelAngles(this.rightArm, this.bipedRightArmwear);
		copyModelAngles(this.leftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.rightLeg, this.bipedRightLegwear);
	}

}
