package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

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

public class RoomDecorBed extends RoomDecorBlocksBase {
	public RoomDecorBed() {
		super();
	}

	@Override
	protected void makeSchematic() {
		IBlockState head = Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
		this.schematic.add(new DecoBlockRotating(0, 0, 0, head, BlockBed.FACING, EnumFacing.NORTH));
		IBlockState foot = Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
		this.schematic.add(new DecoBlockRotating(0, 0, 1, foot, BlockBed.FACING, EnumFacing.NORTH));
	}
}
