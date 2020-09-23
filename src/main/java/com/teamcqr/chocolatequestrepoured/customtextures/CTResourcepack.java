package com.teamcqr.chocolatequestrepoured.customtextures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class CTResourcepack implements IResourcePack {
	
	public static Set<ResourceLocation> VALID_TEXTURES = new HashSet<>();
	public static HashMap<ResourceLocation, InputStream> INPUT_STREAMS = new HashMap<>();
	
	private static final Set<String> DOMAIN_SET = new HashSet<>();

	static {
		DOMAIN_SET.add(Reference.MODID);
	}
	
	
	@Override
	public InputStream getInputStream(ResourceLocation var1) throws IOException {
		return INPUT_STREAMS.getOrDefault(var1, null);
	}

	@Override
	public boolean resourceExists(ResourceLocation var1) {
		return VALID_TEXTURES.contains(var1);
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
		return Reference.MODID + "-custom-textures";
	}

}
