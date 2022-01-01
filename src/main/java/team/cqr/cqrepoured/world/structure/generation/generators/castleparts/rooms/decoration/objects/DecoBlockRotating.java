package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.BlockState;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3i;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class DecoBlockRotating extends DecoBlockBase {
	protected PropertyDirection directionProperty;
	protected final Direction DEFAULT_SIDE = Direction.NORTH;
	protected Direction initialFacing;

	protected DecoBlockRotating(int x, int y, int z, BlockState initialState, PropertyDirection directionProperty, Direction initialFacing, BlockStateGenArray.GenerationPhase generationPhase) {
		super(x, y, z, initialState, generationPhase);
		this.directionProperty = directionProperty;
		this.initialFacing = initialFacing;
	}

	protected DecoBlockRotating(Vec3i offset, BlockState initialState, PropertyDirection directionProperty, Direction initialFacing, BlockStateGenArray.GenerationPhase generationPhase) {
		super(offset, initialState, generationPhase);
		this.directionProperty = directionProperty;
		this.initialFacing = initialFacing;
	}

	@Override
	protected BlockState getState(Direction side) {
		int rotations = DungeonGenUtils.getCWRotationsBetween(this.DEFAULT_SIDE, side);
		return this.blockState.withProperty(this.directionProperty, DungeonGenUtils.rotateFacingNTimesAboutY(this.initialFacing, rotations));
	}
}
