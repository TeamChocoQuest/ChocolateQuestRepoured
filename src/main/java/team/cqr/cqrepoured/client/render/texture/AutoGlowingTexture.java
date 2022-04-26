package team.cqr.cqrepoured.client.render.texture;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
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
		TextureManager texManager = Minecraft.getInstance().getTextureManager();
		if (texManager.getTexture(glowingTexture) == null) {
			// Correct method? Old one was "loadTexture"
			texManager.register(glowingTexture, new AutoGlowingTexture(originalTexture, glowingTexture));
		}
		return glowingTexture;
	}

	@Override
	public void load(IResourceManager resourceManager) throws IOException {
		this.releaseId();

		try (IResource originalResource = resourceManager.getResource(this.originalTexture)) {
			// Needed to get the GL-texture id
			Texture originalTextureObject = Minecraft.getInstance().getTextureManager().getTexture(this.originalTexture);
			NativeImage originalImage = NativeImage.read(originalResource.getInputStream());
			NativeImage glowingImage = new NativeImage(originalImage.getWidth(), originalImage.getHeight(), false/* , bufferedimage.getType() */);

			// Nonexistant in 1.15+ cause the getters are nullable...
			try {
				// DONE: Fix this for the CTS!! Cts for whatever reason tries to load png as mcmeta file...
				TextureMetadataSection texturemetadatasection = originalResource.getMetadata(new TextureMetadataSectionSerializer());
				boolean flagBlur = false;
				boolean flagClamp = false;
				if (texturemetadatasection != null) {
					flagBlur = texturemetadatasection.isBlur();
					flagClamp = texturemetadatasection.isClamp();
				}

				GlowingMetadataSection glowInformation = originalResource.getMetadata(new GlowingMetadataSectionSerializer());
				if (glowInformation != null) {
					glowInformation.getGlowingSections().forEach(section -> section.forEach((x, y) -> {
						glowingImage.setPixelRGBA(x, y, originalImage.getPixelRGBA(x, y));

						// Remove it from the original
						originalImage.setPixelRGBA(x, y, 0);
					}));
					//originalImage.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight(), 0);
				}

				/*
				 * String name = this.texture.getPath().replace("/", "-"); File outputFile = new File("D:\\GitHub\\Repositories\\ChocolateQuestRepoured\\run", name + ".png"); glowingBI.writeToFile(outputFile); outputFile = new
				 * File("D:\\GitHub\\Repositories\\ChocolateQuestRepoured\\run", name + "_orig.png"); bufferedimage.writeToFile(outputFile);
				 */

			} catch (RuntimeException runtimeexception) {
				LOGGER.warn("Failed reading metadata of: {}", this.originalTexture, runtimeexception);
			}

			TextureUtil.prepareImage(this.getId(), glowingImage.getWidth(), glowingImage.getHeight());
			glowingImage.upload(0, 0, 0, false);

			// Also upload the changes to the original texture...
			TextureUtil.prepareImage(originalTextureObject.getId(), originalImage.getWidth(), originalImage.getHeight());
			if (originalTextureObject instanceof DynamicTexture) {
				((DynamicTexture) originalTextureObject).upload();
			} else {
				originalImage.upload(0, 0, 0, false);
			}
		}
	}

}
