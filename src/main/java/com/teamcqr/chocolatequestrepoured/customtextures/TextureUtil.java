package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.io.FileInputStream;

import com.teamcqr.chocolatequestrepoured.util.ReflectionHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureUtil {

	@SideOnly(Side.CLIENT)
	public static boolean loadTextureInternal(File textureFile, ResourceLocation resLoc) {
		if (textureFile != null && textureFile.exists() && resLoc != null) {
			// This code basically loads a new texture or reloads an existing one
			try {
				CTResourcepack.VALID_TEXTURES.remove(resLoc);
				CTResourcepack.INPUT_STREAMS.put(resLoc, new FileInputStream(textureFile));
				TextureManager tm = Minecraft.getMinecraft().getTextureManager();
				tm.deleteTexture(resLoc);
				ITextureObject tex = new ThreadDownloadImageData(textureFile, null, resLoc, new ImageBufferDownload());
				tex.loadTexture((IResourceManager) ReflectionHelper.reflectGetField(tm, new String[]{"resourceManager"}).get(tm) );
				tm.bindTexture(resLoc);
				tm.loadTexture(resLoc, tex);
				return true;
			} catch (Exception ex) {
				// Ignore
			}
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public static boolean unloadTexture(ResourceLocation texture) {
		try {
			TextureManager tm = Minecraft.getMinecraft().getTextureManager();
			tm.deleteTexture(texture);
			CTResourcepack.VALID_TEXTURES.remove(texture);
			return true;
		} catch (Exception ex) {
			// Ignore
		}
		return false;
	}

}
