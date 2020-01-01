package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.ArrayList;
import java.util.HashSet;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RoomDecorBed extends RoomDecorBlocks {
	public RoomDecorBed() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockOffset(0, 0, 0, Blocks.BED));
		this.schematic.add(new DecoBlockOffset(0, 0, 1, Blocks.BED));
	}

	@Override
	public void build(World world, CastleRoom room, CastleDungeon dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap) {
		ArrayList<DecoBlockOffset> rotated = this.alignSchematic(side);
		boolean head = true;

		for (DecoBlockOffset placement : rotated) {
			BlockPos pos = start.add(placement.offset);
			IBlockState blockState = this.getRotatedBlockState(placement.block, side);

			if (head) {
				blockState = blockState.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
				head = false;
			} else {
				blockState = blockState.withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
			}

			world.setBlockState(pos, blockState);
			decoMap.add(pos);
		}
	}

	@Override
	protected IBlockState getRotatedBlockState(Block block, EnumFacing side) {
		IBlockState result = block.getDefaultState();

		if (block == Blocks.BED) {
			result = result.withProperty(BlockBed.FACING, side);
		}

		return result;
	}
}
