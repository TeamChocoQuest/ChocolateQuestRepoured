package team.cqr.cqrepoured.client.models.entities.boss;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.util.Reference;

public class ModelGiantTortoiseGecko extends AnimatedGeoModel<EntityCQRGiantTortoise> {

	static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(Reference.MODID, "animations/giant_tortoise.animation.json");
	static final ResourceLocation MODEL_RESLOC = new ResourceLocation(Reference.MODID, "geo/giant_tortoise.geo.json");

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
		return entity.getTextureCount() > 1 ? new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_tortoise_" + entity.getTextureIndex() + ".png") : this.texture;
	}

	private static final String BONE_IDENT_HEAD = "head";
	private static final String BONE_IDENT_LEGJOINT_BR = "legJointBR";
	private static final String BONE_IDENT_LEGJOINT_BL = "legJointBL";
	private static final String BONE_IDENT_LEGJOINT_FR = "legJointFR";
	private static final String BONE_IDENT_LEGJOINT_FL = "legJointFL";

	/*
	 * Bones needed for walking:
	 * - legJoint FR
	 * - legJoint FL
	 * - legJoint BR
	 * - legJoint BL
	 * - head
	 */
	@Override
	public void setLivingAnimations(EntityCQRGiantTortoise entity, Integer uniqueID) {
		super.setLivingAnimations(entity, uniqueID);
		if (!entity.isInShell()) {
			IBone headBone = this.getAnimationProcessor().getBone(BONE_IDENT_HEAD);
			IBone legFL = this.getAnimationProcessor().getBone(BONE_IDENT_LEGJOINT_FL);
			IBone legFR = this.getAnimationProcessor().getBone(BONE_IDENT_LEGJOINT_FR);
			IBone legBL = this.getAnimationProcessor().getBone(BONE_IDENT_LEGJOINT_BL);
			IBone legBR = this.getAnimationProcessor().getBone(BONE_IDENT_LEGJOINT_BR);

			headBone.setRotationX(entity.rotationPitch * 0.017453292F);
			headBone.setRotationY(entity.rotationYawHead * 0.017453292F);

			legFR.setRotationX(MathHelper.cos(entity.limbSwing * 0.6662F) * 1.4F * entity.limbSwingAmount);
			legFL.setRotationX(MathHelper.cos(entity.limbSwing * 0.6662F + (float) Math.PI) * 1.4F * entity.limbSwingAmount);
			legBL.setRotationX(MathHelper.cos(entity.limbSwing * 0.6662F + (float) Math.PI) * 1.4F * entity.limbSwingAmount);
			legFR.setRotationX(MathHelper.cos(entity.limbSwing * 0.6662F) * 1.4F * entity.limbSwingAmount);
			
			legFR.setRotationY(MathHelper.sin(entity.limbSwing * 0.6662F) * entity.limbSwingAmount + 0.7853981633974483F);
			legFL.setRotationY(MathHelper.sin(entity.limbSwing * 0.6662F + (float) Math.PI) * entity.limbSwingAmount - 0.7853981633974483F);
			legBL.setRotationY(MathHelper.sin(entity.limbSwing * 0.6662F) * entity.limbSwingAmount - 2.356194490192345F);
			legBR.setRotationY( MathHelper.sin(entity.limbSwing * 0.6662F + (float) Math.PI) * entity.limbSwingAmount + 2.356194490192345F);
			
		}
	}

}
