package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

public class TextureSet {

	private static final Random random = new Random();
	private String name;
	private Map<ResourceLocation, Set<ResourceLocation>> entityTextureMap = new HashMap<>();
	private static Map<String, File> textures = new HashMap<>();
	private static Map<String, ResourceLocation> texNameRLMap = new HashMap<>();

	// FOR CLIENT
	public TextureSet(String name) {
		this.name = name;
		this.entityTextureMap.clear();
	}

	public void addTexture(ResourceLocation entity, ResourceLocation texture) {
		this.entityTextureMap.computeIfAbsent(entity, key -> new HashSet<>()).add(texture);
	}

	// FOR SERVER
	public TextureSet(Properties config, String name) {
		this.name = name;
		try {
			for (String entry : config.stringPropertyNames()) {
				if (entry.startsWith("#")) {
					continue;
				}
				String rlkey = entry.replace('.', ':');
				ResourceLocation resLoc = new ResourceLocation(rlkey);
				String texturesString = config.getProperty(entry, "");
				if (texturesString.isEmpty()) {
					continue;
				}
				texturesString.replaceAll(" ", "");
				// This strings represent the FILE PATHS, not the actual resource locations
				for (String texture : texturesString.split(",")) {
					File tf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES, texture + ".png");
					if (tf != null && tf.exists()) {
						textures.put(texture + ".png", tf);
						ResourceLocation rs = new ResourceLocation(Reference.MODID + "_ctts_" + this.name, texture + ".png");
						texNameRLMap.put(texture + ".png", rs);
						// if(TextureSetManager.loadTexture(tf, rs)) {
						this.addTexture(resLoc, rs);
						// }
					}
				}
			}
			if (!this.entityTextureMap.isEmpty()) {
				TextureSetManager.registerTextureSet(this);
			}
		} catch (Exception ex) {
			this.entityTextureMap.clear();
		}
	}

	@Nullable
	public ResourceLocation getRandomTextureFor(Entity ent) {
		ResourceLocation ers = EntityList.getKey(ent);
		System.out.println("Searching texture for " + ers.toString() + " in texture set: " + name);
		if (this.entityTextureMap.containsKey(ers) && this.entityTextureMap.get(ers).size() > 0) {
			Object[] textures = this.entityTextureMap.get(ers).toArray();
			int indx = random.nextInt(textures.length);
			System.out.println("Returning: " + ((ResourceLocation) textures[indx]).toString());
			return (ResourceLocation) textures[indx];
		}
		System.out.println("No textures defined! Returning null as resloc");
		return null;
	}

	public String getName() {
		return this.name;
	}

	public Set<ResourceLocation> getTextures() {
		Set<ResourceLocation> ret = new HashSet<>();
		for (Set<ResourceLocation> st : this.entityTextureMap.values()) {
			try {
				ret.addAll(st);
			} catch (Exception ex) {

			}
		}
		return ret;
	}

	public void clearTextureCache() {
		for (Set<ResourceLocation> st : this.entityTextureMap.values()) {
			try {
				st.clear();
			} catch (Exception ex) {

			}
		}
		this.entityTextureMap.clear();
		TextureSet.textures.clear();
	}

	public static Map<String, File> getLoadedTextures() {
		return new HashMap<>(TextureSet.textures);
	}

	public static ResourceLocation getResLocOfTexture(String textureFilePath) {
		return texNameRLMap.getOrDefault(textureFilePath, new ResourceLocation("cqrepoured:wtf"));
	}

	public Map<ResourceLocation, Set<ResourceLocation>> getMappings() {
		return new HashMap<>(this.entityTextureMap);
	}
}
