package team.cqr.cqrepoured.resources;

import java.io.File;
import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.CQRMain;

public class CQRRepositorySource implements RepositorySource {

	private static final RepositorySource INSTANCE = new CQRRepositorySource();
	public static final PackSource PACK_SOURCE = PackSource.DEFAULT;
	private static final Pack.Info SERVER_PACK_INFO = null;
	
	public static RepositorySource instance() {
		return INSTANCE;
	}
	
	private CQRRepositorySource() {
		super();
	}
	
	@Override
	public void loadPacks(Consumer<Pack> pOnLoad) {
		File folder = CQRMain.CQ_CONFIG_FOLDER;
		if (folder != null) {
			if (!folder.exists() || !folder.isDirectory()) {
				if (folder.isDirectory()) {
					if (!folder.delete()) {
						// TODO: Log
					}
				}
				if (!folder.mkdirs()) {
					// TODO: Log
				}
			}
			final PackResources packResources = new CQRFolderPackResources(folder.toPath());
			
			Pack pack = Pack.create(
					CQRConstants.PACK_RESOURCES_ID, 
					Component.literal("CQRepoured resources"), 
					true,
					(s) -> packResources,
					SERVER_PACK_INFO,
					PackType.SERVER_DATA,
					Position.TOP,
					true,
					PACK_SOURCE
			);
			
			pOnLoad.accept(pack);
		}
	}

}
