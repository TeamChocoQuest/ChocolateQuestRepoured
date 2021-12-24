package team.cqr.cqrepoured.client.model.geo;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public abstract class ModelGeoCQRBase<T extends AbstractEntityCQR & IAnimatable> extends AnimatedGeoModel<T> {

	protected final Int2ObjectMap<ResourceLocation> textureVariantCache = new Int2ObjectArrayMap<>();

	protected final ResourceLocation modelLocation;
	protected final ResourceLocation textureLocation;
	protected final String entityName;

	protected ModelGeoCQRBase(ResourceLocation model, ResourceLocation textureDefault, final String entityName) {
		this.modelLocation = model;
		this.textureLocation = textureDefault;
		this.entityName = entityName;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}

		if (entity.getTextureCount() > 1) {
			return this.textureVariantCache.computeIfAbsent(entity.getTextureIndex(), k -> {
				String s = String.format("textures/entity/%s_%d.png", this.entityName, k);
				return new ResourceLocation(CQRMain.MODID, s);
			});
		}

		return this.textureLocation;
	}

	@Override
	public ResourceLocation getModelLocation(T entity) {
		return this.modelLocation;
	}

}
