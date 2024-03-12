package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.collection.Cache2D;
import team.cqr.cqrepoured.common.primitive.IntUtil;
import team.cqr.cqrepoured.generation.init.CQRStructurePieceTypes;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.DungeonInhabitant;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.InhabitantSelector;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.NoiseContributor;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.NoiseUtil2;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.NoiseUtil2.NoiseConfiguration;
import team.cqr.cqrepoured.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.protection.ProtectedRegion;
import team.cqr.cqrepoured.protection.ProtectedRegionManager;
import team.cqr.cqrepoured.protection.ProtectionSettings;

// TODO: Change to extends jigsaw structure piece
// TODO: Move the inhabitants access to a own object
// TODO: Move protection settings to codec object
public class CQRStructurePiece extends StructurePiece implements NoiseContributor {

	@Deprecated
	private static final Codec<Cache2D<HeightInfo>> HEIGHT_MAP_CODEC = Cache2D.codec(HeightInfo.CODEC, HeightInfo[]::new);
	private static final NoiseConfiguration DEFAULT_NOISE_CONFIGURATION = new NoiseConfiguration(6, 24, 1.0D, 1.0D);
	private final BlockPos pos;
	private final CQRLevel level;
	private Optional<ProtectedRegion> protectedRegion;
	@Deprecated
	private final Cache2D<HeightInfo> heightMap;
	private final StructureProcessorList processors = new StructureProcessorList(List.of());
	private final NoiseUtil2 noiseUtil = NoiseUtil2.get(DEFAULT_NOISE_CONFIGURATION);
	private final BoundingBox unextendedBoundingBox;

	@Deprecated
	private static class HeightInfo {

		private static final Codec<HeightInfo> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					Codec.BOOL.fieldOf("valid").forGetter(heightInfo -> heightInfo.valid),
					Codec.INT.fieldOf("min").forGetter(heightInfo -> heightInfo.min),
					Codec.INT.fieldOf("max").forGetter(heightInfo -> heightInfo.max))
					.apply(instance, HeightInfo::new);
		});
		private boolean valid;
		private int min;
		private int max;

		public HeightInfo() {
			this(false, 0, 0);
		}

		private HeightInfo(boolean valid, int min, int max) {
			this.valid = valid;
			this.min = min;
			this.max = max;
		}

		public void update(int height) {
			if (!valid) {
				valid = true;
				min = height;
				max = height;
			} else {
				min = Math.min(min, height);
				max = Math.max(max, height);
			}
		}

	}
	
	protected CQRStructurePiece(BlockPos pos, CQRLevel level, Optional<ProtectedRegion> protectedRegion, int groundLevelDelta) {
		super(CQRStructurePieceTypes.CQR_STRUCTURE_PIECE_TYPE.get(), 0, calculateBoundingBox(level));
		this.pos = pos;
		this.level = level;
		this.protectedRegion = protectedRegion;
		this.heightMap = calculateHeightMap(pos, this.boundingBox, level, groundLevelDelta);
		this.unextendedBoundingBox = DEFAULT_NOISE_CONFIGURATION.unextend(this.boundingBox);
	}

	@SuppressWarnings("deprecation")
	private static BoundingBox calculateBoundingBox(CQRLevel level) {
		AtomicReference<BoundingBox> boundingBox = new AtomicReference<>();
		MutableBlockPos mutablePos = new MutableBlockPos();

		level.getSections().forEach(section -> {
			SectionPos sectionPos = section.getPos();
			
			IntUtil.forEachSectionCoord((x, y, z) -> {
				CQRSection.setPos(mutablePos, sectionPos, x, y, z);
				BlockState state = section.getBlockState(mutablePos);
				if (state == null || state == Blocks.AIR.defaultBlockState()) {
					return;
				}

				if (boundingBox.get() == null) {
					boundingBox.set(new BoundingBox(mutablePos));
				} else {
					boundingBox.get().encapsulate(mutablePos);
				}
			});
		});

		if (boundingBox.get() == null) {
			// TODO empty structure -> log warning? throw error?
			boundingBox.set(new BoundingBox(level.getCenter().center()));
		}

		return DEFAULT_NOISE_CONFIGURATION.extend(boundingBox.get());
	}

	@Deprecated
	private static Cache2D<HeightInfo> calculateHeightMap(BlockPos pos, BoundingBox boundingBox, CQRLevel level, int groundLevelDelta) {
		Cache2D<HeightInfo> heightMap = new Cache2D<>(boundingBox.minX(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxZ(), null, HeightInfo[]::new,
				HeightInfo::new);
		MutableBlockPos mutablePos = new MutableBlockPos();
		
		level.getSections().forEach(section -> {
			SectionPos sectionPos = section.getPos();
			
			IntUtil.forEachSectionCoord((x, y, z) -> {
				CQRSection.setPos(mutablePos, sectionPos, x, y, z);
				if (mutablePos.getY() >= pos.getY() + groundLevelDelta) {
					return;
				}

				BlockState state = section.getBlockState(mutablePos);
				if (state == null || state == Blocks.AIR.defaultBlockState()) {
					return;
				}

				heightMap.get(mutablePos).update(mutablePos.getY());
			});
		});
		
		return heightMap;
	}

	public CQRStructurePiece(CompoundTag nbt) {
		super(CQRStructurePieceTypes.CQR_STRUCTURE_PIECE_TYPE.get(), nbt);
		this.pos = NbtUtils.readBlockPos(nbt.getCompound("pos"));
		this.level = new CQRLevel(nbt.getCompound("level"));
		this.protectedRegion = Optional.of("protected_region")
				.filter(nbt::contains)
				.map(nbt::getCompound)
				.map(ProtectedRegion::readFromNBT);
		this.heightMap = HEIGHT_MAP_CODEC.decode(NbtOps.INSTANCE, nbt.get("height_map"))
				.getOrThrow(false, CQRepoured.LOGGER::error)
				.getFirst();
		this.unextendedBoundingBox = DEFAULT_NOISE_CONFIGURATION.unextend(this.boundingBox);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
		nbt.put("level", this.level.save());
		this.protectedRegion.ifPresent(protectedRegion -> {
			nbt.put("protected_region", protectedRegion.writeToNBT());
		});
		nbt.put("height_map", HEIGHT_MAP_CODEC.encode(this.heightMap, NbtOps.INSTANCE, new CompoundTag())
				.getOrThrow(false, CQRepoured.LOGGER::error));
	}

	@Override
	public void postProcess(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator pGenerator, RandomSource pRandom, BoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
		if (this.protectedRegion.isPresent()) {
			Optional<ProtectedRegion> protectedRegion;
			synchronized (this) {
				protectedRegion = this.protectedRegion;
				this.protectedRegion = Optional.empty();
			}
			if (protectedRegion.isPresent()) {
				IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(pLevel.getLevel());
				if (protectedRegionManager != null) {
					protectedRegionManager.addProtectedRegion(protectedRegion.get());
				}
			}
		}

		this.level.generate(pLevel, pBox, new EntityFactory(pLevel.getLevel()), this.processors.list());
	}

	@Override
	public double getContribution(int x, int y, int z) {
		if (!this.boundingBox.isInside(x, y, z)) {
			return 0.0D;
		}
		for (Vec3i offset : this.noiseUtil.getNearestPositions()) {
			if (!this.unextendedBoundingBox.isInside(x - offset.getX(), y - offset.getY(), z - offset.getZ())) {
				continue;
			}
			BlockState blockState = this.level.getBlockState(x - offset.getX(), y - offset.getY(), z - offset.getZ());
			if (blockState == null || blockState == Blocks.AIR.defaultBlockState()) {
				continue;
			}
			return this.noiseUtil.getBeardContribution(-offset.getX(), -offset.getY(), -offset.getZ(), this.pos.getY());
		}
		return 0.0D;
	}

	public static record Builder(BlockPos pos, CQRLevel level, DungeonInhabitant inhabitant, EntityFactory entityFactory, int groundLevelDelta,
			Optional<ProtectedRegion.Builder> protectedRegionBuilder, RandomSource random) {

		public static Builder create(GenerationContext context, BlockPos pos, InhabitantSelector inhabitantMap, int groundLevelDelta,
				Optional<ProtectionSettings> protectionSettings) {
			ServerLevel level = WorldDungeonGenerator.getLevel(context.chunkGenerator());
			CQRLevel structureLevel = new CQRLevel(SectionPos.of(pos), level.getSeed());
			DungeonInhabitant inhabitant = inhabitantMap.get(context, pos);
			EntityFactory entityFactory = new EntityFactory(level);
			Optional<ProtectedRegion.Builder> protectedRegionBuilder = protectionSettings.map(settings -> new ProtectedRegion.Builder(pos, settings));
			RandomSource random = context.random();
			return new Builder(pos, structureLevel, inhabitant, entityFactory, groundLevelDelta, protectedRegionBuilder, random);
		}

		public CQRStructurePiece build() {
			return new CQRStructurePiece(this.pos, this.level, this.protectedRegionBuilder.map(ProtectedRegion.Builder::build), this.groundLevelDelta);
		}

		public DungeonPlacement getPlacement(BlockPos partPos) {
			return this.getPlacement(partPos, Mirror.NONE, Rotation.NONE, this.inhabitant);
		}

		public DungeonPlacement getPlacement(BlockPos partPos, Mirror mirror, Rotation rotation) {
			return this.getPlacement(partPos, mirror, rotation, this.inhabitant);
		}

		public DungeonPlacement getPlacement(BlockPos partPos, DungeonInhabitant inhabitant) {
			return this.getPlacement(partPos, Mirror.NONE, Rotation.NONE, inhabitant);
		}

		public DungeonPlacement getPlacement(BlockPos partPos, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant) {
			return new DungeonPlacement(this.pos, partPos, mirror, rotation, inhabitant, this.protectedRegionBuilder, this.entityFactory, this.random);
		}

	}

}
