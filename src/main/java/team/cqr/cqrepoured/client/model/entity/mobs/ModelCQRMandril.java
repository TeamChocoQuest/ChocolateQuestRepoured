package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;

/**
 * ModelMonkey - Created by DerToaster Created using Tabula 7.0.1
 */
public class ModelCQRMandril extends ModelCQRBiped {

	public ModelRenderer snout;
	public ModelRenderer tail1;
	public ModelRenderer tail2;
	public ModelRenderer tail3;

	public ModelCQRMandril() {
		super(128, 64);
	}

	@Override
	protected void initModelParts() {
		super.initModelParts();

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8, 0.0F);
		this.bipedHead.setRotationPoint(0.0F, -6.0F, -5.0F);

		this.snout = new ModelRenderer(this, 64, 0);
		this.snout.addBox(0.0F, 0.0F, 0.0F, 4, 3, 3, 0.0F);
		this.snout.setRotationPoint(-2.0F, 5.0F, -7.0F);
		this.bipedHead.addChild(this.snout);

		this.bipedBody = new ModelRenderer(this, 32, 0);
		this.bipedBody.addBox(-4.0F, -0.4F, -6.5F, 8, 12, 4, 0.0F);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.setRotationDeg(this.bipedBody, 22.5D, 0.0D, 0.0D);

		this.tail1 = new ModelRenderer(this, 64, 16);
		this.tail1.addBox(0.0F, -0.2F, -1.0F, 1, 4, 1, 0.0F);
		this.tail1.setRotationPoint(-0.5F, 10.0F, -2.5F);
		this.setRotationDeg(this.tail1, 100.0D, 18.26D, 0.2D);
		this.bipedBody.addChild(this.tail1);

		this.tail2 = new ModelRenderer(this, 64, 24);
		this.tail2.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1, 0.0F);
		this.tail2.setRotationPoint(0.0F, 3.5F, -1.0F);
		this.setRotationDeg(this.tail2, 20.87D, 0.0D, 28.65D);
		this.tail1.addChild(this.tail2);

		this.tail3 = new ModelRenderer(this, 64, 32);
		this.tail3.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1, 0.0F);
		this.tail3.setRotationPoint(0.0F, 3.5F, 0.0F);
		this.setRotationDeg(this.tail3, -20.87D, 0.0D, 33.91D);
		this.tail2.addChild(this.tail3);

		this.bipedRightArm = new ModelRenderer(this, 56, 0);
		this.bipedRightArm.addBox(-3.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedRightArm.setRotationPoint(-5.0F, 1.5F, -3.0F);
		this.setRotationDeg(this.bipedRightArm, -15.0D, 0.0D, 0.0D);

		this.bipedLeftArm = new ModelRenderer(this, 72, 0);
		this.bipedLeftArm.addBox(-1.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 1.5F, -3.0F);
		this.setRotationDeg(this.bipedLeftArm, -15.0D, 0.0D, 0.0D);

		this.bipedRightLeg = new ModelRenderer(this, 88, 0);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedRightLeg.setRotationPoint(-2.2F, 12.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 104, 0);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftLeg.setRotationPoint(2.2F, 12.0F, 0.0F);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor,
			Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		this.setRotationDeg(this.bipedBody, 22.5D, 0.0D, 0.0D);

		this.bipedHead.setRotationPoint(0.0F, -6.0F, -5.0F);
		this.bipedRightArm.setRotationPoint(-5.0F, 1.5F, -3.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 1.5F, -3.0F);

		if (Math.abs(limbSwingAmount) > 0.1F) {
			this.renderTailAnimation(entityIn.ticksExisted, 1.5F);
		} else {
			this.renderTailAnimation(entityIn.ticksExisted, 1.0F);
		}
	}

	protected void renderTailAnimation(float ageInTicks, float speedMultiplier) {
		float angleY = MathHelper.sin(((2.0F * (float) Math.PI) / (50.0F / speedMultiplier)) * ageInTicks) / 3.0F;
		float angleX = MathHelper.cos(((2.0F * (float) Math.PI) / (80.0F / speedMultiplier)) * ageInTicks) / 4.0F;
		float angleZ = MathHelper.cos(((2.0F * (float) Math.PI) / (100.0F / speedMultiplier)) * ageInTicks) / 3.0F;
		this.tail1.rotateAngleY = angleY;
		this.tail2.rotateAngleX = angleX * 0.75F;
		this.tail2.rotateAngleZ = -angleX * 0.5F;
		this.tail3.rotateAngleX = angleZ;
	}

}
