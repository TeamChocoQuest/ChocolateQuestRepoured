package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonSurface;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlockSpecial;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartCover;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartEntity;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartPlateau;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class GeneratorSurface extends AbstractDungeonGenerator<DungeonSurface> {

	private CQStructure structure;
	private PlacementSettings settings;
	private BlockPos structurePos;

	public GeneratorSurface(World world, BlockPos pos, DungeonSurface dungeon) {
		super(world, pos, dungeon);
	}

	@Override
	protected void preProcess() {
		File file = this.dungeon.getStructureFileFromDirectory(this.dungeon.getStructureFolderPath());
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
		String mobType = this.dungeon.getDungeonMob();
		if (mobType == DungeonInhabitantManager.DEFAULT_INHABITANT_IDENT) {
			mobType = DungeonInhabitantManager.getInhabitantDependingOnDistance(this.world, this.pos.getX(), this.pos.getZ()).getName();
		}
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.structurePos, this.structure.getBlockInfoList(), this.settings, mobType));
		this.dungeonGenerator.add(new DungeonPartEntity(this.world, this.dungeonGenerator, this.structurePos, this.structure.getEntityInfoList(), this.settings, mobType));
		this.dungeonGenerator.add(new DungeonPartBlockSpecial(this.world, this.dungeonGenerator, this.structurePos, this.structure.getSpecialBlockInfoList(), this.settings, mobType));
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
