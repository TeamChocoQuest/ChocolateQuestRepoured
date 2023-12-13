package team.cqr.cqrepoured.client.render.texture;

import java.util.function.BiFunction;

import mod.azure.azurelib.cache.texture.GeoAbstractTexture;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public abstract class CQRAbstractTexture extends GeoAbstractTexture {
	
	protected final ResourceLocation originalLocation;
	protected final ResourceLocation location;

	protected CQRAbstractTexture(ResourceLocation originalLocation, ResourceLocation location) {
		super();
		this.originalLocation = originalLocation;
		this.location = location;
	}

	public static ResourceLocation get(ResourceLocation originalLocation, String appendix, BiFunction<ResourceLocation, ResourceLocation, AbstractTexture> constructor) {
		final ResourceLocation resultingLocation = appendBeforeEnding(originalLocation, appendix); 
		GeoAbstractTexture.generateTexture(originalLocation, (textureManager) -> {
			 if (textureManager.getTexture(resultingLocation, MissingTextureAtlasSprite.getTexture()) == MissingTextureAtlasSprite.getTexture()) {
				 textureManager.register(resultingLocation, constructor.apply(originalLocation, resultingLocation));
			 }
		});
		
		return resultingLocation;
	}

	public static ResourceLocation appendBeforeEnding(ResourceLocation location, String suffix) {
		return GeoAbstractTexture.appendToPath(location, suffix);
	}

}
