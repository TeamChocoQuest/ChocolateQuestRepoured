package team.cqr.cqrepoured.customtextures;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;

public class TextureSet {
	
	public static final Codec<Pair<ResourceLocation, List<String>>> TEXTURE_ENTRIES_CODEC = Codec.pair(ResourceLocation.CODEC.fieldOf("entity").codec(), Codec.STRING.listOf().fieldOf("textures").codec());
	
	public static final Codec<TextureSet> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(
				Codec.STRING.fieldOf("name").forGetter(obj -> {
					return obj.getName();
				}),
				TEXTURE_ENTRIES_CODEC.listOf().fieldOf("textures").forGetter(obj -> {
					return obj.getMappingsForCodec();
				})
		).apply(instance, TextureSet::new);
	});
			
	private static final Random random = new Random();
	private String name;
	private volatile Map<ResourceLocation, Set<ResourceLocation>> entityTextureMap = new HashMap<>();
	private static volatile Map<String, File> files = new HashMap<>();
	private static volatile  Map<String, ResourceLocation> texNameRLMap = new HashMap<>();

	// FOR CLIENT
	public TextureSet(String name) {
		this.name = name;
		this.entityTextureMap.clear();
	}

	public void addTexture(ResourceLocation entity, ResourceLocation texture) {
		this.entityTextureMap.computeIfAbsent(entity, key -> new HashSet<>()).add(texture);
	}
	
	// Codec
	public TextureSet(String name, List<Pair<ResourceLocation, List<String>>> entries) {
		this.name = name.toLowerCase();
		
		for(Pair<ResourceLocation, List<String>> pair : entries) {
			for (String texture : pair.getSecond()) {
				File tf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES, texture + ".png");
				if (tf != null && tf.exists()) {
					files.put(texture + ".png", tf);
					ResourceLocation rs = new ResourceLocation(CQRMain.MODID + "_ctts_" + this.name, texture + ".png");
					texNameRLMap.put(texture + ".png", rs);
					// if(TextureSetManager.loadTexture(tf, rs)) {
					this.addTexture(pair.getFirst(), rs);
					// }

					// Meta file
					File mf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES, texture + ".png.mcmeta");
					if (mf != null && mf.exists()) {
						files.put(texture + ".png.mcmeta", mf);
						rs = new ResourceLocation(CQRMain.MODID + "_ctts_" + this.name, texture + ".png.mcmeta");
						texNameRLMap.put(texture + ".png.mcmeta", rs);
					}
				}
			}
		}
	}

	// FOR SERVER
	public TextureSet(Properties config, String name) {
		this.name = name.toLowerCase();
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
				texturesString = texturesString.replaceAll(" ", "");
				// This strings represent the FILE PATHS, not the actual resource locations
				for (String texture : texturesString.split(",")) {
					File tf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES, texture + ".png");
					if (tf != null && tf.exists()) {
						files.put(texture + ".png", tf);
						ResourceLocation rs = new ResourceLocation(CQRMain.MODID + "_ctts_" + this.name, texture + ".png");
						texNameRLMap.put(texture + ".png", rs);
						// if(TextureSetManager.loadTexture(tf, rs)) {
						this.addTexture(resLoc, rs);
						// }

						// Meta file
						File mf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES, texture + ".png.mcmeta");
						if (mf != null && mf.exists()) {
							files.put(texture + ".png.mcmeta", mf);
							rs = new ResourceLocation(CQRMain.MODID + "_ctts_" + this.name, texture + ".png.mcmeta");
							texNameRLMap.put(texture + ".png.mcmeta", rs);
						}
					}
				}
			}
			if (!this.entityTextureMap.isEmpty()) {
				TextureSetManager.registerTextureSet(this);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			this.entityTextureMap.clear();
		}
	}

	@Nullable
	public ResourceLocation getRandomTextureFor(Entity ent) {
		ResourceLocation ers = EntityList.getKey(ent);
		// System.out.println("Searching texture for " + ers.toString() + " in texture set: " + name);
		if (this.entityTextureMap.containsKey(ers) && !this.entityTextureMap.get(ers).isEmpty()) {
			Object[] textures = this.entityTextureMap.get(ers).toArray();
			int indx = random.nextInt(textures.length);
			// System.out.println("Returning: " + ((ResourceLocation) textures[indx]).toString());
			return (ResourceLocation) textures[indx];
		}
		// System.out.println("No textures defined! Returning null as resloc");
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
		TextureSet.files.clear();
	}

	public static Map<String, File> getLoadedFiles() {
		return new HashMap<>(TextureSet.files);
	}

	public static ResourceLocation getResLocOfTexture(String textureFilePath) {
		return texNameRLMap.getOrDefault(textureFilePath, new ResourceLocation("cqrepoured:wtf"));
	}

	public Map<ResourceLocation, Set<ResourceLocation>> getMappings() {
		return new HashMap<>(this.entityTextureMap);
	}
	
	public List<Pair<ResourceLocation, List<String>>> getMappingsForCodec() {
		List<Pair<ResourceLocation, List<String>>> result = new ArrayList<>();
		for(Map.Entry<ResourceLocation, Set<ResourceLocation>> entry : this.getMappings().entrySet()) {
			//Now get the files...
			List<String> list = new ArrayList<>();
			for(ResourceLocation textureID : entry.getValue()) {
				String fileName = textureID.getNamespace();
				if(fileName.endsWith(".png")) {
					fileName = fileName.substring(0, fileName.length() - ".png".length());
				} else if(fileName.endsWith(".png.mcmeta")) {
					fileName = fileName.substring(0, fileName.length() - ".png.mcmeta".length());
				}
				list.add(fileName);
			}
			result.add(Pair.of(entry.getKey(), list));
		}
		
		return result;
	}
}
