package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketCustomTextures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ClientPacketHandler {

	public static void handleCTPacketClientside(SPacketCustomTextures message) {
		TextureSetManager.unloadTextures();
		CTResourcepack.clear();
		Map<String, File> fileMap = new HashMap<>();
		for (Map.Entry<String, byte[]> textureEntry : message.getTextureMap().entrySet()) {
			String path = textureEntry.getKey();

			// !!Path already contains the .png extension
			File tf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES_SYNC, path);
			if (tf != null) {
				if (tf.exists()) {
					tf.delete();
				}
				if (tf.getParentFile() != null && !tf.getParentFile().exists()) {
					tf.getParentFile().mkdirs();
				}
				if (CompressionUtil.decodeBase64ToFile(tf, textureEntry.getValue())) {
					fileMap.put(path, tf);
				} else {
					CQRMain.logger.warn("Unable to decode a file using base64! Entry: {}   File: {}", textureEntry.getValue(), tf);
				}

			}
		}
		// Now the texture sets themselves...
		for (Map.Entry<String, Map<ResourceLocation, Set<ResourceLocation>>> tsEntry : message.getTextureSets().entrySet()) {
			TextureSet ts = new TextureSet(tsEntry.getKey());

			for (Map.Entry<ResourceLocation, Set<ResourceLocation>> texEntry : tsEntry.getValue().entrySet()) {
				for (ResourceLocation trs : texEntry.getValue()) {
					File file = fileMap.getOrDefault(trs.getPath(), null);
					if (file != null) {
						TextureUtil.loadTextureInternal(file, trs);
					}
					ts.addTexture(texEntry.getKey(), trs);
				}
			}

			TextureSetManager.registerTextureSet(ts);
		}

		// and finally we need to reload our resourcepack
		TextureUtil.reloadResourcepacks();
	}

}
