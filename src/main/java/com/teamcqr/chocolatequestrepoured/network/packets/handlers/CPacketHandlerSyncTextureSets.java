package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.customtextures.CompressionUtil;
import com.teamcqr.chocolatequestrepoured.customtextures.TextureSet;
import com.teamcqr.chocolatequestrepoured.customtextures.TextureSetManager;
import com.teamcqr.chocolatequestrepoured.customtextures.TextureUtil;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.CustomTexturesPacket;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerSyncTextureSets implements IMessageHandler<CustomTexturesPacket, IMessage> {

	public CPacketHandlerSyncTextureSets() {
	}

	@Override
	public IMessage onMessage(CustomTexturesPacket message, MessageContext ctx) {
		if(ctx.side.isClient()) {
			Map<String, File> fileMap = new HashMap<>();
			for(Map.Entry<String,String> textureEntry : message.getTextureMap().entrySet()) {
				String path = textureEntry.getKey();
				
				//!!Path already contains the .png extension
				File tf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES, path);
				if (tf != null) {
					if(tf.exists()) {
						tf.delete();
					}
					if (CompressionUtil.decodeBase64ToFile(tf, textureEntry.getValue())) {
						fileMap.put(path, tf);
					} else {
						//TODO: Log warning!!
					}
					
				}
			}
			//Now the texture sets themselves...
			for(Map.Entry<String, Map<ResourceLocation, Set<ResourceLocation>>> tsEntry : message.getTextureSets().entrySet()) {
				TextureSet ts = new TextureSet(tsEntry.getKey());

				for(Map.Entry<ResourceLocation, Set<ResourceLocation>> texEntry : tsEntry.getValue().entrySet()) {
					for(ResourceLocation trs : texEntry.getValue()) {
						File file = fileMap.getOrDefault(trs.getPath(), null);
						if(file != null) {
							TextureUtil.loadTextureInternal(file, trs);
						}
						ts.addTexture(texEntry.getKey(), trs);
					}
				}
				
				TextureSetManager.registerTextureSet(ts);
			}
		}
		
		return null;
	}

}
