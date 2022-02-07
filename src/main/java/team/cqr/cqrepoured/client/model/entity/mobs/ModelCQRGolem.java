package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGolem;

public class ModelCQRGolem extends ModelCQRBiped<EntityCQRGolem> {

	ModelRenderer nose;
	ModelRenderer bodyLower;

	public ModelCQRGolem() {
		super(64, 64, false);

		// Biped components that need to be "adjusted"
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4F, -8F, -4F, 8, 8, 8, 0.0F);
		this.head.setPos(0F, 0F, 0F);
		this.head.setTexSize(64, 64);
		this.head.mirror = true;
		this.setRotateAngle(this.head, 0F, 0F, 0F);

		this.hat.visible = false;

		this.rightArm = new ModelRenderer(this, 40, 32);
		this.rightArm.addBox(-3F, -2F, -2F, 4, 12, 4, 0.0F);
		this.rightArm.setPos(-5F, 2F, 0F);
		this.rightArm.setTexSize(64, 64);
		this.rightArm.mirror = true;
		this.setRotateAngle(this.rightArm, 0F, 0F, 0F);

		this.leftArm = new ModelRenderer(this, 40, 16);
		this.leftArm.addBox(-1F, -2F, -2F, 4, 12, 4, 0.0F);
		this.leftArm.setPos(5F, 2F, 0F);
		this.leftArm.setTexSize(64, 64);
		this.leftArm.mirror = true;
		this.setRotateAngle(this.leftArm, 0F, 0F, 0F);

		this.rightLeg = new ModelRenderer(this, 0, 32);
		this.rightLeg.addBox(-2F, 0F, -2F, 4, 12, 4, 0.0F);
		this.rightLeg.setPos(-3F, 12F, 0F);
		this.rightLeg.setTexSize(64, 64);
		this.rightLeg.mirror = true;
		this.setRotateAngle(this.rightLeg, 0F, 0F, 0F);

		this.leftLeg = new ModelRenderer(this, 0, 16);
		this.leftLeg.addBox(-2F, 0F, -2F, 4, 12, 4, 0.0F);
		this.leftLeg.setPos(3F, 12F, 0F);
		this.leftLeg.setTexSize(64, 64);
		this.leftLeg.mirror = true;
		this.setRotateAngle(this.leftLeg, 0F, 0F, 0F);
		//

		this.nose = new ModelRenderer(this, 32, 0);
		this.nose.addBox(-1F, -3F, -6F, 2, 4, 2, 0.0F);
		this.nose.setPos(0F, 0F, 0F);
		this.nose.setTexSize(64, 64);
		this.nose.mirror = true;
		this.setRotateAngle(this.nose, 0F, 0F, 0F);

		this.body = new ModelRenderer(this, 16, 16);
		this.body.addBox(-4F, 0F, -2F, 8, 6, 4, 0.0F);
		this.body.setPos(0F, 0F, 0F);
		this.body.setTexSize(64, 64);
		this.body.mirror = true;
		this.setRotateAngle(this.body, 0F, 0F, 0F);

		this.bodyLower = new ModelRenderer(this, 18, 26);
		this.bodyLower.addBox(-3F, 6F, -2F, 6, 6, 4, 0.0F);
		this.bodyLower.setPos(0F, 0F, 0F);
		this.bodyLower.setTexSize(64, 64);
		this.bodyLower.mirror = true;
		this.setRotateAngle(this.bodyLower, 0F, 0F, 0F);

		this.body.addChild(this.bodyLower);
		this.head.addChild(this.nose);
	}

}
