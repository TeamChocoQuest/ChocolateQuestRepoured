package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.CQRWeightedRandom;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class CastleRoomLibrary extends CastleRoomDecoratedBase {
	private enum ShelfPattern {
		LONG_VERTICAL, LONG_HORIZONTAL
	}

	private ShelfPattern pattern;
	private BlockPos shelfStart = null;
	private int shelfXLen = 0;
	private int shelfZLen = 0;
	private int shelfHeight = 0;

	public CastleRoomLibrary(int sideLength, int height, int floor) {
		super(sideLength, height, floor);
		this.roomType = EnumRoomType.LIBRARY;
		this.maxSlotsUsed = 2;
		this.defaultCeiling = true;
		this.defaultFloor = true;

		CQRWeightedRandom<ShelfPattern> randomPattern = new CQRWeightedRandom<>(random);
		randomPattern.add(ShelfPattern.LONG_VERTICAL, 1);
		randomPattern.add(ShelfPattern.LONG_HORIZONTAL, 1);

		this.pattern = randomPattern.next();
	}

	@Override
	protected void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
		// allow 1 space from the wall to start
		shelfStart = this.getDecorationStartPos().south().east();
		shelfXLen = this.getDecorationLengthX() - 2;
		shelfZLen = this.getDecorationLengthZ() - 2;
		shelfHeight = this.getDecorationLengthY() - 2; // leave some room to the ceiling

		switch (pattern) {
		case LONG_VERTICAL:
			generateVertical(genArray);
			break;
		case LONG_HORIZONTAL:
			generateHorizontal(genArray);
			break;
		default:
			break;
		}
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

	private void generateVertical(BlockStateGenArray genArray) {
		for (int x = 0; x < shelfXLen; x++) {
			for (int y = 0; y < shelfHeight; y++) {
				for (int z = 0; z < shelfZLen; z++) {
					if ((x % 2 == 0) && (z != (shelfZLen / 2))) {
						BlockPos pos = shelfStart.add(x, y, z);
						genArray.addBlockState(pos, Blocks.BOOKSHELF.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
						this.usedDecoPositions.add(pos);
					}
				}
			}
		}
	}

	private void generateHorizontal(BlockStateGenArray genArray) {
		for (int x = 0; x < shelfXLen; x++) {
			for (int y = 0; y < shelfHeight; y++) {
				for (int z = 0; z < shelfZLen; z++) {
					if ((z % 2 == 0) && (x != (shelfXLen / 2))) {
						BlockPos pos = shelfStart.add(x, y, z);
						genArray.addBlockState(pos, Blocks.BOOKSHELF.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
						this.usedDecoPositions.add(pos);
					}
				}
			}
		}
	}

	@Override
	protected void makeRoomBlockAdjustments() {
		if (this.isRootRoomInBlock) {
			for (CastleRoomBase blockRoom : this.roomsInBlock) {
				if (blockRoom instanceof CastleRoomLibrary) {
					((CastleRoomLibrary) blockRoom).setPattern(this.pattern);
				}
			}
		}
	}

	public void setPattern(ShelfPattern pattern) {
		this.pattern = pattern;
	}
}
