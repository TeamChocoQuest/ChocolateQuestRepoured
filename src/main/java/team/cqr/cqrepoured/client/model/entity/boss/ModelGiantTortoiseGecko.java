package team.cqr.cqrepoured.client.model.entity.boss;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.ModelGeoCQRBase;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class ModelGiantTortoiseGecko extends ModelGeoCQRBase<EntityCQRGiantTortoise> {

	public ModelGiantTortoiseGecko(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(CQRMain.MODID, "animations/giant_tortoise.animation.json");

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRGiantTortoise animatable) {
		return ANIMATION_RESLOC;
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

			headBone.setRotationX((float) Math.toRadians(-entity.xRot));
			headBone.setRotationY((float) Math.toRadians(-(entity.yHeadRot - entity.yBodyRot)));

		}
	}

}
