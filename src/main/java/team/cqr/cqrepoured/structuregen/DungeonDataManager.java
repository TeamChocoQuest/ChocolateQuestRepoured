package team.cqr.cqrepoured.structuregen;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.data.FileIOUtil;

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

	@Nullable
	public static DungeonDataManager getInstance(World world) {
		if (!world.isRemote) {
			return INSTANCES.get(world);
		}
		return null;
	}

	public static void handleWorldLoad(World world) {
		if (!world.isRemote && !INSTANCES.containsKey(world)) {
			INSTANCES.put(world, new DungeonDataManager(world));
			INSTANCES.get(world).readData();
		}
	}

	public static void handleWorldSave(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).saveData();
		}
	}

	public static void handleWorldUnload(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).saveData();
			INSTANCES.remove(world);
		}
	}

	public static void addDungeonEntry(World world, DungeonBase dungeon, BlockPos position, DungeonSpawnType spawnType) {
		if (INSTANCES.containsKey(world)) {
			INSTANCES.get(world).addDungeonEntry(dungeon, position, spawnType);
		}
	}

	public static Set<String> getSpawnedDungeonNames(World world) {
		if (INSTANCES.containsKey(world)) {
			return INSTANCES.get(world).getSpawnedDungeonNames();
		}
		return Collections.emptySet();
	}

	public static Set<DungeonInfo> getLocationsOfDungeon(World world, DungeonBase dungeon) {
		if (INSTANCES.containsKey(world)) {
			return INSTANCES.get(world).getLocationsOfDungeon(dungeon);
		}
		return Collections.emptySet();
	}

	public static boolean isDungeonSpawnLimitMet(World world, DungeonBase dungeon) {
		if (INSTANCES.containsKey(world)) {
			return INSTANCES.get(world).isDungeonSpawnLimitMet(dungeon);
		}
		return false;
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

	private void addDungeonEntry(DungeonBase dungeon, BlockPos location, DungeonSpawnType spawnType) {
		Set<DungeonInfo> spawnedLocs = this.dungeonData.computeIfAbsent(dungeon.getDungeonName(), key -> Collections.synchronizedSet(new HashSet<>()));
		if (spawnedLocs.add(new DungeonInfo(location, spawnType))) {
			this.modifiedSinceLastSave = true;
		}
	}

	private Set<String> getSpawnedDungeonNames() {
		return this.dungeonData.keySet();
	}

	private Set<DungeonInfo> getLocationsOfDungeon(DungeonBase dungeon) {
		return this.dungeonData.getOrDefault(dungeon.getDungeonName(), Collections.emptySet());
	}

	private boolean isDungeonSpawnLimitMet(DungeonBase dungeon) {
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
