package team.cqr.cqrepoured.client.render.texture;

import java.util.function.BiFunction;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.texture.GeoAbstractTexture;

public abstract class CQRAbstractTexture extends GeoAbstractTexture {

	protected CQRAbstractTexture(ResourceLocation originalLocation, ResourceLocation location) {
		super();
	}

	public static ResourceLocation get(ResourceLocation originalLocation, String appendix, BiFunction<ResourceLocation, ResourceLocation, AbstractTexture> constructor) {
		return GeoAbstractTexture.get(originalLocation, appendix, constructor);
	}

	public static ResourceLocation appendBeforeEnding(ResourceLocation location, String suffix) {
		return GeoAbstractTexture.appendToPath(location, suffix);
	}

}
