package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.IRoomDecor;
import team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class CastleRoomHallway extends CastleRoomGenericBase {
	public enum Alignment {
		VERTICAL, HORIZONTAL;

		private boolean canHaveInteriorWall(EnumFacing side) {
			if (this == VERTICAL) {
				return (side == EnumFacing.WEST || side == EnumFacing.EAST);
			} else {
				return (side == EnumFacing.NORTH || side == EnumFacing.SOUTH);
			}
		}
	}

	private Alignment alignment;
	EnumFacing patternStartFacing;

	public CastleRoomHallway(int sideLength, int height, Alignment alignment, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.HALLWAY;
		this.alignment = alignment;
		this.defaultFloor = true;
		this.defaultCeiling = true;
		this.patternStartFacing = EnumFacing.HORIZONTALS[this.random.nextInt(EnumFacing.HORIZONTALS.length)];
	}

	@Override
	protected void generateDefaultFloor(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		for (int z = 0; z < this.getDecorationLengthZ(); z++) {
			for (int x = 0; x < this.getDecorationLengthX(); x++) {
				BlockPos pos = this.getNonWallStartPos().add(x, 0, z);
				IBlockState tcBlock = Blocks.GRAY_GLAZED_TERRACOTTA.getDefaultState();
				EnumFacing tcFacing;

				// Terracotta patterns are formed in a 2x2 square from the pattern (going clockwise) N E S W
				// So create that pattern here given some starting facing
				if (pos.getZ() % 2 == 0) {
					if (pos.getX() % 2 == 0) {
						tcFacing = this.patternStartFacing;
					} else {
						tcFacing = this.patternStartFacing.rotateY();
					}
				} else {
					if (pos.getX() % 2 == 0) {
						tcFacing = this.patternStartFacing.rotateYCCW();
					} else {
						tcFacing = this.patternStartFacing.rotateY().rotateY();
					}
				}
				tcBlock = tcBlock.withProperty(BlockHorizontal.FACING, tcFacing);
				genArray.addBlockState(pos, tcBlock, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
			}
		}
	}

	@Override
	protected void addMidDecoration(World world, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		IRoomDecor pillar = RoomDecorTypes.PILLAR;
		int halfX = this.getDecorationLengthX() / 2;
		int halfZ = this.getDecorationLengthZ() / 2;

		// Offset by 1 since the pillar is 3x3
		--halfX;
		--halfZ;

		BlockPos pillarStart = this.roomOrigin.add(halfX, 1, halfZ);
		pillar.build(world, genArray, this, dungeon, pillarStart, EnumFacing.NORTH, this.usedDecoPositions);
	}

	@Override
	protected IBlockState getFloorBlock(DungeonRandomizedCastle dungeon) {
		return Blocks.GRAY_GLAZED_TERRACOTTA.getDefaultState();
	}

	@Override
	public void copyPropertiesOf(CastleRoomBase room) {
		if (room instanceof CastleRoomHallway) {
			this.patternStartFacing = ((CastleRoomHallway) room).patternStartFacing;
		}
	}
}
