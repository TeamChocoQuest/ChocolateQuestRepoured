package team.cqr.cqrepoured.client.render.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.util.Perlin2D;

public class InvisibilityTexture extends AbstractTexture {

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
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		if (renderManager.renderEngine.getTexture(invisibilityTexture) == null) {
			renderManager.renderEngine.loadTexture(invisibilityTexture, new InvisibilityTexture(originalTexture, invisibilityTexture));
		}
		return invisibilityTexture;
	}

	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException {
		this.deleteGlTexture();

		try (IResource iresource = resourceManager.getResource(this.originalTextureLocation)) {
			BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());

			// CQR Start
			PERLIN.setup(RANDOM.nextLong(), 4.0F);
			for (int x = 0; x < bufferedimage.getWidth(); x++) {
				for (int y = 0; y < bufferedimage.getHeight(); y++) {
					int argb = bufferedimage.getRGB(x, y);
					if ((argb >>> 24) <= 2) {
						continue;
					}
					float f = PERLIN.getNoiseAt(x, y);
					bufferedimage.setRGB(x, y, ((int) (f * 255.0F) << 24) | (argb & 0x00FFFFFF));
				}
			}
			// CQR End

			boolean flag = false;
			boolean flag1 = false;

			if (iresource.hasMetadata()) {
				try {
					TextureMetadataSection texturemetadatasection = (TextureMetadataSection) iresource.getMetadata("texture");

					if (texturemetadatasection != null) {
						flag = texturemetadatasection.getTextureBlur();
						flag1 = texturemetadatasection.getTextureClamp();
					}
				} catch (RuntimeException runtimeexception) {
					LOGGER.warn("Failed reading metadata of: {}", this.originalTextureLocation, runtimeexception);
				}
			}

			TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, flag, flag1);
		}
	}

}
