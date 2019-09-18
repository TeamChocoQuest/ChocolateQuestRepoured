package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Nether Dragon - ArloTheEpic
 * Created using Tabula 7.0.1
 */
public class ModelNetherDragonHead extends ModelBase {
    public ModelRenderer Head_Back;
    public ModelRenderer Mouth_Top;
    public ModelRenderer Mouth_Bottom;
    public ModelRenderer Horn_Right_A;
    public ModelRenderer Horn_Left_A;
    public ModelRenderer Snout_Right;
    public ModelRenderer Snout_Left;
    public ModelRenderer Horn_Right_B;
    public ModelRenderer Horn_Right_C;
    public ModelRenderer Horn_Left_B;
    public ModelRenderer Horn_Left_C;

    public ModelNetherDragonHead() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.Mouth_Bottom = new ModelRenderer(this, 1, 46);
        this.Mouth_Bottom.setRotationPoint(18.0F, 4.0F, 4.5F);
        this.Mouth_Bottom.addBox(-0.5F, 0.0F, -4.0F, 16, 3, 9, 0.0F);
        this.Horn_Right_C = new ModelRenderer(this, 44, 4);
        this.Horn_Right_C.setRotationPoint(2.5F, -2.3F, 0.5F);
        this.Horn_Right_C.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(Horn_Right_C, 0.0F, 0.0F, 0.7740535232594852F);
        this.Horn_Left_C = new ModelRenderer(this, 44, 4);
        this.Horn_Left_C.setRotationPoint(2.5F, -2.3F, 0.5F);
        this.Horn_Left_C.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(Horn_Left_C, 0.0F, 0.0F, 0.7740535232594852F);
        this.Mouth_Top = new ModelRenderer(this, 55, 46);
        this.Mouth_Top.setRotationPoint(18.0F, 1.0F, 4.5F);
        this.Mouth_Top.addBox(-0.5F, 0.0F, -4.0F, 16, 3, 9, 0.0F);
        this.Horn_Right_B = new ModelRenderer(this, 80, 4);
        this.Horn_Right_B.setRotationPoint(3.1F, -2.8F, 0.5F);
        this.Horn_Right_B.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(Horn_Right_B, 0.0F, 0.0F, 0.8196066167365371F);
        this.Horn_Left_A = new ModelRenderer(this, 32, 1);
        this.Horn_Left_A.setRotationPoint(0.0F, -2.8F, 9.9F);
        this.Horn_Left_A.addBox(0.0F, 0.0F, 0.0F, 3, 4, 3, 0.0F);
        this.setRotateAngle(Horn_Left_A, -0.5009094953223726F, 0.3518583772020568F, -0.13439035240356337F);
        this.Snout_Right = new ModelRenderer(this, 0, 0);
        this.Snout_Right.setRotationPoint(14.5F, -1.0F, -4.0F);
        this.Snout_Right.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.Horn_Left_B = new ModelRenderer(this, 80, 4);
        this.Horn_Left_B.setRotationPoint(3.1F, -2.8F, 0.5F);
        this.Horn_Left_B.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(Horn_Left_B, 0.0F, 0.0F, 0.8196066167365371F);
        this.Head_Back = new ModelRenderer(this, 52, 24);
        this.Head_Back.setRotationPoint(-5.0F, 7.5F, 5.0F);
        this.Head_Back.addBox(0.0F, 0.0F, 0.0F, 18, 9, 10, 0.0F);
        this.setRotateAngle(Head_Back, 0.0F, 1.5707963267948966F, 0.0F);
        this.Horn_Right_A = new ModelRenderer(this, 32, 1);
        this.Horn_Right_A.setRotationPoint(1.1F, -1.8F, -2.4F);
        this.Horn_Right_A.addBox(0.0F, 0.0F, 0.0F, 3, 4, 3, 0.0F);
        this.setRotateAngle(Horn_Right_A, 0.5009094953223726F, -0.3518583772020568F, -0.13439035240356337F);
        this.Snout_Left = new ModelRenderer(this, 0, 0);
        this.Snout_Left.setRotationPoint(14.5F, -1.0F, 4.0F);
        this.Snout_Left.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.Head_Back.addChild(this.Mouth_Bottom);
        this.Horn_Right_B.addChild(this.Horn_Right_C);
        this.Horn_Left_B.addChild(this.Horn_Left_C);
        this.Head_Back.addChild(this.Mouth_Top);
        this.Horn_Right_A.addChild(this.Horn_Right_B);
        this.Head_Back.addChild(this.Horn_Left_A);
        this.Mouth_Top.addChild(this.Snout_Right);
        this.Horn_Left_A.addChild(this.Horn_Left_B);
        this.Head_Back.addChild(this.Horn_Right_A);
        this.Mouth_Top.addChild(this.Snout_Left);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Head_Back.render(f5);
        
        //this.Mouth_Bottom.rotateAngleZ = new Float(Math.toRadians(33.75D));
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
