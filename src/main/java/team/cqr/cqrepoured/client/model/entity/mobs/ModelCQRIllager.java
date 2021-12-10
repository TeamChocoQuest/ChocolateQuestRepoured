package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;

public class ModelCQRIllager extends ModelCQRBiped {

	private final ModelRenderer nose;

	public ModelCQRIllager() {
		super(64, 64, false);

		this.bipedHead = new ModelRenderer(this);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);

		this.bipedHeadwear = new ModelRenderer(this, 32, 0);
		this.bipedHeadwear.addBox(-4.0F, -10.0F, -4.0F, 8, 12, 8, 0.0F + 0.45F);

		this.bipedHead.addChild(this.bipedHeadwear);
		this.bipedHeadwear.showModel = false;

		this.nose = new ModelRenderer(this);
		this.nose.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.nose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);

		this.bipedHead.addChild(this.nose);

		this.bipedBody = new ModelRenderer(this);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
		this.bipedBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0.0F + 0.5F);

		ModelRenderer modelrenderer = new ModelRenderer(this, 44, 22);
		modelrenderer.mirror = true;
		modelrenderer.addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 0, 22);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 0, 22);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);

		this.bipedRightArm = new ModelRenderer(this, 40, 46);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 40, 46);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
	}

}
