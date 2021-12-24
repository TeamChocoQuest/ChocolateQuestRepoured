package team.cqr.cqrepoured.client.render.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.util.Perlin2D;

public class InvisibilityTexture extends AbstractTextureCQR {

	private static final Random RANDOM = new Random();
	private static final Perlin2D PERLIN = new Perlin2D(RANDOM.nextLong(), 4.0F);

	public InvisibilityTexture(ResourceLocation originalTextureLocation, ResourceLocation textureLocation) {
		super(originalTextureLocation, textureLocation);
	}

	public static ResourceLocation get(ResourceLocation originalTextureLocation) {
		ResourceLocation textureLocation = appendBeforeEnding(originalTextureLocation, "_invisible");
		TextureManager textureManager = Minecraft.getMinecraft().renderEngine;
		ITextureObject texture = textureManager.getTexture(textureLocation);

		if (!(texture instanceof InvisibilityTexture)) {
			texture = new InvisibilityTexture(originalTextureLocation, textureLocation);
			textureManager.loadTexture(textureLocation, texture);
		}

		return textureLocation;
	}

	@Override
	protected BufferedImage onLoadImage(BufferedImage image, IResource resource) throws IOException {
		PERLIN.setup(RANDOM.nextLong(), 4.0F);

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int argb = image.getRGB(x, y);
				if ((argb >>> 24) <= 2) {
					continue;
				}
				float f = PERLIN.getNoiseAt(x, y);
				image.setRGB(x, y, ((int) (f * 255.0F) << 24) | (argb & 0x00FFFFFF));
			}
		}

		return image;
	}

}
