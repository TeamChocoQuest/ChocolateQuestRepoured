package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;

public class ModelCQRIllager extends ModelCQRBiped {

	public ModelRenderer nose;
	public ModelRenderer bodyOverlay;

	public ModelCQRIllager() {
		super(128, 64);
	}

	@Override
	protected void initModelParts() {
		super.initModelParts();

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);

		this.nose = new ModelRenderer(this, 0, 32);
		this.nose.addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);
		this.nose.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.bipedHead.addChild(this.nose);

		this.bipedBody = new ModelRenderer(this, 32, 0);
		this.bipedBody.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

		this.bodyOverlay = new ModelRenderer(this, 8, 32);
		this.bodyOverlay.addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0.5F);
		this.bodyOverlay.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addChild(this.bodyOverlay);

		this.bipedRightArm = new ModelRenderer(this, 60, 0);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 76, 0);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 92, 0);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 108, 0);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
	}

}
