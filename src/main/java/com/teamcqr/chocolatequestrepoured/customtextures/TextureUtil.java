package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureUtil {

	@SideOnly(Side.CLIENT)
	public static boolean loadTextureInternal(File textureFile, ResourceLocation resLoc) {
		if (textureFile != null && textureFile.exists() && resLoc != null) {
			// This code basically loads a new texture or reloads an existing one
			try {
				CTResourcepack.add(resLoc, textureFile);
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

	@SideOnly(Side.CLIENT)
	public static void reloadResourcepacks() {
		CTResourcepack.loadAllTextures();
		IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
		if (rm instanceof SimpleReloadableResourceManager) {
			((SimpleReloadableResourceManager) rm).reloadResourcePack(CTResourcepack.getInstance());
		} else {
			FMLClientHandler.instance().refreshResources(VanillaResourceType.TEXTURES);
		}
	}

}
