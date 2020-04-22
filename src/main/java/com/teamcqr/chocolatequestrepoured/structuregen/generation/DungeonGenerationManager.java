package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DungeonGenerationManager {

	private static final Map<World, DungeonGenerationManager> instances = new HashMap<>();

	private final List<Structure> dungeonPartList = new ArrayList<>();
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

	public static void handleWorldLoad(World world) {
		if (world != null && !world.isRemote && DungeonGenerationManager.getInstance(world) == null) {
			DungeonGenerationManager.createInstance(world);
			DungeonGenerationManager.getInstance(world).loadData();
			CQRMain.logger.info("Loaded {} parts to generate", DungeonGenerationManager.getInstance(world).dungeonPartList.size());
		}
	}

	public static void handleWorldSave(World world) {
		if (world != null && !world.isRemote) {
			DungeonGenerationManager.getInstance(world).saveData();
		}
	}

	public static void handleWorldUnload(World world) {
		if (world != null && !world.isRemote) {
			CQRMain.logger.info("Saved {} parts to generate", DungeonGenerationManager.getInstance(world).dungeonPartList.size());
			DungeonGenerationManager.deleteInstance(world);
		}
	}

	public static void handleWorldTick(World world) {
		if (world != null && !world.isRemote) {
			DungeonGenerationManager.getInstance(world).tick();
		}
	}

	@Nullable
	private static DungeonGenerationManager getInstance(World world) {
		if (world != null && !world.isRemote) {
			return DungeonGenerationManager.instances.get(world);
		}
		return null;
	}

	private static void createInstance(World world) {
		if (world != null && !world.isRemote && !DungeonGenerationManager.instances.containsKey(world)) {
			DungeonGenerationManager.instances.put(world, new DungeonGenerationManager(world));
		}
	}

	private static void deleteInstance(World world) {
		if (world != null && !world.isRemote && DungeonGenerationManager.instances.containsKey(world)) {
			DungeonGenerationManager.instances.remove(world);
		}
	}

	public static void addStructure(World world, Structure structure) {
		if (world != null && !world.isRemote) {
			DungeonGenerationManager.getInstance(world).dungeonPartList.add(structure);
		}
	}

	private void loadData() {
		if (!this.world.isRemote) {
			if (!this.folder.exists()) {
				this.folder.mkdirs();
			}
			for (File file : FileUtils.listFiles(this.folder, new String[] { "nbt" }, false)) {
				this.createStructureFromFile(file);
			}
		}
	}

	private void createStructureFromFile(File file) {
		try (InputStream inputStream = new FileInputStream(file)) {
			NBTTagCompound compound = CompressedStreamTools.readCompressed(inputStream);
			this.dungeonPartList.add(new Structure(this.world, compound));
		} catch (IOException e) {
			CQRMain.logger.info("Failed to load structure from file: " + file.getName(), e);
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
			for (Structure structure : this.dungeonPartList) {
				this.createFileFromStructure(this.folder, structure);
			}
		}
	}

	private void createFileFromStructure(File folder, Structure structure) {
		File file = new File(folder, structure.getUuid().toString() + ".nbt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			try (OutputStream outputStream = new FileOutputStream(file)) {
				CompressedStreamTools.writeCompressed(structure.writeToNBT(), outputStream);
			}
		} catch (IOException e) {
			CQRMain.logger.info("Failed to save structure to file: " + file.getName(), e);
		}
	}

	private void tick() {
		if (!this.dungeonPartList.isEmpty()) {
			List<Integer> toRemove = new ArrayList<>();

			for (int i = 0; i < this.dungeonPartList.size(); i++) {
				Structure structure = this.dungeonPartList.get(i);
				structure.tick(this.world);
				if (structure.isGenerated()) {
					toRemove.add(i);
					CQRMain.logger.info("Generated dungeon!");
				}
			}

			for (int i = 0; i < toRemove.size(); i++) {
				this.dungeonPartList.remove((int) toRemove.get(i) - i);
			}
		}
	}

}