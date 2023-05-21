package team.cqr.cqrepoured.client.model.geo;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.geo.exception.GeckoLibException;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.customtextures.IHasTextureOverride;
import team.cqr.cqrepoured.entity.ITextureVariants;

public abstract class AbstractModelGeoCQRBase<T extends Entity & IAnimatable & IAnimationTickable> extends AnimatedTickingGeoModel<T> {

	protected final ResourceLocation MODEL_RESLOC;
	protected final ResourceLocation TEXTURE_DEFAULT;
	protected final String ENTITY_REGISTRY_PATH_NAME;

	protected ResourceLocation[] textureVariantCache = null;

	public AbstractModelGeoCQRBase(ResourceLocation model, ResourceLocation textureDefault, final String entityName) {
		super();
		this.MODEL_RESLOC = model;
		this.TEXTURE_DEFAULT = textureDefault;
		this.ENTITY_REGISTRY_PATH_NAME = entityName;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		if (entity instanceof IHasTextureOverride) {
			// Custom texture start
			if (((IHasTextureOverride) entity).hasTextureOverride()) {
				return ((IHasTextureOverride) entity).getTextureOverride();
			}
		}
		// Custom texture end
		if (entity instanceof ITextureVariants) {
			if (((ITextureVariants) entity).getTextureCount() > 1) {
				if (this.textureVariantCache == null) {
					this.textureVariantCache = new ResourceLocation[((ITextureVariants) entity).getTextureCount()];
				}
				final int index = ((ITextureVariants) entity).getTextureIndex();
				if (this.textureVariantCache[index] == null) {
					this.textureVariantCache[index] = new ResourceLocation(CQRMain.MODID, "textures/entity/" + this.ENTITY_REGISTRY_PATH_NAME + "_" + index + ".png");
				}
				return this.textureVariantCache[index];
			}
		}
		return this.TEXTURE_DEFAULT;
	}

	@Override
	public ResourceLocation getModelLocation(T object) {
		return this.MODEL_RESLOC;
	}

	// Support hierarchical aniamtion loading
	@Override
	public Animation getAnimation(String name, IAnimatable animatable) {
		ResourceLocation animationID = this.getAnimationFileLocation((T) animatable);
		AnimationFile bakedAnimations = GeckoLibCache.getInstance().getAnimations().get(animationID);
		if (bakedAnimations == null) {
			throw new GeckoLibException(this.getAnimationFileLocation((T) animatable), "Could not find animation file. Please double check name.");
		}
		if (!bakedAnimations.getAllAnimations().contains(name)) {
			return super.getAnimation(name, animatable);
		}
		return bakedAnimations.getAnimation(name);
	}

}
