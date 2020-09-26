package com.teamcqr.chocolatequestrepoured.structuregen;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class DungeonDataManager {

	public static class DungeonInfo {
		private BlockPos pos;
		private DungeonSpawnType spawnType;

		public DungeonInfo(BlockPos pos, DungeonSpawnType spawnType) {
			this.pos = pos.toImmutable();
			this.spawnType = spawnType;
		}

		public DungeonInfo(NBTTagCompound compound) {
			this.readFromNBT(compound);
		}

		public NBTTagCompound writeToNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setTag("pos", NBTUtil.createPosTag(this.pos));
			compound.setInteger("spawnType", this.spawnType.ordinal());
			return compound;
		}

		public void readFromNBT(NBTTagCompound compound) {
			if (compound.hasKey("pos", Constants.NBT.TAG_COMPOUND)) {
				this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
			} else {
				this.pos = NBTUtil.getPosFromTag(compound);
			}
			this.spawnType = DungeonSpawnType.values()[compound.getInteger("spawnType")];
		}
	}

	public enum DungeonSpawnType {
		DUNGEON_GENERATION, LOCKED_COORDINATE, DUNGEON_PLACER_ITEM;
	}

	private static final Map<World, DungeonDataManager> INSTANCES = Collections.synchronizedMap(new HashMap<>());

	private final Map<String, Set<DungeonInfo>> dungeonData = Collections.synchronizedMap(new HashMap<>());
	private final File file;
	private boolean modifiedSinceLastSave = false;

	public DungeonDataManager(World world) {
		int dim = world.provider.getDimension();
		if (dim == 0) {
			this.file = new File(world.getSaveHandler().getWorldDirectory(), "data/CQR/structures.nbt");
		} else {
			this.file = new File(world.getSaveHandler().getWorldDirectory(), "DIM" + dim + "/data/CQR/structures.nbt");
		}
	}

	public static void handleWorldLoad(World world) {
		if (isWorldValid(world) && !INSTANCES.containsKey(world)) {
			createInstance(world);
			try {
				getInstance(world).readData();
			} catch(NullPointerException npe) {
				CQRMain.logger.warn("Found no datamanager instance for world {}! Error: {}", world.getWorldInfo().getWorldName(), npe);
			}
		}
	}

	public static void handleWorldUnload(World world) {
		if (isWorldValid(world)) {
			while (DungeonGeneratorThread.isDungeonGeneratorThreadRunning(world)) {
				// wait
			}
			try {
				getInstance(world).saveData();
			} catch(NullPointerException npe) {
				CQRMain.logger.warn("Found no datamanager instance for world {}! Error: {}", world.getWorldInfo().getWorldName(), npe);
			}
			deleteInstance(world);
		}
	}

	public static void handleWorldSave(World world) {
		if (isWorldValid(world)) {
			try {
				getInstance(world).saveData();
			} catch (NullPointerException npe) {
				CQRMain.logger.warn("Found no datamanager instance for world {}! Error: {}", world.getWorldInfo().getWorldName(), npe);
			}
		}
	}

	public static void addDungeonEntry(World world, DungeonBase dungeon, BlockPos position, DungeonSpawnType spawnType) {
		if (isWorldValid(world)) {
			try {
				getInstance(world).insertDungeonEntry(dungeon.getDungeonName(), position, spawnType);
			} catch (NullPointerException npe) {
				CQRMain.logger.warn("Found no datamanager instance for world {}! Error: {}", world.getWorldInfo().getWorldName(), npe);
			}
		}
	}

	@Nullable
	public static DungeonDataManager getInstance(World world) {
		if (isWorldValid(world)) {
			return INSTANCES.get(world);
		}
		return null;
	}

	private static boolean isWorldValid(World world) {
		return world != null && !world.isRemote;
	}

	private static void createInstance(World world) {
		if (isWorldValid(world) && !INSTANCES.containsKey(world)) {
			INSTANCES.put(world, new DungeonDataManager(world));
		}
	}

	private static void deleteInstance(World world) {
		if (isWorldValid(world) && INSTANCES.containsKey(world)) {
			INSTANCES.remove(world);
		}
	}

	public static Set<String> getSpawnedDungeonNames(World world) {
		try {
			return getInstance(world).getSpawnedDungeonNames();
		} catch (NullPointerException npe) {
			CQRMain.logger.warn("Found no datamanager instance for world {}! Error: {}", world.getWorldInfo().getWorldName(), npe);
			return new HashSet<>();
		}
	}

	private Set<String> getSpawnedDungeonNames() {
		return this.dungeonData.keySet();
	}

	public static Set<DungeonInfo> getLocationsOfDungeon(World world, String dungeon) {
		try {
			return getInstance(world).getLocationsOfDungeon(dungeon);
		} catch (NullPointerException npe) {
			CQRMain.logger.warn("Found no datamanager instance for world {}! Error: {}", world.getWorldInfo().getWorldName(), npe);
			return new HashSet<>();
		}
	}

	private Set<DungeonInfo> getLocationsOfDungeon(String dungeon) {
		return this.dungeonData.getOrDefault(dungeon, new HashSet<>());
	}

	public void insertDungeonEntry(String dungeon, BlockPos location, DungeonSpawnType spawnType) {
		Set<DungeonInfo> spawnedLocs = this.dungeonData.computeIfAbsent(dungeon, key -> new HashSet<>());
		if (spawnedLocs.add(new DungeonInfo(location, spawnType))) {
			this.modifiedSinceLastSave = true;
		}
	}

	public void saveData() {
		if (this.modifiedSinceLastSave) {
			this.file.delete();
			try {
				if (!this.file.createNewFile()) {
					CQRMain.logger.warn("Unable to create file: " + this.file.getAbsolutePath() + "! Information about dungeons may be lost!");
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			NBTTagCompound root = new NBTTagCompound();
			for (Map.Entry<String, Set<DungeonInfo>> data : this.dungeonData.entrySet()) {
				Set<DungeonInfo> dungeonInfos = data.getValue();
				if (!dungeonInfos.isEmpty()) {
					NBTTagList nbtTagList = new NBTTagList();
					for (DungeonInfo dungeonInfo : dungeonInfos) {
						nbtTagList.appendTag(dungeonInfo.writeToNBT());
					}
					root.setTag(data.getKey(), nbtTagList);
				}
			}
			FileIOUtil.saveNBTCompoundToFile(root, this.file);

			this.modifiedSinceLastSave = false;
		}
	}

	public void readData() {
		this.dungeonData.clear();

		NBTTagCompound root = FileIOUtil.getRootNBTTagOfFile(this.file);
		if (root == null) {
			return;
		}
		for (String key : root.getKeySet()) {
			Set<DungeonInfo> dungeonInfos = new HashSet<>();
			for (NBTBase nbt : root.getTagList(key, Constants.NBT.TAG_COMPOUND)) {
				dungeonInfos.add(new DungeonInfo((NBTTagCompound) nbt));
			}
			if (!dungeonInfos.isEmpty()) {
				String dungeonName = key.substring(0, 4).equals("dun-") ? key.substring(4) : key;
				this.dungeonData.put(dungeonName, dungeonInfos);
			}
		}
	}

	public boolean isDungeonSpawnLimitMet(DungeonBase dungeon) {
		if (dungeon.getSpawnLimit() < 0) {
			return false;
		}
		if (this.dungeonData.isEmpty()) {
			return false;
		}
		Set<DungeonInfo> spawnedLocs = this.dungeonData.get(dungeon.getDungeonName());
		if (spawnedLocs == null) {
			return false;
		}
		return spawnedLocs.stream().filter(dungeonInfo -> dungeonInfo.spawnType == DungeonSpawnType.DUNGEON_GENERATION).count() >= dungeon.getSpawnLimit();
	}

}
