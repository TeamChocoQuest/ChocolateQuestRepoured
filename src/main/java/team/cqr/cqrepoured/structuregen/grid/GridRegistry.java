package team.cqr.cqrepoured.structuregen.grid;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.PropertyFileHelper;

public class GridRegistry {
	
	static Map<String, DungeonGrid> ENTRIES = new HashMap<>();
	static DungeonGrid DEFAULT_GRID;
	
	public static void loadGridFiles() {
		DungeonGrid.clearIdents();
		ENTRIES.clear();

		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_DUNGEON_GRID_FOLDER, new String[] { "properties", "prop", "cfg" }, true);
		CQRMain.logger.info("Loading {} grid configuration files...", files.size());

		for (File file : files) {
			DungeonGrid grid = createGridFromFile(file);

			if (grid != null) {
				ENTRIES.put(grid.getIdentifier(), grid);
			}
		}
		
		if(!ENTRIES.containsKey("default")) {
			ENTRIES.put("default", DungeonGrid.getDefaultGrid());
		}
		
		DEFAULT_GRID = ENTRIES.get("default");
	}
	
	public static DungeonGrid getByIdOrDefault(final String id) {
		return ENTRIES.getOrDefault(id, DEFAULT_GRID);
	}
	
	private static DungeonGrid createGridFromFile(File file) {
		Properties prop = PropertyFileHelper.readPropFile(file);
		if (prop == null) {
			return null;
		}

		String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
		return DungeonGrid.create(name, prop);
	}

	public static Collection<DungeonGrid> grids() {
		return ENTRIES.values();
	}
	
}
