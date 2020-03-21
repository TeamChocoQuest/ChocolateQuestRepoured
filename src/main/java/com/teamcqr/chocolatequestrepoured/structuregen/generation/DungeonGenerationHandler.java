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

import com.teamcqr.chocolatequestrepoured.CQRMain;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class DungeonGenerationHandler {

	private static final Map<Integer, DungeonGenerationHandler> instances = new HashMap<>();

	private final List<Structure> dungeonPartList = new ArrayList<>();
	private final World world;
	private final File file;

	public DungeonGenerationHandler(World world) {
		this.world = world;
		int dim = world.provider.getDimension();
		if (dim == 0) {
			this.file = new File(world.getSaveHandler().getWorldDirectory(), "data/cqr_structure_parts.nbt");
		} else {
			this.file = new File(world.getSaveHandler().getWorldDirectory(), "DIM" + dim + "/data/cqr_structure_parts.nbt");
		}
	}

	public static void addStructure(World world, Structure structure) {
		DungeonGenerationHandler instance = getInstance(world);
		instance.dungeonPartList.add(structure);
		structure.addLightParts();
	}

	private static DungeonGenerationHandler getInstance(World world) {
		int dim = world.provider.getDimension();
		if (!instances.containsKey(dim)) {
			createInstance(world);
		}
		return instances.get(dim);
	}

	private static void createInstance(World world) {
		int dim = world.provider.getDimension();
		if (!instances.containsKey(dim)) {
			instances.put(dim, new DungeonGenerationHandler(world));
		}
	}

	private static void deleteInstance(World world) {
		int dim = world.provider.getDimension();
		instances.remove(dim);
	}

	public static void handleWorldLoad(World world) {
		if (!world.isRemote) {
			DungeonGenerationHandler.createInstance(world);
			DungeonGenerationHandler.getInstance(world).loadData();
			CQRMain.logger.info("Loaded " + DungeonGenerationHandler.getInstance(world).dungeonPartList.size() + " parts to generate");
		}
	}

	public static void handleWorldSave(World world) {
		if (!world.isRemote) {
			DungeonGenerationHandler.getInstance(world).saveData();
		}
	}

	public static void handleWorldUnload(World world) {
		if (!world.isRemote) {
			CQRMain.logger.info("Saved " + DungeonGenerationHandler.getInstance(world).dungeonPartList.size() + " parts to generate");
			DungeonGenerationHandler.deleteInstance(world);
		}
	}

	public static void handleWorldTick(World world) {
		if (!world.isRemote) {
			DungeonGenerationHandler.getInstance(world).tick();
		}
	}

	private void loadData() {
		if (this.file.exists()) {
			try {
				InputStream inputStream = new FileInputStream(this.file);
				NBTTagCompound compound = CompressedStreamTools.readCompressed(inputStream);
				this.readFromNBT(compound);
				inputStream.close();
			} catch (IOException e) {
				CQRMain.logger.error("Failed to load dungeon parts", e);
			}
		}
	}

	private void saveData() {
		if (!this.file.exists()) {
			try {
				if (!this.file.getParentFile().exists()) {
					this.file.getParentFile().mkdirs();
				}
				this.file.createNewFile();
			} catch (IOException e) {
				CQRMain.logger.error("Failed to create file for dungeon parts", e);
			}
		}

		try {
			OutputStream outputStream = new FileOutputStream(this.file);
			NBTTagCompound compound = this.writeToNBT();
			CompressedStreamTools.writeCompressed(compound, outputStream);
			outputStream.close();
		} catch (IOException e) {
			CQRMain.logger.error("Failed to save dungeon parts", e);
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

	private NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < this.dungeonPartList.size(); i++) {
			nbtTagList.appendTag(this.dungeonPartList.get(i).writeToNBT());
		}
		compound.setTag("parts", nbtTagList);

		return compound;
	}

	private void readFromNBT(NBTTagCompound compound) {
		this.dungeonPartList.clear();

		NBTTagList nbtTagList = compound.getTagList("parts", 10);
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			this.dungeonPartList.add(new Structure(this.world, nbtTagList.getCompoundTagAt(i)));
		}
	}

}
