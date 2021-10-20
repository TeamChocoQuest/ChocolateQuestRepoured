package team.cqr.cqrepoured.client.glowingeyes;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.Reference;

public class GlowingEyesResourcepack implements IResourcePack {
	
	public static final GlowingEyesResourcepack INSTANCE = new GlowingEyesResourcepack();

	private Map<ResourceLocation, File> FILES = new ConcurrentHashMap<>();
	private Set<String> DOMAIN_SET = new HashSet<>();
	private Set<String> NON_GLOW_TEXTURES = new ConcurrentSet<>();
	private Set<ResourceLocation> KNOWN_GLOW_TEXTURES = new HashSet<>();
	
	protected static final String SINGLE_NUMBER_REGEX = "[1-9]+[0-9]*";
	protected static final String PATTERN_REGEX = SINGLE_NUMBER_REGEX + "|" + SINGLE_NUMBER_REGEX + "-" + SINGLE_NUMBER_REGEX + "|" + SINGLE_NUMBER_REGEX; 

	private static InputStream getEmptyTextureStream() {
		return null;
	}

	private static boolean hasEyeConfigFile(ResourceLocation resloc) {
		try {
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(resloc.getNamespace(), resloc.getPath() + ".eyes")).getInputStream();
			stream.close();
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	@Override
	public InputStream getInputStream(final ResourceLocation location) throws IOException {
		// First: Check if we have the texture in the cache
		File file = this.FILES.getOrDefault(location, null);
		if (file != null) {
			return new FileInputStream(file);
		}
		// If it is in the cache, return it
		if (KNOWN_GLOW_TEXTURES.contains(location)) {
			return getEmptyTextureStream();
		}
		ResourceLocation original = new ResourceLocation(location.getPath().replace('.', ':'));

		final IResource originalStream = Minecraft.getMinecraft().getResourceManager().getResource(original);
		final IResource eyeConfigStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(original.getNamespace(), original.getPath() + ".eyes"));

		new Thread(() -> {
			try {
				InputStream origStream = originalStream.getInputStream();
				final BufferedImage bi = ImageIO.read(origStream);
				origStream.close();
				BufferedImage newImage = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
				originalStream.close();

				InputStream configStream = eyeConfigStream.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(configStream));
				configStream.close();
				while (reader.ready()) {
					String line = reader.readLine();
					//DONE: Create regex that can detect the pattern
					if(!line.matches(PATTERN_REGEX)) {
						continue;
					}
					String par1 = line.split("-")[0];
					String[] split = par1.split("|");
					Tuple<Integer, Integer> pos1 = new Tuple<>(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
					String par2 = line.split("-")[2];
					split = par2.split("|");
					Tuple<Integer, Integer> pos2 = new Tuple<>(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
					assert pos1.getFirst() <= pos2.getFirst();
					assert pos1.getSecond() <= pos2.getSecond();
					for (int px = pos1.getFirst(); px <= pos2.getFirst(); px++) {
						for (int py = pos1.getSecond(); py <= pos2.getSecond(); py++) {
							newImage.setRGB(px, py, bi.getRGB(px, py));
						}
					}
				}
				File outputFile = new File(CQRMain.CQ_GLOWING_TEXTURES_FOLDER, original.toString());
				if (outputFile != null) {
					if (outputFile.exists()) {
						outputFile.delete();
					}
					if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
						outputFile.getParentFile().mkdirs();
					}
				}
				ImageIO.write(newImage, "png", outputFile);

				FILES.put(location, outputFile);
			} catch (Exception ex) {
				NON_GLOW_TEXTURES.add(location.toString());
			}
		}).start();

		return null;
	}

	@Override
	public boolean resourceExists(ResourceLocation location) {
		if (location.getNamespace().equals(Reference.MODID + "_glowing_eyes") || FILES.containsKey(location) || hasEyeConfigFile(location)) {
			if (NON_GLOW_TEXTURES.contains(location.toString())) {
				return false;
			}
			// The glow ressource location store the original rl in their path
			ResourceLocation original = new ResourceLocation(location.getPath().replace('.', ':'));
			try {
				// I know this is dirty but it helps us, the exception gets thrown when the texture does not exist
				Minecraft.getMinecraft().getResourceManager().getResource(original);
				return true;
			} catch (IOException ioe) {
				return false;
			}
		}
		return false;
	}

	@Override
	public Set<String> getResourceDomains() {
		return DOMAIN_SET;
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	@Override
	public String getPackName() {
		return "CQR-Glowing Eyes Repository";
	}

}
