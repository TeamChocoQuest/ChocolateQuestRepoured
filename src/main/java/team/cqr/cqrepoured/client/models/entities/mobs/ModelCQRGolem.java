package team.cqr.cqrepoured.client.models.entities.mobs;

import net.minecraft.client.model.ModelRenderer;
import team.cqr.cqrepoured.client.models.entities.ModelCQRBiped;

public class ModelCQRGolem extends ModelCQRBiped {

	ModelRenderer nose;
	ModelRenderer bodyLower;

	public ModelCQRGolem() {
		super(64, 64, false);

		// Biped components that need to be "adjusted"
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, 0.0F);
		this.bipedHead.setRotationPoint(0F, 0F, 0F);
		this.bipedHead.setTextureSize(64, 64);
		this.bipedHead.mirror = true;
		this.setRotateAngle(this.bipedHead, 0F, 0F, 0F);

		this.bipedHeadwear.showModel = false;

		this.bipedRightArm = new ModelRenderer(this, 40, 32);
		this.bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4, 0.0F);
		this.bipedRightArm.setRotationPoint(-5F, 2F, 0F);
		this.bipedRightArm.setTextureSize(64, 64);
		this.bipedRightArm.mirror = true;
		this.setRotateAngle(this.bipedRightArm, 0F, 0F, 0F);

		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5F, 2F, 0F);
		this.bipedLeftArm.setTextureSize(64, 64);
		this.bipedLeftArm.mirror = true;
		this.setRotateAngle(this.bipedLeftArm, 0F, 0F, 0F);

		this.bipedRightLeg = new ModelRenderer(this, 0, 32);
		this.bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4, 0.0F);
		this.bipedRightLeg.setRotationPoint(-3F, 12F, 0F);
		this.bipedRightLeg.setTextureSize(64, 64);
		this.bipedRightLeg.mirror = true;
		this.setRotateAngle(this.bipedRightLeg, 0F, 0F, 0F);

		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4, 0.0F);
		this.bipedLeftLeg.setRotationPoint(3F, 12F, 0F);
		this.bipedLeftLeg.setTextureSize(64, 64);
		this.bipedLeftLeg.mirror = true;
		this.setRotateAngle(this.bipedLeftLeg, 0F, 0F, 0F);
		//

		this.nose = new ModelRenderer(this, 32, 0);
		this.nose.addBox(-1F, -3F, -6F, 2, 4, 2, 0.0F);
		this.nose.setRotationPoint(0F, 0F, 0F);
		this.nose.setTextureSize(64, 64);
		this.nose.mirror = true;
		this.setRotateAngle(this.nose, 0F, 0F, 0F);

		this.bipedBody = new ModelRenderer(this, 16, 16);
		this.bipedBody.addBox(-4F, 0F, -2F, 8, 6, 4, 0.0F);
		this.bipedBody.setRotationPoint(0F, 0F, 0F);
		this.bipedBody.setTextureSize(64, 64);
		this.bipedBody.mirror = true;
		this.setRotateAngle(this.bipedBody, 0F, 0F, 0F);

		this.bodyLower = new ModelRenderer(this, 18, 26);
		this.bodyLower.addBox(-3F, 6F, -2F, 6, 6, 4, 0.0F);
		this.bodyLower.setRotationPoint(0F, 0F, 0F);
		this.bodyLower.setTextureSize(64, 64);
		this.bodyLower.mirror = true;
		this.setRotateAngle(this.bodyLower, 0F, 0F, 0F);

		this.bipedBody.addChild(this.bodyLower);
		this.bipedHead.addChild(this.nose);
	}

}
