package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureUtil {
	
	@SideOnly(Side.CLIENT)
	public static boolean loadTextureInternal(File textureFile, ResourceLocation resLoc) {
		if(textureFile != null && textureFile.exists() && resLoc != null) {
			//This code basically loads a new texture or reloads an existing one
			try {
				TextureManager tm = Minecraft.getMinecraft().getTextureManager();
				tm.deleteTexture(resLoc);
				ITextureObject tex = new ThreadDownloadImageData(textureFile, null, resLoc, new ImageBufferDownload());
				tm.loadTexture(resLoc, tex);
				return true;
			} catch(Exception ex) {
				//Ignore
			}
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean unloadTexture(ResourceLocation texture) {
		try {
			TextureManager tm = Minecraft.getMinecraft().getTextureManager();
			tm.deleteTexture(texture);
			return true;
		} catch(Exception ex) {
			//Ignore
		}
		return false;
	}
	
}
