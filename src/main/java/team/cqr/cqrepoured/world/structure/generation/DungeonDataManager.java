package team.cqr.cqrepoured.world.structure.generation;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.util.data.FileIOUtil;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

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

	private static final Map<World, DungeonDataManager> INSTANCES = new HashMap<>();

	private final SetMultimap<String, DungeonInfo> dungeonData = HashMultimap.create();
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

	public static void onWorldLoad(World world) {
		INSTANCES.computeIfAbsent(world, k -> {
			DungeonDataManager v = new DungeonDataManager(k);
			v.readData();
			return v;
		});
	}

	public static void onWorldSave(World world) {
		INSTANCES.get(world).saveData();
	}

	public static void onWorldUnload(World world) {
		INSTANCES.get(world).saveData();
		INSTANCES.remove(world);
	}

	public static void addDungeonEntry(World world, DungeonBase dungeon, BlockPos position, DungeonSpawnType spawnType) {
		INSTANCES.get(world).addDungeonEntry(dungeon, position, spawnType);
	}

	public static Set<String> getSpawnedDungeonNames(World world) {
		return INSTANCES.get(world).getSpawnedDungeonNames();
	}

	public static Set<DungeonInfo> getLocationsOfDungeon(World world, DungeonBase dungeon) {
		return INSTANCES.get(world).getLocationsOfDungeon(dungeon);
	}

	public static boolean isDungeonSpawnLimitMet(World world, DungeonBase dungeon) {
		return INSTANCES.get(world).isDungeonSpawnLimitMet(dungeon);
	}

	public void saveData() {
		if (this.modifiedSinceLastSave) {
			NBTTagCompound root = new NBTTagCompound();
			for (Map.Entry<String, Collection<DungeonInfo>> data : this.dungeonData.asMap().entrySet()) {
				Collection<DungeonInfo> dungeonInfos = data.getValue();
				if (!dungeonInfos.isEmpty()) {
					NBTTagList nbtTagList = new NBTTagList();
					for (DungeonInfo dungeonInfo : dungeonInfos) {
						nbtTagList.appendTag(dungeonInfo.writeToNBT());
					}
					root.setTag(data.getKey(), nbtTagList);
				}
			}
			FileIOUtil.writeNBTToFile(root, this.file);

			this.modifiedSinceLastSave = false;
		}
	}

	public void readData() {
		this.dungeonData.clear();

		if (!this.file.exists()) {
			return;
		}

		NBTTagCompound root = FileIOUtil.readNBTFromFile(this.file);

		for (String key : root.getKeySet()) {
			for (NBTBase nbt : root.getTagList(key, Constants.NBT.TAG_COMPOUND)) {
				this.dungeonData.put(key, new DungeonInfo((NBTTagCompound) nbt));
			}
		}
	}

	private void addDungeonEntry(DungeonBase dungeon, BlockPos location, DungeonSpawnType spawnType) {
		if (this.dungeonData.put(dungeon.getDungeonName(), new DungeonInfo(location, spawnType))) {
			this.modifiedSinceLastSave = true;
		}
	}

	private Set<String> getSpawnedDungeonNames() {
		return this.dungeonData.keySet();
	}

	private Set<DungeonInfo> getLocationsOfDungeon(DungeonBase dungeon) {
		return this.dungeonData.get(dungeon.getDungeonName());
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
