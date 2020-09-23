package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.io.FileInputStream;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureUtil {
	
	@SideOnly(Side.CLIENT)
	public static boolean loadTextureInternal(File textureFile, ResourceLocation resLoc) {
		if (textureFile != null && textureFile.exists() && resLoc != null) {
			// This code basically loads a new texture or reloads an existing one
			try {
				CTResourcepack.add(resLoc, new FileInputStream(textureFile));
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
			CTResourcepack.remove(texture);
			return true;
		} catch (Exception ex) {
			// Ignore
		}
		return false;
	}

}
