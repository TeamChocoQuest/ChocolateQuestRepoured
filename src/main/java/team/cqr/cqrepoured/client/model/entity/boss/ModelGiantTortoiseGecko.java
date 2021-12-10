package team.cqr.cqrepoured.client.model.entity.boss;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class ModelGiantTortoiseGecko extends AnimatedGeoModel<EntityCQRGiantTortoise> {

	static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(CQRMain.MODID, "animations/giant_tortoise.animation.json");
	static final ResourceLocation MODEL_RESLOC = new ResourceLocation(CQRMain.MODID, "geo/giant_tortoise.geo.json");

	private ResourceLocation texture;

	public ModelGiantTortoiseGecko(ResourceLocation texture) {
		super();
		this.texture = texture;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRGiantTortoise animatable) {
		return ANIMATION_RESLOC;
	}

	@Override
	public ResourceLocation getModelLocation(EntityCQRGiantTortoise object) {
		return MODEL_RESLOC;
	}

	@Override
	public ResourceLocation getTextureLocation(EntityCQRGiantTortoise entity) {
		// Custom texture start
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}
		// Custom texture end
		return entity.getTextureCount() > 1 ? new ResourceLocation(CQRMain.MODID, "textures/entity/boss/giant_tortoise_" + entity.getTextureIndex() + ".png")
				: this.texture;
	}

	private static final String BONE_IDENT_HEAD = "head";
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
	public void setLivingAnimations(EntityCQRGiantTortoise entity, Integer uniqueID, @SuppressWarnings("rawtypes") AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		if (entity.getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_WALK) {
			IBone headBone = this.getAnimationProcessor().getBone(BONE_IDENT_HEAD);

			headBone.setRotationX((float) Math.toRadians(-entity.rotationPitch));
			headBone.setRotationY((float) Math.toRadians(-(entity.rotationYawHead - entity.rotationYaw)));

		}
	}

}
