package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Necromancer - Arlo The Epic
 * Created using Tabula 7.0.1
 */
public class ModelPigMage extends ModelBase {
    public ModelRenderer Torso;
    public ModelRenderer Head;
    public ModelRenderer Arm_Left;
    public ModelRenderer Arm_Right;
    public ModelRenderer Leg_Right;
    public ModelRenderer Leg_Left;
    public ModelRenderer Leg_Cape;
    public ModelRenderer Hood_Rear;
    public ModelRenderer Eyes;
    public ModelRenderer Hood_Top;
    public ModelRenderer Hood_Left;
    public ModelRenderer Hood_Right;
    public ModelRenderer Hood_Front;
    public ModelRenderer Arm_Left_Bone;
    public ModelRenderer Arm_Right_Bone;

    public ModelPigMage() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.Head = new ModelRenderer(this, 83, 0);
        this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.setRotateAngle(Head, 0.0F, 0.017453292519943295F, 0.0F);
        this.Arm_Left_Bone = new ModelRenderer(this, 51, 2);
        this.Arm_Left_Bone.setRotationPoint(2.0F, 8.0F, 0.0F);
        this.Arm_Left_Bone.addBox(-1.0F, -2.0F, -1.0F, 2, 4, 2, 0.0F);
        this.Hood_Left = new ModelRenderer(this, 24, 14);
        this.Hood_Left.setRotationPoint(4.5F, 0.0F, 0.0F);
        this.Hood_Left.addBox(0.0F, 0.0F, -9.0F, 0, 11, 9, 0.0F);
        this.Arm_Right_Bone = new ModelRenderer(this, 51, 2);
        this.Arm_Right_Bone.setRotationPoint(-2.0F, 6.0F, 0.0F);
        this.Arm_Right_Bone.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.Arm_Left = new ModelRenderer(this, 32, 34);
        this.Arm_Left.setRotationPoint(4.0F, 2.0F, 0.0F);
        this.Arm_Left.addBox(-0.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.Torso = new ModelRenderer(this, 0, 18);
        this.Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Torso.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.Arm_Right = new ModelRenderer(this, 32, 34);
        this.Arm_Right.setRotationPoint(-4.0F, 2.0F, 0.0F);
        this.Arm_Right.addBox(-4.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.Leg_Cape = new ModelRenderer(this, 0, 50);
        this.Leg_Cape.setRotationPoint(0.0F, 12.0F, 2.0F);
        this.Leg_Cape.addBox(-4.0F, 0.0F, 0.0F, 8, 12, 0, 0.0F);
        this.setRotateAngle(Leg_Cape, 0.2792526803190927F, 0.0F, 0.0F);
        this.Hood_Rear = new ModelRenderer(this, 42, 23);
        this.Hood_Rear.setRotationPoint(0.0F, 2.0F, 4.5F);
        this.Hood_Rear.addBox(-4.5F, -10.5F, 0.0F, 9, 11, 0, 0.0F);
        this.Hood_Front = new ModelRenderer(this, 60, 23);
        this.Hood_Front.setRotationPoint(0.0F, 0.0F, -9.0F);
        this.Hood_Front.addBox(-4.5F, 0.0F, 0.0F, 9, 11, 0, 0.0F);
        this.Hood_Top = new ModelRenderer(this, 24, 14);
        this.Hood_Top.setRotationPoint(0.0F, -10.5F, 0.0F);
        this.Hood_Top.addBox(-4.5F, 0.0F, -9.0F, 9, 0, 9, 0.0F);
        this.Hood_Right = new ModelRenderer(this, 24, 14);
        this.Hood_Right.setRotationPoint(-4.5F, 0.0F, 0.0F);
        this.Hood_Right.addBox(0.0F, 0.0F, -9.0F, 0, 11, 9, 0.0F);
        this.Eyes = new ModelRenderer(this, 54, 18);
        this.Eyes.setRotationPoint(0.0F, -4.0F, -4.1F);
        this.Eyes.addBox(-3.0F, 0.0F, 0.0F, 6, 1, 0, 0.0F);
        this.Leg_Right = new ModelRenderer(this, 0, 34);
        this.Leg_Right.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.Leg_Right.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Leg_Left = new ModelRenderer(this, 0, 34);
        this.Leg_Left.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.Leg_Left.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Torso.addChild(this.Head);
        this.Arm_Left.addChild(this.Arm_Left_Bone);
        this.Hood_Top.addChild(this.Hood_Left);
        this.Arm_Right.addChild(this.Arm_Right_Bone);
        this.Torso.addChild(this.Arm_Left);
        this.Torso.addChild(this.Arm_Right);
        this.Torso.addChild(this.Leg_Cape);
        this.Head.addChild(this.Hood_Rear);
        this.Hood_Top.addChild(this.Hood_Front);
        this.Hood_Rear.addChild(this.Hood_Top);
        this.Hood_Top.addChild(this.Hood_Right);
        this.Head.addChild(this.Eyes);
        this.Torso.addChild(this.Leg_Right);
        this.Torso.addChild(this.Leg_Left);
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
