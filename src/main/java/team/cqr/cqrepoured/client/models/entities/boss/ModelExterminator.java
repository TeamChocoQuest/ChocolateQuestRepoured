package team.cqr.cqrepoured.client.models.entities.boss;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.objects.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.util.PartialTicksUtil;
import team.cqr.cqrepoured.util.Reference;

public class ModelExterminator extends AnimatedGeoModel<EntityCQRExterminator> {

	static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(Reference.MODID, "animations/exterminator.animation.json");
	static final ResourceLocation MODEL_RESLOC = new ResourceLocation(Reference.MODID, "geo/exterminator.geo.json");

	private ResourceLocation texture;

	public ModelExterminator(ResourceLocation texture) {
		super();
		this.texture = texture;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRExterminator animatable) {
		return ANIMATION_RESLOC;
	}

	@Override
	public ResourceLocation getModelLocation(EntityCQRExterminator object) {
		return MODEL_RESLOC;
	}

	@Override
	public ResourceLocation getTextureLocation(EntityCQRExterminator entity) {
		// Custom texture start
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}
		// Custom texture end
		return entity.getTextureCount() > 1 ? new ResourceLocation(Reference.MODID, "textures/entity/boss/exterminator_" + entity.getTextureIndex() + ".png") : this.texture;
	}

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

		if (entity.getHealth() < 0.01 || entity.isDead || !entity.isEntityAlive()) {
			return;
		}
		
		try {
			final float partialTicks = PartialTicksUtil.getCurrentPartialTicks();

			if (entity.limbSwingAmount >= 0.01D && !(entity.isCannonRaised() || entity.getCurrentAnimation().equalsIgnoreCase(EntityCQRExterminator.ANIM_NAME_THROW))) {
				IBone torsoBone = this.getAnimationProcessor().getBone(BONE_IDENT_TORSO);

				// Taken from RenderIronGolem.class
				float f1 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
				float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;

				torsoBone.setRotationZ((float) Math.toRadians(6.5F * f2));
			}

			// Taken from ModelIronGolem.class
			final float legAngle = 1.5F * this.triangleWave(entity.limbSwing, 13.0F) * entity.limbSwingAmount;
			
			IBone leftLeg = this.getAnimationProcessor().getBone(BONE_IDENT_LEFT_LEG);
			IBone rightLeg = this.getAnimationProcessor().getBone(BONE_IDENT_RIGHT_LEG);
			
			if(!entity.isUsingKickAnimation()) {
				rightLeg.setRotationX(legAngle);
			}
			leftLeg.setRotationX(-legAngle);
		} catch (NullPointerException npe) {
			//Ignore, happens when model doesn't feature those bones
		}
	}

}
