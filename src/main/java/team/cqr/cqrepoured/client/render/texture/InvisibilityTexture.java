package team.cqr.cqrepoured.client.render.texture;

import java.io.IOException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.util.Perlin2D;

public class InvisibilityTexture extends Texture {

	private static final Random RANDOM = new Random();
	private static final Perlin2D PERLIN = new Perlin2D(RANDOM.nextLong(), 4.0F);
	private static final Logger LOGGER = LogManager.getLogger();
	protected final ResourceLocation originalTextureLocation;
	protected final ResourceLocation textureLocation;

	public InvisibilityTexture(ResourceLocation originalTextureLocation, ResourceLocation textureLocation) {
		this.originalTextureLocation = originalTextureLocation;
		this.textureLocation = textureLocation;
	}

	public static ResourceLocation get(ResourceLocation originalTexture) {
		String path = originalTexture.getPath();
		int i = path.lastIndexOf('.');
		ResourceLocation invisibilityTexture = new ResourceLocation(originalTexture.getNamespace(), path.substring(0, i) + "_invisible" + path.substring(i));
		EntityRendererManager renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
		if (renderManager.textureManager.getTexture(invisibilityTexture) == null) {
			renderManager.textureManager.loadTexture(invisibilityTexture, new InvisibilityTexture(originalTexture, invisibilityTexture));
		}
		return invisibilityTexture;
	}
	
	@Override
	public void load(IResourceManager resourceManager) throws IOException {
		this.releaseId();

		try (IResource iresource = resourceManager.getResource(this.originalTextureLocation)) {
			NativeImage bufferedimage = NativeImage.read(TextureUtil.readResource(iresource.getInputStream()));

			// CQR Start
			PERLIN.setup(RANDOM.nextLong(), 4.0F);
			for (int x = 0; x < bufferedimage.getWidth(); x++) {
				for (int y = 0; y < bufferedimage.getHeight(); y++) {
					int argb = bufferedimage.getPixelRGBA(x, y);
					if ((argb >>> 24) <= 2) {
						continue;
					}
					float f = PERLIN.getNoiseAt(x, y);
					bufferedimage.setPixelRGBA(x, y, ((int) (f * 255.0F) << 24) | (argb & 0x00FFFFFF));
				}
			}
			// CQR End

			boolean flag = false;
			boolean flag1 = false;

			//if (iresource.hasMetadata()) {
				try {
					TextureMetadataSection texturemetadatasection = iresource.getMetadata(TextureMetadataSection.SERIALIZER);

					if (texturemetadatasection != null) {
						flag = texturemetadatasection.isBlur();
						flag1 = texturemetadatasection.isClamp();
					}
				} catch (RuntimeException runtimeexception) {
					LOGGER.warn("Failed reading metadata of: {}", this.originalTextureLocation, runtimeexception);
				}
			//}

			TextureUtil.uploadTextureImageAllocate(this.getId(), bufferedimage, flag, flag1);
		}
	}

}
