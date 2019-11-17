package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Necromancer - Arlo The Epic
 * Created using Tabula 7.0.0
 */
public class ModelNecromancer extends ModelBase {
    public ModelRenderer Torso;
    public ModelRenderer Head;
    public ModelRenderer Arm_Left;
    public ModelRenderer Arm_Right;
    public ModelRenderer Leg_Right;
    public ModelRenderer Leg_Left;
    public ModelRenderer Leg_Cape;
    public ModelRenderer Buckle;
    public ModelRenderer Nose;
    public ModelRenderer Hood_Rear;
    public ModelRenderer Eyes;
    public ModelRenderer Hood_Top;
    public ModelRenderer Hood_Bottom;
    public ModelRenderer Hood_Left;
    public ModelRenderer Hood_Right;
    public ModelRenderer Hood_Front;

    public ModelNecromancer() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.Nose = new ModelRenderer(this, 24, 2);
        this.Nose.setRotationPoint(0.0F, -3.0F, -4.0F);
        this.Nose.addBox(-1.0F, 0.0F, -2.0F, 2, 4, 2, 0.0F);
        this.Arm_Right = new ModelRenderer(this, 16, 34);
        this.Arm_Right.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.Arm_Right.addBox(-4.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Leg_Cape = new ModelRenderer(this, 3, 20);
        this.Leg_Cape.setRotationPoint(0.0F, 12.0F, 2.0F);
        this.Leg_Cape.addBox(-4.0F, 0.0F, 0.0F, 8, 12, 0, 0.0F);
        this.setRotateAngle(Leg_Cape, 0.2792526803190927F, 0.0F, 0.0F);
        this.Leg_Right = new ModelRenderer(this, 0, 34);
        this.Leg_Right.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.Leg_Right.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Hood_Front = new ModelRenderer(this, 60, 23);
        this.Hood_Front.setRotationPoint(0.0F, 0.0F, -9.0F);
        this.Hood_Front.addBox(-4.5F, 0.0F, 0.0F, 9, 11, 0, 0.0F);
        this.Leg_Left = new ModelRenderer(this, 0, 34);
        this.Leg_Left.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.Leg_Left.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Hood_Rear = new ModelRenderer(this, 42, 23);
        this.Hood_Rear.setRotationPoint(0.0F, 0.0F, 4.5F);
        this.Hood_Rear.addBox(-4.5F, -10.5F, 0.0F, 9, 11, 0, 0.0F);
        this.Arm_Left = new ModelRenderer(this, 16, 34);
        this.Arm_Left.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.Arm_Left.addBox(-0.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Hood_Left = new ModelRenderer(this, 24, 14);
        this.Hood_Left.setRotationPoint(4.5F, 0.0F, 0.0F);
        this.Hood_Left.addBox(0.0F, 0.0F, -9.0F, 0, 11, 9, 0.0F);
        this.Head = new ModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
        this.setRotateAngle(Head, 0.0F, 0.017453292519943295F, 0.0F);
        this.Eyes = new ModelRenderer(this, 54, 18);
        this.Eyes.setRotationPoint(0.0F, -4.0F, -4.1F);
        this.Eyes.addBox(-3.0F, 0.0F, 0.0F, 6, 1, 0, 0.0F);
        this.Hood_Top = new ModelRenderer(this, 24, 14);
        this.Hood_Top.setRotationPoint(0.0F, -10.5F, 0.0F);
        this.Hood_Top.addBox(-4.5F, 0.0F, -9.0F, 9, 0, 9, 0.0F);
        this.Hood_Right = new ModelRenderer(this, 24, 14);
        this.Hood_Right.setRotationPoint(-4.5F, 0.0F, 0.0F);
        this.Hood_Right.addBox(0.0F, 0.0F, -9.0F, 0, 11, 9, 0.0F);
        this.Torso = new ModelRenderer(this, 0, 18);
        this.Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Torso.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.Buckle = new ModelRenderer(this, 78, 2);
        this.Buckle.setRotationPoint(0.0F, 12.0F, -2.1F);
        this.Buckle.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 0, 0.0F);
        this.Hood_Bottom = new ModelRenderer(this, 0, 0);
        this.Hood_Bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Hood_Bottom.addBox(-4.5F, 0.0F, -9.0F, 9, 0, 9, 0.0F);
        this.Head.addChild(this.Nose);
        this.Torso.addChild(this.Arm_Right);
        this.Torso.addChild(this.Leg_Cape);
        this.Torso.addChild(this.Leg_Right);
        this.Hood_Top.addChild(this.Hood_Front);
        this.Torso.addChild(this.Leg_Left);
        this.Head.addChild(this.Hood_Rear);
        this.Torso.addChild(this.Arm_Left);
        this.Hood_Top.addChild(this.Hood_Left);
        this.Torso.addChild(this.Head);
        this.Head.addChild(this.Eyes);
        this.Hood_Rear.addChild(this.Hood_Top);
        this.Hood_Top.addChild(this.Hood_Right);
        this.Torso.addChild(this.Buckle);
        this.Hood_Rear.addChild(this.Hood_Bottom);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Torso.render(f5);
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
