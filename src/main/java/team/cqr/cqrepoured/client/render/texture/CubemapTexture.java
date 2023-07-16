package team.cqr.cqrepoured.client.render.texture;

import java.io.IOException;
import java.util.Optional;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class CubemapTexture extends AbstractTexture {

	protected final ResourceLocation originalTexture;
	protected final ResourceLocation texture;

	public CubemapTexture(ResourceLocation originalTexture, ResourceLocation texture) {
		this.originalTexture = originalTexture;
		this.texture = texture;
	}

	/**
	 * Inserts "_cubemap" into the given {@code ResourceLocation} before the last dot.<br>
	 * <br>
	 * If there is no texture registered for the new {@code ResourceLocation} then a new {@code CubemapTexture} will be
	 * created.<br>
	 * The right-{@code ResourceLocation} will be computed by inserting "_right" into the given {@code ResourceLocation}
	 * before to last dot.<br>
	 * The left-{@code ResourceLocation} will be computed by inserting "_left" into the given {@code ResourceLocation}
	 * before to last dot.<br>
	 * The top-{@code ResourceLocation} will be computed by inserting "_top" into the given {@code ResourceLocation} before
	 * to last dot.<br>
	 * The bottom-{@code ResourceLocation} will be computed by inserting "_bottom" into the given {@code ResourceLocation}
	 * before to last dot.<br>
	 * The front-{@code ResourceLocation} will be computed by inserting "_front" into the given {@code ResourceLocation}
	 * before to last dot.<br>
	 * The back-{@code ResourceLocation} will be computed by inserting "_back" into the given {@code ResourceLocation}
	 * before to last dot.
	 * 
	 * @return The new {@code ResourceLocation} with "_cubemap" inserted before the last dot.
	 */
	public static ResourceLocation get(ResourceLocation originalTexture) {
		return CQRAbstractTexture.get(originalTexture, "cubemap", CubemapTexture::new);
	}

	@Override
	public void load(ResourceManager resourceManager) throws IOException {
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, this.getId());
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_BASE_LEVEL, 0);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_MAX_LEVEL, 0);
		this.load(resourceManager, CQRAbstractTexture.appendBeforeEnding(this.originalTexture, "_right"), GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X);
		this.load(resourceManager, CQRAbstractTexture.appendBeforeEnding(this.originalTexture, "_left"), GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X);
		this.load(resourceManager, CQRAbstractTexture.appendBeforeEnding(this.originalTexture, "_top"), GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y);
		this.load(resourceManager, CQRAbstractTexture.appendBeforeEnding(this.originalTexture, "_bottom"), GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y);
		this.load(resourceManager, CQRAbstractTexture.appendBeforeEnding(this.originalTexture, "_front"), GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z);
		this.load(resourceManager, CQRAbstractTexture.appendBeforeEnding(this.originalTexture, "_back"), GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);
	}

	private void load(ResourceManager resourceManager, ResourceLocation location, int target) throws IOException {
		Optional<Resource> optResource = resourceManager.getResource(location);
		optResource.ifPresent(resource -> {
			NativeImage image = NativeImage.read(resource.open());
			GL11.glTexImage2D(target, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image.pixels);
		});
	}

}
