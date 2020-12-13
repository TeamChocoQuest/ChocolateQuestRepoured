package team.cqr.cqrepoured.client.models.entities.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.EnumHandSide;
import team.cqr.cqrepoured.client.models.entities.ModelCQRBiped;

/** Copied from vanilla skeleton model but modified */
public class ModelCQRSkeleton extends ModelCQRBiped {

	public ModelCQRSkeleton() {
		super(64, 64, false);

		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);

		this.setClothingLayerVisible(false);
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
