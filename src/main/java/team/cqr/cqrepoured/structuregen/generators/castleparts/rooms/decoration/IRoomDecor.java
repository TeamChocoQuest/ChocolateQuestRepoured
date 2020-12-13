package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.HashSet;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.CastleRoomBase;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public interface IRoomDecor {
	boolean wouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap, CastleRoomBase room);

	void build(World world, BlockStateGenArray genArray, CastleRoomBase room, DungeonRandomizedCastle dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap);
}
