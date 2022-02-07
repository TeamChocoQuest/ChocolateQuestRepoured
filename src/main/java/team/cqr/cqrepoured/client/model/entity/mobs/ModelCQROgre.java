package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQROgre;

/**
 * ModelOgre - Sir Squidly Created using Tabula 7.0.1
 */
public class ModelCQROgre extends ModelCQRBiped<EntityCQROgre> {

	public ModelRenderer BodyLower1;
	public ModelRenderer Nose1;
	public ModelRenderer Jaw1;

	public ModelCQROgre() {
		super(96, 64, true);

		this.body = new ModelRenderer(this, 16, 16);
		this.body.setPos(0.0F, 1.0F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 4, 4, 0.0F);
		this.leftLeg = new ModelRenderer(this, 16, 48);
		this.leftLeg.setPos(2.3F, 12.0F, 0.1F);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leftArm = new ModelRenderer(this, 32, 48);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.setRotateAngle(this.leftArm, -0.0F, 0.0F, -0.10000736613927509F);
		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.setPos(0.0F, 0.0F, -0.5F);
		this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.55F);
		this.Nose1 = new ModelRenderer(this, 64, 0);
		this.Nose1.setPos(-1.0F, -3.3F, -4.5F);
		this.Nose1.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
		this.setRotateAngle(this.Nose1, -0.2617993877991494F, 0.0F, 0.0F);
		this.BodyLower1 = new ModelRenderer(this, 66, 19);
		this.BodyLower1.setPos(-4.5F, 4.0F, -3.5F);
		this.BodyLower1.addBox(0.0F, 0.0F, 0.0F, 9, 7, 6, 0.0F);
		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.setPos(2.2F, 12.0F, 0.1F);
		this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.hat = new ModelRenderer(this, 32, 0);
		this.hat.setPos(0.0F, -1.0F, -1.0F);
		this.hat.addBox(-4.0F, -7.0F, -4.0F, 8, 8, 8, 0.25F);
		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.setPos(-5.0F, 2.0F, 0.0F);
		this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
		this.setRotateAngle(this.bipedRightArmwear, 0.0F, 0.0F, 0.10000736613927509F);
		this.Jaw1 = new ModelRenderer(this, 60, 32);
		this.Jaw1.setPos(-4.5F, -2.5F, -4.5F);
		this.Jaw1.addBox(0.0F, 0.0F, 0.0F, 9, 4, 9, 0.0F);
		this.rightArm = new ModelRenderer(this, 40, 16);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.setRotateAngle(this.rightArm, 0.0F, 0.0F, 0.10000736613927509F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, -1.0F, -1.0F);
		this.head.addBox(-4.0F, -7.0F, -4.0F, 8, 8, 8, 0.0F);
		this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
		this.bipedLeftArmwear.setPos(5.0F, 2.0F, 0.0F);
		this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
		this.setRotateAngle(this.bipedLeftArmwear, -0.0F, 0.0F, -0.10000736613927509F);
		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.setPos(-2.2F, 12.0F, 0.1F);
		this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.rightLeg = new ModelRenderer(this, 0, 16);
		this.rightLeg.setPos(-2.3F, 12.0F, 0.0F);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.head.addChild(this.Nose1);
		this.body.addChild(this.BodyLower1);
		this.head.addChild(this.Jaw1);
	}

}
