//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;
//
//import net.minecraft.util.Direction;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.math.vector.Vector3i;
//import net.minecraft.world.World;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.util.DungeonGenUtils;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.CastleRoomBase;
//import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.IRoomDecor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//public abstract class RoomDecorEntityBase implements IRoomDecor {
//	protected List<Vector3i> footprint; // Array of blockstates and their offsets
//
//	protected RoomDecorEntityBase() {
//		this.footprint = new ArrayList<>();
//	}
//
//	@Override
//	public boolean wouldFit(BlockPos start, Direction side, Set<BlockPos> decoArea, Set<BlockPos> decoMap, CastleRoomBase room) {
//		List<Vector3i> rotated = this.alignFootprint(this.footprint, side);
//
//		for (Vector3i placement : rotated) {
//			BlockPos pos = start.add(placement);
//			if (!decoArea.contains(pos) || decoMap.contains(pos)) {
//				return false;
//			}
//		}
//
//		return true;
//	}
//
//	@Override
//	public void build(World world, BlockStateGenArray genArray, CastleRoomBase room, DungeonRandomizedCastle dungeon, BlockPos start, Direction side, Set<BlockPos> decoMap) {
//		List<Vector3i> rotated = this.alignFootprint(this.footprint, side);
//
//		for (Vector3i placement : rotated) {
//			BlockPos pos = start.add(placement);
//			decoMap.add(pos);
//		}
//		this.createEntityDecoration(world, start, genArray, side);
//	}
//
//	protected abstract void createEntityDecoration(World world, BlockPos pos, BlockStateGenArray genArray, Direction side);
//
//	protected List<Vector3i> alignFootprint(List<Vector3i> unrotated, Direction side) {
//		List<Vector3i> result = new ArrayList<>();
//
//		unrotated.forEach(v -> result.add(DungeonGenUtils.rotateVec3i(v, side)));
//
//		return result;
//	}
//}
