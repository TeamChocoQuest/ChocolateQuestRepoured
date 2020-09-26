package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.CustomTexturesPacket;
import com.teamcqr.chocolatequestrepoured.util.reflection.ReflectionField;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

public class ClientPacketHandler {
	
	private static  final  ReflectionField<IReloadableResourceManager> RESOURCE_MANAGER = new ReflectionField<>(Minecraft.class, "field_110451_am", "resourceManager");
	
	public static void handleCTPacketClientside(CustomTexturesPacket message) {
		CTResourcepack.clear();
		Map<String, File> fileMap = new HashMap<>();
		for (Map.Entry<String, String> textureEntry : message.getTextureMap().entrySet()) {
			String path = textureEntry.getKey();

			// !!Path already contains the .png extension
			File tf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES, path);
			if (tf != null) {
				if (tf.exists()) {
					tf.delete();
				}
				if (CompressionUtil.decodeBase64ToFile(tf, textureEntry.getValue())) {
					fileMap.put(path, tf);
				} else {
					// TODO: Log warning!!
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
		IReloadableResourceManager rm = RESOURCE_MANAGER.get(Minecraft.getMinecraft());
		if(rm instanceof SimpleReloadableResourceManager) {
			((SimpleReloadableResourceManager)rm).reloadResourcePack(CTResourcepack.getInstance());
		} else {
			Minecraft.getMinecraft().scheduleResourcesRefresh();
		}
		CTResourcepack.loadAllTextures();
	}
	
}
