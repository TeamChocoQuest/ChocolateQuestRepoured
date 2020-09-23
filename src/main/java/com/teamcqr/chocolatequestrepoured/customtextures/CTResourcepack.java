package com.teamcqr.chocolatequestrepoured.customtextures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class CTResourcepack implements IResourcePack {

	private Set<ResourceLocation> VALID_TEXTURES = new HashSet<>();
	private HashMap<ResourceLocation, InputStream> INPUT_STREAMS = new HashMap<>();
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

	public static void add(ResourceLocation resLoc, InputStream stream) {
		getInstance().addImpl(resLoc, stream);
	}

	private void addImpl(ResourceLocation resLoc, InputStream stream) {
		this.VALID_TEXTURES.add(resLoc);
		this.INPUT_STREAMS.put(resLoc, stream);
		this.DOMAIN_SET.add(resLoc.getNamespace());
	}

	public static void remove(ResourceLocation texture) {
		getInstance().removeImpl(texture);
	}

	private void removeImpl(ResourceLocation texture) {
		this.INPUT_STREAMS.remove(texture);
		this.VALID_TEXTURES.remove(texture);
		this.DOMAIN_SET.remove(texture.getNamespace());
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
	}

}
