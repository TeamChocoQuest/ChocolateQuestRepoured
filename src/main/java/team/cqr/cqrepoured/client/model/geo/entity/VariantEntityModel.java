package team.cqr.cqrepoured.client.model.geo.entity;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.loading.object.BakedAnimations;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.customtextures.IHasTextureOverride;
import team.cqr.cqrepoured.entity.bases.VariantEntity;

public class VariantEntityModel<T extends VariantEntity & GeoEntity> extends AbstractModelGeoCQRBase<T> {

	public VariantEntityModel(ResourceLocation model, ResourceLocation textureDefault, final String entityName) {
		super(model, textureDefault, entityName);
	}
	
	public VariantEntityModel(ResourceLocation model, ResourceLocation textureDefault, final String entityName, final ResourceLocation... animationHierarchy) {
		super(model, textureDefault, entityName, animationHierarchy);
	}
	
	@Override
	public ResourceLocation getTextureResource(T entity) {
		ResourceLocation superResult = super.getTextureResource(entity);
		if (entity.getClientOverrides().isEmpty()) {
			return superResult;
		}
		if (entity instanceof IHasTextureOverride ito && ito.hasTextureOverride()) {
			return superResult;
		}
		
		return entity.getClientOverrides().get().optTexture().orElse(superResult);
	}
	
	@Override
	public ResourceLocation getModelResource(T entity) {
		ResourceLocation superResult = super.getModelResource(entity);
		if (entity.getClientOverrides().isEmpty()) {
			return superResult;
		}
		
		return entity.getClientOverrides().get().optModel().orElse(superResult);
	}
	
	@Override
	public ResourceLocation getAnimationResource(T entity) {
		ResourceLocation superResult = super.getAnimationResource(entity);
		if (entity.getClientOverrides().isEmpty()) {
			return superResult;
		}
		return entity.getClientOverrides().get().optAnimations().orElse(superResult);
	}
	
	@Override
	protected BakedAnimations[] getAnimationHierarchy(T entity) {
		BakedAnimations[] result = super.getAnimationHierarchy(entity);
		if (entity.getClientOverrides().isPresent()) {
			if (entity.getClientOverrides().get().optAnimations().isPresent()) {
				BakedAnimations anim = GeckoLibCache.getBakedAnimations().get(entity.getClientOverrides().get().optAnimations().get());
				if (anim != null) {
					return ArrayUtils.add(result, anim);
				}
			}
		}
		return result;
	}

}
