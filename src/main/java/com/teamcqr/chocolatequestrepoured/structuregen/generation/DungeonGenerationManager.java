package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonDataManager;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonGeneratorThread;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegionManager;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DungeonGenerationManager {

	private static final Map<World, DungeonGenerationManager> INSTANCES = Collections.synchronizedMap(new HashMap<>());

	private final List<DungeonGenerator> dungeonGeneratorList = Collections.synchronizedList(new ArrayList<>());
	private final World world;
	private final File folder;

	public DungeonGenerationManager(World world) {
		this.world = world;
		int dim = world.provider.getDimension();
		if (dim == 0) {
			this.folder = new File(world.getSaveHandler().getWorldDirectory(), "data/CQR/structure_parts");
		} else {
			this.folder = new File(world.getSaveHandler().getWorldDirectory(), "DIM" + dim + "/data/CQR/structure_parts");
		}
	}

	@Nullable
	public static DungeonGenerationManager getInstance(World world) {
		if (!world.isRemote) {
			return INSTANCES.get(world);
		}
		return null;
	}

	public static void handleWorldLoad(World world) {
		if (!world.isRemote && !INSTANCES.containsKey(world)) {
			INSTANCES.put(world, new DungeonGenerationManager(world));
			INSTANCES.get(world).loadData();
			CQRMain.logger.info("Loaded {} parts to generate", INSTANCES.get(world).dungeonGeneratorList.size());
		}
	}

	public static void handleWorldSave(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).saveData();
		}
	}

	public static void handleWorldUnload(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
			while (DungeonGeneratorThread.isDungeonGeneratorThreadRunning(world)) {
				// wait
			}
			INSTANCES.get(world).saveData();
			CQRMain.logger.info("Saved {} parts to generate", INSTANCES.get(world).dungeonGeneratorList.size());
			INSTANCES.remove(world);
		}
	}

	public static void handleWorldTick(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).tick();
		}
	}

	public static void addStructure(World world, DungeonGenerator structure, @Nullable DungeonBase dungeon, DungeonDataManager.DungeonSpawnType spawnType, boolean generateImmediately) {
		if (world != null && !world.isRemote) {
			if (dungeon != null) {
				structure.setupProtectedRegion(dungeon);
			}
			structure.setupLight();
			structure.startGeneration();
			if (dungeon != null) {
				DungeonDataManager.addDungeonEntry(world, dungeon, structure.getPos(), spawnType);
			}
			ProtectedRegionManager.getInstance(world).addProtectedRegion(structure.getProtectedRegion());
			if (generateImmediately) {
				while (!structure.isGenerated()) {
					structure.tick();
				}
				CQRMain.logger.info("Generated dungeon {} at {}", structure.getDungeonName(), structure.getPos());
			} else {
				INSTANCES.get(world).dungeonGeneratorList.add(structure);
			}
		}
	}

	private void saveData() {
		if (!this.world.isRemote) {
			if (!this.folder.exists()) {
				this.folder.mkdirs();
			}
			for (File file : FileUtils.listFiles(this.folder, new String[] { "nbt" }, false)) {
				file.delete();
			}
			for (DungeonGenerator structure : this.dungeonGeneratorList) {
				this.createFileFromStructure(this.folder, structure);
			}
		}
	}

	private void createFileFromStructure(File folder, DungeonGenerator dungeonGenerator) {
		File file = new File(folder, dungeonGenerator.getUuid().toString() + ".nbt");
		try {
			if (!file.exists() && !file.createNewFile()) {
				throw new FileNotFoundException();
			}
			try (OutputStream outputStream = new FileOutputStream(file)) {
				CompressedStreamTools.writeCompressed(dungeonGenerator.writeToNBT(), outputStream);
			}
		} catch (IOException e) {
			CQRMain.logger.info("Failed to save structure to file: " + file.getName(), e);
		}
	}

	private void loadData() {
		if (!this.world.isRemote) {
			if (!this.folder.exists()) {
				this.folder.mkdirs();
			}
			this.dungeonGeneratorList.clear();
			for (File file : FileUtils.listFiles(this.folder, new String[] { "nbt" }, false)) {
				this.createStructureFromFile(file);
			}
		}
	}

	private void createStructureFromFile(File file) {
		try (InputStream inputStream = new FileInputStream(file)) {
			NBTTagCompound compound = CompressedStreamTools.readCompressed(inputStream);
			this.dungeonGeneratorList.add(new DungeonGenerator(this.world, compound));
		} catch (IOException e) {
			CQRMain.logger.info("Failed to load structure from file: " + file.getName(), e);
		}
	}

	private void tick() {
		for (int i = 0; i < this.dungeonGeneratorList.size(); i++) {
			DungeonGenerator dungeonGenerator = this.dungeonGeneratorList.get(i);

			dungeonGenerator.tick();

			if (dungeonGenerator.isGenerated()) {
				this.dungeonGeneratorList.remove(i--);
				CQRMain.logger.info("Generated dungeon {} at {}", dungeonGenerator.getDungeonName(), dungeonGenerator.getPos());
			}
		}
	}

}
