package com.teamcqr.chocolatequestrepoured.structuregen;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class DungeonDataManager {

	private static final Map<World, DungeonDataManager> INSTANCES = Collections.synchronizedMap(new HashMap<>());

	private boolean modifiedSinceLastSave = false;
	private final Map<String, Set<BlockPos>> dungeonData = Collections.synchronizedMap(new HashMap<>());
	protected final String DATA_FILE_NAME = "structures.nbt";
	private File file;

	public static void handleWorldLoad(World world) {
		if (isWorldValid(world) && !INSTANCES.containsKey(world)) {
			createInstance(world);
			getInstance(world).readData();
		}
	}

	public static void handleWorldUnload(World world) {
		if (isWorldValid(world)) {
			while (DungeonGeneratorThread.isDungeonGeneratorThreadRunning(world)) {
				// wait
			}
			getInstance(world).saveData();
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

	public static void addDungeonEntry(World world, DungeonBase dungeon, BlockPos position) {
		if (isWorldValid(world)) {
			try {
				getInstance(world).insertDungeonEntry(dungeon.getDungeonName(), position);
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
			return new HashSet<String>();
		}
	}

	private Set<String> getSpawnedDungeonNames() {
		return this.dungeonData.keySet();
	}

	public static Set<BlockPos> getLocationsOfDungeon(World world, String dungeon) {
		try {
			return getInstance(world).getLocationsOfDungeon(dungeon);
		} catch (NullPointerException npe) {
			CQRMain.logger.warn("Found no datamanager instance for world {}! Error: {}", world.getWorldInfo().getWorldName(), npe);
			return new HashSet<BlockPos>();
		}
	}

	private Set<BlockPos> getLocationsOfDungeon(String dungeon) {
		return this.dungeonData.getOrDefault(dungeon, new HashSet<>());
	}

	public DungeonDataManager(World world) {
		int dim = world.provider.getDimension();
		String path = world.getSaveHandler().getWorldDirectory().getAbsolutePath();
		if (dim == 0) {
			path += "/data/CQR/";
		} else {
			path += "/DIM" + dim + "/data/CQR/";
		}
		this.file = FileIOUtil.getOrCreateFile(path, this.DATA_FILE_NAME);
	}

	public void insertDungeonEntry(String dungeon, BlockPos location) {
		Set<BlockPos> spawnedLocs = this.dungeonData.getOrDefault(dungeon, new HashSet<>());
		if (spawnedLocs.add(location)) {
			this.dungeonData.put(dungeon, spawnedLocs);
			if (!this.modifiedSinceLastSave) {
				this.modifiedSinceLastSave = true;
			}
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
			NBTTagList dungeonNames = FileIOUtil.getOrCreateTagList(root, "dungeons", Constants.NBT.TAG_STRING);
			for (Map.Entry<String, Set<BlockPos>> data : this.dungeonData.entrySet()) {
				if (!data.getValue().isEmpty()) {
					NBTTagList locs = FileIOUtil.getOrCreateTagList(root, "dun-" + data.getKey(), Constants.NBT.TAG_COMPOUND);
					for (BlockPos loc : data.getValue()) {
						locs.appendTag(NBTUtil.createPosTag(loc));
					}
					dungeonNames.appendTag(new NBTTagString(data.getKey()));
				}
			}
			FileIOUtil.saveNBTCompoundToFile(root, this.file);
			this.modifiedSinceLastSave = false;
		}
	}

	public void readData() {
		NBTTagCompound root = FileIOUtil.getRootNBTTagOfFile(this.file);
		NBTTagList dungeons = FileIOUtil.getOrCreateTagList(root, "dungeons", Constants.NBT.TAG_STRING);
		dungeons.forEach(new Consumer<NBTBase>() {

			@Override
			public void accept(NBTBase t) {
				if (t instanceof NBTTagString) {
					NBTTagString tag = (NBTTagString) t;
					String s = tag.getString();
					Set<BlockPos> poss = DungeonDataManager.this.dungeonData.getOrDefault(s, new HashSet<>());
					NBTTagList data = FileIOUtil.getOrCreateTagList(root, "dun-" + s, Constants.NBT.TAG_COMPOUND);
					data.forEach(new Consumer<NBTBase>() {
						@Override
						public void accept(NBTBase t1) {
							if (t1 instanceof NBTTagCompound) {
								NBTTagCompound tag1 = (NBTTagCompound) t1;
								poss.add(NBTUtil.getPosFromTag(tag1));
							}
						}
					});
					DungeonDataManager.this.dungeonData.put(s, poss);
				}
			}
		});
	}

	public boolean isDungeonSpawnLimitMet(DungeonBase dungeon) {
		if (dungeon.getSpawnLimit() < 0) {
			return false;
		}
		if (this.dungeonData.isEmpty()) {
			return false;
		}
		Set<BlockPos> spawnedLocs = this.dungeonData.getOrDefault(dungeon.getDungeonName(), new HashSet<>());
		if (spawnedLocs.isEmpty()) {
			return false;
		}
		return spawnedLocs.size() >= dungeon.getSpawnLimit();
	}

}
