package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacingHelper;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class PosInfoForceFieldNexus extends AbstractBlockInfo {

	public static final int ID = 3;

	public PosInfoForceFieldNexus(BlockPos pos) {
		super(pos);
	}

	public PosInfoForceFieldNexus(BlockPos pos, NBTTagIntArray nbtTagIntArray) {
		super(pos);
		this.readFromNBT(nbtTagIntArray, null, null);
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, EDungeonMobType dungeonMob, ProtectedRegion protectedRegion) {
		BlockPos transformedPos = dungeonPartPos.add(Template.transformedBlockPos(settings, this.pos));

		if (protectedRegion != null) {
			if (BlockPlacingHelper.setBlockState2(world, transformedPos, ModBlocks.FORCE_FIELD_NEXUS.getDefaultState(), 18, false)) {
				protectedRegion.addBlockDependency(transformedPos);
			} else {
				CQRMain.logger.warn("Failed to place force field nexus at {}", transformedPos);
			}
		} else {
			BlockPlacingHelper.setBlockState2(world, transformedPos, Blocks.AIR.getDefaultState(), 18, false);
		}
	}

	@Override
	public int getId() {
		return ID;
	}

}
