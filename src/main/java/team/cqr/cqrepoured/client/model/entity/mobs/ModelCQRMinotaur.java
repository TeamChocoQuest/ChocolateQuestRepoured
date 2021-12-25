package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;

public class ModelCQRMinotaur extends ModelCQRBiped {

	public ModelRenderer snout;
	public ModelRenderer hornR1;
	public ModelRenderer hornL1;
	public ModelRenderer hornR2;
	public ModelRenderer hornL2;
	public ModelRenderer tail1;
	public ModelRenderer tail2;

	public ModelCQRMinotaur() {
		super(128, 64);
	}

	@Override
	protected void initModelParts() {
		super.initModelParts();

		this.snout = new ModelRenderer(this, 0, 32);
		this.snout.addBox(0.0F, 0.0F, 0.0F, 4, 3, 1, 0.0F);
		this.snout.setRotationPoint(-2.0F, -3.0F, -5.0F);
		this.bipedHead.addChild(this.snout);

		this.hornR1 = new ModelRenderer(this, 10, 32);
		this.hornR1.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
		this.hornR1.setRotationPoint(-6.8F, -6.0F, -3.0F);
		this.setRotationDeg(this.hornR1, 8.0D, 0.0D, -70.0D);
		this.bipedHead.addChild(this.hornR1);

		this.hornL1 = new ModelRenderer(this, 18, 32);
		this.hornL1.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
		this.hornL1.setRotationPoint(6.0F, -8.0F, -3.0F);
		this.setRotationDeg(this.hornL1, 8.0D, 0.0D, 70.0D);
		this.bipedHead.addChild(this.hornL1);

		this.hornR2 = new ModelRenderer(this, 26, 32);
		this.hornR2.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
		this.hornR2.setRotationPoint(-7.7F, -8.2F, -3.0F);
		this.setRotationDeg(this.hornR2, 14.0D, 0.0D, -45.0D);
		this.bipedHead.addChild(this.hornR2);

		this.hornL2 = new ModelRenderer(this, 30, 32);
		this.hornL2.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
		this.hornL2.setRotationPoint(7.0F, -9.0F, -3.0F);
		this.setRotationDeg(this.hornL2, 14.0D, 0.0D, 45.0D);
		this.bipedHead.addChild(this.hornL2);

		this.tail1 = new ModelRenderer(this, 34, 32);
		this.tail1.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1, 0.0F);
		this.tail1.setRotationPoint(-0.5F, 10.5F, 1.3F);
		this.setRotationDeg(this.tail1, 21.0D, 0.0D, 0.0D);
		this.bipedBody.addChild(this.tail1);

		this.tail2 = new ModelRenderer(this, 66, 27);
		this.tail2.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
		this.tail2.setRotationPoint(-0.5F, 4.2F, -0.5F);
		this.setRotationDeg(this.tail2, -5.0D, 0.0D, 0.0D);
		this.tail1.addChild(this.tail2);
	}

}
