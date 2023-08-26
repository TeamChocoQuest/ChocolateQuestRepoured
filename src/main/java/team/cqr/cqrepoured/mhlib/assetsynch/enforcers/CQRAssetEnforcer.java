package team.cqr.cqrepoured.mhlib.assetsynch.enforcers;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import de.dertoaster.multihitboxlib.assetsynch.AbstractAssetEnforcementManager;
import de.dertoaster.multihitboxlib.assetsynch.impl.GlibAnimationEnforcementManager;
import de.dertoaster.multihitboxlib.assetsynch.impl.GlibModelEnforcementManager;
import de.dertoaster.multihitboxlib.assetsynch.impl.TextureEnforcementManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import meldexun.reflectionutil.ReflectionMethod;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRConstants;

public class CQRAssetEnforcer extends AbstractAssetEnforcementManager {
	
	protected static final ReflectionMethod<Optional<byte[]>> METHOD_ENCODE_DATA = new ReflectionMethod<>(AbstractAssetEnforcementManager.class, "encodeData", "encodeData", ResourceLocation.class);
	protected static final ReflectionMethod<Boolean> METHOD_RECEIVE_AND_LOAD_INTERNALLY = new ReflectionMethod<>(AbstractAssetEnforcementManager.class, "receiveAndLoadInternally", "receiveAndLoadInternally", ResourceLocation.class, byte[].class);
	
	protected static final Map<String, AbstractAssetEnforcementManager> MANAGER_MAPPING = createMappings();
	protected static final AbstractAssetEnforcementManager _DEFAULT = new TextureEnforcementManager();

	@Override
	protected File createServerDirectory() {
		return CQRConstants.Resources.Files.CTS_ASSET_DIR;
	}

	protected static Map<String, AbstractAssetEnforcementManager> createMappings() {
		Map<String, AbstractAssetEnforcementManager> result = new Object2ObjectArrayMap<>(3);
		
		result.put(CQRConstants.Resources.Domains.TEXTURE_PACK_DOMAIN, new TextureEnforcementManager());
		result.put(CQRConstants.Resources.Domains.GEO_MODEL_PACK_DOMAIN, new GlibModelEnforcementManager());
		result.put(CQRConstants.Resources.Domains.GEO_ANIMATION_PACK_DOMAIN, new GlibAnimationEnforcementManager());
		
		return result;
	}

	@Override
	protected File createSynchDirectory() {
		return CQRConstants.Resources.Files.CTS_SYNC_DIR;
	}
	
	@Override
	protected File getFileForId(ResourceLocation id) {
		// Resourcelocation domain does not matter here!
		// It all lies within the same folder...
		if (id == null) {
			return this.getSidedDirectory();
		}
		final File destination = new File(this.getSidedDirectory(), id.getPath());
		return destination;
	}
	
	protected AbstractAssetEnforcementManager getInternalManager(final ResourceLocation rs) {
		String[] split = rs.getPath().split("/");
		final String key = split[0];
		AbstractAssetEnforcementManager result = MANAGER_MAPPING.getOrDefault(key, _DEFAULT);
		return result;
	}

	@Override
	protected Optional<byte[]> encodeData(ResourceLocation arg0) {
		AbstractAssetEnforcementManager manager = this.getInternalManager(arg0);
		if (manager == null) {
			return Optional.empty();
		}
		return METHOD_ENCODE_DATA.invoke(manager, arg0);
	}

	//Unused
	@Override
	public String getSubDirectoryName() {
		return null;
	}

	@Override
	protected boolean receiveAndLoadInternally(ResourceLocation arg0, byte[] arg1) {
		AbstractAssetEnforcementManager manager = this.getInternalManager(arg0);
		if (manager == null) {
			return false;
		}
		return METHOD_RECEIVE_AND_LOAD_INTERNALLY.invoke(manager, arg0, arg1);
	}

}
