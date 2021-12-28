package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;

public class ModelCQRGolem extends ModelCQRBiped {

	public ModelRenderer nose;
	public ModelRenderer bodyLower;

	public ModelCQRGolem() {
		super(128, 64);
	}

	@Override
	protected void initModelParts() {
		super.initModelParts();

		this.nose = new ModelRenderer(this, 0, 32);
		this.nose.addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, 0.0F);
		this.nose.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.addChild(this.nose);

		this.bipedBody = new ModelRenderer(this, 32, 0);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 6, 4, 0.0F);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

		this.bodyLower = new ModelRenderer(this, 8, 32);
		this.bodyLower.addBox(-3.0F, 6.0F, -2.0F, 6, 6, 4, 0.0F);
		this.bodyLower.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addChild(this.bodyLower);

	}

}
