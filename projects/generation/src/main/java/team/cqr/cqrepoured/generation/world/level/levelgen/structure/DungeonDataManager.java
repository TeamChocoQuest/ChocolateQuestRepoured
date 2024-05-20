package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.serialization.CodecUtil;

//TODO: Replace with worldSavedData or capability
public class DungeonDataManager extends SavedData {
	
	public enum DungeonSpawnType implements StringRepresentable {
		DUNGEON_GENERATION, 
		LOCKED_COORDINATE, 
		DUNGEON_PLACER_ITEM;

		public static final Codec<DungeonSpawnType> CODEC = StringRepresentable.fromEnum(DungeonSpawnType::values);
		
		@Override
		public String getSerializedName() {
			return this.name();
		}
	}

	public static class DungeonInfo {
		
		public static final Codec<DungeonInfo> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					BlockPos.CODEC.fieldOf("position").forGetter(di -> di.pos),
					DungeonSpawnType.CODEC.fieldOf("type").forGetter(di -> di.spawnType)
				).apply(instance, DungeonInfo::new);
		});
		
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

	private final Map<ResourceLocation, List<DungeonInfo>> dungeonData;
	
	public static final Codec<DungeonDataManager> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.unboundedMap(ResourceLocation.CODEC, DungeonInfo.CODEC.listOf()).fieldOf("dungeondata").forGetter(dm -> dm.dungeonData)
			).apply(instance, DungeonDataManager::new);
	});

	private DungeonDataManager(Map<ResourceLocation, List<DungeonInfo>> data) {
		this.dungeonData = data;
	}
	
	private static DungeonDataManager fromNBT(CompoundTag tag) {
		return CodecUtil.decodeNBT(DungeonDataManager.CODEC, tag).get();
	}
	
	private DungeonDataManager() {
		this.dungeonData = new Object2ObjectArrayMap<ResourceLocation, List<DungeonInfo>>();
	}
	
	public static final String KEY = CQRepoured.prefix("dungeondata").toString();

	@Nullable
	public static DungeonDataManager getInstance(Level world) {
		if (world instanceof ServerLevel sl) {
			DimensionDataStorage storage = sl.getDataStorage();
			return storage.computeIfAbsent(DungeonDataManager::fromNBT, DungeonDataManager::new, KEY);
		}
		return null;
	}

	public static Set<ResourceLocation> getSpawnedDungeonNames(Level world) {
		DungeonDataManager instance = getInstance(world);
		if (instance != null) {
			return instance.dungeonData.keySet();
		}
		return Collections.emptySet();
	}

	public static List<DungeonInfo> getLocationsOfDungeon(Level world, ResourceLocation dungeon) {
		DungeonDataManager instance = getInstance(world);
		if (instance != null) {
			return instance.dungeonData.getOrDefault(dungeon, Collections.emptyList());
		}
		return Collections.emptyList();
	}

//	public static boolean isDungeonSpawnLimitMet(Level world, DungeonBase dungeon) {
//		if (INSTANCES.containsKey(world)) {
//			return INSTANCES.get(world).isDungeonSpawnLimitMet(dungeon);
//		}
//		return false;
//	}

	public static int getDungeonGenerationCount(Level world, ResourceLocation name) {
		DungeonDataManager instance = getInstance(world);
		if (instance != null) {
			return instance.getLocationsOfDungeon(name).size();
		} else {
			return -1;
		}
	}

	private void addDungeonEntry(ResourceLocation dungeon, BlockPos location, DungeonSpawnType spawnType) {
		List<DungeonInfo> spawnedLocs = this.dungeonData.computeIfAbsent(dungeon, key -> Collections.emptyList());
		if (spawnedLocs.add(new DungeonInfo(location, spawnType))) {
			this.setDirty(true);
		}
	}

	private Set<ResourceLocation> getSpawnedDungeonNames() {
		return this.dungeonData.keySet();
	}

	private List<DungeonInfo> getLocationsOfDungeon(ResourceLocation dungeon) {
		return this.dungeonData.getOrDefault(dungeon, Collections.emptyList());
	}

	// TODO: Correct??
	@Override
	public CompoundTag save(CompoundTag pCompoundTag) {
		Optional<Tag> optTag = CodecUtil.encodeNBT(CODEC, this, pCompoundTag);
		if (optTag.isPresent() && optTag.get() instanceof CompoundTag) {
			return (CompoundTag) optTag.get();
		} else {
			return new CompoundTag();
		}
	}

	public static void addDungeonEntry(ServerLevel level, ResourceLocation structureName, BlockPos position, DungeonSpawnType dungeonGeneration) {
		DungeonDataManager instance = getInstance(level);
		if (instance != null) {
			instance.addDungeonEntry(structureName, position, dungeonGeneration);
		}
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
