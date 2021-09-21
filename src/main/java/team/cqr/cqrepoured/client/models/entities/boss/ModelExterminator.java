package team.cqr.cqrepoured.client.models.entities.boss;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.objects.entity.boss.exterminator.EntityCQRExterminator;
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
		return entity.getTextureCount() > 1 ? new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_tortoise_" + entity.getTextureIndex() + ".png") : this.texture;
	}

}
