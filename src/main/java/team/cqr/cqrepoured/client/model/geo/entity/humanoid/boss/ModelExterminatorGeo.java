package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;

public class ModelExterminatorGeo extends AbstractModelGeoCQRBase<EntityCQRExterminator> {

	public ModelExterminatorGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.EXTERMINATOR, CQRAnimations.Entity._GENERIC_HUMANOID);
	}

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRExterminator animatable) {
		return CQRAnimations.Entity.EXTERMINATOR;
	}
	/* No longer needed, handled by animation now!
	private static final String BONE_IDENT_LEFT_LEG = "leg_left";
	private static final String BONE_IDENT_RIGHT_LEG = "leg_right";
	private static final String BONE_IDENT_TORSO = "body";

	// Taken from ModelIronGolem.class
	private float triangleWave(float p_78172_1_, float p_78172_2_) {
		return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
	}

	@Override
	public void setLivingAnimations(EntityCQRExterminator entity, Integer uniqueID, @SuppressWarnings("rawtypes") AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		if (entity.getHealth() < 0.01 || entity.isDeadOrDying() || !entity.isAlive()) {
			return;
		}

		try {
			final float partialTicks = customPredicate.getPartialTick();

			if (entity.limbSwingAmount >= 0.01D && (!entity.isCannonRaised() && ((entity.getCurrentAnimation() == null) || !entity.getCurrentAnimation().equalsIgnoreCase(EntityCQRExterminator.ANIM_NAME_THROW)))) {
				IBone torsoBone = this.getAnimationProcessor().getBone(BONE_IDENT_TORSO);

				// Taken from RenderIronGolem.class
				float f1 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
				float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;

				torsoBone.setRotationZ((float) Math.toRadians(6.5F * f2));
			}

			// Taken from ModelIronGolem.class
			float limbSwingAmount = Math.min(entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks, 1);
			float limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
			final float legAngle = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;

			IBone leftLeg = this.getAnimationProcessor().getBone(BONE_IDENT_LEFT_LEG);
			IBone rightLeg = this.getAnimationProcessor().getBone(BONE_IDENT_RIGHT_LEG);

			if (!entity.isUsingKickAnimation()) {
				rightLeg.setRotationX(legAngle);
			}
			leftLeg.setRotationX(-legAngle);
		} catch (NullPointerException npe) {
			// Ignore, happens when model doesn't feature those bones
		}
	}*/

}
