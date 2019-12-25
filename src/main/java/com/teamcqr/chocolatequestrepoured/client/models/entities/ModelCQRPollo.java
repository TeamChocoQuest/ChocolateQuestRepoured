package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

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

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body.render(f5);
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
