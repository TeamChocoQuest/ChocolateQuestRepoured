package team.cqr.cqrepoured.client.render.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSectionSerializer;

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
		EntityRendererManager renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
		if (renderManager.textureManager.getTexture(glowingTexture) == null) {
			renderManager.textureManager.loadTexture(glowingTexture, new AutoGlowingTexture(originalTexture, glowingTexture));
		}
		return glowingTexture;
	}

	@Override
	public void load(IResourceManager resourceManager) throws IOException {
		this.releaseId();

		try (IResource iresource = resourceManager.getResource(this.originalTexture)) {
			// Needed to get the GL-texture id
			Texture ito = Minecraft.getInstance().textureManager.getTexture(iresource.getLocation());
			NativeImage bufferedimage = NativeImage.read(TextureUtil.readResource(iresource.getInputStream()));
			NativeImage glowingBI = new NativeImage(bufferedimage.getWidth(), bufferedimage.getHeight(), false/*, bufferedimage.getType()*/);

			boolean flag = false;
			boolean flag1 = false;

			//Nonexistant in 1.15+ cause the getters are nullable...
			//if (iresource.hasMetadata()) {
				try {
					// DONE: Fix this for the CTS!! Cts for whatever reason tries to load png as mcmeta file...
					TextureMetadataSection texturemetadatasection = iresource.getMetadata(new TextureMetadataSectionSerializer());

					if (texturemetadatasection != null) {
						flag = texturemetadatasection.isBlur();
						flag1 = texturemetadatasection.isClamp();
					}

					GlowingMetadataSection glowInformation = iresource.getMetadata(new GlowingMetadataSectionSerializer());
					if (glowInformation != null) {
						for (Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> area : glowInformation.getGlowingSections()) {
							for (int ix = area.getA().getA(); ix < area.getB().getA(); ix++) {
								for (int iy = area.getA().getB(); iy < area.getB().getB(); iy++) {
									glowingBI.setPixelRGBA(ix, iy, bufferedimage.getPixelRGBA(ix, iy));

									// Remove it from the original
									bufferedimage.setPixelRGBA(ix, iy, 0);
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
			//}

			TextureUtil.uploadTextureImageAllocate(this.getId(), glowingBI, flag, flag1);

			// Also upload the changes to the original texture...
			TextureUtil.uploadTextureImage(ito.getId(), bufferedimage);
		}
	}

}
