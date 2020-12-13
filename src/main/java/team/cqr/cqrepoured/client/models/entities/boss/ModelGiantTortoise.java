package team.cqr.cqrepoured.client.models.entities.boss;

import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise;

/**
 * CQRTurtleBossShell - DerToaster Created using Tabula 7.0.1
 */
public class ModelGiantTortoise extends AdvancedModelBase {
	public AdvancedModelRenderer mainPart;
	public AdvancedModelRenderer belly;
	public AdvancedModelRenderer top;
	public AdvancedModelRenderer neck;
	public AdvancedModelRenderer top1;
	public AdvancedModelRenderer top2;
	public AdvancedModelRenderer top3;
	public AdvancedModelRenderer top4;
	public AdvancedModelRenderer head;
	public AdvancedModelRenderer jaw;
	// Leg FL
	public AdvancedModelRenderer legJointFL;
	public AdvancedModelRenderer legFL;
	public AdvancedModelRenderer footFL;

	// Leg FR
	public AdvancedModelRenderer legJointFR;
	public AdvancedModelRenderer legFR;
	public AdvancedModelRenderer footFR;

	// Leg BL
	public AdvancedModelRenderer legJointBL;
	public AdvancedModelRenderer legBL;
	public AdvancedModelRenderer footBL;

	// Leg BR
	public AdvancedModelRenderer legJointBR;
	public AdvancedModelRenderer legBR;
	public AdvancedModelRenderer footBR;

	private AdvancedModelRenderer[] subParts = new AdvancedModelRenderer[14];
	private AdvancedModelRenderer[] legJoints = new AdvancedModelRenderer[4];
	private AdvancedModelRenderer[] knees = new AdvancedModelRenderer[4];
	private AdvancedModelRenderer[] feet = new AdvancedModelRenderer[4];

	private ModelAnimator animator;

	public ModelGiantTortoise() {
		this.textureWidth = 192;
		this.textureHeight = 192;

		this.animator = ModelAnimator.create();

		this.mainPart = new AdvancedModelRenderer(this, 0, 0);
		this.mainPart.setRotationPoint(0.0F, 11.0F, 0.0F);
		this.mainPart.addBox(-16.0F, -8.0F, -16.0F, 32, 14, 32, 0.0F);

		this.top = new AdvancedModelRenderer(this, 0, 80);
		this.top.setRotationPoint(0.0F, -10.0F, 0.0F);
		this.top.addBox(-14.0F, 0.0F, -14.0F, 28, 2, 28, 0.0F);

		this.belly = new AdvancedModelRenderer(this, 0, 46);
		this.belly.setRotationPoint(-10.0F, 2.0F, -14.0F);
		this.belly.addBox(0.0F, 4.0F, 0.0F, 20, 2, 28, 0.0F);

		this.neck = new AdvancedModelRenderer(this, 50, 120);
		this.neck.setRotationPoint(0.0F, 0.0F, -17.0F);
		this.neck.addBox(-5.0F, -5.0F, -1.0F, 10, 10, 2, 0.0F);

		this.top1 = new AdvancedModelRenderer(this, 0, 120);
		this.top1.setRotationPoint(7.0F, -2.0F, 7.0F);
		this.top1.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);

		this.top2 = new AdvancedModelRenderer(this, 0, 120);
		this.top2.setRotationPoint(7.0F, -2.0F, -7.0F);
		this.top2.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);

		this.top3 = new AdvancedModelRenderer(this, 0, 120);
		this.top3.setRotationPoint(-7.0F, -2.0F, -7.0F);
		this.top3.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);

		this.top4 = new AdvancedModelRenderer(this, 0, 120);
		this.top4.setRotationPoint(-7.0F, -2.0F, 7.0F);
		this.top4.addBox(-5.0F, 0.0F, -5.0F, 10, 2, 10, 0.0F);

		// Sub Parts
		this.head = new AdvancedModelRenderer(this, 140, 60);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.5F, -4.0F, -9.0F, 9, 7, 9, 0.0F);
		this.subParts[0] = this.head;

		this.jaw = new AdvancedModelRenderer(this, 140, 80);
		this.jaw.setRotationPoint(0.0F, 3.5F, -1.0F);
		this.jaw.addBox(-4.5F, -0.5F, -8.0F, 9, 1, 8, 0.0F);
		this.subParts[1] = this.jaw;

		// this.setRotateAngle(jaw, 0.4363323129985824F, 0.0F, 0.0F);
		// leg Front Left
		this.legJointFL = new AdvancedModelRenderer(this, 0, 140);
		this.legJointFL.setRotationPoint(13.0F, 3.5F, -13.0F);
		this.legJointFL.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
		this.setRotateAngle(this.legJointFL, 0.0F, -0.7853981633974483F, 0.0F);
		this.subParts[2] = this.legJointFL;
		this.legJoints[0] = this.legJointFL;

		this.legFL = new AdvancedModelRenderer(this, 140, 0);
		this.legFL.setRotationPoint(0.0F, -4.0F, -5.0F);
		this.legFL.addBox(-4.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.setRotateAngle(this.legFL, -0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[3] = this.legFL;
		this.knees[0] = this.legFL;

		this.footFL = new AdvancedModelRenderer(this, 140, 25);
		this.footFL.setRotationPoint(0.0F, 8.0F, 0.5F);
		this.footFL.addBox(-3.5F, 0.0F, 0.0F, 7, 8, 7, 0.0F);
		this.setRotateAngle(this.footFL, 0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[4] = this.footFL;
		this.feet[0] = this.footFL;

		// Leg Front Right
		this.legJointFR = new AdvancedModelRenderer(this, 0, 140);
		this.legJointFR.setRotationPoint(-13.0F, 3.5F, -13.0F);
		this.legJointFR.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
		this.setRotateAngle(this.legJointFR, 0.0F, 0.7853981633974483F, 0.0F);
		this.subParts[5] = this.legJointFR;
		this.legJoints[1] = this.legJointFR;

		this.legFR = new AdvancedModelRenderer(this, 140, 0);
		this.legFR.setRotationPoint(0.0F, -4.0F, -5.0F);
		this.legFR.addBox(-4.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.setRotateAngle(this.legFR, -0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[6] = this.legFR;
		this.knees[1] = this.legFR;

		this.footFR = new AdvancedModelRenderer(this, 140, 25);
		this.footFR.setRotationPoint(0.0F, 8.0F, 0.5F);
		this.footFR.addBox(-3.5F, 0.0F, 0.0F, 7, 8, 7, 0.0F);
		this.setRotateAngle(this.footFR, 0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[7] = this.footFR;
		this.feet[1] = this.footFR;

		// Leg Back Right
		this.legJointBR = new AdvancedModelRenderer(this, 0, 140);
		this.legJointBR.setRotationPoint(-13.0F, 3.5F, 13.0F);
		this.legJointBR.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
		this.setRotateAngle(this.legJointBR, 0.0F, 2.356194490192345F, 0.0F);
		this.subParts[8] = this.legJointBR;
		this.legJoints[2] = this.legJointBR;

		this.legBR = new AdvancedModelRenderer(this, 140, 0);
		this.legBR.setRotationPoint(0.0F, -4.0F, -5.0F);
		this.legBR.addBox(-4.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.setRotateAngle(this.legBR, -0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[9] = this.legBR;
		this.knees[2] = this.legBR;

		this.footBR = new AdvancedModelRenderer(this, 140, 25);
		this.footBR.setRotationPoint(0.0F, 8.0F, 0.5F);
		this.footBR.addBox(-3.5F, 0.0F, 0.0F, 7, 8, 7, 0.0F);
		this.setRotateAngle(this.footBR, 0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[10] = this.footBR;
		this.feet[2] = this.footBR;

		// Leg Back Left
		this.legJointBL = new AdvancedModelRenderer(this, 0, 140);
		this.legJointBL.setRotationPoint(13.0F, 3.5F, 13.0F);
		this.legJointBL.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
		this.setRotateAngle(this.legJointBL, 0.0F, -2.356194490192345F, 0.0F);
		this.subParts[11] = this.legJointBL;
		this.legJoints[3] = this.legJointBL;

		this.legBL = new AdvancedModelRenderer(this, 140, 0);
		this.legBL.setRotationPoint(0.0F, -4.0F, -5.0F);
		this.legBL.addBox(-4.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.setRotateAngle(this.legBL, -0.7853981633974483F, 0.0F, 0.0F);
		this.subParts[12] = this.legBL;
		this.knees[3] = this.legBL;

		this.footBL = new AdvancedModelRenderer(this, 140, 25);
		this.footBL.setRotationPoint(0.0F, 8.0F, 0.5F);
		this.footBL.addBox(-3.5F, 0.0F, 0.0F, 7, 8, 7, 0.0F);
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

		// LLib stuff
		this.updateDefaultPose();

		this.belly.scaleChildren = true;
		this.footBL.scaleChildren = true;
		this.footBR.scaleChildren = true;
		this.footFL.scaleChildren = true;
		this.footFR.scaleChildren = true;
		this.head.scaleChildren = true;
		this.jaw.scaleChildren = true;
		this.legBL.scaleChildren = true;
		this.legBR.scaleChildren = true;
		this.legFL.scaleChildren = true;
		this.legFR.scaleChildren = true;
		this.legJointBL.scaleChildren = true;
		this.legJointBR.scaleChildren = true;
		this.legJointFL.scaleChildren = true;
		this.legJointFR.scaleChildren = true;
		this.mainPart.scaleChildren = true;
		this.neck.scaleChildren = true;
		this.top.scaleChildren = true;
		this.top1.scaleChildren = true;
		this.top2.scaleChildren = true;
		this.top3.scaleChildren = true;
		this.top4.scaleChildren = true;
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		EntityCQRGiantTortoise turtle = (EntityCQRGiantTortoise) entity;
		this.animate(turtle, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		this.mainPart.render(scale);
	}

	public void animate(IAnimatedEntity iAnimated, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		EntityCQRGiantTortoise turtle = (EntityCQRGiantTortoise) iAnimated;

		this.animator.update(turtle);
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, turtle);

		// Animation code here

		/*
		 * Move in shell animation - DONE
		 */
		if (turtle.getAnimation() == EntityCQRGiantTortoise.ANIMATION_MOVE_LEGS_IN) {
			this.animator.setAnimation(EntityCQRGiantTortoise.ANIMATION_MOVE_LEGS_IN);
			// Make legs straight
			this.animator.startKeyframe(turtle.getAnimation().getDuration() / 2);
			for (int i = 0; i < this.legJoints.length; i++) {
				this.animator.rotate(this.knees[i], -(float) Math.toRadians(45), 0, 0);
				this.animator.rotate(this.feet[i], -(float) Math.toRadians(45), 0, 0);
				this.animator.move(this.legJoints[i], 0, -1, 0);
			}
			this.animator.move(this.mainPart, 0, 0.4F * 16, 0);
			this.animator.endKeyframe();
			// animator.setStaticKeyframe(1);
			// animator.setStaticKeyframe(turtle.getAnimation().getDuration() /2 -1);
			// Move legs in
			this.animator.startKeyframe(turtle.getAnimation().getDuration() / 2);
			// previous animation part so it looks properly
			for (int i = 0; i < this.legJoints.length; i++) {
				this.animator.rotate(this.knees[i], -(float) Math.toRadians(45), 0, 0);
				this.animator.rotate(this.feet[i], -(float) Math.toRadians(45), 0, 0);
				this.animator.move(this.legJoints[i], 0, -1, 0);
			}
			this.animator.move(this.mainPart, 0, 0.4F * 16, 0);
			// move thing works in pixels, 16 is equals to 1 block
			float offsetXZ = 16;
			this.animator.move(this.legJointFL, -offsetXZ, 0, offsetXZ);
			this.animator.move(this.legJointBL, -offsetXZ, 0, -offsetXZ);
			this.animator.move(this.legJointFR, offsetXZ, 0, offsetXZ);
			this.animator.move(this.legJointBR, offsetXZ, 0, -offsetXZ);
			this.animator.move(this.head, 0, 0, 0.75F * 16);
			this.animator.endKeyframe();
			// animator.setStaticKeyframe(5);
		}

		/*
		 * Move out of shell animation - DONE
		 */
		if (turtle.getAnimation() == EntityCQRGiantTortoise.ANIMATION_MOVE_LEGS_OUT) {
			float offsetXZ = 16;
			this.animator.setAnimation(EntityCQRGiantTortoise.ANIMATION_MOVE_LEGS_OUT);
			this.animator.startKeyframe(0);
			this.animator.move(this.mainPart, 0, 0.4F * 16, 0);
			for (int i = 0; i < this.legJoints.length; i++) {
				this.animator.rotate(this.knees[i], -(float) Math.toRadians(45), 0, 0);
				this.animator.rotate(this.feet[i], -(float) Math.toRadians(45), 0, 0);
			}

			this.animator.startKeyframe(0);
			for (int i = 0; i < this.legJoints.length; i++) {
				this.animator.move(this.legJoints[i], 0, -1, 0);
			}
			this.animator.move(this.head, 0, 0, 0.75F * 16);
			this.animator.move(this.legJointFL, -offsetXZ, 0, offsetXZ);
			this.animator.move(this.legJointBL, -offsetXZ, 0, -offsetXZ);
			this.animator.move(this.legJointFR, offsetXZ, 0, offsetXZ);
			this.animator.move(this.legJointBR, offsetXZ, 0, -offsetXZ);
			this.animator.endKeyframe();

			this.animator.resetKeyframe(turtle.getAnimation().getDuration() / 2);

			this.animator.endKeyframe();

			this.animator.resetKeyframe(turtle.getAnimation().getDuration() / 2);
		}

		/*
		 * Spin animation
		 */
		if (turtle.getAnimation() == EntityCQRGiantTortoise.ANIMATION_SPIN) {
			this.animator.setAnimation(EntityCQRGiantTortoise.ANIMATION_SPIN);

			int TIME_FOR_ONE_SPIN = 20;
			this.animator.startKeyframe(turtle.getAnimation().getDuration());
			this.animator.rotate(this.mainPart, 0, (float) Math.toRadians(360 * (turtle.getAnimation().getDuration() / TIME_FOR_ONE_SPIN)), 0);
			this.animator.endKeyframe();
		}

		/*
		 * Stun animation
		 */
		if (turtle.getAnimation() == EntityCQRGiantTortoise.ANIMATION_STUNNED) {
			this.animator.setAnimation(EntityCQRGiantTortoise.ANIMATION_STUNNED);

			for (int i = 0; i < 4; i++) {
				this.setRotateAngle(this.legJoints[i], 2 * this.legJoints[i].defaultRotationX, this.legJoints[i].defaultRotationY, 0);
				this.setRotateAngle(this.knees[i], 2 * this.knees[i].defaultRotationX, this.knees[i].defaultRotationY, 0);
				this.setRotateAngle(this.feet[i], (float) (this.feet[i].defaultRotationX - Math.toRadians(45)), this.feet[i].defaultRotationY, 0);
			}

			float offsetXZ = 0.95F;
			this.legJointFL.offsetX = -offsetXZ;
			this.legJointFL.offsetZ = offsetXZ;

			this.legJointBL.offsetX = -offsetXZ;
			this.legJointBL.offsetZ = -offsetXZ;

			this.legJointFR.offsetX = offsetXZ;
			this.legJointFR.offsetZ = offsetXZ;

			this.legJointBR.offsetX = offsetXZ;
			this.legJointBR.offsetZ = -offsetXZ;

			this.mainPart.offsetY = 0.5F;
			this.head.offsetZ = 0.75F;

			this.animator.startKeyframe(20);

			offsetXZ = 18F;
			this.animator.move(this.head, 0, 0, -0.75F * 16);
			this.animator.move(this.legJointFL, offsetXZ, 0, -offsetXZ);
			this.animator.move(this.legJointBL, offsetXZ, 0, offsetXZ);
			this.animator.move(this.legJointFR, -offsetXZ, 0, -offsetXZ);
			this.animator.move(this.legJointBR, -offsetXZ, 0, offsetXZ);

			this.animator.endKeyframe();
			this.animator.resetKeyframe(turtle.getAnimation().getDuration() - 20);
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

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		this.resetToDefaultPose();

		EntityCQRGiantTortoise turtle = (EntityCQRGiantTortoise) entityIn;

		// Walking animations
		boolean showSubParts = true;
		if (turtle.isInShell()) {
			if (turtle.bypassInShell() || turtle.getAnimation() == EntityCQRGiantTortoise.ANIMATION_MOVE_LEGS_IN || turtle.getAnimation() == EntityCQRGiantTortoise.ANIMATION_MOVE_LEGS_OUT) {
				showSubParts = true;
			} else {
				showSubParts = false;
				this.mainPart.offsetY = 0.5F;
			}
		} else {
			this.head.rotateAngleX = headPitch * 0.017453292F;
			this.head.rotateAngleY = netHeadYaw * 0.017453292F;

			this.legJointFR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.legJointFR.rotateAngleY = MathHelper.sin(limbSwing * 0.6662F) * limbSwingAmount + 0.7853981633974483F;

			this.legJointFL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
			this.legJointFL.rotateAngleY = MathHelper.sin(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount - 0.7853981633974483F;

			this.legJointBR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
			this.legJointBR.rotateAngleY = MathHelper.sin(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount + 2.356194490192345F;

			this.legJointBL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.legJointBL.rotateAngleY = MathHelper.sin(limbSwing * 0.6662F) * limbSwingAmount - 2.356194490192345F;
		}
		for (AdvancedModelRenderer box : this.subParts) {
			box.showModel = showSubParts;
		}
	}
}
