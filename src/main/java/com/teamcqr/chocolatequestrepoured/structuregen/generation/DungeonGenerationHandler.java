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
			this.file = new File(world.getSaveHandler().getWorldDirectory(), "data\\cqr_structure_parts.nbt");
		} else {
			this.file = new File(world.getSaveHandler().getWorldDirectory(), "DIM" + dim + "\\data\\cqr_structure_parts.nbt");
		}
	}

	/*
	 * public static void addCQStructurePart(World world, CQStructurePart part, PlacementSettings settings, BlockPos pos, int dungeonChunkX, int dungeonChunkZ, DungeonBase dungeon) {
	 * EDungeonMobType dungeonMobType = dungeon.getDungeonMob();
	 * if (dungeonMobType == EDungeonMobType.DEFAULT) {
	 * dungeonMobType = EDungeonMobType.getMobTypeDependingOnDistance(dungeonChunkX, dungeonChunkZ);
	 * }
	 * boolean replaceBanners = dungeon.replaceBanners();
	 * EBanners dungeonBanner = dungeonMobType.getBanner();
	 * boolean hasShield = dungeon.isProtectedFromModifications();
	 * 
	 * DungeonGenerationHandler instance = getInstance(world);
	 * instance.dungeonPartList.add(new StructurePart(part, settings, pos, dungeonChunkX, dungeonChunkZ, dungeonMobType, replaceBanners, dungeonBanner, hasShield));
	 * }
	 * 
	 * public static void addCQStructurePart(World world, CQStructurePart part, PlacementSettings settings, BlockPos pos, int dungeonChunkX, int dungeonChunkZ, EDungeonMobType dungeonMobType, boolean replaceBanners, EBanners dungeonBanner,
	 * boolean hasShield) {
	 * DungeonGenerationHandler instance = getInstance(world);
	 * instance.dungeonPartList.add(new StructurePart(part, settings, pos, dungeonChunkX, dungeonChunkZ, dungeonMobType, replaceBanners, dungeonBanner, hasShield));
	 * }
	 */

	public static void addStructure(World world, Structure structure) {
		DungeonGenerationHandler instance = getInstance(world);
		instance.dungeonPartList.add(structure);
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
				this.dungeonPartList.clear();
				this.dungeonPartList.addAll(this.readList(compound.getTagList("parts", 10)));
				inputStream.close();
			} catch (IOException e) {
				CQRMain.logger.error("Failed to load dungeon parts", e);
			}
		}

		// CQRMain.logger.info("Loaded " + this.dungeonPartList.size() + " parts to generate");
	}

	private void saveData() {
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				CQRMain.logger.error("Failed to create file for dungeon parts", e);
			}
		}

		try {
			OutputStream outputStream = new FileOutputStream(this.file);
			NBTTagCompound compound = new NBTTagCompound();
			compound.setTag("parts", this.writeList(this.dungeonPartList));
			CompressedStreamTools.writeCompressed(compound, outputStream);
			outputStream.close();
		} catch (IOException e) {
			CQRMain.logger.error("Failed to save dungeon parts", e);
		}

		// CQRMain.logger.info("Saved " + this.dungeonPartList.size() + " parts to generate");
	}

	private void tick() {
		if (!this.dungeonPartList.isEmpty()) {
			List<Integer> toRemove = new ArrayList<>();

			/*
			 * if (this.world.getTotalWorldTime() % CQRConfig.advanced.dungeonGenerationFrequencyInLoaded == 0) {
			 * for (int i = 0; i < this.dungeonPartList.size(); i++) {
			 * StructurePart structurePart = this.dungeonPartList.get(i);
			 * CQStructurePart part = structurePart.getPart();
			 * PlacementSettings settings = structurePart.getSettings();
			 * BlockPos pos = structurePart.getPos();
			 * 
			 * if (DungeonGenerationHandler.isAreaLoaded(this.world, pos, part, settings.getRotation())) {
			 * part.addBlocksToWorld(this.world, pos, settings, structurePart.getDungeonChunkX(), structurePart.getDungeonChunkZ(), structurePart.getDungeonMobType(), structurePart.isReplaceBanners(),
			 * structurePart.getDungeonBanner(), structurePart.isHasShield());
			 * toRemove.add(i);
			 * if (toRemove.size() == CQRConfig.advanced.dungeonGenerationCountInLoaded) {
			 * break;
			 * }
			 * }
			 * }
			 * }
			 * 
			 * if (toRemove.isEmpty() && this.world.getTotalWorldTime() % CQRConfig.advanced.dungeonGenerationFrequencyInUnloaded == 0) {
			 * for (int i = 0; i < this.dungeonPartList.size(); i++) {
			 * StructurePart structurePart = this.dungeonPartList.get(i);
			 * CQStructurePart part = structurePart.getPart();
			 * PlacementSettings settings = structurePart.getSettings();
			 * BlockPos pos = structurePart.getPos();
			 * 
			 * part.addBlocksToWorld(this.world, pos, settings, structurePart.getDungeonChunkX(), structurePart.getDungeonChunkZ(), structurePart.getDungeonMobType(), structurePart.isReplaceBanners(), structurePart.getDungeonBanner(),
			 * structurePart.isHasShield());
			 * toRemove.add(i);
			 * if (toRemove.size() == CQRConfig.advanced.dungeonGenerationCountInUnloaded) {
			 * break;
			 * }
			 * }
			 * }
			 *
			 * if (!toRemove.isEmpty()) {
			 * CQRMain.logger.info("Forced/Post generating " + toRemove.size() + " structure parts. " + (this.dungeonPartList.size() - toRemove.size()) + " parts left.");
			 * }
			 */

			for (int i = 0; i < this.dungeonPartList.size(); i++) {
				Structure structure = this.dungeonPartList.get(i);
				structure.tick(this.world);
				if (structure.isGenerated()) {
					toRemove.add(i);
				}
			}

			for (int i = 0; i < toRemove.size(); i++) {
				this.dungeonPartList.remove((int) toRemove.get(i) - i);
			}
		}
	}

	private List<Structure> readList(NBTTagList nbtTagList) {
		List<Structure> list = new ArrayList<>(nbtTagList.tagCount());
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			list.add(new Structure(this.world, nbtTagList.getCompoundTagAt(i)));
		}
		return list;
	}

	private NBTTagList writeList(List<Structure> list) {
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < list.size(); i++) {
			nbtTagList.appendTag(list.get(i).writeToNBT());
		}
		return nbtTagList;
	}

}
