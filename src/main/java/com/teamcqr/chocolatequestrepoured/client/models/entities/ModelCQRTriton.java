package com.teamcqr.chocolatequestrepoured.client.models.entities;

import java.util.HashMap;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRTriton;

import mcalibrary.MCAVersionChecker;
import mcalibrary.animation.AnimationHandler;
import mcalibrary.client.MCAModelRenderer;
import mcalibrary.math.Matrix4f;
import mcalibrary.math.Quaternion;
import net.minecraft.entity.Entity;

public class ModelCQRTriton extends ModelCQRBiped {
	public final int MCA_MIN_REQUESTED_VERSION = 5;
	public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

	/*MCAModelRenderer head;
	MCAModelRenderer body;
	MCAModelRenderer rightarm;
	MCAModelRenderer leftarm;*/
	MCAModelRenderer mouthTentacle2;
	MCAModelRenderer mouthTentacle1;
	MCAModelRenderer mouthTentacle3;
	MCAModelRenderer mouthTentacle4;
	MCAModelRenderer tail1;
	MCAModelRenderer tail2;
	MCAModelRenderer tail3;
	MCAModelRenderer tail4;
	MCAModelRenderer tail5;
	MCAModelRenderer tail6;
	
	
	public ModelCQRTriton(float modelSize) {
		//super(modelSize, false);
		this(modelSize, 0, 64, 64);
	}

	public ModelCQRTriton(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
		super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn, false);
		MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

		textureWidth = textureWidthIn;
		textureHeight = textureHeightIn;

		bipedHead = new MCAModelRenderer(this, "head", 0, 0);
		bipedHead.mirror = false;
		bipedHead.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8, modelSize);
		((MCAModelRenderer) bipedHead).setInitialRotationPoint(0.0F, -4.0F, 2.0F);
		((MCAModelRenderer) bipedHead).setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		bipedHead.setTextureSize(64, 64);
		//this.bipedHead = head;
		parts.put(bipedHead.boxName, (MCAModelRenderer) bipedHead);

		bipedBody = new MCAModelRenderer(this, "body", 0, 16);
		bipedBody.mirror = false;
		bipedBody.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, modelSize);
		((MCAModelRenderer) bipedBody).setInitialRotationPoint(0.0F, -4.0F, 2.0F);
		((MCAModelRenderer) bipedBody).setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		bipedBody.setTextureSize(64, 64);
		//this.bipedBody = body;
		parts.put(bipedBody.boxName, (MCAModelRenderer) bipedBody);

		bipedRightArm = new MCAModelRenderer(this, "rightarm", 40, 16);
		bipedRightArm.mirror = false;
		bipedRightArm.addBox(-3.0F, -10.0F, -2.0F, 4, 12, 4, modelSize);
		((MCAModelRenderer) bipedRightArm).setInitialRotationPoint(-5.0F, -6.0F, 2.0F);
		((MCAModelRenderer) bipedRightArm).setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		bipedRightArm.setTextureSize(64, 64);
		//this.bipedRightArm = rightarm;
		parts.put(bipedRightArm.boxName, (MCAModelRenderer) bipedRightArm);

		bipedLeftArm = new MCAModelRenderer(this, "leftarm", 24, 16);
		bipedLeftArm.mirror = false;
		bipedLeftArm.addBox(-1.0F, -10.0F, -2.0F, 4, 12, 4, modelSize);
		((MCAModelRenderer) bipedLeftArm).setInitialRotationPoint(5.0F, -6.0F, 2.0F);
		((MCAModelRenderer) bipedLeftArm).setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		bipedLeftArm.setTextureSize(64, 64);
		//this.bipedLeftArm = leftarm;
		parts.put(bipedLeftArm.boxName, (MCAModelRenderer) bipedLeftArm);

		mouthTentacle2 = new MCAModelRenderer(this, "mouthTentacle2", 32, 0);
		mouthTentacle2.mirror = false;
		mouthTentacle2.addBox(0.0F, -4.0F, 0.0F, 1, 4, 1, modelSize);
		mouthTentacle2.setInitialRotationPoint(-1.0F, 1.5F, 3.0F);
		mouthTentacle2.setInitialRotationMatrix(
				new Matrix4f().set(new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F)).transpose());
		mouthTentacle2.setTextureSize(64, 64);
		parts.put(mouthTentacle2.boxName, mouthTentacle2);
		bipedHead.addChild(mouthTentacle2);

		mouthTentacle1 = new MCAModelRenderer(this, "mouthTentacle1", 32, 0);
		mouthTentacle1.mirror = false;
		mouthTentacle1.addBox(0.0F, -4.0F, 0.0F, 1, 4, 1, modelSize);
		mouthTentacle1.setInitialRotationPoint(-2.0F, 1.5F, 3.0F);
		mouthTentacle1.setInitialRotationMatrix(
				new Matrix4f().set(new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F)).transpose());
		mouthTentacle1.setTextureSize(64, 64);
		parts.put(mouthTentacle1.boxName, mouthTentacle1);
		bipedHead.addChild(mouthTentacle1);

		mouthTentacle3 = new MCAModelRenderer(this, "mouthTentacle3", 32, 0);
		mouthTentacle3.mirror = false;
		mouthTentacle3.addBox(0.0F, -4.0F, 0.0F, 1, 4, 1, modelSize);
		mouthTentacle3.setInitialRotationPoint(0.0F, 1.5F, 3.0F);
		mouthTentacle3.setInitialRotationMatrix(
				new Matrix4f().set(new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F)).transpose());
		mouthTentacle3.setTextureSize(64, 64);
		parts.put(mouthTentacle3.boxName, mouthTentacle3);
		bipedHead.addChild(mouthTentacle3);

		mouthTentacle4 = new MCAModelRenderer(this, "mouthTentacle4", 32, 0);
		mouthTentacle4.mirror = false;
		mouthTentacle4.addBox(0.0F, -4.0F, 0.0F, 1, 4, 1, modelSize);
		mouthTentacle4.setInitialRotationPoint(1.0F, 1.5F, 3.0F);
		mouthTentacle4.setInitialRotationMatrix(
				new Matrix4f().set(new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F)).transpose());
		mouthTentacle4.setTextureSize(64, 64);
		parts.put(mouthTentacle4.boxName, mouthTentacle4);
		bipedHead.addChild(mouthTentacle4);

		tail1 = new MCAModelRenderer(this, "tail1", 0, 56);
		tail1.mirror = false;
		tail1.addBox(-4.0F, 0.0F, 0.0F, 8, 4, 4, modelSize);
		tail1.setInitialRotationPoint(0.0F, -16.0F, -2.5F);
		tail1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tail1.setTextureSize(64, 64);
		parts.put(tail1.boxName, tail1);
		bipedBody.addChild(tail1);

		tail2 = new MCAModelRenderer(this, "tail2", 0, 48);
		tail2.mirror = false;
		tail2.addBox(-3.0F, 0.0F, 0.0F, 6, 4, 4, modelSize);
		tail2.setInitialRotationPoint(0.0F, -4.0F, -1.0F);
		tail2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tail2.setTextureSize(64, 64);
		parts.put(tail2.boxName, tail2);
		tail1.addChild(tail2);

		tail3 = new MCAModelRenderer(this, "tail3", 24, 56);
		tail3.mirror = false;
		tail3.addBox(-2.5F, 0.0F, 0.0F, 5, 4, 4, modelSize);
		tail3.setInitialRotationPoint(0.0F, -4.0F, -1.0F);
		tail3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tail3.setTextureSize(64, 64);
		parts.put(tail3.boxName, tail3);
		tail2.addChild(tail3);

		tail4 = new MCAModelRenderer(this, "tail4", 24, 48);
		tail4.mirror = false;
		tail4.addBox(-2.0F, 0.0F, 0.0F, 4, 4, 4, modelSize);
		tail4.setInitialRotationPoint(0.0F, -4.0F, -2.0F);
		tail4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tail4.setTextureSize(64, 64);
		parts.put(tail4.boxName, tail4);
		tail3.addChild(tail4);

		tail5 = new MCAModelRenderer(this, "tail5", 42, 57);
		tail5.mirror = false;
		tail5.addBox(-1.5F, 0.0F, -4.0F, 3, 3, 4, modelSize);
		tail5.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		tail5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tail5.setTextureSize(64, 64);
		parts.put(tail5.boxName, tail5);
		tail4.addChild(tail5);

		tail6 = new MCAModelRenderer(this, "tail6", 42, 57);
		tail6.mirror = false;
		tail6.addBox(-1.5F, 0.0F, -4.0F, 3, 3, 4, modelSize);
		tail6.setInitialRotationPoint(0.0F, 0.0F, -4.0F);
		tail6.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tail6.setTextureSize(64, 64);
		parts.put(tail6.boxName, tail6);
		tail5.addChild(tail6);

		
		this.bipedLeftLeg.showModel = false;
		this.bipedRightLeg.showModel = false;
		this.bipedLeftLegwear.showModel = false;
		this.bipedRightLegwear.showModel = false;
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		EntityCQRTriton entity = (EntityCQRTriton) par1Entity;

		AnimationHandler.performAnimationInModel(parts, entity);

//Render every non-child part
		bipedHead.render(par7);
		bipedBody.render(par7);
		bipedRightArm.render(par7);
		bipedLeftArm.render(par7);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
	}
	
	public MCAModelRenderer getModelRendererFromName(String name) {
		return parts.get(name);
	}
}