package team.cqr.cqrepoured.resources;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import team.cqr.cqrepoured.CQRConstants;

public class CQRFolderPackResources extends PathPackResources {

	CQRFolderPackResources(Path pRoot) {
		super(CQRConstants.PACK_RESOURCES_ID, pRoot, true);
	}
	
	// Changes so we don't have to keep the assets/<domain>/... structure => it will just work for cqr and will act like it already is in assets/cqrepoured
	@Override
	public IoSupplier<InputStream> getResource(PackType pPackType, ResourceLocation pLocation) {
		if (pPackType.equals(PackType.CLIENT_RESOURCES)) {
			return null;
		}
		if (!pLocation.getNamespace().equals(CQRConstants.MODID)) {
			return null;
		}
		return PathPackResources.getResource(pLocation, this.root);
	}
	
	@Override
	public void listResources(PackType pPackType, String pNamespace, String pPath, ResourceOutput pResourceOutput) {
		if (!pNamespace.equals(CQRConstants.MODID)) {
			return;
		}
		if (!pPackType.equals(PackType.SERVER_DATA)) {
			return;
		}
		
		super.listResources(pPackType, pNamespace, pPath, pResourceOutput);
	}
	
	@Override
	public Set<String> getNamespaces(PackType pType) {
		return pType.equals(PackType.SERVER_DATA) ? Set.of(CQRConstants.MODID) : Set.of();
	}

}
