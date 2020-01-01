package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Nether Dragon - ArloTheEpic
 * Created using Tabula 7.0.0
 */
public class ModelNetherDragon extends ModelBase {
    //Head
	public ModelRenderer Head_Back;
	//Snout
    public ModelRenderer mouthTop;
    public ModelRenderer mouthBottom;
    public ModelRenderer snoutRight;
    public ModelRenderer snoutLeft;
    //Horns
    public ModelRenderer hornRightA;
    public ModelRenderer hornLeftA;
    public ModelRenderer hornRightB;
    public ModelRenderer hornRightC;
    public ModelRenderer hornLeftB;
    public ModelRenderer hornLeftC;
    //Body
    public ModelRenderer part1;
    public ModelRenderer part2;
    public ModelRenderer part3;
    public ModelRenderer part4;
    public ModelRenderer part5;
    public ModelRenderer part6;
    public ModelRenderer part7;
    public ModelRenderer part8;
    public ModelRenderer part9;
    public ModelRenderer part10;
    public ModelRenderer part11;
    public ModelRenderer part12;
    public ModelRenderer part13;
    public ModelRenderer part14;
    public ModelRenderer part15;
    public ModelRenderer part16;

    public ModelNetherDragon() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.part2 = new ModelRenderer(this, 0, 20);
        this.part2.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part2.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.part1 = new ModelRenderer(this, 0, 20);
        this.part1.setRotationPoint(-0.0F, 5.0F, 5.0F);
        this.part1.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.hornLeftC = new ModelRenderer(this, 44, 4);
        this.hornLeftC.setRotationPoint(2.5F, -2.3F, 0.5F);
        this.hornLeftC.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(hornLeftC, 0.0F, 0.0F, 0.7740535232594852F);
        this.snoutRight = new ModelRenderer(this, 0, 0);
        this.snoutRight.setRotationPoint(14.5F, -1.0F, -4.0F);
        this.snoutRight.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.part10 = new ModelRenderer(this, 0, 20);
        this.part10.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part10.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.snoutLeft = new ModelRenderer(this, 0, 0);
        this.snoutLeft.setRotationPoint(14.5F, -1.0F, 4.0F);
        this.snoutLeft.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.hornLeftB = new ModelRenderer(this, 80, 4);
        this.hornLeftB.setRotationPoint(3.1F, -2.8F, 0.5F);
        this.hornLeftB.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(hornLeftB, 0.0F, 0.0F, 0.8196066167365371F);
        this.part4 = new ModelRenderer(this, 0, 20);
        this.part4.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part4.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.Head_Back = new ModelRenderer(this, 52, 24);
        this.Head_Back.setRotationPoint(-4.5F, 0.0F, -32.0F);
        this.Head_Back.addBox(0.0F, 0.0F, 0.0F, 18, 10, 10, 0.0F);
        this.setRotateAngle(Head_Back, 0.0F, 1.5707963267948966F, 0.0F);
        this.part13 = new ModelRenderer(this, 41, 0);
        this.part13.setRotationPoint(-12.0F, 1.0F, 1.0F);
        this.part13.addBox(-14.0F, -6.0F, -6.0F, 14, 10, 10, 0.0F);
        this.part6 = new ModelRenderer(this, 0, 20);
        this.part6.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part6.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.part15 = new ModelRenderer(this, 98, 22);
        this.part15.setRotationPoint(-12.0F, 0.0F, -0.0F);
        this.part15.addBox(-9.0F, -3.0F, -3.0F, 9, 6, 6, 0.0F);
        this.part8 = new ModelRenderer(this, 0, 20);
        this.part8.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part8.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.hornLeftA = new ModelRenderer(this, 32, 1);
        this.hornLeftA.setRotationPoint(0.0F, -2.8F, 9.9F);
        this.hornLeftA.addBox(0.0F, 0.0F, 0.0F, 3, 4, 3, 0.0F);
        this.setRotateAngle(hornLeftA, -0.5009094953223726F, 0.3518583772020568F, -0.13439035240356337F);
        this.hornRightB = new ModelRenderer(this, 80, 4);
        this.hornRightB.setRotationPoint(3.1F, -2.8F, 0.5F);
        this.hornRightB.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(hornRightB, 0.0F, 0.0F, 0.8196066167365371F);
        this.hornRightC = new ModelRenderer(this, 44, 4);
        this.hornRightC.setRotationPoint(2.5F, -2.3F, 0.5F);
        this.hornRightC.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(hornRightC, 0.0F, 0.0F, 0.7740535232594852F);
        this.mouthBottom = new ModelRenderer(this, 1, 46);
        this.mouthBottom.setRotationPoint(18.0F, 4.0F, 4.5F);
        this.mouthBottom.addBox(-0.5F, 0.0F, -4.0F, 16, 3, 9, 0.0F);
        this.mouthTop = new ModelRenderer(this, 55, 46);
        this.mouthTop.setRotationPoint(18.0F, 1.0F, 4.5F);
        this.mouthTop.addBox(-0.5F, 0.0F, -4.0F, 16, 3, 9, 0.0F);
        this.hornRightA = new ModelRenderer(this, 32, 1);
        this.hornRightA.setRotationPoint(1.1F, -1.8F, -2.4F);
        this.hornRightA.addBox(0.0F, 0.0F, 0.0F, 3, 4, 3, 0.0F);
        this.setRotateAngle(hornRightA, 0.5009094953223726F, -0.3518583772020568F, -0.13439035240356337F);
        this.part11 = new ModelRenderer(this, 0, 20);
        this.part11.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part11.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.part5 = new ModelRenderer(this, 0, 20);
        this.part5.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part5.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.part16 = new ModelRenderer(this, 90, 12);
        this.part16.setRotationPoint(-9.0F, 0.0F, 0.0F);
        this.part16.addBox(-6.0F, -2.0F, -2.0F, 6, 4, 4, 0.0F);
        this.part9 = new ModelRenderer(this, 0, 20);
        this.part9.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part9.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.part12 = new ModelRenderer(this, 0, 20);
        this.part12.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part12.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.part7 = new ModelRenderer(this, 0, 20);
        this.part7.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part7.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.part3 = new ModelRenderer(this, 0, 20);
        this.part3.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.part3.addBox(-12.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
        this.part14 = new ModelRenderer(this, 0, 0);
        this.part14.setRotationPoint(-14.0F, -1.0F, -1.0F);
        this.part14.addBox(-12.0F, -4.0F, -4.0F, 12, 8, 8, 0.0F);
        this.part1.addChild(this.part2);
        this.Head_Back.addChild(this.part1);
        this.hornLeftB.addChild(this.hornLeftC);
        this.mouthTop.addChild(this.snoutRight);
        this.part9.addChild(this.part10);
        this.mouthTop.addChild(this.snoutLeft);
        this.hornLeftA.addChild(this.hornLeftB);
        this.part3.addChild(this.part4);
        this.part12.addChild(this.part13);
        this.part5.addChild(this.part6);
        this.part14.addChild(this.part15);
        this.part7.addChild(this.part8);
        this.Head_Back.addChild(this.hornLeftA);
        this.hornRightA.addChild(this.hornRightB);
        this.hornRightB.addChild(this.hornRightC);
        this.Head_Back.addChild(this.mouthBottom);
        this.Head_Back.addChild(this.mouthTop);
        this.Head_Back.addChild(this.hornRightA);
        this.part10.addChild(this.part11);
        this.part4.addChild(this.part5);
        this.part15.addChild(this.part16);
        this.part8.addChild(this.part9);
        this.part11.addChild(this.part12);
        this.part6.addChild(this.part7);
        this.part2.addChild(this.part3);
        this.part13.addChild(this.part14);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Head_Back.render(f5);
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
