package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;

/**
 * ModelTurtleChestplate - Sir Squidly Created using Tabula 7.1.0
 */
public class ModelArmorTurtle extends ModelCustomArmorBase {

	public ModelRenderer shell1;
	public ModelRenderer shell2;
	public ModelRenderer legR1_1;
	public ModelRenderer legR1_2;

	public ModelArmorTurtle(float scale) {
		super(scale, 64, 64);

		this.shell1 = new ModelRenderer(this, 0, 32);
		this.shell1.setRotationPoint(-2.0F, -2.0F, 3.5F);
		this.shell1.addBox(0.0F, 0.0F, 0.0F, 12, 16, 4, 0.0F);

		this.shell2 = new ModelRenderer(this, 32, 32);
		this.shell2.setRotationPoint(0.0F, 0.0F, 7.5F);
		this.shell2.addBox(0.0F, 0.0F, 0.0F, 8, 12, 2, 0.0F);

		this.legR1_1 = new ModelRenderer(this, 16, 52);
		this.legR1_1.setRotationPoint(1.0F, 0.0F, -0.5F);
		this.legR1_1.addBox(0.0F, 0.0F, 0.0F, 3, 7, 5, 0.0F);
		this.setRotateAngle(legR1_1, 0.0F, 0.0F, -0.4363323129985824F);

		this.legR1_2 = new ModelRenderer(this, 0, 52);
		this.legR1_2.setRotationPoint(0.0F, -1.2F, -0.5F);
		this.legR1_2.addBox(0.0F, 0.0F, 0.0F, 3, 7, 5, 0.0F);
		this.setRotateAngle(legR1_2, 0.0F, 0.0F, 0.4363323129985824F);

		bipedBody.addChild(shell1);
		bipedBody.addChild(shell2);
		bipedLeftLeg.addChild(legR1_2);
		bipedLeftLeg.addChild(legR1_1);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}