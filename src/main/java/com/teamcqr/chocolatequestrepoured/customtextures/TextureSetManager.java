package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureSetManager {
	
	private Map<String, TextureSet> textureSets = new HashMap<>();
	private static TextureSetManager INSTANCE;
	
	private TextureSetManager() {
	}
	
	public static TextureSetManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new TextureSetManager();
		}
		return INSTANCE;
	}
	
	public static boolean loadTextureSet(String name) {
		return getInstance().loadTextureSetImpl(name);
	}
	
	public static boolean loadTexture(File textureFile, ResourceLocation resLoc) {
		return getInstance().loadTextureInternal(textureFile, resLoc);
	}
	
	public static void registerTextureSet(TextureSet set) {
		getInstance().registerTextureSetImpl(set);
	}
	
	private void registerTextureSetImpl(TextureSet set) {
		this.textureSets.put(set.getName(), set);
	}

	private boolean loadTextureInternal(File textureFile, ResourceLocation resLoc) {
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
	
	private boolean loadTextureSetImpl(String name) {
		
		return false;
	}

}
