package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * CQRWasp - DerToaster
 * Created using Tabula 7.0.1
 */
public class ModelCQRWasp extends ModelBase {
    public ModelRenderer bodyMain;
    public ModelRenderer head;
    public ModelRenderer bodyBack;
    public ModelRenderer wingL;
    public ModelRenderer wingR;
    public ModelRenderer sting;

    public ModelCQRWasp(float size) {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.head = new ModelRenderer(this, 0, 10);
        this.head.setRotationPoint(0.0F, 0.0F, -2.9F);
        this.head.addBox(-1.5F, -1.5F, -3.0F, 3, 3, 3, size);
        this.wingR = new ModelRenderer(this, 0, 23);
        this.wingR.setRotationPoint(-1.0F, -1.0F, 0.0F);
        this.wingR.addBox(0.0F, 0.0F, -2.0F, 8, 0, 4, size);
        this.setRotateAngle(wingR, 0.0F, -2.8797932657906435F, 0.3839724354387525F);
        this.wingL = new ModelRenderer(this, 0, 19);
        this.wingL.setRotationPoint(1.0F, -1.0F, 0.0F);
        this.wingL.addBox(0.0F, 0.0F, -2.0F, 8, 0, 4, size);
        this.setRotateAngle(wingL, 0.0F, -0.2617993877991494F, -0.3839724354387525F);
        this.sting = new ModelRenderer(this, 15, 10);
        this.sting.setRotationPoint(0.0F, 0.3F, 4.0F);
        this.sting.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 4, size);
        this.setRotateAngle(sting, -0.7853981633974483F, 0.0F, 0.0F);
        this.bodyMain = new ModelRenderer(this, 0, 0);
        this.bodyMain.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.bodyMain.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 6, size);
        this.bodyBack = new ModelRenderer(this, 29, 9);
        this.bodyBack.setRotationPoint(0.0F, 0.0F, 2.4F);
        this.bodyBack.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 4, size);
        this.setRotateAngle(bodyBack, -0.40142572795869574F, 0.0F, 0.0F);
        this.bodyMain.addChild(this.head);
        this.bodyMain.addChild(this.wingR);
        this.bodyMain.addChild(this.wingL);
        this.bodyBack.addChild(this.sting);
        this.bodyMain.addChild(this.bodyBack);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.bodyMain.render(f5);
        float angle = -22.5F;
        angle += 45F * new Float((1D + Math.sin((2D * Math.PI) / 4) * entity.ticksExisted));
        angle = new Float(Math.toRadians(new Double(angle)));
        this.wingL.rotateAngleZ = angle;
        this.wingR.rotateAngleZ = -angle;
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
