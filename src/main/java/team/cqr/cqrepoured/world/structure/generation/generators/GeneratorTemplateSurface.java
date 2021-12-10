package team.cqr.cqrepoured.world.structure.generation.generators;

import java.io.File;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonTemplateSurface;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.part.CoverDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.generation.part.PlateauDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

public class GeneratorTemplateSurface extends AbstractDungeonGenerator<DungeonTemplateSurface> {

	private CQStructure structure;
	private Mirror mirror = Mirror.NONE;
	private Rotation rotation = Rotation.NONE;
	private BlockPos structurePos;

	public GeneratorTemplateSurface(World world, BlockPos pos, DungeonTemplateSurface dungeon, Random rand) {
		super(world, pos, dungeon, rand);
	}

	@Override
	protected void preProcess() {
		File file = this.dungeon.getStructureFileFromDirectory(this.dungeon.getStructureFolderPath(), this.random);
		if (file == null) {
			throw new NullPointerException("No structure file found in folder " + this.dungeon.getStructureFolderPath());
		}
		this.structure = this.loadStructureFromFile(file);

		if (this.dungeon.rotateDungeon()) {
			this.rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
			this.mirror = Mirror.values()[this.random.nextInt(Mirror.values().length)];
		}

		this.structurePos = Offset.CENTER.apply(this.pos, this.structure, this.mirror, this.rotation);

		if (this.dungeon.doBuildSupportPlatform()) {
			BlockPos startPos = this.structurePos.up(this.dungeon.getUnderGroundOffset()).down();
			BlockPos endPos = startPos
					.add(DungeonPlacement.transform(this.structure.getSize().getX() - 1, 0, this.structure.getSize().getZ() - 1, this.mirror, this.rotation));
			BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
			BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
			PlateauDungeonPart.Builder partBuilder = new PlateauDungeonPart.Builder(pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ(),
					CQRConfig.general.supportHillWallSize);
			partBuilder.setSupportHillBlock(this.dungeon.getSupportBlock());
			partBuilder.setSupportHillTopBlock(this.dungeon.getSupportTopBlock());
			this.dungeonBuilder.add(partBuilder);
		}
	}

	@Override
	protected void buildStructure() {
		this.structure.addAll(this.dungeonBuilder, this.pos, Offset.CENTER, this.mirror, this.rotation);
	}

	@Override
	protected void postProcess() {
		if (this.dungeon.isCoverBlockEnabled()) {
			BlockPos startPos = this.structurePos;
			BlockPos endPos = startPos
					.add(DungeonPlacement.transform(this.structure.getSize().getX() - 1, 0, this.structure.getSize().getZ() - 1, this.mirror, this.rotation));
			BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
			BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
			this.dungeonBuilder.add(new CoverDungeonPart.Builder(pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getZ(), this.dungeon.getCoverBlock()));
		}
	}

}
