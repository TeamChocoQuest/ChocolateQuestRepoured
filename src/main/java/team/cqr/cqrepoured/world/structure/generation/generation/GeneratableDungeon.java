package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import team.cqr.cqrepoured.init.CQRStructurePieceTypes;
import team.cqr.cqrepoured.util.Cache2D;
import team.cqr.cqrepoured.util.IntUtil;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

// TODO: Change to extends jigsaw structure piece
// TODO: Move the inhabitants access to a own object
// TODO: Move protection settings to codec object
public class GeneratableDungeon extends StructurePiece implements INoiseAffectingStructurePiece {

	private final String dungeonName;
	private final BlockPos pos;
	private final CQRLevel level;
	private final ProtectedRegion.Builder protectedRegionBuilder;
	private final int undergroundOffset;
	private final Cache2D<GroundData> groundData;
	private final StructureProcessorList processors = null;

	static class GroundData {

		private int min;
		private int max;

		public GroundData(int minMax) {
			this(minMax, minMax);
		}

		public GroundData(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public void update(int y) {
			if (y < min)
				min = y;
			if (y > max)
				max = y;
		}
	}
	
	protected GeneratableDungeon(String dungeonName, BlockPos pos, CQRLevel level, ProtectedRegion.Builder protectedRegionBuilder, int undergroundOffset) {
		super(CQRStructurePieceTypes.CQR_STRUCTURE_PIECE_TYPE, 0, calculateBoundingBox(level));
		this.dungeonName = dungeonName;
		this.pos = pos;
		this.level = level;
		this.protectedRegionBuilder = protectedRegionBuilder;
		this.undergroundOffset = undergroundOffset;
		this.groundData = this.calculateGroundData();
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

	private Cache2D<GroundData> calculateGroundData() {
		Cache2D<GroundData> groundData = new Cache2D<>(boundingBox.minX(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxZ(), null, GroundData[]::new);
		MutableBlockPos mutablePos = new MutableBlockPos();
		
		level.getSections().forEach(section -> {
			SectionPos sectionPos = section.getPos();
			
			IntUtil.forEachSectionCoord((x, y, z) -> {
				CQRSection.setPos(mutablePos, sectionPos, x, y, z);
				if (mutablePos.getY() >= pos.getY() + undergroundOffset) {
					return;
				}

				BlockState state = section.getBlockState(mutablePos);
				if (state == null || state == Blocks.AIR.defaultBlockState()) {
					return;
				}
				
				groundData.compute(mutablePos, (k, v) -> {
					if (v == null) {
						return new GroundData(mutablePos.getY());
					}
					v.update(mutablePos.getY());
					return v;
				});
			});
		});
		
		return groundData;
	}

	public GeneratableDungeon(CompoundTag nbt) {
		super(CQRStructurePieceTypes.CQR_STRUCTURE_PIECE_TYPE, nbt);
		this.dungeonName = nbt.getString("dungeonName");
		this.pos = NbtUtils.readBlockPos(nbt.getCompound("pos"));
		this.level = new CQRLevel(nbt.getCompound("level"));
		this.protectedRegionBuilder = new ProtectedRegion.Builder(nbt.getCompound("protectedRegionBuilder"));
		this.undergroundOffset = 0;
		this.boundingBox = new BoundingBox(nbt.getInt("x0"), nbt.getInt("y0"), nbt.getInt("y0"), nbt.getInt("x1"), nbt.getInt("y1"), nbt.getInt("y1"));
		this.groundData = new Cache2D<>(this.boundingBox.minX(), this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxZ(), null, GroundData[]::new);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
		nbt.putString("dungeonName", this.dungeonName);
		nbt.put("pos", NbtUtils.writeBlockPos(this.pos));
		nbt.put("level", this.level.save());
		nbt.put("protectedRegionBuilder", this.protectedRegionBuilder.writeToNBT());
		nbt.putInt("x0", this.boundingBox.minX());
		nbt.putInt("y0", this.boundingBox.minY());
		nbt.putInt("z0", this.boundingBox.minZ());
		nbt.putInt("x1", this.boundingBox.maxX());
		nbt.putInt("y1", this.boundingBox.maxY());
		nbt.putInt("z1", this.boundingBox.maxZ());
	}

	@Override
	public void postProcess(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator pGenerator, RandomSource pRandom, BoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(pLevel.getLevel());
		ProtectedRegion protectedRegion = this.protectedRegionBuilder.build(pLevel.getLevel());
		if (protectedRegion != null) {
			protectedRegion.markDirty();
			protectedRegionManager.addProtectedRegion(protectedRegion);
		}

		//Structure processors...
		List<StructureProcessor> processorList = new ArrayList<>();
		if(this.processors != null) {
			processorList.addAll(this.processors.list());
		}
		
		this.level.generate(pLevel, pBox, new ServerEntityFactory(pLevel.getLevel()), processorList);
	}

	@Override
	public double getContribution(int x, int y, int z) {
		double maxNoise = 0.0D;

		for (int dx = -NoiseUtil.RADIUS; dx <= NoiseUtil.RADIUS; dx++) {
			if (!groundData.inBoundsX(x + dx)) {
				continue;
			}

			for (int dz = -NoiseUtil.RADIUS; dz <= NoiseUtil.RADIUS; dz++) {
				if (!groundData.inBoundsZ(z + dz)) {
					continue;
				}

				GroundData data = groundData.getUnchecked(x + dx, z + dz);
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
	
	public Optional<DungeonBase> getDungeonConfig() {
		if(this.dungeonName.isEmpty()) {
			return Optional.empty();
		}
		return Optional.ofNullable(DungeonRegistry.getInstance().getDungeon(this.dungeonName));
	}

	public static class Builder {

		private final String dungeonName;
		private final BlockPos pos;
		private final DungeonInhabitant defaultInhabitant;
		private final ServerEntityFactory entityFactory;
		private final ServerLevel serverLevel;
		private final CQRLevel level;
		private final ProtectedRegion.Builder protectedRegionBuilder;

		// TODO temp solution
		private int undergroundOffset;

		public Builder(ServerLevel level, BlockPos pos, DungeonBase config) {
			this(level, pos, config.getDungeonName(), config.getDungeonMob());
			this.undergroundOffset = config.getUnderGroundOffset();
		}

		public Builder(ServerLevel level, BlockPos pos, String dungeonName, String defaultInhabitant) {
			this.dungeonName = dungeonName;
			this.pos = pos;
			this.defaultInhabitant = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(defaultInhabitant, level, pos.getX(), pos.getZ());
			this.entityFactory = new ServerEntityFactory(level);
			this.level = new CQRLevel(SectionPos.of(pos), level.getSeed());
			this.serverLevel = level;
			this.protectedRegionBuilder = new ProtectedRegion.Builder(dungeonName, pos);
		}

		public GeneratableDungeon build() {
			return new GeneratableDungeon(this.dungeonName, this.pos, this.level, this.protectedRegionBuilder, this.undergroundOffset);
		}

		public DungeonPlacement getPlacement(BlockPos partPos) {
			return this.getPlacement(partPos, Mirror.NONE, Rotation.NONE, this.defaultInhabitant);
		}

		public DungeonPlacement getPlacement(BlockPos partPos, Mirror mirror, Rotation rotation) {
			return this.getPlacement(partPos, mirror, rotation, this.defaultInhabitant);
		}

		public DungeonPlacement getPlacement(BlockPos partPos, DungeonInhabitant inhabitant) {
			return this.getPlacement(partPos, Mirror.NONE, Rotation.NONE, inhabitant);
		}

		public DungeonPlacement getPlacement(BlockPos partPos, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant) {
			return new DungeonPlacement(this.pos, partPos, mirror, rotation, inhabitant, this.protectedRegionBuilder, this.entityFactory, this.serverLevel.getRandom());
		}

		public String getDungeonName() {
			return dungeonName;
		}

		public BlockPos getPos() {
			return pos;
		}

		public DungeonInhabitant getDefaultInhabitant() {
			return defaultInhabitant;
		}

		public ServerEntityFactory getEntityFactory() {
			return entityFactory;
		}

		public CQRLevel getLevel() {
			return level;
		}

		public ProtectedRegion.Builder getProtectedRegionBuilder() {
			return protectedRegionBuilder;
		}

		public void write3DArray(BlockState[][][] blocks, BlockPos referenceLoc) {
			this.write3DArray(blocks, referenceLoc, false);
		}
		
		public void write3DArray(BlockState[][][] blocks, BlockPos referenceLoc, boolean resetDataInArray) {
			for (int i = 0; i < blocks.length; i++) {
				for (int j = 0; j < blocks[i].length; j++) {
					for (int k = 0; k < blocks[i][j].length; k++) {
						if (blocks[i][j][k] != null) {
							this.getLevel().setBlockState(referenceLoc.offset(i, j, k), blocks[i][j][k], (b) -> {});
							if(resetDataInArray) {
								blocks[i][j][k] = null;
							}
						}
					}
				}
			}
		}
		
	}

}
