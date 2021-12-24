package team.cqr.cqrepoured.client.render.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class CubemapTexture extends AbstractTextureCQR {

	public CubemapTexture(ResourceLocation originalTextureLocation, ResourceLocation textureLocation) {
		super(originalTextureLocation, textureLocation);
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
	public static ResourceLocation get(ResourceLocation originalTextureLocation) {
		ResourceLocation textureLocation = appendBeforeEnding(originalTextureLocation, "_cubemap");
		TextureManager textureManager = Minecraft.getMinecraft().renderEngine;
		ITextureObject texture = textureManager.getTexture(textureLocation);

		if (!(texture instanceof CubemapTexture)) {
			texture = new CubemapTexture(originalTextureLocation, textureLocation);
			textureManager.loadTexture(textureLocation, texture);
		}

		return textureLocation;
	}

	@Override
	protected boolean onLoadTexture(IResourceManager resourceManager, boolean reload) throws IOException {
		this.deleteGlTexture();

		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, this.getGlTextureId());
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_BASE_LEVEL, 0);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_MAX_LEVEL, 0);
		this.load(resourceManager, appendBeforeEnding(this.originalTextureLocation, "_right"), GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X);
		this.load(resourceManager, appendBeforeEnding(this.originalTextureLocation, "_left"), GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X);
		this.load(resourceManager, appendBeforeEnding(this.originalTextureLocation, "_top"), GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y);
		this.load(resourceManager, appendBeforeEnding(this.originalTextureLocation, "_bottom"), GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y);
		this.load(resourceManager, appendBeforeEnding(this.originalTextureLocation, "_front"), GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z);
		this.load(resourceManager, appendBeforeEnding(this.originalTextureLocation, "_back"), GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);
		return false;
	}

	private void load(IResourceManager resourceManager, ResourceLocation location, int target) throws IOException {
		try (IResource iresource = resourceManager.getResource(location)) {
			BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
			int w = bufferedimage.getWidth();
			int h = bufferedimage.getHeight();
			IntBuffer data = ByteBuffer.allocateDirect(w * h * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
			data.put(bufferedimage.getRGB(0, 0, w, h, new int[w * h], 0, w));
			data.flip();
			GL11.glTexImage2D(target, 0, GL11.GL_RGBA8, w, h, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, data);
		}
	}

}
