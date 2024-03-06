package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.collection.Cache2D;
import team.cqr.cqrepoured.common.primitive.IntUtil;
import team.cqr.cqrepoured.generation.init.CQRStructurePieceTypes;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.INoiseAffectingStructurePiece;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.NoiseUtil;
import team.cqr.cqrepoured.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.protection.ProtectedRegion;
import team.cqr.cqrepoured.protection.ProtectedRegionManager;
import team.cqr.cqrepoured.protection.ProtectionSettings;

// TODO: Change to extends jigsaw structure piece
// TODO: Move the inhabitants access to a own object
// TODO: Move protection settings to codec object
public class CQRStructurePiece extends StructurePiece implements INoiseAffectingStructurePiece {

	private static final Codec<Cache2D<HeightInfo>> HEIGHT_MAP_CODEC = Cache2D.codec(HeightInfo.CODEC, HeightInfo[]::new);
	private final CQRLevel level;
	private Optional<ProtectedRegion> protectedRegion;
	private final Cache2D<HeightInfo> heightMap;
	private final StructureProcessorList processors = null;

	private static class HeightInfo {

		private static final Codec<HeightInfo> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					Codec.INT.fieldOf("min").forGetter(groundData -> groundData.min),
					Codec.INT.fieldOf("max").forGetter(groundData -> groundData.max))
					.apply(instance, HeightInfo::new);
		});
		private int min;
		private int max;

		public HeightInfo(int minMax) {
			this(minMax, minMax);
		}

		public HeightInfo(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public void update(int height) {
			if (height < min)
				min = height;
			if (height > max)
				max = height;
		}

	}
	
	protected CQRStructurePiece(BlockPos pos, CQRLevel level, Optional<ProtectedRegion> protectedRegion, int groundLevelDelta) {
		super(CQRStructurePieceTypes.CQR_STRUCTURE_PIECE_TYPE.get(), 0, calculateBoundingBox(level));
		this.level = level;
		this.protectedRegion = protectedRegion;
		this.heightMap = calculateHeightMap(pos, this.boundingBox, level, groundLevelDelta);
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
			return new BoundingBox(level.getCenter().center());
		}

		return boundingBox.get().inflatedBy(16);
	}

	private static Cache2D<HeightInfo> calculateHeightMap(BlockPos pos, BoundingBox boundingBox, CQRLevel level, int groundLevelDelta) {
		Cache2D<HeightInfo> heightMap = new Cache2D<>(boundingBox.minX(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxZ(), null, HeightInfo[]::new);
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

				heightMap.compute(mutablePos, (k, v) -> {
					if (v == null) {
						return new HeightInfo(k.getY());
					}
					v.update(k.getY());
					return v;
				});
			});
		});
		
		return heightMap;
	}

	public CQRStructurePiece(CompoundTag nbt) {
		super(CQRStructurePieceTypes.CQR_STRUCTURE_PIECE_TYPE.get(), nbt);
		this.level = new CQRLevel(nbt.getCompound("level"));
		this.protectedRegion = Optional.of("protected_region")
				.filter(nbt::contains)
				.map(nbt::getCompound)
				.map(ProtectedRegion::readFromNBT);
		this.heightMap = HEIGHT_MAP_CODEC.decode(NbtOps.INSTANCE, nbt.get("height_map"))
				.getOrThrow(false, CQRepoured.LOGGER::error)
				.getFirst();
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
		double maxNoise = 0.0D;

		for (int dx = -NoiseUtil.RADIUS; dx <= NoiseUtil.RADIUS; dx++) {
			if (!heightMap.inBoundsX(x + dx)) {
				continue;
			}

			for (int dz = -NoiseUtil.RADIUS; dz <= NoiseUtil.RADIUS; dz++) {
				if (!heightMap.inBoundsZ(z + dz)) {
					continue;
				}

				HeightInfo data = heightMap.getUnchecked(x + dx, z + dz);
				if (data == null) {
					continue;
				}

				if (y < data.min - NoiseUtil.RADIUS || y > data.max + NoiseUtil.RADIUS) {
					continue;
				}

				int dy = y - Mth.clamp(y, data.min, data.max);
				maxNoise = Math.max(NoiseUtil.getContribution(dx, dy, dz), maxNoise);
			}
		}

		return maxNoise;
	}

	public static record Builder(BlockPos pos, CQRLevel level, DungeonInhabitant inhabitant, EntityFactory entityFactory, int groundLevelDelta, Optional<ProtectedRegion.Builder> protectedRegionBuilder, RandomSource random) {

		public Builder(ServerLevel level, BlockPos pos, DungeonInhabitant inhabitant, int groundLevelDelta, Optional<ProtectionSettings> protectionSettings, RandomSource random) {
			this(pos, new CQRLevel(SectionPos.of(pos), level.getSeed()), inhabitant, new EntityFactory(level), groundLevelDelta, protectionSettings.map(settings -> new ProtectedRegion.Builder(pos, settings)), random);
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
