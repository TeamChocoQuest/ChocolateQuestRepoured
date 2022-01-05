package team.cqr.cqrepoured.customtextures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;

public class CTResourcepack implements IResourcePack {

	private Set<ResourceLocation> VALID_TEXTURES = new HashSet<>();
	private Map<ResourceLocation, File> FILES = new HashMap<>();
	private Set<String> DOMAIN_SET = new HashSet<>();

	private static CTResourcepack INSTANCE;

	public static CTResourcepack getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CTResourcepack();
		}
		return INSTANCE;
	}

	private CTResourcepack() {
	}

	public static void add(ResourceLocation resLoc, File file) {
		getInstance().addImpl(resLoc, file);
	}

	private void addImpl(ResourceLocation resLoc, File file) {
		try {
			this.VALID_TEXTURES.add(resLoc);
			this.DOMAIN_SET.add(resLoc.getNamespace());
			this.FILES.put(resLoc, file);
		} catch (Exception ex) {
			// Ignore
		}

	}

	public static void remove(ResourceLocation texture) {
		getInstance().removeImpl(texture);
	}

	private void removeImpl(ResourceLocation texture) {
		this.VALID_TEXTURES.remove(texture);
		this.DOMAIN_SET.remove(texture.getNamespace());
		this.FILES.remove(texture);
	}

	public static void clear() {
		getInstance().clearImpl();
	}

	private void clearImpl() {
		this.VALID_TEXTURES.clear();
		this.DOMAIN_SET.clear();
		this.FILES.clear();
	}

	public static void loadAllTextures() {
		getInstance().loadAllTexturesImpl();
	}

	private void loadAllTexturesImpl() {
		TextureManager tm = Minecraft.getInstance().getTextureManager();
		for (Map.Entry<ResourceLocation, File> entry : this.FILES.entrySet()) {
			if (entry.getKey().getPath().endsWith(".mcmeta")) {
				continue;
			}
			DownloadingTexture dlt = new DownloadingTexture(entry.getValue(), null, entry.getKey(), false, null);
			//ThreadDownloadImageData tex = new ThreadDownloadImageData(entry.getValue(), null, entry.getKey(), new UniversalImageBuffer());
			//Looks like this is no longer required in 1.16... was hacky either way
			/*try {
				dlt.setBufferedImage(ImageIO.read(entry.getValue()));
			} catch (IOException e) {
				// Ignore
			}*/
			tm.register(entry.getKey(), dlt);
		}
	}

	@Override
	public InputStream getRootResource(String p_195763_1_) throws IOException {
		return null;
	}

	@Override
	public InputStream getResource(ResourcePackType p_195761_1_, ResourceLocation p_195761_2_) throws IOException {
		File file = this.FILES.getOrDefault(p_195761_2_, null);
		if (file != null) {
			return new FileInputStream(file);
		}
		return null;
	}

	@Override
	public Collection<ResourceLocation> getResources(ResourcePackType p_225637_1_, String p_225637_2_, String p_225637_3_, int p_225637_4_, Predicate<String> p_225637_5_) {
		return null;
	}

	@Override
	public boolean hasResource(ResourcePackType p_195764_1_, ResourceLocation p_195764_2_) {
		return this.FILES.containsKey(p_195764_2_) && p_195764_1_ == ResourcePackType.CLIENT_RESOURCES;
	}

	@Override
	public Set<String> getNamespaces(ResourcePackType p_195759_1_) {
		return this.DOMAIN_SET;
	}

	@Override
	public <T> T getMetadataSection(IMetadataSectionSerializer<T> p_195760_1_) throws IOException {
		return null;
	}

	@Override
	public String getName() {
		return "CQR-NPC-Textures";
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
