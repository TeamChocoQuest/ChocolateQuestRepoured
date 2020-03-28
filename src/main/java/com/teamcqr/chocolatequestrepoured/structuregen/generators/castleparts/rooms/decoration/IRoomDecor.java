package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.HashSet;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomBase;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRoomDecor {
	boolean wouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap, CastleRoomBase room);

	void build(World world, BlockStateGenArray genArray, CastleRoomBase room, CastleDungeon dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap);
}
