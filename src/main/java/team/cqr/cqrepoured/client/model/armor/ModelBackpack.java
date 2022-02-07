package team.cqr.cqrepoured.client.model.armor;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class ModelBackpack<T extends LivingEntity> extends ModelCustomArmorBase<T> {

	private ModelRenderer mainBag;
	private ModelRenderer leftBag;
	private ModelRenderer rightBag;
	// private ModelRenderer topBag;
	// private ModelRenderer backBag;

	public ModelBackpack(float scale) {
		super(scale, 64, 32);

		this.mainBag = new ModelRenderer(this, 0, 0);
		this.mainBag.setPos(0.0F, 0.0F, 0.0F);
		this.mainBag.addBox(-5.0F, 0.0F, 2.01F, 10, 12, 8, 0.0F);
		this.rightBag = new ModelRenderer(this, 36, 0);
		this.rightBag.setPos(0.0F, 0.0F, 0.0F);
		this.rightBag.addBox(-10.0F, 2.0F, 2.01F, 5, 8, 8, 0.0F);
		this.leftBag = new ModelRenderer(this, 36, 0);
		this.leftBag.setPos(0.0F, 0.0F, 0.0F);
		this.leftBag.mirror = true;
		this.leftBag.addBox(5.0F, 2.0F, 2.01F, 5, 8, 8, 0.0F);
		// this.topBag = new ModelRenderer(this, 36, 0);
		// this.topBag.addBox(0.0F, -6.0F, 0.0F, 8, 8, 5, size);
		// this.topBag.setRotationPoint(-4.0F, 0.0F, 4.0F);
		// this.backBag = new ModelRenderer(this, 36, 0);
		// this.backBag.addBox(-4.0F, 0.0F, 0.0F, 8, 8, 5, 0.0F);
		// this.backBag.setRotationPoint(0.0F, 4.25F, 9.0F);

		this.head = new ModelRenderer(this);
		this.head.visible = true;
		this.hat = new ModelRenderer(this);
		this.hat.visible = true;
		this.body = new ModelRenderer(this, 32, 16);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.01F);
		this.rightArm = new ModelRenderer(this);
		this.rightArm.visible = true;
		this.leftArm = new ModelRenderer(this);
		this.leftArm.visible = true;
		this.rightLeg = new ModelRenderer(this);
		this.rightLeg.visible = true;
		this.leftLeg = new ModelRenderer(this);
		this.leftLeg.visible = true;

		this.body.addChild(this.mainBag);
		this.body.addChild(this.leftBag);
		this.body.addChild(this.rightBag);
		// this.bipedBody.addChild(this.topBag);
		// this.bipedBody.addChild(this.backBag);
	}

}
