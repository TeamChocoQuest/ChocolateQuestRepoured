package team.cqr.cqrepoured.client.render.texture;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection;

public class AutoGlowingTexture extends AbstractTexture {

	public AutoGlowingTexture(ResourceLocation originalLocation, ResourceLocation location) {
		super(originalLocation, location);
	}

	public static ResourceLocation get(ResourceLocation originalLocation) {
		return get(originalLocation, "glowing", AutoGlowingTexture::new);
	}

	@Override
	protected boolean onLoadTexture(IResource resource, NativeImage originalImage, NativeImage newImage) {
		GlowingMetadataSection glowingMetadata = null;
		try {
			glowingMetadata = resource.getMetadata(GlowingMetadataSection.SERIALIZER);
		} catch (RuntimeException e) {
			LOGGER.warn("Failed reading glowing metadata of: {}", location, e);
		}

		if (glowingMetadata == null || glowingMetadata.isEmpty()) {
			return false;
		}
		glowingMetadata.getGlowingSections().forEach(section -> section.forEach((x, y) -> {
			newImage.setPixelRGBA(x, y, originalImage.getPixelRGBA(x, y));

			// Remove it from the original
			originalImage.setPixelRGBA(x, y, 0);
		}));
		return true;
	}

}
