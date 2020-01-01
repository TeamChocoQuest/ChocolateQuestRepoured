package com.teamcqr.chocolatequestrepoured.client.models.entities;

import java.util.HashMap;

import mcalibrary.MCAVersionChecker;
import mcalibrary.animation.AnimationHandler;
import mcalibrary.client.MCAModelRenderer;
import mcalibrary.math.Matrix4f;
import mcalibrary.math.Quaternion;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelCQRWasp extends ModelBase {
	public final int MCA_MIN_REQUESTED_VERSION = 5;
	public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

	MCAModelRenderer bodyMain;
	MCAModelRenderer head;
	MCAModelRenderer wingL;
	MCAModelRenderer wingR;
	MCAModelRenderer body2;
	MCAModelRenderer sting1;

	public ModelCQRWasp() {
		MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

		textureWidth = 64;
		textureHeight = 64;

		bodyMain = new MCAModelRenderer(this, "bodyMain", 0, 0);
		bodyMain.mirror = false;
		bodyMain.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 6);
		bodyMain.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		bodyMain.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		bodyMain.setTextureSize(64, 64);
		parts.put(bodyMain.boxName, bodyMain);

		head = new MCAModelRenderer(this, "head", 0, 10);
		head.mirror = false;
		head.addBox(-1.5F, -1.5F, 6.0F, 3, 3, 3);
		head.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		head.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		head.setTextureSize(64, 64);
		parts.put(head.boxName, head);
		bodyMain.addChild(head);

		wingL = new MCAModelRenderer(this, "wingL", 0, 19);
		wingL.mirror = false;
		wingL.addBox(0.5F, 1.0F, 1.5F, 8, 0, 4);
		wingL.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		wingL.setInitialRotationMatrix(
				new Matrix4f().set(new Quaternion(0.0F, 0.1305262F, 0.0F, 0.9914449F)).transpose());
		wingL.setTextureSize(64, 64);
		parts.put(wingL.boxName, wingL);
		bodyMain.addChild(wingL);

		wingR = new MCAModelRenderer(this, "wingR", 0, 23);
		wingR.mirror = false;
		wingR.addBox(0.5F, 1.0F, -5.5F, 8, 0, 4);
		wingR.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		wingR.setInitialRotationMatrix(
				new Matrix4f().set(new Quaternion(0.0F, 0.9914448F, 0.0F, 0.13052624F)).transpose());
		wingR.setTextureSize(64, 64);
		parts.put(wingR.boxName, wingR);
		bodyMain.addChild(wingR);

		body2 = new MCAModelRenderer(this, "body2", 29, 9);
		body2.mirror = false;
		body2.addBox(-1.5F, -1.5F, -3.25F, 3, 3, 4);
		body2.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		body2.setInitialRotationMatrix(
				new Matrix4f().set(new Quaternion(-0.19936793F, 0.0F, 0.0F, 0.9799247F)).transpose());
		body2.setTextureSize(64, 64);
		parts.put(body2.boxName, body2);
		bodyMain.addChild(body2);

		sting1 = new MCAModelRenderer(this, "sting1", 15, 10);
		sting1.mirror = false;
		sting1.addBox(-1.0F, 1.0F, -5.0F, 2, 2, 4);
		sting1.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		sting1.setInitialRotationMatrix(
				new Matrix4f().set(new Quaternion(-0.38268346F, 0.0F, 0.0F, 0.9238795F)).transpose());
		sting1.setTextureSize(64, 64);
		parts.put(sting1.boxName, sting1);
		body2.addChild(sting1);

	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		/*EntityCQRBee entity = (EntityCQRBee) par1Entity;
	//TODO: Implement WASPS first
		AnimationHandler.performAnimationInModel(parts, entity);*/

//Render every non-child part
		bodyMain.render(par7);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6,
			Entity par7Entity) {
	}

	public MCAModelRenderer getModelRendererFromName(String name) {
		return parts.get(name);
	}
}