package team.cqr.cqrepoured.client.model.geo.entity.boss;

import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimationState;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class ModelGiantTortoiseGeo extends AbstractModelGeoCQRBase<EntityCQRGiantTortoise> {

	public ModelGiantTortoiseGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	public ResourceLocation getAnimationResource(EntityCQRGiantTortoise animatable) {
		return CQRAnimations.Entity.GIANT_TORTOISE;
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
	public void setCustomAnimations(EntityCQRGiantTortoise animatable, long instanceId, AnimationState<EntityCQRGiantTortoise> animationState) {
		// TODO Auto-generated method stub
		super.setCustomAnimations(animatable, instanceId, animationState);
		if (animatable.getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_WALK) {
			CoreGeoBone headBone = this.getAnimationProcessor().getBone(BONE_IDENT_HEAD);

			headBone.setRotX((float) Math.toRadians(-animatable.getXRot()));
			headBone.setRotY((float) Math.toRadians(-(animatable.yHeadRot - animatable.yBodyRot)));

		}
	}

}
