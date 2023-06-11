package team.cqr.cqrepoured.world.structure.generation.grid;

import org.apache.commons.io.FileUtils;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.PropertyFileHelper;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

public class GridRegistry {

	private static GridRegistry instance;
	private final Map<String, DungeonGrid> name2grid = new HashMap<>();
	private final List<DungeonGrid> grids = new ArrayList<>();

	public static GridRegistry getInstance() {
		if (instance == null) {
			instance = new GridRegistry();
		}
		return instance;
	}

	public void loadGridFiles() {
		this.name2grid.clear();
		this.grids.clear();

		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_DUNGEON_GRID_FOLDER, new String[] { "properties", "prop", "cfg" }, true);
		CQRMain.logger.info("Loading {} grid configuration files...", files.size());

		for (File file : files) {
			DungeonGrid grid = this.createGridFromFile(file);

			if (grid != null) {
				this.name2grid.computeIfAbsent(grid.getName(), k -> grid);
			}
		}

		this.grids.addAll(this.name2grid.values());
		this.grids.sort(Comparator.comparingInt(DungeonGrid::getPriority));
		IntStream.range(0, this.grids.size()).forEach(i -> this.grids.get(i).setId(i));
	}

	public static DungeonGrid createGridFromFile(File file) {
		Properties prop = PropertyFileHelper.readPropFile(file);
		if (prop == null) {
			return null;
		}

		String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
		return createGridFromFile(prop, name);
	}
	
	public static DungeonGrid createGridFromFile(Properties prop, String name) {
		return new DungeonGrid(name, prop);
	}

	@Nullable
	public DungeonGrid getGrid(final String name) {
		return this.name2grid.get(name);
	}

	public Collection<DungeonGrid> getGrids() {
		return Collections.unmodifiableCollection(this.grids);
	}

}
