package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;

public class ModelBackpack extends ModelCustomArmor {

	private ModelRenderer mainBag;
	private ModelRenderer leftBag;
	private ModelRenderer rightBag;
	// private ModelRenderer topBag;
	// private ModelRenderer backBag;

	public ModelBackpack(float scale) {
		super(scale, 64, 32);

		this.bipedBody = new ModelRenderer(this, 32, 16);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.01F);

		this.mainBag = new ModelRenderer(this, 0, 0);
		this.mainBag.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.mainBag.addBox(-5.0F, 0.0F, 2.01F, 10, 12, 8, 0.0F);

		this.rightBag = new ModelRenderer(this, 36, 0);
		this.rightBag.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightBag.addBox(-10.0F, 2.0F, 2.01F, 5, 8, 8, 0.0F);

		this.leftBag = new ModelRenderer(this, 36, 0);
		this.leftBag.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftBag.mirror = true;
		this.leftBag.addBox(5.0F, 2.0F, 2.01F, 5, 8, 8, 0.0F);

		// this.topBag = new ModelRenderer(this, 36, 0);
		// this.topBag.addBox(0.0F, -6.0F, 0.0F, 8, 8, 5, size);
		// this.topBag.setRotationPoint(-4.0F, 0.0F, 4.0F);

		// this.backBag = new ModelRenderer(this, 36, 0);
		// this.backBag.addBox(-4.0F, 0.0F, 0.0F, 8, 8, 5, 0.0F);
		// this.backBag.setRotationPoint(0.0F, 4.25F, 9.0F);

		this.bipedBody.addChild(this.mainBag);
		this.bipedBody.addChild(this.leftBag);
		this.bipedBody.addChild(this.rightBag);
		// this.bipedBody.addChild(this.topBag);
		// this.bipedBody.addChild(this.backBag);

		this.bipedHead = new ModelRenderer(this);
		this.bipedHead.isHidden = true;
		this.bipedHeadwear = new ModelRenderer(this);
		this.bipedHeadwear.isHidden = true;
		this.bipedRightArm = new ModelRenderer(this);
		this.bipedRightArm.isHidden = true;
		this.bipedLeftArm = new ModelRenderer(this);
		this.bipedLeftArm.isHidden = true;
		this.bipedRightLeg = new ModelRenderer(this);
		this.bipedRightLeg.isHidden = true;
		this.bipedLeftLeg = new ModelRenderer(this);
		this.bipedLeftLeg.isHidden = true;
	}

}
