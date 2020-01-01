package com.teamcqr.chocolatequestrepoured.client.models.entities;

import java.util.HashMap;

import mcalibrary.MCAVersionChecker;
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
		MCAVersionChecker.checkForLibraryVersion(this.getClass(), this.MCA_MIN_REQUESTED_VERSION);

		this.textureWidth = 64;
		this.textureHeight = 64;

		this.bodyMain = new MCAModelRenderer(this, "bodyMain", 0, 0);
		this.bodyMain.mirror = false;
		this.bodyMain.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 6);
		this.bodyMain.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		this.bodyMain.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		this.bodyMain.setTextureSize(64, 64);
		this.parts.put(this.bodyMain.boxName, this.bodyMain);

		this.head = new MCAModelRenderer(this, "head", 0, 10);
		this.head.mirror = false;
		this.head.addBox(-1.5F, -1.5F, 6.0F, 3, 3, 3);
		this.head.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		this.head.setTextureSize(64, 64);
		this.parts.put(this.head.boxName, this.head);
		this.bodyMain.addChild(this.head);

		this.wingL = new MCAModelRenderer(this, "wingL", 0, 19);
		this.wingL.mirror = false;
		this.wingL.addBox(0.5F, 1.0F, 1.5F, 8, 0, 4);
		this.wingL.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		this.wingL.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.1305262F, 0.0F, 0.9914449F)).transpose());
		this.wingL.setTextureSize(64, 64);
		this.parts.put(this.wingL.boxName, this.wingL);
		this.bodyMain.addChild(this.wingL);

		this.wingR = new MCAModelRenderer(this, "wingR", 0, 23);
		this.wingR.mirror = false;
		this.wingR.addBox(0.5F, 1.0F, -5.5F, 8, 0, 4);
		this.wingR.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		this.wingR.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.9914448F, 0.0F, 0.13052624F)).transpose());
		this.wingR.setTextureSize(64, 64);
		this.parts.put(this.wingR.boxName, this.wingR);
		this.bodyMain.addChild(this.wingR);

		this.body2 = new MCAModelRenderer(this, "body2", 29, 9);
		this.body2.mirror = false;
		this.body2.addBox(-1.5F, -1.5F, -3.25F, 3, 3, 4);
		this.body2.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		this.body2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.19936793F, 0.0F, 0.0F, 0.9799247F)).transpose());
		this.body2.setTextureSize(64, 64);
		this.parts.put(this.body2.boxName, this.body2);
		this.bodyMain.addChild(this.body2);

		this.sting1 = new MCAModelRenderer(this, "sting1", 15, 10);
		this.sting1.mirror = false;
		this.sting1.addBox(-1.0F, 1.0F, -5.0F, 2, 2, 4);
		this.sting1.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		this.sting1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.38268346F, 0.0F, 0.0F, 0.9238795F)).transpose());
		this.sting1.setTextureSize(64, 64);
		this.parts.put(this.sting1.boxName, this.sting1);
		this.body2.addChild(this.sting1);

	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		/*
		 * EntityCQRBee entity = (EntityCQRBee) par1Entity; //TODO: Implement WASPS first AnimationHandler.performAnimationInModel(parts, entity);
		 */

		// Render every non-child part
		this.bodyMain.render(par7);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
	}

	public MCAModelRenderer getModelRendererFromName(String name) {
		return this.parts.get(name);
	}
}