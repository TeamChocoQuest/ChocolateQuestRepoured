package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.CQRBlocks;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacingHelper;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class BlockInfoForceFieldNexus extends AbstractBlockInfo {

	public BlockInfoForceFieldNexus(BlockPos pos) {
		super(pos);
	}

	public BlockInfoForceFieldNexus(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoForceFieldNexus(BlockPos pos, NBTTagIntArray nbtTagIntArray) {
		super(pos);
		this.readFromNBT(nbtTagIntArray, null, null);
	}

	public BlockInfoForceFieldNexus(int x, int y, int z, NBTTagIntArray nbtTagIntArray) {
		super(x, y, z);
		this.readFromNBT(nbtTagIntArray, null, null);
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, String dungeonMob, ProtectedRegion protectedRegion) {
		BlockPos transformedPos = dungeonPartPos.add(Template.transformedBlockPos(settings, this.getPos()));

		if (protectedRegion != null) {
			if (BlockPlacingHelper.setBlockState(world, transformedPos, CQRBlocks.FORCE_FIELD_NEXUS.getDefaultState(), 18, CQRMain.isPhosphorInstalled || CQRConfig.advanced.instantLightUpdates)) {
				protectedRegion.addBlockDependency(transformedPos);
			} else {
				CQRMain.logger.warn("Failed to place force field nexus at {}", transformedPos);
			}
		} else {
			BlockPlacingHelper.setBlockState(world, transformedPos, Blocks.AIR.getDefaultState(), 18, CQRConfig.advanced.instantLightUpdates);
		}
	}

	@Override
	public int getId() {
		return NEXUS_INFO_ID;
	}

}
