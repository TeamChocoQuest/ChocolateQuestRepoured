package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.init.CQRStructures;
import team.cqr.cqrepoured.util.Cache2D;
import team.cqr.cqrepoured.util.IntUtil;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

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
		super(CQRStructures.GENERATABLE_DUNGEON, 0);
		this.dungeonName = dungeonName;
		this.pos = pos;
		this.level = level;
		this.protectedRegionBuilder = protectedRegionBuilder;
		this.undergroundOffset = undergroundOffset;
		this.boundingBox = this.calculateBoundingBox();
		this.groundData = this.calculateGroundData();
	}

	private MutableBoundingBox calculateBoundingBox() {
		MutableBoundingBox boundingBox = MutableBoundingBox.getUnknownBox();
		Mutable mutablePos = new Mutable();
		
		level.getSections().forEach(section -> {
			SectionPos sectionPos = section.getPos();
			
			IntUtil.forEachSectionCoord((x, y, z) -> {
				CQRSection.setPos(mutablePos, sectionPos, x, y, z);
				BlockState state = section.getBlockState(mutablePos);
				if (state == null || state == Blocks.AIR.defaultBlockState()) {
					return;
				}

				boundingBox.x0 = Math.min(mutablePos.getX(), boundingBox.x0);
				boundingBox.y0 = Math.min(mutablePos.getY(), boundingBox.y0);
				boundingBox.z0 = Math.min(mutablePos.getZ(), boundingBox.z0);
				boundingBox.x1 = Math.max(mutablePos.getX(), boundingBox.x1);
				boundingBox.y1 = Math.max(mutablePos.getY(), boundingBox.y1);
				boundingBox.z1 = Math.max(mutablePos.getZ(), boundingBox.z1);
			});
		});

		boundingBox.x0 -= 16;
		boundingBox.y0 -= 16;
		boundingBox.z0 -= 16;
		boundingBox.x1 += 16;
		boundingBox.y1 += 16;
		boundingBox.z1 += 16;
		return boundingBox;
	}

	private Cache2D<GroundData> calculateGroundData() {
		Cache2D<GroundData> groundData = new Cache2D<>(boundingBox.x0, boundingBox.z0, boundingBox.x1, boundingBox.z1, null, GroundData[]::new);
		Mutable mutablePos = new Mutable();
		
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

	public GeneratableDungeon(TemplateManager templateManager, CompoundNBT nbt) {
		super(CQRStructures.GENERATABLE_DUNGEON, nbt);
		this.dungeonName = nbt.getString("dungeonName");
		this.pos = NBTUtil.readBlockPos(nbt.getCompound("pos"));
		this.level = new CQRLevel(nbt.getCompound("level"));
		this.protectedRegionBuilder = new ProtectedRegion.Builder(nbt.getCompound("protectedRegionBuilder"));
		this.undergroundOffset = 0;
		this.boundingBox = new MutableBoundingBox(nbt.getInt("x0"), nbt.getInt("y0"), nbt.getInt("y0"), nbt.getInt("x1"), nbt.getInt("y1"), nbt.getInt("y1"));
		this.groundData = new Cache2D<>(this.boundingBox.x0, this.boundingBox.z0, this.boundingBox.x1, this.boundingBox.z1, null, GroundData[]::new);
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {
		nbt.putString("dungeonName", this.dungeonName);
		nbt.put("pos", NBTUtil.writeBlockPos(this.pos));
		nbt.put("level", this.level.save());
		nbt.put("protectedRegionBuilder", this.protectedRegionBuilder.writeToNBT());
		nbt.putInt("x0", this.boundingBox.x0);
		nbt.putInt("y0", this.boundingBox.y0);
		nbt.putInt("z0", this.boundingBox.z0);
		nbt.putInt("x1", this.boundingBox.x1);
		nbt.putInt("y1", this.boundingBox.y1);
		nbt.putInt("z1", this.boundingBox.z1);
	}

	@Override
	public boolean postProcess(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
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
		return true;
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

				int dy = y - MathHelper.clamp(y, data.min, data.max);
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
		private final CQRLevel level;
		private final ProtectedRegion.Builder protectedRegionBuilder;

		// TODO temp solution
		private int undergroundOffset;

		public Builder(ServerWorld level, BlockPos pos, DungeonBase config) {
			this(level, pos, config.getDungeonName(), config.getDungeonMob());
			this.undergroundOffset = config.getUnderGroundOffset();
		}

		public Builder(ServerWorld level, BlockPos pos, String dungeonName, String defaultInhabitant) {
			this.dungeonName = dungeonName;
			this.pos = pos;
			this.defaultInhabitant = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(defaultInhabitant, level, pos.getX(), pos.getZ());
			this.entityFactory = new ServerEntityFactory(level);
			this.level = new CQRLevel(SectionPos.of(pos), level.getSeed());
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
			return new DungeonPlacement(this.pos, partPos, mirror, rotation, inhabitant, this.protectedRegionBuilder, this.entityFactory);
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

		public ICQRLevel getLevel() {
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
							this.getLevel().setBlockState(referenceLoc.offset(i, j, k), blocks[i][j][k]);
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
