package team.cqr.cqrepoured.common;

import java.io.File;
import java.nio.file.Path;

import de.dertoaster.multihitboxlib.util.LazyLoadField;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import team.cqr.cqrepoured.common.registration.RegistrationIDSupplier;

public class CQRConstants {
	
	/**
	 * Use {@link CQRepoured#MODID} instead.
	 */
	@Deprecated
	public static final String MODID = "cqrepoured";
	public static final String PACK_RESOURCES_ID = MODID + "_folder_resources";
	
	public static class JEI {
		public static final ResourceLocation PLUGIN_ID = CQRepoured.prefix("jei_plugin");
		
		public static final ResourceLocation NPC_TRADE_CATEGORY_UID = CQRepoured.prefix("jei/category/npc_trade");
	}
	
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
			
			public static final String generateCTSDomain(final RegistrationIDSupplier ris) {
				if (ris.getId() == null) {
					throw new IllegalStateException("Texture sets have not been initialized yet!");
				}
				String[] split = ris.getId().getPath().split("/");
				return CTS_DOMAIN_BASE + "_" + split[split.length - 1];
			}
		}
	}
	
	public static class Entity {
		public static class PirateCaptain {
			public static final String PARROT_BONE_NAME = "shoulderEntityLeft";
		}
	}

	public static class NBT {
		public static final String KEY_TRADE_PROFILE_DATA = "trade-profile-data";
		public static final String KEY_ENTITY_VARIANT = "entity-variant";
		public static final String KEY_ENTITY_VARIANT_ASSETS = "entity-variant-assets";
		
		public static class StructureTemplate {
			public static final String KEY_CQR_FILE_VERSION = "cqr-file-version";
			public static final String KEY_AUTHOR = "author";
			public static final String KEY_SIZE = "size";
			public static final String KEY_NBT_TAG_LIST = "block-nbt-data";
			public static final String KEY_BLOCKSTATES_LIST = "blockstates";
			public static final String KEY_BLOCKDATA_BYTES = "byte-data";
		}
	}
	
	public static class Translation {
		
		protected static String prefix(String ident) {
			return MODID + "." + ident;
		}
		
		protected static String prefixDescription(String ident) {
			return prefix("description." + ident);
		}
		
		protected static String prefixGui(String gui, String path) {
			return prefixDescription("gui." + gui + "." + path);
		}
		
		public static class Trade {
			public static final String TRADE_RESULT_SUCCESS = prefixGui("trade", "result.success");
			public static final String TRADE_RESULT_NO_TRADE = prefixGui("trade", "result.no_trade");
			public static final String TRADE_RESULT_NO_INPUT = prefixGui("trade", "result.no_input");
			public static final String TRADE_RESULT_NO_STOCK = prefixGui("trade", "result.no_stock");
			public static final String TRADE_RESULT_INPUT_TYPE_INVALID = prefixGui("trade", "result.item_mismatch");
			public static final String TRADE_RESULT_CUSTOMER_RULES_NOT_MET = prefixGui("trade", "result.customer_rules_not_met");
		}
	}
}
