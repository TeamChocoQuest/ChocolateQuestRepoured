package team.cqr.cqrepoured.client.models.entities.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import team.cqr.cqrepoured.client.models.entities.ModelCQRBiped;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRIllager;
import team.cqr.cqrepoured.objects.items.ItemPotionHealing;

public class ModelCQRIllager extends ModelCQRBiped {

	private final ModelRenderer arms;
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

		this.arms = new ModelRenderer(this);
		this.arms.setRotationPoint(0.0F, 0.0F + 2.0F, 0.0F);
		this.arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);

		ModelRenderer modelrenderer = new ModelRenderer(this, 44, 22);
		modelrenderer.mirror = true;
		modelrenderer.addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);

		this.arms.addChild(modelrenderer);
		this.arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, 0.0F);

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

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entityIn instanceof EntityCQRIllager) {
			EntityCQRIllager abstractillager = (EntityCQRIllager) entityIn;

			if (abstractillager.isAggressive() || abstractillager.getHeldItemMainhand().getItem() instanceof ItemPotionHealing) {
				this.bipedRightArm.showModel = true;
				this.bipedLeftArm.showModel = true;
			} else {
				this.bipedRightArm.showModel = false;
				this.bipedLeftArm.showModel = false;
				this.arms.render(scale);
			}

			super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so
	 * that arms and legs swing back and forth) and par2 represents how "far" arms and legs
	 * can swing at most.
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor,
			Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		/*
		 * Armors get messed up when illager arm poses are enabled
		 * 
		 * AbstractIllager.IllagerArmPose illagerArmPose = ((EntityCQRIllager) entityIn).getIllagerArmPose();
		 * if (illagerArmPose == IllagerArmPose.CROSSED) {
		 * this.arms.rotationPointY = 3.0F;
		 * this.arms.rotationPointZ = -1.0F;
		 * this.arms.rotateAngleX = -0.75F;
		 * }
		 */
	}

}
