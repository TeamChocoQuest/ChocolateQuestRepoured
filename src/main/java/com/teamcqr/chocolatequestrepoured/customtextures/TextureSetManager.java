package com.teamcqr.chocolatequestrepoured.customtextures;

import java.util.HashMap;
import java.util.Map;

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
	
	public static void registerTextureSet(TextureSet set) {
		getInstance().registerTextureSetImpl(set);
	}
	
	private void registerTextureSetImpl(TextureSet set) {
		this.textureSets.put(set.getName(), set);
	}

	private boolean loadTextureSetImpl(String name) {
		
		return false;
	}

}
