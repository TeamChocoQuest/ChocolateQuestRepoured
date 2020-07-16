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
	private static Set<File> textures = new HashSet<>();

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
					File tf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES.getAbsolutePath() + texture + ".png");
					if (tf != null && tf.exists()) {
						textures.add(tf);
						ResourceLocation rs = new ResourceLocation(Reference.MODID + "_ctts_" + name, texture);
						// if(TextureSetManager.loadTexture(tf, rs)) {
						entityTextureMap.getOrDefault(resLoc, new HashSet<ResourceLocation>()).add(rs);
						// }
					}
				}
			}
			if (!entityTextureMap.isEmpty()) {
				TextureSetManager.registerTextureSet(this);
			}
		} catch (Exception ex) {
			entityTextureMap.clear();
		}
	}

	@Nullable
	public ResourceLocation getRandomTextureFor(Entity ent) {
		ResourceLocation ers = EntityList.getKey(ent);
		if (entityTextureMap.containsKey(ers)) {
			Object[] textures = entityTextureMap.get(ers).toArray();
			int indx = random.nextInt(textures.length);
			return (ResourceLocation) textures[indx];
		}
		return null;
	}

	public String getName() {
		return name;
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

	public static Set<File> getLoadedTextures() {
		return new HashSet<File>(TextureSet.textures);
	}
	
	public Map<ResourceLocation, Set<ResourceLocation>> getMappings() {
		return new HashMap<ResourceLocation, Set<ResourceLocation>>(this.entityTextureMap);
	}
}
