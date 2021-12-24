package team.cqr.cqrepoured.client.render.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractTextureCQR extends AbstractTexture {

	private static final Logger LOGGER = LogManager.getLogger();
	protected final ResourceLocation originalTextureLocation;
	protected final ResourceLocation textureLocation;

	protected AbstractTextureCQR(ResourceLocation originalTextureLocation, ResourceLocation textureLocation) {
		this.originalTextureLocation = originalTextureLocation;
		this.textureLocation = textureLocation;
	}

	protected static ResourceLocation appendBeforeEnding(ResourceLocation location, String suffix) {
		String path = location.getPath();
		int i = path.lastIndexOf('.');
		return new ResourceLocation(location.getNamespace(), path.substring(0, i) + suffix + path.substring(i));
	}

	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException {
		if (!this.onLoadTexture(resourceManager, this.glTextureId == -1)) {
			return;
		}

		this.deleteGlTexture();
		IResource resource = null;

		try {
			resource = resourceManager.getResource(this.originalTextureLocation);
			BufferedImage image = TextureUtil.readBufferedImage(resource.getInputStream());

			image = this.onLoadImage(image, resource);

			boolean blur = false;
			boolean clamp = false;

			if (resource.hasMetadata()) {
				try {
					TextureMetadataSection texturemetadatasection = resource.getMetadata("texture");

					if (texturemetadatasection != null) {
						blur = texturemetadatasection.getTextureBlur();
						clamp = texturemetadatasection.getTextureClamp();
					}

					image = this.onLoadMetadata(image, resource);
				} catch (RuntimeException e) {
					LOGGER.warn("Failed reading metadata of: {}", this.originalTextureLocation, e);
				}
			}

			TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), image, blur, clamp);

			this.onUploadImage(image, resource);
		} finally {
			IOUtils.closeQuietly(resource);
		}
	}

	/**
	 * @param reload true if this texture gets reloaded
	 * @return false to skip texture loading
	 */
	protected boolean onLoadTexture(IResourceManager resourceManager, boolean reload) throws IOException {
		return true;
	}

	protected BufferedImage onLoadImage(BufferedImage image, IResource resource) throws IOException {
		return image;
	}

	protected BufferedImage onLoadMetadata(BufferedImage image, IResource resource) throws IOException {
		return image;
	}

	protected void onUploadImage(BufferedImage image, IResource resource) throws IOException {

	}

	public ResourceLocation getOriginalTextureLocation() {
		return originalTextureLocation;
	}

	public ResourceLocation getTextureLocation() {
		return textureLocation;
	}

}
