package com.teamcqr.chocolatequestrepoured.customtextures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class CTResourcepack implements IResourcePack {

	private Set<ResourceLocation> VALID_TEXTURES = new HashSet<>();
	private HashMap<ResourceLocation, InputStream> INPUT_STREAMS = new HashMap<>();
	private HashMap<ResourceLocation, File> FILES = new HashMap<>();
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

	@Override
	public InputStream getInputStream(ResourceLocation var1) throws IOException {
		return INPUT_STREAMS.getOrDefault(var1, null);
	}

	@Override
	public boolean resourceExists(ResourceLocation var1) {
		return INPUT_STREAMS.containsKey(var1);
	}

	@Override
	public Set<String> getResourceDomains() {
		return DOMAIN_SET;
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer var1, String var2) throws IOException {
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	@Override
	public String getPackName() {
		return "CQR-NPC-Textures";
	}

	public static void add(ResourceLocation resLoc, File file) {
		getInstance().addImpl(resLoc, file);
	}

	private void addImpl(ResourceLocation resLoc, File file) {
		try {
			this.INPUT_STREAMS.put(resLoc, new FileInputStream(file));
			
			this.VALID_TEXTURES.add(resLoc);
			this.DOMAIN_SET.add(resLoc.getNamespace());
			this.FILES.put(resLoc, file);
		} catch(Exception ex) {
			//Ignore
		}
		
	}

	public static void remove(ResourceLocation texture) {
		getInstance().removeImpl(texture);
	}

	private void removeImpl(ResourceLocation texture) {
		this.INPUT_STREAMS.remove(texture);
		this.VALID_TEXTURES.remove(texture);
		this.DOMAIN_SET.remove(texture.getNamespace());
		this.FILES.remove(texture);
	}

	public static void clear() {
		getInstance().clearImpl();
	}

	private void clearImpl() {
		this.INPUT_STREAMS.values().forEach(new Consumer<InputStream>() {

			@Override
			public void accept(InputStream t) {
				try {
					t.close();
				} catch (IOException e) {
				}
			}
		});
		this.INPUT_STREAMS.clear();
		this.VALID_TEXTURES.clear();
		this.DOMAIN_SET.clear();
		this.FILES.clear();
	}

	public static void loadAllTextures() {
		getInstance().loadAllTexturesImpl();
	}

	private void loadAllTexturesImpl() {
		TextureManager tm = Minecraft.getMinecraft().getTextureManager();
		for (Map.Entry<ResourceLocation, File> entry : this.FILES.entrySet()) {
			ThreadDownloadImageData tex = new ThreadDownloadImageData(entry.getValue(), null, entry.getKey(), new ImageBufferDownload());
			try {
				tex.setBufferedImage(ImageIO.read(entry.getValue()));
			} catch (IOException e) {
				//Ignore
			}
			tm.loadTexture(entry.getKey(), tex);
		}
	}

}
