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
	
	private static GridRegistry instance;
	Map<String, DungeonGrid> ENTRIES = new HashMap<>();
	DungeonGrid DEFAULT_GRID;
	
	public static GridRegistry getInstance() {
		if (instance == null) {
			instance = new GridRegistry();
		}
		return instance;
	}
	
	public void loadGridFiles() {
		DungeonGrid.clearIdents();
		ENTRIES.clear();

		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_DUNGEON_GRID_FOLDER, new String[] { "properties", "prop", "cfg" }, true);
		CQRMain.logger.info("Loading {} grid configuration files...", files.size());

		for (File file : files) {
			DungeonGrid grid = createGridFromFile(file);

			if (grid != null) {
				ENTRIES.put(grid.getName(), grid);
			}
		}
		
		if(!ENTRIES.containsKey("default")) {
			ENTRIES.put("default", DungeonGrid.getDefaultGrid());
		}
		
		DEFAULT_GRID = ENTRIES.get("default");
	}
	
	public DungeonGrid getByIdOrDefault(final String id) {
		return ENTRIES.getOrDefault(id, DEFAULT_GRID);
	}
	
	private DungeonGrid createGridFromFile(File file) {
		Properties prop = PropertyFileHelper.readPropFile(file);
		if (prop == null) {
			return null;
		}

		String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
		return new DungeonGrid(name, prop);
	}

	public Collection<DungeonGrid> grids() {
		return ENTRIES.values();
	}
	
}
