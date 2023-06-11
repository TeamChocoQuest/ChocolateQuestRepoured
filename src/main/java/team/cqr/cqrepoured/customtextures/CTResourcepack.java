package team.cqr.cqrepoured.customtextures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;

public class CTResourcepack implements PackResources {
	
	/*public static final IPackFinder PACK_FINDER = new IPackFinder() {
		
		@Override
		public void loadPacks(Consumer<ResourcePackInfo> pInfoConsumer, IFactory pInfoFactory) {
			pInfoConsumer.accept(ResourcePackInfo.create(CQRMain.MODID + ":CQR-ResourcePack", true, CTResourcepack::getInstance, pInfoFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILT_IN));
			
		}
	};*/

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
			NativeImage ni;
			try {
				ni = NativeImage.read(new FileInputStream(entry.getValue()));
				
				DynamicTexture dynTex = new DynamicTexture(ni);
				tm.register(entry.getKey(), dynTex);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//ThreadDownloadImageData tex = new ThreadDownloadImageData(entry.getValue(), null, entry.getKey(), new UniversalImageBuffer());
			//Looks like this is no longer required in 1.16... was hacky either way
			/*try {
				dlt.setBufferedImage(ImageIO.read(entry.getValue()));
			} catch (IOException e) {
				// Ignore
			}*/
		}
	}

	@Override
	public boolean hasResource(PackType p_195764_1_, ResourceLocation p_195764_2_) {
		return this.FILES.containsKey(p_195764_2_) && p_195764_1_ == PackType.CLIENT_RESOURCES;
	}
	
	@Override
	public Set<String> getNamespaces(PackType p_195759_1_) {
		return this.DOMAIN_SET;
	}

	@Override
	public String packId() {
		return "CQR-NPC-Textures";
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IoSupplier<InputStream> getRootResource(String... pElements) {
		return null;
	}

	@Override
	public IoSupplier<InputStream> getResource(PackType pPackType, ResourceLocation pLocation) {
		File file = this.FILES.getOrDefault(pLocation, null);
		if (file != null) {
			return () -> new FileInputStream(file);
		}
		return null;
	}

	@Override
	public void listResources(PackType pPackType, String pNamespace, String pPath, ResourceOutput pResourceOutput) {
		
	}

	@Override
	public <T> T getMetadataSection(MetadataSectionSerializer<T> pDeserializer) throws IOException {
		return null;
	}

}
