package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.CustomTexturesPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.TextureSetPacket;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureSetManager {

	private Map<String, TextureSet> textureSets = new HashMap<>();
	private static TextureSetManager INSTANCE;

	private TextureSetManager() {
	}

	public static TextureSetManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextureSetManager();
		}
		return INSTANCE;
	}

	public static boolean loadTextureSet(String name) {
		return getInstance().loadTextureSetImpl(name);
	}

	public static void registerTextureSet(TextureSet set) {
		getInstance().registerTextureSetImpl(set);
	}

	public static void unloadTextures() {
		try {
			getInstance().unloadTexturesImpl();
		} catch (NoSuchMethodError ex) {
			// Ignore
		}
	}

	public static void sendTexturesToClient(EntityPlayerMP joiningPlayer) {
		try {
			getInstance().sendTexturesToClientImpl(joiningPlayer);
		} catch (NoSuchMethodError ex) {
			// Ignore
		}
	}
	
	public static void sendTextureSetsToClient(EntityPlayerMP player) {
		try {
			getInstance().sendTextureSetsToClientImpl(player);
		} catch (NoSuchMethodError ex) {
			// Ignore
		}
	}

	@SideOnly(Side.SERVER)
	private void sendTexturesToClientImpl(EntityPlayerMP joiningPlayer) {
		// First things first: we gotta send over all loaded textures
		CustomTexturesPacket packet = new CustomTexturesPacket();
		for (File texture : TextureSet.getLoadedTextures()) {
			String base64 = CompressionUtil.encodeFileToBase64(texture);
			String path = texture.getAbsolutePath().replaceFirst(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES.getAbsolutePath(), "");

			packet.addPair(base64, path);
		}
		CQRMain.NETWORK.sendTo(packet, joiningPlayer);
		
		//now we send the texture sets themselves...
		// -> NO, we wait for the request from client
		
	}
	
	@SideOnly(Side.SERVER)
	private void sendTextureSetsToClientImpl(EntityPlayerMP client) {
		for(TextureSet ts : this.textureSets.values()) {
			TextureSetPacket packet = new TextureSetPacket(ts.getName(), ts.getMappings());
			CQRMain.NETWORK.sendTo(packet, client);
		}
	}

	private void registerTextureSetImpl(TextureSet set) {
		this.textureSets.put(set.getName(), set);
	}

	private boolean loadTextureSetImpl(String name) {

		return false;
	}

	@SideOnly(Side.CLIENT)
	private void unloadTexturesImpl() {
		for (TextureSet set : textureSets.values()) {
			for (ResourceLocation rs : set.getTextures()) {
				try {
					TextureUtil.unloadTexture(rs);
				} catch (Exception ex) {
					// Ignore
				}
			}
			set.clearTextureCache();
		}
		this.textureSets.clear();
	}

}
