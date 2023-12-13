package team.cqr.cqrepoured.client.model.geo.entity.boss;

import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimationState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;

public class ModelEnderCalamityGeo extends AbstractModelGeoCQRBase<EntityCQREnderCalamity> {

	public ModelEnderCalamityGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	public ResourceLocation getAnimationResource(EntityCQREnderCalamity animatable) {
		return CQRAnimations.Entity.ENDER_CALAMITY;
	}

	private static final String BONE_IDENT_ROOT = "root";
	private static final String BONE_IDENT_BODY = "body";
	// private static final String BONE_IDENT_HEAD = "head";
	/*
	 * private static final String BONE_IDENT_LEGJOINT_BR = "legJointBR";
	 * private static final String BONE_IDENT_LEGJOINT_BL = "legJointBL";
	 * private static final String BONE_IDENT_LEGJOINT_FR = "legJointFR";
	 * private static final String BONE_IDENT_LEGJOINT_FL = "legJointFL";
	 */

	/*
	 * Bones needed for walking:
	 * - legJoint FR
	 * - legJoint FL
	 * - legJoint BR
	 * - legJoint BL
	 * - head
	 */

	@Override
	public void setCustomAnimations(EntityCQREnderCalamity entity, long uniqueID, AnimationState<EntityCQREnderCalamity> customPredicate) {
		// TODO: Fix buggy rotation
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		// IBone headBone = this.getAnimationProcessor().getBone(BONE_IDENT_HEAD);
		// IBone rootBone = this.getAnimationProcessor().getBone(BONE_IDENT_ROOT);

		// rootBone.setRotationY(-entity.rotationYaw);

		// headBone.setRotationX((float) Math.toRadians(-entity.rotationPitch) /*- rootBone.getRotationX()*/);
		// headBone.setRotationY((float) Math.toRadians(-(entity.rotationYawHead - entity.rotationYaw))/* -
		// rootBone.getRotationY()*/);
		CoreGeoBone rootBone = this.getAnimationProcessor().getBone(BONE_IDENT_ROOT);
		CoreGeoBone bodyBone = this.getAnimationProcessor().getBone(BONE_IDENT_BODY);
		float correctPitch = bodyBone.getRotX();
		if (entity.rotateBodyPitch()) {
			float pitch = (float) Math.toRadians(this.getPitch(entity, customPredicate.getPartialTick()) - 90F);
			pitch -= rootBone.getRotX();
			// System.out.println("Client pitch: " + pitch);
			// System.out.println("Client prev pitch: " + entity.prevRotationPitchCQR);
			bodyBone.setRotX(pitch);
		} else {
			bodyBone.setRotX(correctPitch);
		}

	}

	protected float getPitch(EntityCQREnderCalamity entity, float partialTicks) {
		return this.interpolateRotation(entity.prevRotationPitchCQR, entity.rotationPitchCQR, partialTicks);
	}

	protected float interpolateRotation(float prevRotation, float rotation, float partialTicks) {
		return prevRotation + Mth.wrapDegrees(rotation - prevRotation) * partialTicks;
	}

}
