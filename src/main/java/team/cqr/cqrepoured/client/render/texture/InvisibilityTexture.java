package team.cqr.cqrepoured.client.render.texture;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.fml.loading.FMLEnvironment;
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
	protected RenderCall loadTexture(ResourceManager resourceManager, Minecraft mc) throws IOException {
		AbstractTexture originalTexture;

		try {
			originalTexture = mc.submit(() -> mc.getTextureManager().getTexture(this.originalLocation)).get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw new IOException("Failed to load original texture: " + this.originalLocation, e);
		}

		Resource textureBaseResource = resourceManager.getResource(this.originalLocation).get();
		NativeImage baseImage = originalTexture instanceof DynamicTexture dynamicTexture ?
				dynamicTexture.getPixels() : NativeImage.read(textureBaseResource.open());
		NativeImage newImage = null;
		Optional<TextureMetadataSection> textureBaseMeta = textureBaseResource.metadata().getSection(TextureMetadataSection.SERIALIZER);
		boolean blur = textureBaseMeta.isPresent() && textureBaseMeta.get().isBlur();
		boolean clamp = textureBaseMeta.isPresent() && textureBaseMeta.get().isClamp();

		newImage = new NativeImage(baseImage.getWidth(), baseImage.getHeight(), true /*Idk what this does...*/);
		
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

		if (!FMLEnvironment.production) {
			printDebugImageToDisk(this.originalLocation, baseImage);
			printDebugImageToDisk(this.location, newImage);
		}

		final NativeImage finalNewImage = newImage;
		
		return () -> {
			uploadSimple(getId(), finalNewImage, blur, clamp);

			if (originalTexture instanceof DynamicTexture dynamicTexture) {
				dynamicTexture.upload();
			}
			else {
				uploadSimple(originalTexture.getId(), baseImage, blur, clamp);
			}
		};
	}

}
