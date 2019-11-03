package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * CQRTurtleBossShell - DerToaster
 * Created using Tabula 7.0.1
 */
public class ModelGiantTortoise extends ModelBase {
    public ModelRenderer mainPart;
    public ModelRenderer belly;
    public ModelRenderer top;
    public ModelRenderer legJointFR;
    public ModelRenderer legJointFL;
    public ModelRenderer legJointBR;
    public ModelRenderer legJointBL;
    public ModelRenderer neck;
    public ModelRenderer top1;
    public ModelRenderer top2;
    public ModelRenderer top3;
    public ModelRenderer top4;
    public ModelRenderer legFR;
    public ModelRenderer footFR;
    public ModelRenderer legFL;
    public ModelRenderer footFL;
    public ModelRenderer legBR;
    public ModelRenderer footBR;
    public ModelRenderer legBL;
    public ModelRenderer footBL;
    public ModelRenderer head;
    public ModelRenderer mouth;

    public ModelGiantTortoise() {
        this.textureWidth = 192;
        this.textureHeight = 192;
        this.top4 = new ModelRenderer(this, 0, 120);
        this.top4.setRotationPoint(-7.0F, -2.0F, 7.0F);
        this.top4.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);
        this.legJointFL = new ModelRenderer(this, 0, 140);
        this.legJointFL.setRotationPoint(13.0F, 3.5F, -13.0F);
        this.legJointFL.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
        this.setRotateAngle(legJointFL, 0.0F, -0.7853981633974483F, 0.0F);
        this.mainPart = new ModelRenderer(this, 0, 0);
        this.mainPart.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.mainPart.addBox(-16.0F, -8.0F, -16.0F, 32, 14, 32, 0.0F);
        this.legJointBR = new ModelRenderer(this, 0, 140);
        this.legJointBR.setRotationPoint(-13.0F, 3.5F, 13.0F);
        this.legJointBR.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
        this.setRotateAngle(legJointBR, 0.0F, 2.356194490192345F, 0.0F);
        this.legBR = new ModelRenderer(this, 140, 0);
        this.legBR.setRotationPoint(0.0F, 1.5F, 1.0F);
        this.legBR.addBox(-4.0F, 0.0F, -8.0F, 8, 8, 8, 0.0F);
        this.setRotateAngle(legBR, -0.7853981633974483F, 0.0F, 0.0F);
        this.legBL = new ModelRenderer(this, 140, 0);
        this.legBL.setRotationPoint(0.0F, 1.5F, 1.0F);
        this.legBL.addBox(-4.0F, 0.0F, -8.0F, 8, 8, 8, 0.0F);
        this.setRotateAngle(legBL, -0.7853981633974483F, 0.0F, 0.0F);
        this.top2 = new ModelRenderer(this, 0, 120);
        this.top2.setRotationPoint(7.0F, -2.0F, -7.0F);
        this.top2.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);
        this.footFR = new ModelRenderer(this, 140, 25);
        this.footFR.setRotationPoint(0.0F, 3.0F, -2.7F);
        this.footFR.addBox(-3.5F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
        this.setRotateAngle(footFR, 0.7853981633974483F, 0.0F, 0.0F);
        this.footBL = new ModelRenderer(this, 140, 25);
        this.footBL.setRotationPoint(0.0F, 3.0F, -2.7F);
        this.footBL.addBox(-3.5F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
        this.setRotateAngle(footBL, 0.7853981633974483F, 0.0F, 0.0F);
        this.footBR = new ModelRenderer(this, 140, 25);
        this.footBR.setRotationPoint(0.0F, 3.0F, -2.7F);
        this.footBR.addBox(-3.5F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
        this.setRotateAngle(footBR, 0.7853981633974483F, 0.0F, 0.0F);
        this.belly = new ModelRenderer(this, 0, 46);
        this.belly.setRotationPoint(-10.0F, 2.0F, -14.0F);
        this.belly.addBox(0.0F, 4.0F, 0.0F, 20, 2, 28, 0.0F);
        this.top3 = new ModelRenderer(this, 0, 120);
        this.top3.setRotationPoint(-7.0F, -2.0F, -7.0F);
        this.top3.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);
        this.legFR = new ModelRenderer(this, 140, 0);
        this.legFR.setRotationPoint(0.0F, 1.5F, 1.0F);
        this.legFR.addBox(-4.0F, 0.0F, -8.0F, 8, 8, 8, 0.0F);
        this.setRotateAngle(legFR, -0.7853981633974483F, 0.0F, 0.0F);
        this.legFL = new ModelRenderer(this, 140, 0);
        this.legFL.setRotationPoint(0.0F, 1.5F, 1.0F);
        this.legFL.addBox(-4.0F, 0.0F, -8.0F, 8, 8, 8, 0.0F);
        this.setRotateAngle(legFL, -0.7853981633974483F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 140, 60);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.5F, -4.0F, -9.0F, 9, 7, 9, 0.0F);
        this.legJointFR = new ModelRenderer(this, 0, 140);
        this.legJointFR.setRotationPoint(-13.0F, 3.5F, -13.0F);
        this.legJointFR.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
        this.setRotateAngle(legJointFR, 0.0F, 0.7853981633974483F, 0.0F);
        this.neck = new ModelRenderer(this, 50, 120);
        this.neck.setRotationPoint(0.0F, 0.0F, -17.0F);
        this.neck.addBox(-5.0F, -5.0F, -1.0F, 10, 10, 2, 0.0F);
        this.mouth = new ModelRenderer(this, 140, 80);
        this.mouth.setRotationPoint(0.0F, 3.5F, -1.0F);
        this.mouth.addBox(-4.5F, -0.5F, -8.0F, 9, 1, 8, 0.0F);
        this.setRotateAngle(mouth, 0.4363323129985824F, 0.0F, 0.0F);
        this.footFL = new ModelRenderer(this, 140, 25);
        this.footFL.setRotationPoint(0.0F, 3.0F, -2.7F);
        this.footFL.addBox(-3.5F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
        this.setRotateAngle(footFL, 0.7853981633974483F, 0.0F, 0.0F);
        this.top = new ModelRenderer(this, 0, 80);
        this.top.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.top.addBox(-14.0F, 0.0F, -14.0F, 28, 2, 28, 0.0F);
        this.legJointBL = new ModelRenderer(this, 0, 140);
        this.legJointBL.setRotationPoint(13.0F, 3.5F, 13.0F);
        this.legJointBL.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
        this.setRotateAngle(legJointBL, 0.0F, -2.356194490192345F, 0.0F);
        this.top1 = new ModelRenderer(this, 0, 120);
        this.top1.setRotationPoint(7.0F, -2.0F, 7.0F);
        this.top1.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);
        this.top.addChild(this.top4);
        this.mainPart.addChild(this.legJointFL);
        this.mainPart.addChild(this.legJointBR);
        this.legJointBR.addChild(this.legBR);
        this.legJointBL.addChild(this.legBL);
        this.top.addChild(this.top2);
        this.legFR.addChild(this.footFR);
        this.legBL.addChild(this.footBL);
        this.legBR.addChild(this.footBR);
        this.mainPart.addChild(this.belly);
        this.top.addChild(this.top3);
        this.legJointFR.addChild(this.legFR);
        this.legJointFL.addChild(this.legFL);
        this.neck.addChild(this.head);
        this.mainPart.addChild(this.legJointFR);
        this.mainPart.addChild(this.neck);
        this.head.addChild(this.mouth);
        this.legFL.addChild(this.footFL);
        this.mainPart.addChild(this.top);
        this.mainPart.addChild(this.legJointBL);
        this.top.addChild(this.top1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.mainPart.render(f5);
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
