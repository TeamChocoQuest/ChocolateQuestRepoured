package team.cqr.cqrepoured.util.datafixer;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.mojang.datafixers.DataFixer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Constants.NBT;

public class DataFixerUtil {

	private static final int V1122 = 1343;
	private static final int V18w20c = 1493;
	private static final int V1165 = 2586;

	public static CompoundTag update(CompoundTag chunkTag) {
		DataFixer dataFixer = DataFixesManager.getDataFixer();
		chunkTag = NbtUtils.update(dataFixer, DefaultTypeReferences.CHUNK, chunkTag, V1122, V18w20c);
		chunkTag = NbtUtils.update(dataFixer, DefaultTypeReferences.CHUNK, chunkTag, V18w20c, V1165);
		chunkTag.putInt("DataVersion", V1165);
		return chunkTag;
	}

	public static IChunk read(Level world, CompoundTag chunkTag) {
		CompoundTag levelTag = chunkTag.getCompound("Level");
		ChunkPos chunkPos = new ChunkPos(levelTag.getInt("xPos"), levelTag.getInt("zPos"));
		BiomeContainer biomeContainer = new BiomeContainer(BIOME_REGISTRY, new int[BiomeContainer.BIOMES_SIZE]);
		UpgradeData upgradeData = levelTag.contains("UpgradeData", NBT.TAG_COMPOUND) ? new UpgradeData(levelTag.getCompound("UpgradeData")) : UpgradeData.EMPTY;
		ListTag sectionTagList = levelTag.getList("Sections", NBT.TAG_COMPOUND);
		ChunkSection[] sectionArray = new ChunkSection[16];

		for (int i = 0; i < sectionTagList.size(); i++) {
			CompoundTag sectionTag = sectionTagList.getCompound(i);
			int y = sectionTag.getByte("Y");
			if (sectionTag.contains("Palette", NBT.TAG_LIST) && sectionTag.contains("BlockStates", NBT.TAG_LONG_ARRAY)) {
				ChunkSection chunksection = new ChunkSection(y << 4);
				chunksection.getStates().read(sectionTag.getList("Palette", NBT.TAG_COMPOUND), sectionTag.getLongArray("BlockStates"));
				chunksection.recalcBlockCounts();
				if (!chunksection.isEmpty()) {
					sectionArray[y] = chunksection;
				}
			}
		}

		IChunk chunk = new Chunk(world, chunkPos, biomeContainer, upgradeData, EmptyTickList.empty(), EmptyTickList.empty(), 0L, sectionArray, (p_222648_1_) -> {
			postLoadChunk(levelTag, p_222648_1_);
		});

		ListTag postProcessingTagListList = levelTag.getList("PostProcessing", NBT.TAG_LIST);
		for (int i = 0; i < postProcessingTagListList.size(); i++) {
			ListTag postProcessingTagList = postProcessingTagListList.getList(i);
			for (int j = 0; j < postProcessingTagList.size(); j++) {
				chunk.addPackedPostProcess(postProcessingTagList.getShort(j), i);
			}
		}

		return chunk;
	}

	private static void postLoadChunk(CompoundTag p_222650_0_, Chunk p_222650_1_) {
		ListTag listnbt = p_222650_0_.getList("Entities", 10);
		Level world = p_222650_1_.getLevel();

		for (int i = 0; i < listnbt.size(); ++i) {
			CompoundTag compoundnbt = listnbt.getCompound(i);
			EntityType.loadEntityRecursive(compoundnbt, world, (p_222655_1_) -> {
				p_222650_1_.addEntity(p_222655_1_);
				return p_222655_1_;
			});
			p_222650_1_.setLastSaveHadEntities(true);
		}

		ListTag listnbt1 = p_222650_0_.getList("TileEntities", 10);

		for (int j = 0; j < listnbt1.size(); ++j) {
			CompoundTag compoundnbt1 = listnbt1.getCompound(j);
			boolean flag = compoundnbt1.getBoolean("keepPacked");
			if (flag) {
				p_222650_1_.setBlockEntityNbt(compoundnbt1);
			} else {
				BlockPos blockpos = new BlockPos(compoundnbt1.getInt("x"), compoundnbt1.getInt("y"), compoundnbt1.getInt("z"));
				BlockEntity tileentity = BlockEntity.loadStatic(p_222650_1_.getBlockState(blockpos), compoundnbt1);
				if (tileentity != null) {
					p_222650_1_.addBlockEntity(tileentity);
				}
			}
		}
	}

	private static final IObjectIntIterable<Biome> BIOME_REGISTRY = new IObjectIntIterable<Biome>() {
		
		private final Biome biome = new Biome.Builder()
				.precipitation(RainType.NONE)
				.biomeCategory(Category.NONE)
				.depth(0.0F)
				.scale(0.0F)
				.temperature(0.0F)
				.downfall(0.0F)
				.specialEffects(new BiomeAmbience.Builder()
						.fogColor(0)
						.waterColor(0)
						.waterFogColor(0)
						.skyColor(0)
						.build())
				.mobSpawnSettings(new MobSpawnInfo.Builder()
						.build())
				.generationSettings(new BiomeGenerationSettings.Builder()
						.surfaceBuilder(new ConfiguredSurfaceBuilder<>(null, null))
						.build())
				.build();
		private final List<Biome> biomeList = Collections.singletonList(biome);

		@Override
		public Iterator<Biome> iterator() {
			return biomeList.iterator();
		}

		@Override
		public int getId(Biome pValue) {
			return 0;
		}

		@Override
		public Biome byId(int pValue) {
			return biome;
		}

	};

}
