package team.cqr.cqrepoured.client.render.texture;

import java.util.function.BiFunction;

import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.texture.GeoAbstractTexture;

public abstract class AbstractTexture extends GeoAbstractTexture {

	protected AbstractTexture(ResourceLocation originalLocation, ResourceLocation location) {
		super(originalLocation, location);
	}

	public static ResourceLocation get(ResourceLocation originalLocation, String appendix, BiFunction<ResourceLocation, ResourceLocation, Texture> constructor) {
		return GeoAbstractTexture.get(originalLocation, appendix, constructor);
	}

	public static ResourceLocation appendBeforeEnding(ResourceLocation location, String suffix) {
		return GeoAbstractTexture.appendBeforeEnding(location, suffix);
	}

}
