package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;

import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.util.GenerationTemplate;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;

public class CastleRoomBridgeTop extends CastleRoomBase {
	protected Alignment alignment;

	public enum Alignment {
		VERTICAL, HORIZONTAL;

		static Alignment fromFacing(Direction facing) {
			return facing.getAxis() == Direction.Axis.X ? HORIZONTAL : VERTICAL;
		}
	}

	public CastleRoomBridgeTop(int sideLength, int height, Direction direction, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.BRIDGE_TOP;
		this.defaultCeiling = false;
		this.defaultFloor = false;
		this.alignment = Alignment.fromFacing(direction);
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		final int startX = 1;
		final int startZ = 1;

		// Don't use decoration length since we don't care if there are walls
		final int endX = this.getRoomLengthX() - 3;
		final int endZ = this.getRoomLengthZ() - 3;

		Predicate<Vec3i> bottom;
		Predicate<Vec3i> edges;

		GenerationTemplate bridgeTopTemplate = new GenerationTemplate(this.getDecorationLengthX(), this.getDecorationLengthY(), this.getDecorationLengthZ());
		if (this.alignment == Alignment.HORIZONTAL) {
			bottom = (v -> (v.getY() == 0) && (v.getZ() >= startZ) && (v.getZ() <= endZ));
			edges = (v -> v.getY() == 1 && ((v.getZ() == startZ) || (v.getZ() == endZ)));
		} else {
			bottom = (v -> (v.getY() == 0) && (v.getX() >= startX) && (v.getX() <= endX));
			edges = (v -> v.getY() == 1 && ((v.getX() == startZ) || (v.getX() == endX)));
		}

		bridgeTopTemplate.addRule(bottom, dungeon.getMainBlockState());
		bridgeTopTemplate.addRule(edges, dungeon.getFancyBlockState());

		bridgeTopTemplate.addToGenArray(this.getNonWallStartPos(), genArray, BlockStateGenArray.GenerationPhase.MAIN);
	}

	@Override
	public boolean canBuildDoorOnSide(Direction side) {
		// Really only works on this side, could add logic to align the doors for other sides later
		return (Alignment.fromFacing(side) == this.alignment);
	}

	@Override
	public boolean reachableFromSide(Direction side) {
		return (Alignment.fromFacing(side) == this.alignment);
	}
}
