package team.cqr.cqrepoured.client.model.geo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.GeckoLibCache;
import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.loading.object.BakedAnimations;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.model.data.EntityModelData;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.customtextures.IHasTextureOverride;
import team.cqr.cqrepoured.entity.ITextureVariants;

public abstract class AbstractModelGeoCQRBase<T extends Entity & GeoEntity> extends GeoModel<T> {

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
		this.turnsHead = false;
	}
	
	public AbstractModelGeoCQRBase(ResourceLocation model, ResourceLocation textureDefault, final String entityName, final ResourceLocation... animationHierarchy) {
		super();
		this.MODEL_RESLOC = model;
		this.TEXTURE_DEFAULT = textureDefault;
		this.ENTITY_REGISTRY_PATH_NAME = entityName;
		this.ANIMATION_HIERARCHY = merge(CQRAnimations._EMPTY, animationHierarchy);
		this.turnsHead = false;
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
	public ResourceLocation getTextureResource(T entity) {
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
					this.textureVariantCache[index] = new ResourceLocation(CQRConstants.MODID, "textures/entity/" + this.ENTITY_REGISTRY_PATH_NAME + "_" + index + ".png");
				}
				return this.textureVariantCache[index];
			}
		}
		return this.TEXTURE_DEFAULT;
	}

	@Override
	public ResourceLocation getModelResource(T object) {
		return this.MODEL_RESLOC;
	}

	@Override
	public Animation getAnimation(T animatable, String name) {
		// Support hierarchical animation loading
		if(this.ANIMATION_HIERARCHY.length > 1) {
			BakedAnimations[] animHierarchy = this.getAnimationHierarchy((T) animatable);
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

		return super.getAnimation(animatable, name);
	}

	protected BakedAnimations[] getAnimationHierarchy(T animatable) {
		BakedAnimations[] result = new BakedAnimations[this.ANIMATION_HIERARCHY.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = GeckoLibCache.getBakedAnimations().get(this.ANIMATION_HIERARCHY[i]);
		}
		return result;
	}

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return CQRAnimations._EMPTY;
	}
	
	private final boolean turnsHead;
	
	@Override
	public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
		if (!this.turnsHead)
			return;

		CoreGeoBone head = getAnimationProcessor().getBone("head");

		if (head != null) {
			EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
		}
	}

}
