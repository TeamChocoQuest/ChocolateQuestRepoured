package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Nether Dragon - ArloTheEpic Created using Tabula 7.0.1
 */
public class ModelNetherDragonHeadSkeletal extends ModelBase {
	public ModelRenderer HeadNonSkeletal;
	public ModelRenderer Mouth_Top;
	public ModelRenderer Mouth_Bottom;
	public ModelRenderer Horn_Right_A;
	public ModelRenderer Horn_Left_A;
	public ModelRenderer topPlate;
	public ModelRenderer spine;
	public ModelRenderer Snout_Right;
	public ModelRenderer Snout_Left;
	public ModelRenderer teeth_top;
	public ModelRenderer teeth_bottom;
	public ModelRenderer Horn_Right_B;
	public ModelRenderer Horn_Right_C;
	public ModelRenderer Horn_Left_B;
	public ModelRenderer Horn_Left_C;
	public ModelRenderer skinRestAndEyes;
	public ModelRenderer spineConnector;
	public ModelRenderer spine2;
	public ModelRenderer ribLUpper;
	public ModelRenderer ribRUpper;
	public ModelRenderer spineConnector2;
	public ModelRenderer ribLUpper2;
	public ModelRenderer ribRUpper2;
	public ModelRenderer ribLMiddle2;
	public ModelRenderer ribLLower2;
	public ModelRenderer ribRMiddle2;
	public ModelRenderer ribRLower2;
	public ModelRenderer ribLMiddle;
	public ModelRenderer ribLLower;
	public ModelRenderer ribRMiddle;
	public ModelRenderer ribRLower;

	public ModelNetherDragonHeadSkeletal() {
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.ribLUpper = new ModelRenderer(this, 100, 20);
		this.ribLUpper.mirror = true;
		this.ribLUpper.setRotationPoint(1.0F, -0.2F, 0.0F);
		this.ribLUpper.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(ribLUpper, -1.5707963267948966F, -2.744530248761083F, 1.5707963267948966F);
		this.ribLMiddle2 = new ModelRenderer(this, 100, 26);
		this.ribLMiddle2.mirror = true;
		this.ribLMiddle2.setRotationPoint(1.0F, 4.0F, 1.0F);
		this.ribLMiddle2.addBox(-1.6F, -0.9F, -1.0F, 2, 5, 2, 0.0F);
		this.setRotateAngle(ribLMiddle2, 0.0F, 0.0F, 1.1780972450961724F);
		this.ribRLower = new ModelRenderer(this, 100, 33);
		this.ribRLower.mirror = true;
		this.ribRLower.setRotationPoint(0.1F, 3.1F, 0.0F);
		this.ribRLower.addBox(-1.0F, 0.1F, -1.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(ribRLower, 0.0F, 0.0F, 1.1780972450961724F);
		this.skinRestAndEyes = new ModelRenderer(this, 0, 31);
		this.skinRestAndEyes.setRotationPoint(-9.0F, 2.0F, 0.0F);
		this.skinRestAndEyes.addBox(0.0F, 0.0F, 0.0F, 9, 7, 10, 0.0F);
		this.Snout_Right = new ModelRenderer(this, 0, 0);
		this.Snout_Right.setRotationPoint(14.5F, -1.0F, -4.0F);
		this.Snout_Right.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		this.Horn_Left_B = new ModelRenderer(this, 88, 7);
		this.Horn_Left_B.setRotationPoint(3.1F, -2.8F, 0.5F);
		this.Horn_Left_B.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(Horn_Left_B, 0.0F, 0.0F, 0.8196066167365371F);
		this.Horn_Left_A = new ModelRenderer(this, 88, 0);
		this.Horn_Left_A.setRotationPoint(-9.0F, -2.8F, 9.9F);
		this.Horn_Left_A.addBox(0.0F, 0.0F, 0.0F, 3, 4, 3, 0.0F);
		this.setRotateAngle(Horn_Left_A, -0.5009094953223726F, 0.3518583772020568F, -0.13439035240356337F);
		this.Horn_Right_B = new ModelRenderer(this, 88, 7);
		this.Horn_Right_B.setRotationPoint(3.1F, -2.8F, 0.5F);
		this.Horn_Right_B.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(Horn_Right_B, 0.0F, 0.0F, 0.8196066167365371F);
		this.ribRUpper = new ModelRenderer(this, 100, 20);
		this.ribRUpper.mirror = true;
		this.ribRUpper.setRotationPoint(-1.0F, -0.2F, 0.0F);
		this.ribRUpper.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(ribRUpper, 1.5707963267948966F, 2.743657584135086F, 1.5707963267948966F);
		this.spineConnector2 = new ModelRenderer(this, 100, 7);
		this.spineConnector2.setRotationPoint(-3.0F, -0.5F, 0.0F);
		this.spineConnector2.addBox(-1.0F, -1.5F, -1.5F, 2, 3, 3, 0.0F);
		this.Horn_Right_A = new ModelRenderer(this, 88, 0);
		this.Horn_Right_A.setRotationPoint(-7.9F, -1.8F, -2.4F);
		this.Horn_Right_A.addBox(0.0F, 0.0F, 0.0F, 3, 4, 3, 0.0F);
		this.setRotateAngle(Horn_Right_A, 0.5009094953223726F, -0.3518583772020568F, -0.13439035240356337F);
		this.Horn_Left_C = new ModelRenderer(this, 88, 13);
		this.Horn_Left_C.setRotationPoint(2.5F, -2.3F, 0.5F);
		this.Horn_Left_C.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotateAngle(Horn_Left_C, 0.0F, 0.0F, 0.7740535232594852F);
		this.spine2 = new ModelRenderer(this, 100, 13);
		this.spine2.setRotationPoint(-6.0F, 0.0F, 0.0F);
		this.spine2.addBox(-2.0F, -1.5F, -2.0F, 4, 3, 4, 0.0F);
		this.ribRUpper2 = new ModelRenderer(this, 100, 20);
		this.ribRUpper2.mirror = true;
		this.ribRUpper2.setRotationPoint(-1.0F, 0.0F, 0.0F);
		this.ribRUpper2.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(ribRUpper2, 1.5707963267948966F, 2.743657584135086F, 1.5707963267948966F);
		this.Mouth_Top = new ModelRenderer(this, 38, 0);
		this.Mouth_Top.setRotationPoint(9.0F, 1.0F, 4.5F);
		this.Mouth_Top.addBox(-0.5F, 0.0F, -4.0F, 16, 2, 9, 0.0F);
		this.spine = new ModelRenderer(this, 100, 0);
		this.spine.setRotationPoint(0.0F, 3.5F, 5.0F);
		this.spine.addBox(-2.0F, -1.5F, -2.0F, 2, 3, 4, 0.0F);
		this.spineConnector = new ModelRenderer(this, 100, 7);
		this.spineConnector.setRotationPoint(-3.0F, -0.5F, 0.0F);
		this.spineConnector.addBox(-1.0F, -1.5F, -1.5F, 2, 3, 3, 0.0F);
		this.ribLLower = new ModelRenderer(this, 100, 33);
		this.ribLLower.mirror = true;
		this.ribLLower.setRotationPoint(0.1F, 3.1F, 0.0F);
		this.ribLLower.addBox(-1.0F, 0.1F, -1.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(ribLLower, 0.0F, 0.0F, 1.1780972450961724F);
		this.ribRMiddle2 = new ModelRenderer(this, 100, 26);
		this.ribRMiddle2.mirror = true;
		this.ribRMiddle2.setRotationPoint(1.0F, 4.0F, 1.0F);
		this.ribRMiddle2.addBox(-1.6F, -0.9F, -1.0F, 2, 5, 2, 0.0F);
		this.setRotateAngle(ribRMiddle2, 0.0F, 0.0F, 1.1780972450961724F);
		this.ribLUpper2 = new ModelRenderer(this, 100, 20);
		this.ribLUpper2.mirror = true;
		this.ribLUpper2.setRotationPoint(1.0F, -0.2F, 0.0F);
		this.ribLUpper2.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(ribLUpper2, -1.5707963267948966F, -2.743657584135086F, 1.5707963267948966F);
		this.ribRLower2 = new ModelRenderer(this, 100, 33);
		this.ribRLower2.mirror = true;
		this.ribRLower2.setRotationPoint(0.1F, 3.1F, 0.0F);
		this.ribRLower2.addBox(-1.0F, 0.1F, -1.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(ribRLower2, 0.0F, 0.0F, 1.1780972450961724F);
		this.Horn_Right_C = new ModelRenderer(this, 88, 13);
		this.Horn_Right_C.setRotationPoint(2.5F, -2.3F, 0.5F);
		this.Horn_Right_C.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotateAngle(Horn_Right_C, 0.0F, 0.0F, 0.7740535232594852F);
		this.HeadNonSkeletal = new ModelRenderer(this, 0, 0);
		this.HeadNonSkeletal.setRotationPoint(-5.0F, 7.5F, -4.0F);
		this.HeadNonSkeletal.addBox(0.0F, 0.0F, 0.0F, 9, 9, 10, 0.0F);
		this.setRotateAngle(HeadNonSkeletal, 0.0F, 1.5707963267948966F, 0.0F);
		this.teeth_top = new ModelRenderer(this, 38, 22);
		this.teeth_top.setRotationPoint(-0.5F, 2.0F, -3.5F);
		this.teeth_top.addBox(0.0F, 0.0F, 0.0F, 15, 2, 8, 0.0F);
		this.ribLMiddle = new ModelRenderer(this, 100, 26);
		this.ribLMiddle.mirror = true;
		this.ribLMiddle.setRotationPoint(1.0F, 4.0F, 1.0F);
		this.ribLMiddle.addBox(-1.6F, -0.9F, -1.0F, 2, 5, 2, 0.0F);
		this.setRotateAngle(ribLMiddle, 0.0F, 0.0F, 1.1780972450961724F);
		this.Mouth_Bottom = new ModelRenderer(this, 38, 11);
		this.Mouth_Bottom.setRotationPoint(9.0F, 5.0F, 4.5F);
		this.Mouth_Bottom.addBox(-0.5F, 0.0F, -4.0F, 16, 2, 9, 0.0F);
		this.ribRMiddle = new ModelRenderer(this, 100, 26);
		this.ribRMiddle.mirror = true;
		this.ribRMiddle.setRotationPoint(1.0F, 4.0F, 1.0F);
		this.ribRMiddle.addBox(-1.6F, -0.9F, -1.0F, 2, 5, 2, 0.0F);
		this.setRotateAngle(ribRMiddle, 0.0F, 0.0F, 1.1780972450961724F);
		this.Snout_Left = new ModelRenderer(this, 0, 0);
		this.Snout_Left.setRotationPoint(14.5F, -1.0F, 4.0F);
		this.Snout_Left.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		this.topPlate = new ModelRenderer(this, 0, 19);
		this.topPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.topPlate.addBox(-9.0F, 0.0F, 0.0F, 9, 2, 10, 0.0F);
		this.ribLLower2 = new ModelRenderer(this, 100, 33);
		this.ribLLower2.mirror = true;
		this.ribLLower2.setRotationPoint(0.1F, 3.1F, 0.0F);
		this.ribLLower2.addBox(-1.0F, 0.1F, -1.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(ribLLower2, 0.0F, 0.0F, 1.1780972450961724F);
		this.teeth_bottom = new ModelRenderer(this, 38, 32);
		this.teeth_bottom.setRotationPoint(0.0F, -2.0F, -3.5F);
		this.teeth_bottom.addBox(0.0F, 0.0F, 0.0F, 15, 2, 8, 0.0F);
		this.spine.addChild(this.ribLUpper);
		this.ribLUpper2.addChild(this.ribLMiddle2);
		this.ribRMiddle.addChild(this.ribRLower);
		this.topPlate.addChild(this.skinRestAndEyes);
		this.Mouth_Top.addChild(this.Snout_Right);
		this.Horn_Left_A.addChild(this.Horn_Left_B);
		this.HeadNonSkeletal.addChild(this.Horn_Left_A);
		this.Horn_Right_A.addChild(this.Horn_Right_B);
		this.spine.addChild(this.ribRUpper);
		this.spine2.addChild(this.spineConnector2);
		this.HeadNonSkeletal.addChild(this.Horn_Right_A);
		this.Horn_Left_B.addChild(this.Horn_Left_C);
		this.spine.addChild(this.spine2);
		this.spine2.addChild(this.ribRUpper2);
		this.HeadNonSkeletal.addChild(this.Mouth_Top);
		this.HeadNonSkeletal.addChild(this.spine);
		this.spine.addChild(this.spineConnector);
		this.ribLMiddle.addChild(this.ribLLower);
		this.ribRUpper2.addChild(this.ribRMiddle2);
		this.spine2.addChild(this.ribLUpper2);
		this.ribRMiddle2.addChild(this.ribRLower2);
		this.Horn_Right_B.addChild(this.Horn_Right_C);
		this.Mouth_Top.addChild(this.teeth_top);
		this.ribLUpper.addChild(this.ribLMiddle);
		this.HeadNonSkeletal.addChild(this.Mouth_Bottom);
		this.ribRUpper.addChild(this.ribRMiddle);
		this.Mouth_Top.addChild(this.Snout_Left);
		this.HeadNonSkeletal.addChild(this.topPlate);
		this.ribLMiddle2.addChild(this.ribLLower2);
		this.Mouth_Bottom.addChild(this.teeth_bottom);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.HeadNonSkeletal.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		EntityCQRNetherDragon dragon = (EntityCQRNetherDragon) entityIn;
		if (dragon.isMouthOpen()) {
			float angle = new Float(Math.toRadians(13));
			setRotateAngle(this.Mouth_Bottom, 0, 0, angle * 2);
			setRotateAngle(this.Mouth_Top, 0, 0, -angle);
		} else {
			setRotateAngle(this.Mouth_Bottom, 0, 0, 0);
			setRotateAngle(this.Mouth_Top, 0, 0, 0);
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
