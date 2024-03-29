package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.common.io.FileIOUtil;

//TODO: Replace with worldSavedData or capability
public class DungeonDataManager {

	public static class DungeonInfo {
		private BlockPos pos;
		private DungeonSpawnType spawnType;

		public DungeonInfo(BlockPos pos, DungeonSpawnType spawnType) {
			this.pos = pos.immutable();
			this.spawnType = spawnType;
		}

		public DungeonInfo(CompoundTag compound) {
			this.readFromNBT(compound);
		}

		public CompoundTag writeToNBT() {
			CompoundTag compound = new CompoundTag();
			compound.put("pos", NbtUtils.writeBlockPos(this.pos));
			compound.putInt("spawnType", this.spawnType.ordinal());
			return compound;
		}

		public void readFromNBT(CompoundTag compound) {
			if (compound.contains("pos", Tag.TAG_COMPOUND)) {
				this.pos = NbtUtils.readBlockPos(compound.getCompound("pos"));
			} else {
				this.pos = NbtUtils.readBlockPos(compound);
			}
			this.spawnType = DungeonSpawnType.values()[compound.getInt("spawnType")];
		}
	}

	public enum DungeonSpawnType {
		DUNGEON_GENERATION, LOCKED_COORDINATE, DUNGEON_PLACER_ITEM;
	}

	private static final Map<Level, DungeonDataManager> INSTANCES = Collections.synchronizedMap(new HashMap<>());

	private final Map<ResourceLocation, Set<DungeonInfo>> dungeonData = Collections.synchronizedMap(new HashMap<>());
	private final File file;
	private boolean modifiedSinceLastSave = false;

	public DungeonDataManager(Level world) {
		this.file = FileIOUtil.getCQRDataFile((ServerLevel) world, "CQR/structures.nbt");
	}

	@Nullable
	public static DungeonDataManager getInstance(Level world) {
		if (!world.isClientSide()) {
			return INSTANCES.get(world);
		}
		return null;
	}

	public static void handleWorldLoad(Level world) {
		if (!world.isClientSide() && !INSTANCES.containsKey(world)) {
			INSTANCES.put(world, new DungeonDataManager(world));
			INSTANCES.get(world).readData();
		}
	}

	public static void handleWorldSave(Level world) {
		if (!world.isClientSide() && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).saveData();
		}
	}

	public static void handleWorldUnload(Level world) {
		if (!world.isClientSide() && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).saveData();
			INSTANCES.remove(world);
		}
	}

	public static void addDungeonEntry(Level world, ResourceLocation name, BlockPos position, DungeonSpawnType spawnType) {
		if (INSTANCES.containsKey(world)) {
			INSTANCES.get(world).addDungeonEntry(name, position, spawnType);
		}
	}

	public static Set<ResourceLocation> getSpawnedDungeonNames(Level world) {
		if (INSTANCES.containsKey(world)) {
			return INSTANCES.get(world).getSpawnedDungeonNames();
		}
		return Collections.emptySet();
	}

	public static Set<DungeonInfo> getLocationsOfDungeon(Level world, ResourceLocation dungeon) {
		if (INSTANCES.containsKey(world)) {
			return INSTANCES.get(world).getLocationsOfDungeon(dungeon);
		}
		return Collections.emptySet();
	}

//	public static boolean isDungeonSpawnLimitMet(Level world, DungeonBase dungeon) {
//		if (INSTANCES.containsKey(world)) {
//			return INSTANCES.get(world).isDungeonSpawnLimitMet(dungeon);
//		}
//		return false;
//	}

	public static int getDungeonGenerationCount(Level world, ResourceLocation name) {
		// TODO
		return -1;
	}

	public void saveData() {
		if (this.modifiedSinceLastSave) {
			CompoundTag root = new CompoundTag();
			for (Map.Entry<ResourceLocation, Set<DungeonInfo>> data : this.dungeonData.entrySet()) {
				Set<DungeonInfo> dungeonInfos = data.getValue();
				if (!dungeonInfos.isEmpty()) {
					ListTag nbtTagList = new ListTag();
					for (DungeonInfo dungeonInfo : dungeonInfos) {
						nbtTagList.add(dungeonInfo.writeToNBT());
					}
					root.put(data.getKey().toString(), nbtTagList);
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

		CompoundTag root = FileIOUtil.readNBT(this.file);

		for (String key : root.getAllKeys()) {
			Set<DungeonInfo> dungeonInfos = new HashSet<>();
			for (Tag nbt : root.getList(key, Tag.TAG_COMPOUND)) {
				dungeonInfos.add(new DungeonInfo((CompoundTag) nbt));
			}
			if (!dungeonInfos.isEmpty()) {
				this.dungeonData.put(new ResourceLocation(key), dungeonInfos);
			}
		}
	}

	private void addDungeonEntry(ResourceLocation dungeon, BlockPos location, DungeonSpawnType spawnType) {
		Set<DungeonInfo> spawnedLocs = this.dungeonData.computeIfAbsent(dungeon, key -> Collections.synchronizedSet(new HashSet<>()));
		if (spawnedLocs.add(new DungeonInfo(location, spawnType))) {
			this.modifiedSinceLastSave = true;
		}
	}

	private Set<ResourceLocation> getSpawnedDungeonNames() {
		return this.dungeonData.keySet();
	}

	private Set<DungeonInfo> getLocationsOfDungeon(ResourceLocation dungeon) {
		return this.dungeonData.getOrDefault(dungeon, Collections.emptySet());
	}

//	private boolean isDungeonSpawnLimitMet(DungeonBase dungeon) {
//		if (dungeon.getSpawnLimit() < 0) {
//			return false;
//		}
//		if (this.dungeonData.isEmpty()) {
//			return false;
//		}
//		Set<DungeonInfo> spawnedLocs = this.dungeonData.get(dungeon.getDungeonName());
//		if (spawnedLocs == null) {
//			return false;
//		}
//		return spawnedLocs.stream().filter(dungeonInfo -> dungeonInfo.spawnType == DungeonSpawnType.DUNGEON_GENERATION).count() >= dungeon.getSpawnLimit();
//	}

}
