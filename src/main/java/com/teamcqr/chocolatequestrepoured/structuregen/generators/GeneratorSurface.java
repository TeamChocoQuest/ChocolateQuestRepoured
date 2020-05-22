package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlockSpecial;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartCover;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartEntity;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartPlateau;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class GeneratorSurface extends AbstractDungeonGenerator {

	private CQStructure structure;
	private PlacementSettings settings;

	public GeneratorSurface(World world, BlockPos pos, DungeonBase dungeon, CQStructure structure, PlacementSettings settings) {
		super(world, getCentralizedPosForStructure(pos, structure, settings), dungeon);
		this.structure = structure;
		this.settings = settings;
	}

	private static BlockPos getCentralizedPosForStructure(BlockPos pos, CQStructure structure, PlacementSettings settings) {
		BlockPos transformedSize = Template.transformedBlockPos(settings, structure.getSize());
		return pos.add(-(transformedSize.getX() >> 1), 0, -(transformedSize.getZ() >> 1));
	}

	@Override
	protected void preProcess() {
		if (this.dungeon.doBuildSupportPlatform()) {
			BlockPos startPos = this.pos.up(this.dungeon.getUnderGroundOffset()).down();
			BlockPos endPos = startPos.add(Template.transformedBlockPos(this.settings, new BlockPos(this.structure.getSize().getX() - 1, 0, this.structure.getSize().getZ() - 1)));
			BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
			BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
			this.dungeonGenerator.add(new DungeonPartPlateau(this.world, this.dungeonGenerator, pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ(), Blocks.STONE, Blocks.GRASS, 8));
		}
	}

	@Override
	protected void buildStructure() {
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.pos, this.structure.getBlockInfoList(), this.settings, this.dungeon.getDungeonMob()));
		this.dungeonGenerator.add(new DungeonPartEntity(this.world, this.dungeonGenerator, this.pos, this.structure.getEntityInfoList(), this.settings, this.dungeon.getDungeonMob()));
		this.dungeonGenerator.add(new DungeonPartBlockSpecial(this.world, this.dungeonGenerator, this.pos, this.structure.getSpecialBlockInfoList(), this.settings, this.dungeon.getDungeonMob()));
	}

	@Override
	protected void postProcess() {
		if (this.dungeon.isCoverBlockEnabled()) {
			BlockPos startPos = this.pos;
			BlockPos endPos = startPos.add(Template.transformedBlockPos(this.settings, this.structure.getSize()));
			BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
			BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
			this.dungeonGenerator.add(new DungeonPartCover(this.world, this.dungeonGenerator, pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getZ()));
		}
	}

}
