package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.EnumHandSide;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;

/** Copied from vanilla skeleton model but modified */
public class ModelCQRSkeleton extends ModelCQRBiped {

	public ModelCQRSkeleton() {
		super(128, 32);
	}

	@Override
	protected void initModelParts() {
		super.initModelParts();

		this.bipedRightArm = new ModelRenderer(this, 56, 0);
		this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 72, 0);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 88, 0);
		this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 104, 0);
		this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
	}

	@Override
	public void postRenderArm(float scale, EnumHandSide side) {
		float f = side == EnumHandSide.RIGHT ? 1.0F : -1.0F;
		ModelRenderer modelrenderer = this.getArmForSide(side);
		modelrenderer.rotationPointX += f;
		modelrenderer.postRender(scale);
		modelrenderer.rotationPointX -= f;
	}

}
