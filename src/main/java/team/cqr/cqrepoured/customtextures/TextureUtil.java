package team.cqr.cqrepoured.customtextures;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.resource.VanillaResourceType;
import net.minecraftforge.fml.client.FMLClientHandler;

public class TextureUtil {

	@OnlyIn(Dist.CLIENT)
	public static boolean loadFileInResourcepack(File textureFile, ResourceLocation resLoc) {
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

	@OnlyIn(Dist.CLIENT)
	public static boolean unloadTexture(ResourceLocation texture) {
		try {
			CTResourcepack.remove(texture);
			return true;
		} catch (Exception ex) {
			// Ignore
		}
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public static void reloadResourcepacks() {
		CTResourcepack.loadAllTextures();
		IResourceManager rm = Minecraft.getInstance().getResourceManager();
		if (rm instanceof SimpleReloadableResourceManager) {
			((SimpleReloadableResourceManager) rm).reloadResourcePack(CTResourcepack.getInstance());
		} else {
			FMLClientHandler.instance().refreshResources(VanillaResourceType.TEXTURES);
		}
	}

}
