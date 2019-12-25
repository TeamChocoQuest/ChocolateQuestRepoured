package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelChicken - DerToaster98
 * Created using Tabula 7.0.1
 */
public class ModelCQRPollo extends ModelChicken {

	public ModelRenderer throat;

    public ModelCQRPollo() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.bill = new ModelRenderer(this, 14, 0);
        this.bill.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.bill.addBox(-1.0F, -2.0F, -4.0F, 2, 2, 2, 0.0F);
        this.leftLeg = new ModelRenderer(this, 52, 0);
        this.leftLeg.mirror = true;
        this.leftLeg.setRotationPoint(1.0F, 4.5F, 1.0F);
        this.leftLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 12, 3, 0.0F);
        this.rightLeg = new ModelRenderer(this, 52, 0);
        this.rightLeg.setRotationPoint(-2.0F, 4.5F, 1.0F);
        this.rightLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 12, 3, 0.0F);
        this.leftWing = new ModelRenderer(this, 44, 11);
        this.leftWing.mirror = true;
        this.leftWing.setRotationPoint(4.0F, -1.0F, 0.0F);
        this.leftWing.addBox(0.0F, -11.0F, -5.0F, 0, 11, 10, 0.0F);
        this.setRotateAngle(leftWing, 0.0F, 0.0F, 1.5707963267948966F);
        this.rightWing = new ModelRenderer(this, 44, 11);
        this.rightWing.setRotationPoint(-4.0F, -1.0F, 0.0F);
        this.rightWing.addBox(0.0F, -11.0F, -5.0F, 0, 11, 10, 0.0F);
        this.setRotateAngle(rightWing, 0.0F, 0.0F, -1.5707963267948966F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, -9.0F, -0.5F);
        this.head.addBox(-2.0F, -4.0F, -3.0F, 4, 4, 6, 0.0F);
        this.setRotateAngle(head, -0.7853981633974483F, 0.0F, 0.0F);
        this.throat = new ModelRenderer(this, 39, 0);
        this.throat.setRotationPoint(0.0F, -2.5F, -5.8F);
        this.throat.addBox(-1.0F, -10.0F, -1.0F, 2, 10, 2, 0.0F);
        this.setRotateAngle(throat, 0.7853981633974483F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 11);
        this.body.setRotationPoint(0.0F, 7.5F, 1.0F);
        this.body.addBox(-4.0F, -4.5F, -6.0F, 8, 9, 12, 0.0F);
        this.head.addChild(this.bill);
        this.body.addChild(this.leftLeg);
        this.body.addChild(this.rightLeg);
        this.body.addChild(this.leftWing);
        this.body.addChild(this.rightWing);
        this.throat.addChild(this.head);
        this.body.addChild(this.throat);
        this.chin.showModel = false;
        this.chin.isHidden = true;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.body.render(scale);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        this.head.rotateAngleX = headPitch * 0.017453292F + new Float(Math.PI);
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.7F * limbSwingAmount;
        if(entityIn.isAirBorne) {
        	this.rightWing.rotateAngleZ = ageInTicks;
            this.leftWing.rotateAngleZ = -ageInTicks;
        }
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
