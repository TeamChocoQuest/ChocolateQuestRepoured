package team.cqr.cqrepoured.mhlib.assetsynch.finders;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.dertoaster.multihitboxlib.assetsynch.assetfinders.AbstractAssetFinder;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.customtextures.TextureSetNew;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;

public class CQRAssetFinder extends AbstractAssetFinder {

	private static final Predicate<ResourceLocation> RS_CHECK_PREDICATE = rs -> !rs.toString().isBlank() && !rs.getNamespace().isBlank() && !rs.getPath().isBlank();
	
	@Override
	public Set<ResourceLocation> get() {
		Set<ResourceLocation> result = new HashSet<>();
		for (TextureSetNew ts : CQRDatapackLoaders.TEXTURE_SETS.getData().values()) {
			if (ts == null) {
				continue;
			}
			result.addAll(ts.getCTSTextures().stream().filter(Objects::nonNull).filter(RS_CHECK_PREDICATE).collect(Collectors.toList()));
		}
		return result;
	}

}
