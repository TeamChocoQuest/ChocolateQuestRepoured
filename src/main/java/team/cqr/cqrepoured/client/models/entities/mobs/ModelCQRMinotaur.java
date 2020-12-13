package team.cqr.cqrepoured.client.models.entities.mobs;

import net.minecraft.client.model.ModelRenderer;
import team.cqr.cqrepoured.client.models.entities.ModelCQRBiped;

public class ModelCQRMinotaur extends ModelCQRBiped {

	public ModelRenderer snout;
	public ModelRenderer hornR1;
	public ModelRenderer hornR2;
	public ModelRenderer hornL1;
	public ModelRenderer hornL2;
	public ModelRenderer tail1;
	public ModelRenderer tail2;

	public ModelCQRMinotaur() {
		super(74, 64, true);

		this.tail2 = new ModelRenderer(this, 66, 27);
		this.tail2.setRotationPoint(-0.5F, 4.2F, -0.5F);
		this.tail2.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(this.tail2, -0.08726646259971647F, 0.0F, 0.0F);
		this.tail1 = new ModelRenderer(this, 70, 21);
		this.tail1.setRotationPoint(-0.5F, 10.5F, 1.3F);
		this.tail1.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1, 0.0F);
		this.setRotateAngle(this.tail1, 0.3665191429188092F, 0.0F, 0.0F);
		this.hornL2 = new ModelRenderer(this, 70, 17);
		this.hornL2.setRotationPoint(7.0F, -9.0F, -3.0F);
		this.hornL2.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
		this.setRotateAngle(this.hornL2, 0.24434609527920614F, 0.0F, 0.7853981633974483F);
		this.snout = new ModelRenderer(this, 64, 0);
		this.snout.setRotationPoint(-2.0F, -3.0F, -5.0F);
		this.snout.addBox(0.0F, 0.0F, 0.0F, 4, 3, 1, 0.0F);
		this.hornR2 = new ModelRenderer(this, 70, 9);
		this.hornR2.setRotationPoint(-7.7F, -8.2F, -3.0F);
		this.hornR2.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
		this.setRotateAngle(this.hornR2, 0.24434609527920614F, 0.0F, -0.7853981633974483F);
		this.hornL1 = new ModelRenderer(this, 66, 12);
		this.hornL1.setRotationPoint(6.0F, -8.0F, -3.0F);
		this.hornL1.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(this.hornL1, 0.13962634015954636F, 0.0F, 1.2217304763960306F);
		this.hornR1 = new ModelRenderer(this, 66, 4);
		this.hornR1.setRotationPoint(-6.8F, -6.0F, -3.0F);
		this.hornR1.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(this.hornR1, 0.13962634015954636F, 0.0F, -1.2217304763960306F);

		this.tail1.addChild(this.tail2);
		this.bipedBody.addChild(this.tail1);

		this.bipedHead.addChild(this.snout);

		this.bipedHead.addChild(this.hornR1);
		this.bipedHead.addChild(this.hornR2);

		this.bipedHead.addChild(this.hornL1);
		this.bipedHead.addChild(this.hornL2);
	}

}
