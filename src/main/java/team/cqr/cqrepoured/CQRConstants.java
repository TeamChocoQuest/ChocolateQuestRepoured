package team.cqr.cqrepoured;

import java.io.File;
import java.nio.file.Path;

import de.dertoaster.multihitboxlib.util.LazyLoadField;
import net.minecraftforge.fml.loading.FMLPaths;
import team.cqr.cqrepoured.customtextures.TextureSetNew;

public class CQRConstants {
	
	public static final String MODID = "cqrepoured";
	
	public static class Resources {
		
		public static class Files {
			public static final LazyLoadField<File> CQR_CONFIG_DIR = new LazyLoadField<>(() -> {
				final Path configDir = FMLPaths.CONFIGDIR.get();
				final File result = new File(configDir.toFile(), "mhlib");
				return result;
			});
			public static final File CTS_SYNC_DIR = new File(CQR_CONFIG_DIR.get(), "_sync");
			public static final File CTS_ASSET_DIR = new File(CQR_CONFIG_DIR.get(), "assetsynch");
		}
		
		public static class Domains {
			public static final String CQR_DOMAIN = MODID;
			public static final String CTS_DOMAIN_BASE = MODID + "_ctts";
			
			public static final String TEXTURE_PACK_DOMAIN = "textures";
			public static final String GEO_MODEL_PACK_DOMAIN = "geo";
			public static final String GEO_ANIMATION_PACK_DOMAIN = "animations";
			
			public static final String generateCTSDomain(final TextureSetNew ts) {
				if (ts.getId() == null) {
					throw new IllegalStateException("Texture sets have not been initialized yet!");
				}
				String[] split = ts.getId().getPath().split("/");
				return CTS_DOMAIN_BASE + "_" + split[split.length - 1];
			}
		}
	}
	
	public static class Entity {
		public static class PirateCaptain {
			public static final String PARROT_BONE_NAME = "shoulderEntityLeft";
		}
	}

}
