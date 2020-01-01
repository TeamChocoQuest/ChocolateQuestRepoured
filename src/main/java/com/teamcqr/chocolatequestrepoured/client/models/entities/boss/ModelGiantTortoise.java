package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;

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
	public ModelRenderer jaw;

	private ModelRenderer[] subParts = new ModelRenderer[14];
	private ModelRenderer[] legJoints = new ModelRenderer[4];
	private ModelRenderer[] knees = new ModelRenderer[4];
	private ModelRenderer[] feet = new ModelRenderer[4];

	public ModelGiantTortoise() {
		this.textureWidth = 192;
		this.textureHeight = 192;

		this.mainPart = new ModelRenderer(this, 0, 0);
		this.mainPart.setRotationPoint(0.0F, 11.0F, 0.0F);
		this.mainPart.addBox(-16.0F, -8.0F, -16.0F, 32, 14, 32, 0.0F);

		this.top = new ModelRenderer(this, 0, 80);
		this.top.setRotationPoint(0.0F, -10.0F, 0.0F);
		this.top.addBox(-14.0F, 0.0F, -14.0F, 28, 2, 28, 0.0F);

		this.belly = new ModelRenderer(this, 0, 46);
		this.belly.setRotationPoint(-10.0F, 2.0F, -14.0F);
		this.belly.addBox(0.0F, 4.0F, 0.0F, 20, 2, 28, 0.0F);

		this.neck = new ModelRenderer(this, 50, 120);
		this.neck.setRotationPoint(0.0F, 0.0F, -17.0F);
		this.neck.addBox(-5.0F, -5.0F, -1.0F, 10, 10, 2, 0.0F);

		this.top1 = new ModelRenderer(this, 0, 120);
		this.top1.setRotationPoint(7.0F, -2.0F, 7.0F);
		this.top1.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);

		this.top2 = new ModelRenderer(this, 0, 120);
		this.top2.setRotationPoint(7.0F, -2.0F, -7.0F);
		this.top2.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);

		this.top3 = new ModelRenderer(this, 0, 120);
		this.top3.setRotationPoint(-7.0F, -2.0F, -7.0F);
		this.top3.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);

		this.top4 = new ModelRenderer(this, 0, 120);
		this.top4.setRotationPoint(-7.0F, -2.0F, 7.0F);
		this.top4.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);

		// Sub Parts
		this.head = new ModelRenderer(this, 140, 60);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.5F, -4.0F, -9.0F, 9, 7, 9, 0.0F);
		this.subParts[0] = this.head;

		this.jaw = new ModelRenderer(this, 140, 80);
		this.jaw.setRotationPoint(0.0F, 3.5F, -1.0F);
		this.jaw.addBox(-4.5F, -0.5F, -8.0F, 9, 1, 8, 0.0F);
		this.subParts[1] = this.jaw;

		// this.setRotateAngle(jaw, 0.4363323129985824F, 0.0F, 0.0F);
		// leg Front Left
		this.legJointFL = new ModelRenderer(this, 0, 140);
		this.legJointFL.setRotationPoint(13.0F, 3.5F, -13.0F);
		this.legJointFL.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
		this.setRotateAngle(this.legJointFL, 0.0F, -0.7853981633974483F, 0.0F);
		this.subParts[2] = this.legJointFL;
		this.legJoints[0] = this.legJointFL;

		this.legFL = new ModelRenderer(this, 140, 0);
		this.legFL.setRotationPoint(0.0F, 1.5F, 1.0F);
		this.legFL.addBox(-4.0F, 0.0F, -8.0F, 8, 8, 8, 0.0F);
		this.setRotateAngle(this.legFL, -0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[3] = this.legFL;
		this.knees[0] = this.legFL;

		this.footFL = new ModelRenderer(this, 140, 25);
		this.footFL.setRotationPoint(0.0F, 3.0F, -2.7F);
		this.footFL.addBox(-3.5F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
		this.setRotateAngle(this.footFL, 0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[4] = this.footFL;
		this.feet[0] = this.footFL;

		// Leg Front Right
		this.legJointFR = new ModelRenderer(this, 0, 140);
		this.legJointFR.setRotationPoint(-13.0F, 3.5F, -13.0F);
		this.legJointFR.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
		this.setRotateAngle(this.legJointFR, 0.0F, 0.7853981633974483F, 0.0F);
		this.subParts[5] = this.legJointFR;
		this.legJoints[1] = this.legJointFR;

		this.legFR = new ModelRenderer(this, 140, 0);
		this.legFR.setRotationPoint(0.0F, 1.5F, 1.0F);
		this.legFR.addBox(-4.0F, 0.0F, -8.0F, 8, 8, 8, 0.0F);
		this.setRotateAngle(this.legFR, -0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[6] = this.legFR;
		this.knees[1] = this.legFR;

		this.footFR = new ModelRenderer(this, 140, 25);
		this.footFR.setRotationPoint(0.0F, 3.0F, -2.7F);
		this.footFR.addBox(-3.5F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
		this.setRotateAngle(this.footFR, 0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[7] = this.footFR;
		this.feet[1] = this.footFR;

		// Leg Back Right
		this.legJointBR = new ModelRenderer(this, 0, 140);
		this.legJointBR.setRotationPoint(-13.0F, 3.5F, 13.0F);
		this.legJointBR.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
		this.setRotateAngle(this.legJointBR, 0.0F, 2.356194490192345F, 0.0F);
		this.subParts[8] = this.legJointBR;
		this.legJoints[2] = this.legJointBR;

		this.legBR = new ModelRenderer(this, 140, 0);
		this.legBR.setRotationPoint(0.0F, 1.5F, 1.0F);
		this.legBR.addBox(-4.0F, 0.0F, -8.0F, 8, 8, 8, 0.0F);
		this.setRotateAngle(this.legBR, -0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[9] = this.legBR;
		this.knees[2] = this.legBR;

		this.footBR = new ModelRenderer(this, 140, 25);
		this.footBR.setRotationPoint(0.0F, 3.0F, -2.7F);
		this.footBR.addBox(-3.5F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
		this.setRotateAngle(this.footBR, 0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[10] = this.footBR;
		this.feet[2] = this.footBR;

		// Leg Back Left
		this.legJointBL = new ModelRenderer(this, 0, 140);
		this.legJointBL.setRotationPoint(13.0F, 3.5F, 13.0F);
		this.legJointBL.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
		this.setRotateAngle(this.legJointBL, 0.0F, -2.356194490192345F, 0.0F);
		this.subParts[11] = this.legJointBL;
		this.legJoints[3] = this.legJointBL;

		this.legBL = new ModelRenderer(this, 140, 0);
		this.legBL.setRotationPoint(0.0F, 1.5F, 1.0F);
		this.legBL.addBox(-4.0F, 0.0F, -8.0F, 8, 8, 8, 0.0F);
		this.setRotateAngle(this.legBL, -0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[12] = this.legBL;
		this.knees[3] = this.legBL;

		this.footBL = new ModelRenderer(this, 140, 25);
		this.footBL.setRotationPoint(0.0F, 3.0F, -2.7F);
		this.footBL.addBox(-3.5F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
		this.setRotateAngle(this.footBL, 0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[13] = this.footBL;
		this.feet[3] = this.footBL;

		// Children relation
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
		this.head.addChild(this.jaw);
		this.legFL.addChild(this.footFL);
		this.mainPart.addChild(this.top);
		this.mainPart.addChild(this.legJointBL);
		this.top.addChild(this.top1);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		boolean renderParts = true;
		EntityCQRGiantTortoise ent = (EntityCQRGiantTortoise) entity;

		if (ent.shouldModelReset()) {
			this.resetParts();
			ent.setAnimationChanged(false);
		}

		switch (ent.getCurrentAnimation()) {
		// DONE: Change offset of model
		case MOVE_PARTS_IN:
			if (ent.getAnimationProgress() >= 150) {
				ent.setAnimationProgress(0);
			}
			if (ent.getAnimationProgress() >= 100) {
				renderParts = false;
				if (ent.getAnimationProgress() > 110) {
					this.mainPart.offsetY = 0.5F;
				}
				if (ent.getAnimationProgress() <= 110) {
					this.mainPart.offsetY = 0.05F * (ent.getAnimationProgress() - 100) + 0.15F;

				} else if (ent.getAnimationProgress() <= 111) {
					// DONE: Spawn particles that indicate that the shell landed
					entity.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entity.posX, entity.posY, entity.posZ, 0.125, 0.125, 0.125);
					entity.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entity.posX, entity.posY, entity.posZ, -0.125, 0.125, 0.125);
					entity.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entity.posX, entity.posY, entity.posZ, 0.125, -0.125, 0.125);
					entity.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entity.posX, entity.posY, entity.posZ, -0.125, -0.125, 0.125);
				}

			} else {
				this.mainPart.offsetY = 0;
				if (ent.getAnimationProgress() <= 60) {
					this.head.offsetZ = ent.getAnimationProgress() * 0.0125F;

					this.mainPart.offsetY = 0.0025F * (ent.getAnimationProgress());

					for (ModelRenderer legJoint : this.legJoints) {
						legJoint.offsetY = -0.0025F * (ent.getAnimationProgress());
					}
				} else {
					this.mainPart.offsetY = 0.15F;
					for (ModelRenderer legJoint : this.legJoints) {
						legJoint.offsetY = -0.00125F * ent.getAnimationProgress();
					}
					float offsetXZ = 0.0125F * (ent.getAnimationProgress() - 59);

					this.legJointFL.offsetX = -offsetXZ;
					this.legJointFL.offsetZ = offsetXZ;

					this.legJointBL.offsetX = -offsetXZ;
					this.legJointBL.offsetZ = -offsetXZ;

					this.legJointFR.offsetX = offsetXZ;
					this.legJointFR.offsetZ = offsetXZ;

					this.legJointBR.offsetX = offsetXZ;
					this.legJointBR.offsetZ = -offsetXZ;
					// this.head.isHidden = true;
					// this.jaw.isHidden = true;
				}
			}
			ent.setAnimationProgress(ent.getAnimationProgress() + 1);
			break;
		case MOVE_PARTS_OUT:
			break;
		case HEALING:
			renderParts = false;
			this.mainPart.offsetY = 0.5F;
			break;
		case NONE:
			renderParts = false;
			this.mainPart.offsetY = 0.5F;
			break;
		case SPIN:
			renderParts = false;
			this.mainPart.offsetY = 0.5F;
			break;
		case SPIN_DOWN:
			renderParts = false;
			this.mainPart.offsetY = 0.5F;
			break;
		case SPIN_UP:
			renderParts = false;
			this.mainPart.offsetY = 0.5F;
			break;
		case WALKING:
			break;
		}

		for (ModelRenderer part : this.subParts) {
			part.showModel = renderParts;
		}
		this.mainPart.render(f5);
	}

	private void resetParts() {
		// TODO: Reset legs offset, head offset and rotations
		this.mainPart.offsetY = 0F;
		this.head.offsetZ = -9F;
		this.jaw.rotateAngleX = 0F;
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		boolean renderParts = true;
		EntityCQRGiantTortoise ent = (EntityCQRGiantTortoise) entityIn;
		switch (ent.getCurrentAnimation()) {
		case MOVE_PARTS_IN:
			break;
		case MOVE_PARTS_OUT:
			break;
		case SPIN:
			renderParts = false;
			// Spinning: Do this with OpenGL in render method, use limbSwingAmount for this
			this.mainPart.rotateAngleY = this.mainPart.rotateAngleY + 0.25F;
			break;
		case SPIN_DOWN:
			renderParts = false;

			if (ent.getAnimationProgress() >= 250) {
				// ent.setCurrentAnimation(ETortoiseAnimState.NONE);
				// ent.setAnimationProgress(0);
			} else {
				ent.setAnimationProgress(ent.getAnimationProgress() + 1);
				this.mainPart.rotateAngleY = this.mainPart.rotateAngleY + (((1F / 50F) * (250 - ent.getAnimationProgress())) / 20F);
			}
			break;
		case SPIN_UP:
			renderParts = false;

			if (ent.getAnimationProgress() >= 250) {
				// ent.setCurrentAnimation(ETortoiseAnimState.SPIN);
				// ent.setAnimationProgress(0);
				this.mainPart.rotateAngleY = this.mainPart.rotateAngleY + 0.25F;
			} else {
				ent.setAnimationProgress(ent.getAnimationProgress() + 1);
				this.mainPart.rotateAngleY = this.mainPart.rotateAngleY + (((1F / 50F) * ent.getAnimationProgress()) / 20F);
			}
			break;
		case WALKING:
			this.legJointFL.rotateAngleY = -0.7853981633974483F + (MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
			this.legJointFR.rotateAngleY = 0.7853981633974483F + -(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);

			this.legJointBL.rotateAngleY = -2.356194490192345F + -(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
			this.legJointBR.rotateAngleY = 2.356194490192345F + (MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
			break;
		case HEALING:
			renderParts = false;

			this.mainPart.rotateAngleZ = (float) Math.toRadians(120 * Math.cos(ageInTicks) / 8F);
			this.mainPart.rotateAngleY = (float) Math.toRadians(120 * Math.sin(ageInTicks) / 8F);

			break;
		case NONE:
			renderParts = false;
			break;
		default:
			break;
		}
		if (renderParts) {
			this.head.rotateAngleX = headPitch * 0.017453292F;
			this.head.rotateAngleY = netHeadYaw * 0.017453292F;
		}

		// DONE: Properly make leg animation
	}
}
