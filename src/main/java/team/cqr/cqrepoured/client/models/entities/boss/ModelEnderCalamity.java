package team.cqr.cqrepoured.client.models.entities.boss;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.util.Reference;

public class ModelEnderCalamity extends AnimatedGeoModel<EntityCQREnderCalamity> {

	static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(Reference.MODID, "animations/ender_calamity.animation.json");
	static final ResourceLocation MODEL_RESLOC = new ResourceLocation(Reference.MODID, "geo/ender_calamity.geo.json");

	private ResourceLocation texture;

	public ModelEnderCalamity(ResourceLocation texture) {
		super();
		this.texture = texture;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQREnderCalamity animatable) {
		return ANIMATION_RESLOC;
	}

	@Override
	public ResourceLocation getModelLocation(EntityCQREnderCalamity object) {
		return MODEL_RESLOC;
	}

	@Override
	public ResourceLocation getTextureLocation(EntityCQREnderCalamity entity) {
		// Custom texture start
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}
		// Custom texture end
		return entity.getTextureCount() > 1 ? new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity_" + entity.getTextureIndex() + ".png") : this.texture;
	}
	
	private static final String BONE_IDENT_HEAD = "head";
	/*private static final String BONE_IDENT_LEGJOINT_BR = "legJointBR";
	private static final String BONE_IDENT_LEGJOINT_BL = "legJointBL";
	private static final String BONE_IDENT_LEGJOINT_FR = "legJointFR";
	private static final String BONE_IDENT_LEGJOINT_FL = "legJointFL";*/

	/*
	 * Bones needed for walking:
	 * - legJoint FR
	 * - legJoint FL
	 * - legJoint BR
	 * - legJoint BL
	 * - head
	 */
	@Override
	public void setLivingAnimations(EntityCQREnderCalamity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone headBone = this.getAnimationProcessor().getBone(BONE_IDENT_HEAD);

		headBone.setRotationX((float) Math.toRadians(-entity.rotationPitch));
		headBone.setRotationY((float) Math.toRadians(-(entity.rotationYawHead +90)));

	}

}
