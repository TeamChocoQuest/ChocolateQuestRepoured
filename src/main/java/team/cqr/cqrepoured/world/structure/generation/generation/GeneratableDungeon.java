package team.cqr.cqrepoured.world.structure.generation.generation;

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
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.util.Cache2D;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

public class GeneratableDungeon extends StructurePiece implements INoiseAffectingStructurePiece {

	private static final IStructurePieceType GENERATABLE_DUNGEON = IStructurePieceType.setPieceId(GeneratableDungeon::new, "generatable_dungeon");

	private final String dungeonName;
	private final BlockPos pos;
	private final CQRLevel level;
	private final ProtectedRegion.Builder protectedRegionBuilder;
	private final int undergroundOffset;
	private final Cache2D<GroundData> groundData;

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
		super(GENERATABLE_DUNGEON, 0);
		this.dungeonName = dungeonName;
		this.pos = pos;
		this.level = level;
		this.protectedRegionBuilder = protectedRegionBuilder;
		this.undergroundOffset = undergroundOffset;
		// TODO calculate correct bounding box
		this.boundingBox = new MutableBoundingBox(pos.offset(-64, -64, -64), pos.offset(64, 64, 64));

		this.groundData = new Cache2D<>(this.boundingBox.x0, this.boundingBox.z0, this.boundingBox.x1, this.boundingBox.z1, null, GroundData[]::new);
		level.sections.values().forEach(section -> {
			SectionPos sectionPos = section.getSectionPos();
			Mutable mutable = new Mutable();
			for (int y = 0; y < 16; y++) {
				mutable.setY(sectionPos.minBlockY() + y);
				if (mutable.getY() >= pos.getY() + undergroundOffset) {
					continue;
				}
				for (int x = 0; x < 16; x++) {
					mutable.setX(sectionPos.minBlockX() + x);
					for (int z = 0; z < 16; z++) {
						mutable.setZ(sectionPos.minBlockZ() + z);
						BlockState state = section.getBlockState(mutable);
						if (state == null || state == Blocks.AIR.defaultBlockState()) {
							continue;
						}
						groundData.compute(mutable, (k, v) -> {
							if (v == null) {
								return new GroundData(mutable.getY());
							}
							v.update(mutable.getY());
							return v;
						});
					}
				}
			}
		});
	}

	protected GeneratableDungeon(TemplateManager templateManager, CompoundNBT nbt) {
		super(GENERATABLE_DUNGEON, nbt);
		this.dungeonName = nbt.getString("dungeonName");
		this.pos = NBTUtil.readBlockPos(nbt.getCompound("pos"));
		this.level = new CQRLevel(nbt);
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

		this.level.generate(pLevel, pBox, new ServerEntityFactory(pLevel.getLevel()));
		return true;
	}

	static final double[] contribution;
	static {
		int r = 16;
		int r1 = r * 2 + 1;
		contribution = new double[r1 * r1 * r1];
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {
					int dx = Math.abs(x);
					if (dx > 0) dx--;
					int dy = Math.abs(y);
					if (dy > 0) dy--;
					int dz = Math.abs(z);
					if (dz > 0) dz--;

					double d = x * x + y * y + z * z;
					d = MathHelper.clamp(1.0D - Math.sqrt(d) * 0.0625D, 0.0D, 1.0D);
					d = d * d * (3.0D - 2.0D * d);

					contribution[((x + r) * r1 + (y + r)) * r1 + (z + r)] = d;
				}
			}
		}
	}

	@Override
	public double getContribution(int x, int y, int z) {
		double noise = 0.0D;
		int r = 16;
		for (int ix = -r; ix <= r; ix++) {
			if (!groundData.inBoundsX(x + ix))
				continue;
			for (int iz = -r; iz <= r; iz++) {
				if (!groundData.inBoundsZ(z + iz))
					continue;
				GroundData data = groundData.getUnchecked(x + ix, z + iz);
				if (data == null)
					continue;
				if (y > data.max)
					continue;
				int iy = y < data.min ? data.min - y : 0;
				if (iy > 16)
					continue;
				double d = contribution[((ix + 16) * 33 + (iy + 16)) * 33 + (iz + 16)];
				if (d > noise)
					noise = d;
			}
		}
		return noise;
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

	}

}
