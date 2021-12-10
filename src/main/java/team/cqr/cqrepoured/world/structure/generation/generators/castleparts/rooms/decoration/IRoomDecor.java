package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration;

import java.util.Set;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.CastleRoomBase;

public interface IRoomDecor {
	boolean wouldFit(BlockPos start, EnumFacing side, Set<BlockPos> decoArea, Set<BlockPos> decoMap, CastleRoomBase room);

	void build(World world, BlockStateGenArray genArray, CastleRoomBase room, DungeonRandomizedCastle dungeon, BlockPos start, EnumFacing side,
			Set<BlockPos> decoMap);
}
