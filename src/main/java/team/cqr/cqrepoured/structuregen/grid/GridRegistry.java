package team.cqr.cqrepoured.structuregen.grid;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.PropertyFileHelper;

public class GridRegistry {
	
	private static GridRegistry instance;
	Map<String, DungeonGrid> ENTRIES = new HashMap<>();
	List<DungeonGrid> grids = new ArrayList<>();
	
	public static GridRegistry getInstance() {
		if (instance == null) {
			instance = new GridRegistry();
		}
		return instance;
	}
	
	public void loadGridFiles() {
		ENTRIES.clear();

		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_DUNGEON_GRID_FOLDER, new String[] { "properties", "prop", "cfg" }, true);
		CQRMain.logger.info("Loading {} grid configuration files...", files.size());

		for (File file : files) {
			DungeonGrid grid = createGridFromFile(file);

			if (grid != null) {
				ENTRIES.computeIfAbsent(grid.getName(), k -> grid);
			}
		}

		grids.addAll(ENTRIES.values());
		grids.sort(Comparator.comparingInt(DungeonGrid::getPriority));
		IntStream.range(0, this.grids.size()).forEach(i -> this.grids.get(i).setId(i));
	}
	
	@Nullable
	public DungeonGrid getById(final String id) {
		return ENTRIES.get(id);
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
