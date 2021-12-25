package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;

/**
 * ModelOgre - Sir Squidly Created using Tabula 7.0.1
 */
public class ModelCQROgre extends ModelCQRBiped {

	public ModelRenderer nose;
	public ModelRenderer jaws;
	public ModelRenderer bodyLower;

	public ModelCQROgre() {
		super(128, 64);
	}

	@Override
	protected void initModelParts() {
		super.initModelParts();

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -7.0F, -4.0F, 8, 8, 8, 0.0F);
		this.bipedHead.setRotationPoint(0.0F, -1.0F, -1.0F);

		this.nose = new ModelRenderer(this, 0, 32);
		this.nose.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
		this.nose.setRotationPoint(-1.0F, -3.3F, -4.5F);
		this.setRotationDeg(this.nose, -15.0D, 0.0D, 0.0D);
		this.bipedHead.addChild(this.nose);

		// TODO Replaced with bipedHeadwear in texture. Does the replacement look fine or should we keep this?
		/*
		 * this.jaws = new ModelRenderer(this, 36, 32);
		 * this.jaws.setRotationPoint(-4.5F, -2.5F, -4.5F);
		 * this.jaws.addBox(0.0F, 0.0F, 0.0F, 9, 4, 9, 0.0F);
		 * this.bipedHead.addChild(this.jaws);
		 */

		this.bipedBody = new ModelRenderer(this, 64, 0);
		this.bipedBody.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 4, 4, 0.0F);

		this.bodyLower = new ModelRenderer(this, 6, 32);
		this.bodyLower.setRotationPoint(-4.5F, 4.0F, -3.5F);
		this.bodyLower.addBox(0.0F, 0.0F, 0.0F, 9, 7, 6, 0.0F);
		this.bipedBody.addChild(this.bodyLower);
	}

}
