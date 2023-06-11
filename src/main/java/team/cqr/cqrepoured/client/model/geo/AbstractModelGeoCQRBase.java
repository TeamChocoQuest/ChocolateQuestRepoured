package team.cqr.cqrepoured.client.model.geo;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.customtextures.IHasTextureOverride;
import team.cqr.cqrepoured.entity.ITextureVariants;

public abstract class AbstractModelGeoCQRBase<T extends Entity & IAnimatable & IAnimationTickable> extends AnimatedTickingGeoModel<T> {

	protected final ResourceLocation MODEL_RESLOC;
	protected final ResourceLocation TEXTURE_DEFAULT;
	protected final String ENTITY_REGISTRY_PATH_NAME;

	protected ResourceLocation[] textureVariantCache = null;
	protected final ResourceLocation[] ANIMATION_HIERARCHY;

	public AbstractModelGeoCQRBase(ResourceLocation model, ResourceLocation textureDefault, final String entityName) {
		super();
		this.MODEL_RESLOC = model;
		this.TEXTURE_DEFAULT = textureDefault;
		this.ENTITY_REGISTRY_PATH_NAME = entityName;
		this.ANIMATION_HIERARCHY = new ResourceLocation[] { CQRAnimations._EMPTY };
	}
	
	public AbstractModelGeoCQRBase(ResourceLocation model, ResourceLocation textureDefault, final String entityName, final ResourceLocation... animationHierarchy) {
		super();
		this.MODEL_RESLOC = model;
		this.TEXTURE_DEFAULT = textureDefault;
		this.ENTITY_REGISTRY_PATH_NAME = entityName;
		this.ANIMATION_HIERARCHY = merge(CQRAnimations._EMPTY, animationHierarchy);
	}
	
	static protected ResourceLocation[] merge(final ResourceLocation appendToEnd, final ResourceLocation... hierarchy) {
		ResourceLocation[] result = new ResourceLocation[hierarchy.length + 1];
		for(int i = 0; i < hierarchy.length; i++) {
			result[i] = hierarchy[i];
		}
		result[result.length - 1] = appendToEnd;
		
		return result;
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

	@Override
	public Animation getAnimation(String name, IAnimatable animatable) {
		// Support hierarchical animation loading
		if(this.ANIMATION_HIERARCHY.length > 1) {
			AnimationFile[] animHierarchy = this.getAnimationHierarchy((T) animatable);
			for (int i = 0; i < animHierarchy.length; i++) {
				if (animHierarchy[i] == null) {
					continue;
				}
				Animation anim = animHierarchy[i].getAnimation(name);
				if (anim != null) {
					return anim;
				}
			}
		}

		return super.getAnimation(name, animatable);
	}

	protected AnimationFile[] getAnimationHierarchy(T animatable) {
		AnimationFile[] result = new AnimationFile[this.ANIMATION_HIERARCHY.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = GeckoLibCache.getInstance().getAnimations().get(this.ANIMATION_HIERARCHY[i]);
		}
		return result;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(T animatable) {
		return CQRAnimations._EMPTY;
	}

}
