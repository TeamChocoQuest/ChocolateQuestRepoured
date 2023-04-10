package team.cqr.cqrepoured.world.structure.generation;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.util.data.FileIOUtil;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class DungeonDataManager {

	public static class DungeonInfo {
		private BlockPos pos;
		private DungeonSpawnType spawnType;

		public DungeonInfo(BlockPos pos, DungeonSpawnType spawnType) {
			this.pos = pos.immutable();
			this.spawnType = spawnType;
		}

		public DungeonInfo(CompoundNBT compound) {
			this.readFromNBT(compound);
		}

		public CompoundNBT writeToNBT() {
			CompoundNBT compound = new CompoundNBT();
			compound.put("pos", NBTUtil.writeBlockPos(this.pos));
			compound.putInt("spawnType", this.spawnType.ordinal());
			return compound;
		}

		public void readFromNBT(CompoundNBT compound) {
			if (compound.contains("pos", Constants.NBT.TAG_COMPOUND)) {
				this.pos = NBTUtil.readBlockPos(compound.getCompound("pos"));
			} else {
				this.pos = NBTUtil.readBlockPos(compound);
			}
			this.spawnType = DungeonSpawnType.values()[compound.getInt("spawnType")];
		}
	}

	public enum DungeonSpawnType {
		DUNGEON_GENERATION, LOCKED_COORDINATE, DUNGEON_PLACER_ITEM;
	}

	private static final Map<IWorld, DungeonDataManager> INSTANCES = Collections.synchronizedMap(new HashMap<>());

	private final Map<String, Set<DungeonInfo>> dungeonData = Collections.synchronizedMap(new HashMap<>());
	private final File file;
	private boolean modifiedSinceLastSave = false;

	public DungeonDataManager(IWorld world) {
		this.file = FileIOUtil.getCQRDataFile((ServerWorld) world, "CQR/structures.nbt");
	}

	@Nullable
	public static DungeonDataManager getInstance(IWorld world) {
		if (!world.isClientSide()) {
			return INSTANCES.get(world);
		}
		return null;
	}

	public static void handleWorldLoad(IWorld world) {
		if (!world.isClientSide() && !INSTANCES.containsKey(world)) {
			INSTANCES.put(world, new DungeonDataManager(world));
			INSTANCES.get(world).readData();
		}
	}

	public static void handleWorldSave(IWorld world) {
		if (!world.isClientSide() && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).saveData();
		}
	}

	public static void handleWorldUnload(IWorld world) {
		if (!world.isClientSide() && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).saveData();
			INSTANCES.remove(world);
		}
	}

	public static void addDungeonEntry(IWorld world, DungeonBase dungeon, BlockPos position, DungeonSpawnType spawnType) {
		if (INSTANCES.containsKey(world)) {
			INSTANCES.get(world).addDungeonEntry(dungeon, position, spawnType);
		}
	}

	public static Set<String> getSpawnedDungeonNames(IWorld world) {
		if (INSTANCES.containsKey(world)) {
			return INSTANCES.get(world).getSpawnedDungeonNames();
		}
		return Collections.emptySet();
	}

	public static Set<DungeonInfo> getLocationsOfDungeon(IWorld world, DungeonBase dungeon) {
		if (INSTANCES.containsKey(world)) {
			return INSTANCES.get(world).getLocationsOfDungeon(dungeon);
		}
		return Collections.emptySet();
	}

	public static boolean isDungeonSpawnLimitMet(IWorld world, DungeonBase dungeon) {
		if (INSTANCES.containsKey(world)) {
			return INSTANCES.get(world).isDungeonSpawnLimitMet(dungeon);
		}
		return false;
	}

	public void saveData() {
		if (this.modifiedSinceLastSave) {
			CompoundNBT root = new CompoundNBT();
			for (Map.Entry<String, Set<DungeonInfo>> data : this.dungeonData.entrySet()) {
				Set<DungeonInfo> dungeonInfos = data.getValue();
				if (!dungeonInfos.isEmpty()) {
					ListNBT nbtTagList = new ListNBT();
					for (DungeonInfo dungeonInfo : dungeonInfos) {
						nbtTagList.add(dungeonInfo.writeToNBT());
					}
					root.put(data.getKey(), nbtTagList);
				}
			}
			FileIOUtil.writeNBT(this.file, root);

			this.modifiedSinceLastSave = false;
		}
	}

	public void readData() {
		this.dungeonData.clear();

		if (!this.file.exists()) {
			return;
		}

		CompoundNBT root = FileIOUtil.readNBT(this.file);

		for (String key : root.getAllKeys()) {
			Set<DungeonInfo> dungeonInfos = new HashSet<>();
			for (INBT nbt : root.getList(key, Constants.NBT.TAG_COMPOUND)) {
				dungeonInfos.add(new DungeonInfo((CompoundNBT) nbt));
			}
			if (!dungeonInfos.isEmpty()) {
				this.dungeonData.put(key, dungeonInfos);
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
