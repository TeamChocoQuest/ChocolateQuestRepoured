package com.teamcqr.chocolatequestrepoured.client.models.entities.customarmor;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRIllager;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPotionHealing;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelCQRIllagerArmor extends ModelBiped {

	public ModelCQRIllagerArmor(float scale) {
		super(scale);
		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);

		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		this.bipedRightLeg.mirror = true;
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);

		this.bipedBody = new ModelRenderer(this, 16, 16);
		this.bipedBody.addBox(-4.0F, 1.0F, -2.0F, 8, 12, 4, scale + 0.75F);

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 8, 8, scale);

		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		if (!(entityIn instanceof EntityCQRIllager)) {
			return;
		}
		this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
		this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
		EntityCQRIllager ent = (EntityCQRIllager) entityIn;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;
		
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		if (!ent.isAggressive() && !(ent.getHeldItemMainhand().getItem() instanceof ItemPotionHealing)) {
			/*
			 * this.arms.rotationPointY = 3.0F;
			 * this.arms.rotationPointZ = -1.0F;
			 * this.arms.rotateAngleX = -0.75F;
			 */
			float angle = new Float(Math.toRadians(-42.97));
			this.setRotateAngle(this.bipedLeftArm, angle, 0, 0);
			this.setRotateAngle(this.bipedRightArm, angle, 0, 0);
		}
		
		if (this.isRiding)
        {
            //this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
            //this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = -1.4137167F;
            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
            this.bipedRightLeg.rotateAngleZ = 0.07853982F;
            this.bipedLeftLeg.rotateAngleX = -1.4137167F;
            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
            this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
        }

		AbstractIllager.IllagerArmPose abstractillager$illagerarmpose = ((EntityCQRIllager) entityIn).getIllagerArmPose();

		if (ent.isAggressive() || ent.getHeldItemMainhand().getItem() instanceof ItemPotionHealing) {
			if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.ATTACKING) {
				float f = MathHelper.sin(this.swingProgress * (float) Math.PI);
				float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
				this.bipedRightArm.rotateAngleZ = 0.0F;
				this.bipedLeftArm.rotateAngleZ = 0.0F;
				this.bipedRightArm.rotateAngleY = 0.15707964F;
				this.bipedLeftArm.rotateAngleY = -0.15707964F;

				if (((EntityLivingBase) entityIn).getPrimaryHand() == EnumHandSide.RIGHT) {
					this.bipedRightArm.rotateAngleX = -1.8849558F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
					this.bipedLeftArm.rotateAngleX = -0.0F + MathHelper.cos(ageInTicks * 0.19F) * 0.5F;
					this.bipedRightArm.rotateAngleX += f * 2.2F - f1 * 0.4F;
					this.bipedLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
				} else {
					this.bipedRightArm.rotateAngleX = -0.0F + MathHelper.cos(ageInTicks * 0.19F) * 0.5F;
					this.bipedLeftArm.rotateAngleX = -1.8849558F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
					this.bipedRightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
					this.bipedLeftArm.rotateAngleX += f * 2.2F - f1 * 0.4F;
				}

				this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
				this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
				this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
				this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
			}
			/*
			 * else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.SPELLCASTING)
			 * {
			 * this.bipedRightArm.rotationPointZ = 0.0F;
			 * this.bipedRightArm.rotationPointX = -5.0F;
			 * this.bipedLeftArm.rotationPointZ = 0.0F;
			 * this.bipedLeftArm.rotationPointX = 5.0F;
			 * this.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			 * this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			 * this.bipedRightArm.rotateAngleZ = 2.3561945F;
			 * this.bipedLeftArm.rotateAngleZ = -2.3561945F;
			 * this.bipedRightArm.rotateAngleY = 0.0F;
			 * this.bipedLeftArm.rotateAngleY = 0.0F;
			 * }
			 */
			else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
				this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
				this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
				this.bipedLeftArm.rotateAngleX = -0.9424779F + this.bipedHead.rotateAngleX;
				this.bipedLeftArm.rotateAngleY = this.bipedHead.rotateAngleY - 0.4F;
				this.bipedLeftArm.rotateAngleZ = ((float) Math.PI / 2F);
			}
		}
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
