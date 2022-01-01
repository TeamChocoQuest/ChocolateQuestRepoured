package team.cqr.cqrepoured.client.render.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection;

public class AutoGlowingTexture extends Texture {

	private static final Logger LOGGER = LogManager.getLogger();

	protected final ResourceLocation originalTexture;
	protected final ResourceLocation texture;

	public AutoGlowingTexture(ResourceLocation originalTexture, ResourceLocation texture) {
		this.originalTexture = originalTexture;
		this.texture = texture;
	}

	public static ResourceLocation get(ResourceLocation originalTexture) {
		String path = originalTexture.getPath();
		int i = path.lastIndexOf('.');
		ResourceLocation glowingTexture = new ResourceLocation(originalTexture.getNamespace(), path.substring(0, i) + "_glowing" + path.substring(i));
		EntityRendererManager renderManager = Minecraft.getMinecraft().getRenderManager();
		if (renderManager.renderEngine.getTexture(glowingTexture) == null) {
			renderManager.renderEngine.loadTexture(glowingTexture, new AutoGlowingTexture(originalTexture, glowingTexture));
		}
		return glowingTexture;
	}

	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException {
		this.deleteGlTexture();

		try (IResource iresource = resourceManager.getResource(this.originalTexture)) {
			// Needed to get the GL-texture id
			ITextureObject ito = Minecraft.getMinecraft().renderEngine.getTexture(iresource.getResourceLocation());
			BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
			BufferedImage glowingBI = new BufferedImage(bufferedimage.getWidth(), bufferedimage.getHeight(), bufferedimage.getType());

			boolean flag = false;
			boolean flag1 = false;

			if (iresource.hasMetadata()) {
				try {
					// DONE: Fix this for the CTS!! Cts for whatever reason tries to load png as mcmeta file...
					TextureMetadataSection texturemetadatasection = (TextureMetadataSection) iresource.getMetadata("texture");

					if (texturemetadatasection != null) {
						flag = texturemetadatasection.getTextureBlur();
						flag1 = texturemetadatasection.getTextureClamp();
					}

					GlowingMetadataSection glowInformation = (GlowingMetadataSection) iresource.getMetadata("glowsections");
					if (glowInformation != null) {
						for (Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> area : glowInformation.getGlowingSections()) {
							for (int ix = area.getFirst().getFirst(); ix < area.getSecond().getFirst(); ix++) {
								for (int iy = area.getFirst().getSecond(); iy < area.getSecond().getSecond(); iy++) {
									glowingBI.setRGB(ix, iy, bufferedimage.getRGB(ix, iy));

									// Remove it from the original
									bufferedimage.setRGB(ix, iy, 0);
								}
							}
						}
					}

					/*
					 * String name = this.texture.getPath().replace("/", "-");
					 * File outputFile = new File(CQRMain.CQ_CONFIG_FOLDER, name);
					 * ImageIO.write(glowingBI, "png", outputFile);
					 */
				} catch (RuntimeException runtimeexception) {
					LOGGER.warn("Failed reading metadata of: {}", this.originalTexture, runtimeexception);
				}
			}

			TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), glowingBI, flag, flag1);

			// Also upload the changes to the original texture...
			TextureUtil.uploadTextureImage(ito.getGlTextureId(), bufferedimage);
		}
	}

}
