package team.cqr.cqrepoured.structuregen.generators;

import java.io.File;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.structuregen.DungeonDataManager;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonTemplateSurface;
import team.cqr.cqrepoured.structuregen.generation.DungeonPartCover;
import team.cqr.cqrepoured.structuregen.generation.DungeonPartPlateau;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.structuregen.structurefile.CQStructure;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class GeneratorTemplateSurface extends AbstractDungeonGenerator<DungeonTemplateSurface> {

	private CQStructure structure;
	private PlacementSettings settings;
	private BlockPos structurePos;

	public GeneratorTemplateSurface(World world, BlockPos pos, DungeonTemplateSurface dungeon, Random rand, DungeonDataManager.DungeonSpawnType spawnType) {
		super(world, pos, dungeon, rand, spawnType);
	}

	@Override
	protected void preProcess() {
		File file = this.dungeon.getStructureFileFromDirectory(this.dungeon.getStructureFolderPath(), this.random);
		if (file == null) {
			throw new NullPointerException("No structure file found in folder " + this.dungeon.getStructureFolderPath());
		}
		this.structure = this.loadStructureFromFile(file);
		this.settings = new PlacementSettings();

		if (this.dungeon.rotateDungeon()) {
			this.settings.setRotation(Rotation.values()[this.random.nextInt(Rotation.values().length)]);
			this.settings.setMirror(Mirror.values()[this.random.nextInt(Mirror.values().length)]);
		}

		// Why do you use a centralized Position? And what does "centralized" actually mean here (should be explained in corresponding method)?
		this.structurePos = DungeonGenUtils.getCentralizedPosForStructure(this.pos, this.structure, this.settings);

		if (this.dungeon.doBuildSupportPlatform()) {
			BlockPos startPos = this.structurePos.up(this.dungeon.getUnderGroundOffset()).down();
			BlockPos endPos = startPos.add(Template.transformedBlockPos(this.settings, new BlockPos(this.structure.getSize().getX() - 1, 0, this.structure.getSize().getZ() - 1)));
			BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
			BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
			this.dungeonGenerator.add(new DungeonPartPlateau(this.world, this.dungeonGenerator, pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ(), this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock(), CQRConfig.general.supportHillWallSize));
		}
	}

	@Override
	protected void buildStructure() {
		DungeonInhabitant mobType = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(this.dungeon.getDungeonMob(), this.world, this.pos.getX(), this.pos.getZ());
		this.structure.addAll(this.world, this.dungeonGenerator, this.structurePos, this.settings, mobType);
	}

	@Override
	protected void postProcess() {
		if (this.dungeon.isCoverBlockEnabled()) {
			BlockPos startPos = this.structurePos;
			BlockPos endPos = startPos.add(Template.transformedBlockPos(this.settings, new BlockPos(this.structure.getSize().getX() - 1, 0, this.structure.getSize().getZ() - 1)));
			BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
			BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
			this.dungeonGenerator.add(new DungeonPartCover(this.world, this.dungeonGenerator, pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getZ(), this.dungeon.getCoverBlock()));
		}
	}

}
