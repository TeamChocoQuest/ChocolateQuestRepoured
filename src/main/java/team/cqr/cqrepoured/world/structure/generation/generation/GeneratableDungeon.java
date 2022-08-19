package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.Random;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

public class GeneratableDungeon extends StructurePiece {

	private static final IStructurePieceType GENERATABLE_DUNGEON = IStructurePieceType.setPieceId(GeneratableDungeon::new, "generatable_dungeon");

	private final String dungeonName;
	private final BlockPos pos;
	private final CQRLevel level;
	private final ProtectedRegion.Builder protectedRegionBuilder;

	protected GeneratableDungeon(String dungeonName, BlockPos pos, CQRLevel level, ProtectedRegion.Builder protectedRegionBuilder) {
		super(GENERATABLE_DUNGEON, 0);
		this.dungeonName = dungeonName;
		this.pos = pos;
		this.level = level;
		this.protectedRegionBuilder = protectedRegionBuilder;
		// TODO calculate correct bounding box
		this.boundingBox = new MutableBoundingBox(pos.offset(-64, -64, -64), pos.offset(64, 64, 64));
	}

	protected GeneratableDungeon(TemplateManager templateManager, CompoundNBT nbt) {
		super(GENERATABLE_DUNGEON, nbt);
		this.dungeonName = nbt.getString("dungeonName");
		this.pos = NBTUtil.readBlockPos(nbt.getCompound("pos"));
		this.level = new CQRLevel(nbt);
		this.protectedRegionBuilder = new ProtectedRegion.Builder(nbt.getCompound("protectedRegionBuilder"));
		this.boundingBox = new MutableBoundingBox(nbt.getInt("x0"), nbt.getInt("y0"), nbt.getInt("y0"), nbt.getInt("x1"), nbt.getInt("y1"), nbt.getInt("y1"));
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

	public static class Builder {

		private final String dungeonName;
		private final BlockPos pos;
		private final DungeonInhabitant defaultInhabitant;
		private final ServerEntityFactory entityFactory;
		private final CQRLevel level;
		private final ProtectedRegion.Builder protectedRegionBuilder;

		public Builder(ServerWorld level, BlockPos pos, DungeonBase config) {
			this(level, pos, config.getDungeonName(), config.getDungeonMob());
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
			return new GeneratableDungeon(this.dungeonName, this.pos, this.level, this.protectedRegionBuilder);
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

		public CQRLevel getLevel() {
			return level;
		}

		public ProtectedRegion.Builder getProtectedRegionBuilder() {
			return protectedRegionBuilder;
		}

	}

}
