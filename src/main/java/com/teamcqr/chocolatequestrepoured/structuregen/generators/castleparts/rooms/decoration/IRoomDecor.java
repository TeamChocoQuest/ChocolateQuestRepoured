package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.HashSet;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoom;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRoomDecor {
	boolean wouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap);

	void build(World world, CastleRoom room, CastleDungeon dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap);
}
