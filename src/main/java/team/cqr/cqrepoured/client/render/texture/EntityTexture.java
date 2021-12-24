package team.cqr.cqrepoured.client.render.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection.Section;
import team.cqr.cqrepoured.client.resources.data.PartMetadataSection;

public class EntityTexture extends AbstractTextureCQR {

	private final Set<String> partsToRender = new HashSet<>();

	public EntityTexture(ResourceLocation textureLocation) {
		super(textureLocation, textureLocation);
	}

	public static EntityTexture get(ResourceLocation textureLocation) {
		TextureManager textureManager = Minecraft.getMinecraft().renderEngine;
		ITextureObject texture = textureManager.getTexture(textureLocation);

		if (!(texture instanceof EntityTexture)) {
			texture = new EntityTexture(textureLocation);
			textureManager.loadTexture(textureLocation, texture);
		}

		return (EntityTexture) texture;
	}

	@Override
	protected boolean onLoadTexture(IResourceManager resourceManager, boolean reload) throws IOException {
		this.partsToRender.clear();
		return true;
	}

	@Override
	protected BufferedImage onLoadMetadata(BufferedImage image, IResource resource) throws IOException {
		GlowingMetadataSection glowingSections = resource.getMetadata("glowsections");

		if (glowingSections != null) {
			for (Section section : glowingSections.getGlowingSections()) {
				for (int x = section.getMinX(); x < section.getMaxX(); x++) {
					for (int y = section.getMinY(); y < section.getMaxY(); y++) {
						image.setRGB(x, y, 0);
					}
				}
			}
		}

		PartMetadataSection partmetadatasection = resource.getMetadata("parts");

		if (partmetadatasection != null) {
			this.partsToRender.addAll(partmetadatasection.getParts());
		}

		return image;
	}

	public Collection<String> getPartsToRender() {
		return Collections.unmodifiableCollection(partsToRender);
	}

}
