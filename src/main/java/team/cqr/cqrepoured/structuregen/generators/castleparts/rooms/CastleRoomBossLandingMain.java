package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.factories.GearedMobFactory;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class CastleRoomBossLandingMain extends CastleRoomDecoratedBase {
	private static final int ROOMS_LONG = 2;
	private static final int ROOMS_SHORT = 1;
	private static final int LANDING_LENGTH_X = 3;
	private static final int LANDING_LENGTH_Z = 2;
	private static final int STAIR_OPENING_LENGTH_Z = 2;
	private static final int BOSS_ROOM_WIDTH = 17; // may want to get this from the boss room itself later

	private EnumFacing doorSide;
	private int numRotations;

	private int endX;
	private int lenX;
	private int endZ;
	private int lenZ;

	private int connectingWallLength;

	private int stairOpeningXStartIdx;
	private int stairOpeningXEndIdx;
	private int stairsDownZIdx;
	private int stairOpeningZStartIdx;
	private int stairOpeningZEndIdx;

	public CastleRoomBossLandingMain(int sideLength, int height, EnumFacing doorSide, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.LANDING_BOSS;
		this.doorSide = doorSide;
		this.numRotations = DungeonGenUtils.getCWRotationsBetween(EnumFacing.NORTH, this.doorSide);
		this.defaultCeiling = true;

		this.endX = ROOMS_LONG * sideLength; // 1 wall space in between them
		this.lenX = this.endX + 1;
		this.endZ = ROOMS_SHORT * sideLength - 1;
		this.lenZ = this.endZ + 1;

		this.stairOpeningXStartIdx = sideLength - 2;
		this.stairOpeningXEndIdx = this.stairOpeningXStartIdx + LANDING_LENGTH_X - 1;

		this.stairsDownZIdx = LANDING_LENGTH_Z;
		this.stairOpeningZStartIdx = LANDING_LENGTH_Z + 1;
		this.stairOpeningZEndIdx = this.stairOpeningZStartIdx + STAIR_OPENING_LENGTH_Z;

		final int gapToBossRoom = 2 + BOSS_ROOM_WIDTH - this.lenX;
		this.connectingWallLength = 0;
		if (gapToBossRoom > 0) {
			// Determine size of walls that come in from each side so there is no space between this and boss room
			this.connectingWallLength = (int) Math.ceil((double) (gapToBossRoom) / 2);
		}
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		Vec3i offset;

		for (int x = 0; x <= this.endX; x++) {
			for (int y = 0; y < this.height; y++) {
				for (int z = 0; z <= this.endZ; z++) {
					IBlockState blockToBuild = this.getBlockToBuild(dungeon, x, y, z);

					offset = DungeonGenUtils.rotateMatrixOffsetCW(new Vec3i(x, y, z), this.lenX, this.lenZ, this.numRotations);
					genArray.addBlockState(this.roomOrigin.add(offset), blockToBuild, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);

					if (blockToBuild.getBlock() != Blocks.AIR) {
						this.usedDecoPositions.add(this.roomOrigin.add(offset));
					}
				}
			}
		}
	}

	private IBlockState getBlockToBuild(DungeonRandomizedCastle dungeon, int x, int y, int z) {
		IBlockState blockToBuild = Blocks.AIR.getDefaultState();

		if (z == 0) {
			if (x < this.connectingWallLength || x > this.endX - this.connectingWallLength || y == this.height - 1) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (y == 0) {
				return Blocks.QUARTZ_BLOCK.getDefaultState();
			}
		} else if (y == 0) {
			if ((x >= this.stairOpeningXStartIdx) && (x <= this.stairOpeningXEndIdx)) {
				if (z < this.stairsDownZIdx) {
					blockToBuild = Blocks.QUARTZ_BLOCK.getDefaultState();
				} else if (z == this.stairsDownZIdx) {
					EnumFacing stairFacing = DungeonGenUtils.rotateFacingNTimesAboutY(EnumFacing.NORTH, this.numRotations);
					blockToBuild = dungeon.getStairBlockState().withProperty(BlockStairs.FACING, stairFacing);
				} else {
					return Blocks.AIR.getDefaultState();
				}
			} else {
				blockToBuild = Blocks.QUARTZ_BLOCK.getDefaultState();
			}
		} else if (y == this.height - 1) {
			blockToBuild = dungeon.getMainBlockState();
		}

		return blockToBuild;
	}

	@Override
	public void decorate(World world, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon, GearedMobFactory mobFactory) {
		this.addEdgeDecoration(world, genArray, dungeon);
		this.addWallDecoration(world, genArray, dungeon);
		this.addSpawners(world, genArray, dungeon, mobFactory);
	}

	@Override
	boolean shouldBuildEdgeDecoration() {
		return false;
	}

	@Override
	boolean shouldBuildWallDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildMidDecoration() {
		return false;
	}

	@Override
	boolean shouldAddSpawners() {
		return true;
	}

	@Override
	boolean shouldAddChests() {
		return false;
	}

	@Override
	public boolean canBuildDoorOnSide(EnumFacing side) {
		return true;
	}

	@Override
	public boolean reachableFromSide(EnumFacing side) {
		return true;
	}
}
