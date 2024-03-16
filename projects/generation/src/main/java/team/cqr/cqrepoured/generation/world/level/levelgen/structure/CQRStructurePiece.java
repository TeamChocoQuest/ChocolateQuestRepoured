package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
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
import team.cqr.cqrepoured.common.primitive.IntUtil;
import team.cqr.cqrepoured.generation.init.CQRStructurePieceTypes;
import team.cqr.cqrepoured.generation.util.SectionUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.DungeonInhabitant;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.InhabitantSelector;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.NoiseContributionCache.NoiseConfiguration;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.NoiseContributor;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.NoiseMap;
import team.cqr.cqrepoured.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.protection.ProtectedRegion;
import team.cqr.cqrepoured.protection.ProtectedRegionManager;
import team.cqr.cqrepoured.protection.ProtectionSettings;

// TODO: Change to extends jigsaw structure piece
// TODO: Move the inhabitants access to a own object
// TODO: Move protection settings to codec object
public class CQRStructurePiece extends StructurePiece implements NoiseContributor {

	private static final NoiseConfiguration NOISE_CONFIG_NEGATIVE = new NoiseConfiguration(8, 8, 8, 1.0D, 1.0D, 1.0D);
	private static final NoiseConfiguration NOISE_CONFIG_POSITIVE = new NoiseConfiguration(8, 24, 8, 1.0D, 0.25D, 1.0D);
	private final CQRLevel level;
	private final NoiseMap noiseMap;
	private Optional<ProtectedRegion> protectedRegion;
	private final StructureProcessorList processors = new StructureProcessorList(List.of());
	
	protected CQRStructurePiece(CQRLevel level, NoiseMap noiseMap, Optional<ProtectedRegion> protectedRegion) {
		super(CQRStructurePieceTypes.CQR_STRUCTURE_PIECE_TYPE.get(), 0, calculateBoundingBox(level));
		this.level = level;
		this.noiseMap = noiseMap;
		this.protectedRegion = protectedRegion;
	}

	@SuppressWarnings("deprecation")
	private static BoundingBox calculateBoundingBox(CQRLevel level) {
		AtomicReference<BoundingBox> boundingBox = new AtomicReference<>();
		MutableBlockPos mutablePos = new MutableBlockPos();

		level.getSections().forEach(section -> {
			SectionPos sectionPos = section.getPos();
			
			IntUtil.forEachSectionCoord((x, y, z) -> {
				SectionUtil.setPos(mutablePos, sectionPos, x, y, z);
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

		return NOISE_CONFIG_POSITIVE.extend(boundingBox.get());
	}

	public CQRStructurePiece(CompoundTag nbt) {
		super(CQRStructurePieceTypes.CQR_STRUCTURE_PIECE_TYPE.get(), nbt);
		this.level = new CQRLevel(nbt.getCompound("level"));
		this.protectedRegion = Optional.of("protected_region")
				.filter(nbt::contains)
				.map(nbt::getCompound)
				.map(ProtectedRegion::readFromNBT);
		this.noiseMap = new NoiseMap(nbt.getCompound("noise"));
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
		nbt.put("level", this.level.save());
		this.protectedRegion.ifPresent(protectedRegion -> {
			nbt.put("protected_region", protectedRegion.writeToNBT());
		});
		nbt.put("noise", this.noiseMap.save());
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
		return noiseMap.get(x, y, z);
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
			NoiseMap noiseMap = new NoiseMap(this.level, this.pos.getY() + this.groundLevelDelta, NOISE_CONFIG_NEGATIVE, NOISE_CONFIG_POSITIVE);
			return new CQRStructurePiece(this.level, noiseMap, this.protectedRegionBuilder.map(ProtectedRegion.Builder::build));
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
