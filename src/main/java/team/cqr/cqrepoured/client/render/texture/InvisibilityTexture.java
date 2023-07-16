package team.cqr.cqrepoured.client.render.texture;

import java.util.Random;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.resources.IResource;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.util.Perlin2D;

public class InvisibilityTexture extends CQRAbstractTexture {

	private static final Random RANDOM = new Random();
	private static final Perlin2D PERLIN = new Perlin2D(RANDOM.nextLong(), 4);

	public InvisibilityTexture(ResourceLocation originalLocation, ResourceLocation location) {
		super(originalLocation, location);
	}

	public static ResourceLocation get(ResourceLocation originalTexture) {
		return get(originalTexture, "_invisible", InvisibilityTexture::new);
	}

	@Override
	protected boolean onLoadTexture(IResource resource, NativeImage originalImage, NativeImage newImage) {
		PERLIN.setup(RANDOM.nextLong(), 4);
		for (int x = 0; x < newImage.getWidth(); x++) {
			for (int y = 0; y < newImage.getHeight(); y++) {
				int abgr = newImage.getPixelRGBA(x, y);
				if ((abgr >>> 24) <= 2) {
					continue;
				}
				float f = PERLIN.getNoiseAt(x, y);
				newImage.setPixelRGBA(x, y, ((int) (f * 255.0F) << 24) | (abgr & 0x00FFFFFF));
			}
		}
		return true;
	}

}
