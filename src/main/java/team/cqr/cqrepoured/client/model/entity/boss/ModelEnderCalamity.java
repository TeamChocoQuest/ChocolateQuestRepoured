package team.cqr.cqrepoured.client.model.entity.boss;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.ModelGeoCQRBase;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;

public class ModelEnderCalamity extends ModelGeoCQRBase<EntityCQREnderCalamity> {

	public ModelEnderCalamity(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	private static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(CQRMain.MODID, "animations/ender_calamity.animation.json");

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQREnderCalamity animatable) {
		return ANIMATION_RESLOC;
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
	public void setLivingAnimations(EntityCQREnderCalamity entity, Integer uniqueID, @SuppressWarnings("rawtypes") AnimationEvent customPredicate) {
		// TODO: Fix buggy rotation
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		// IBone headBone = this.getAnimationProcessor().getBone(BONE_IDENT_HEAD);
		// IBone rootBone = this.getAnimationProcessor().getBone(BONE_IDENT_ROOT);

		// rootBone.setRotationY(-entity.rotationYaw);

		// headBone.setRotationX((float) Math.toRadians(-entity.rotationPitch) /*- rootBone.getRotationX()*/);
		// headBone.setRotationY((float) Math.toRadians(-(entity.rotationYawHead - entity.rotationYaw))/* -
		// rootBone.getRotationY()*/);
		IBone rootBone = this.getAnimationProcessor().getBone(BONE_IDENT_ROOT);
		IBone bodyBone = this.getAnimationProcessor().getBone(BONE_IDENT_BODY);
		float correctPitch = bodyBone.getRotationX();
		if (entity.rotateBodyPitch()) {
			float pitch = (float) Math.toRadians(this.getPitch(entity, Minecraft.getInstance().getRenderPartialTicks()) - 90F);
			pitch -= rootBone.getRotationX();
			// System.out.println("Client pitch: " + pitch);
			// System.out.println("Client prev pitch: " + entity.prevRotationPitchCQR);
			bodyBone.setRotationX(pitch);
		} else {
			bodyBone.setRotationX(correctPitch);
		}

	}

	protected float getPitch(EntityCQREnderCalamity entity, float partialTicks) {
		return this.interpolateRotation(entity.prevRotationPitchCQR, entity.rotationPitchCQR, partialTicks);
	}

	protected float interpolateRotation(float prevRotation, float rotation, float partialTicks) {
		return prevRotation + MathHelper.wrapDegrees(rotation - prevRotation) * partialTicks;
	}

}
