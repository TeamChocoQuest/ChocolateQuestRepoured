package team.cqr.cqrepoured.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.util.GsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackFileNotFoundException;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.util.JsonObjectBuilder;

public class ModFolderPack implements IResourcePack {

	private static final Logger LOGGER = LogManager.getLogger();
	private final String name;
	private final String modid;
	private final Path directory;

	public ModFolderPack(String name, String modid, Path directory) {
		this.name = name;
		this.modid = modid;
		this.directory = directory;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public InputStream getRootResource(String pFileName) throws IOException {
		if (!pFileName.contains("/") && !pFileName.contains("\\")) {
			return this.getResource(pFileName);
		} else {
			throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
		}
	}

	@Override
	public InputStream getResource(ResourcePackType pType, ResourceLocation pLocation) throws IOException {
		if (!this.modid.equals(pLocation.getNamespace())) {
			throw new ResourcePackFileNotFoundException(this.directory.toFile(), this.getResourcePath(pType, pLocation));
		}
		return this.getResource(this.getResourcePath(pType, pLocation));
	}

	@Override
	public Collection<ResourceLocation> getResources(ResourcePackType pType, String pNamespace, String pPath, int pMaxDepth, Predicate<String> pFilter) {
		try {
			Path p1 = this.directory.resolve(pType.getDirectory());
			Path p2 = p1.resolve(pPath);
			if (!Files.exists(p2)) {
				return Collections.emptyList();
			}
			return Files.find(p1.resolve(pPath), pMaxDepth, (p, a) -> {
				if (!a.isRegularFile()) {
					return false;
				}
				String fileName = p.getFileName()
						.toString();
				return !fileName.endsWith(".mcmeta") && pFilter.test(fileName);
			})
					.map(p1::relativize)
					.map(Path::toString)
					.map(s -> s.replace('\\', '/'))
					.map(s -> {
						try {
							return new ResourceLocation(this.modid, s);
						} catch (ResourceLocationException e) {
							LOGGER.error(e.getMessage());
							return null;
						}
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	@Override
	public boolean hasResource(ResourcePackType pType, ResourceLocation pLocation) {
		if (!this.modid.equals(pLocation.getNamespace())) {
			return false;
		}
		return this.hasResource(this.getResourcePath(pType, pLocation));
	}

	@Override
	public Set<String> getNamespaces(ResourcePackType pType) {
		return Collections.singleton(this.modid);
	}

	@Override
	public <T> T getMetadataSection(IMetadataSectionSerializer<T> pDeserializer) throws IOException {
		JsonObject json = JsonObjectBuilder.builder()
				.add("pack", JsonObjectBuilder.builder()
						.addProperty("description", this.name + " resources")
						.addProperty("pack_format", 6)
						.build())
				.build();
		if (!json.has(pDeserializer.getMetadataSectionName())) {
			return null;
		}
		try {
			return pDeserializer.fromJson(GsonHelper.getAsJsonObject(json, pDeserializer.getMetadataSectionName()));
		} catch (JsonParseException e) {
			LOGGER.error("Couldn't load {} metadata", pDeserializer.getMetadataSectionName(), e);
			return null;
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void close() {
		// nothing to do
	}

	private InputStream getResource(String pResourcePath) throws IOException {
		Path path = this.directory.resolve(pResourcePath);
		if (!Files.exists(path)) {
			throw new ResourcePackFileNotFoundException(this.directory.toFile(), pResourcePath);
		}
		if (!Files.isRegularFile(path)) {
			throw new ResourcePackFileNotFoundException(this.directory.toFile(), pResourcePath);
		}
		return Files.newInputStream(path);
	}

	private boolean hasResource(String pResourcePath) {
		Path path = this.directory.resolve(pResourcePath);
		if (!Files.exists(path)) {
			return false;
		}
		if (!Files.isRegularFile(path)) {
			return false;
		}
		return true;
	}

	private String getResourcePath(ResourcePackType pType, ResourceLocation pLocation) {
		return pType.getDirectory() + "/" + pLocation.getPath();
	}

}
