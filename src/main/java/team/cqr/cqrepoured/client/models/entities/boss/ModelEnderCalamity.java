package team.cqr.cqrepoured.client.models.entities.boss;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQREnderCalamity;
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

}
