//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;
//
//import net.minecraft.block.Blocks;
//import net.minecraft.util.math.BlockPos;
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
//public abstract class RoomDecorBlocksBase implements IRoomDecor {
//
//	protected List<DecoBlockBase> schematic; // Array of blockstates and their offsets
//
//	protected RoomDecorBlocksBase() {
//		this.schematic = new ArrayList<>();
//		this.makeSchematic();
//	}
//
//	protected abstract void makeSchematic();
//
//	@Override
//	public boolean wouldFit(BlockPos start, EnumFacing side, Set<BlockPos> decoArea, Set<BlockPos> decoMap, CastleRoomBase room) {
//		List<DecoBlockBase> rotated = this.alignSchematic(side);
//
//		for (DecoBlockBase placement : rotated) {
//			BlockPos pos = start.add(placement.offset);
//			if (!decoArea.contains(pos) || decoMap.contains(pos)) {
//				return false;
//			}
//		}
//
//		return true;
//	}
//
//	@Override
//	public void build(World world, BlockStateGenArray genArray, CastleRoomBase room, DungeonRandomizedCastle dungeon, BlockPos start, EnumFacing side, Set<BlockPos> decoMap) {
//		List<DecoBlockBase> rotated = this.alignSchematic(side);
//
//		for (DecoBlockBase placement : rotated) {
//			BlockPos pos = start.add(placement.offset);
//			genArray.addBlockState(pos, placement.getState(side), placement.getGenPhase(), BlockStateGenArray.EnumPriority.MEDIUM);
//
//			if (placement.getState(side).getBlock() != Blocks.AIR) {
//				decoMap.add(pos);
//			}
//		}
//	}
//
//	protected List<DecoBlockBase> alignSchematic(EnumFacing side) {
//		List<DecoBlockBase> result = new ArrayList<>();
//
//		for (DecoBlockBase p : this.schematic) {
//			result.add(new DecoBlockBase(DungeonGenUtils.rotateVec3i(p.offset, side), p.getState(side), p.getGenPhase()));
//		}
//
//		return result;
//	}
//}
