package team.cqr.cqrepoured.world.structure.generation.generators.stronghold.open;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Tuple;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonStrongholdOpen;
import team.cqr.cqrepoured.world.structure.generation.generation.CQRStructurePiece;
import team.cqr.cqrepoured.world.structure.generation.generators.AbstractDungeonGenerationComponent;
import team.cqr.cqrepoured.world.structure.generation.generators.stronghold.GeneratorStrongholdOpen;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

public class StrongholdFloorOpen extends AbstractDungeonGenerationComponent<DungeonStrongholdOpen, GeneratorStrongholdOpen> {

	private final Random random;
	private BlockPos[][] roomGrid;
	private Tuple<Integer, Integer> entranceStairBlockPosition;
	private Tuple<Integer, Integer> entranceStairIndex;
	private Tuple<Integer, Integer> exitStairBlockPosition;
	private Tuple<Integer, Integer> exitStairIndex;
	private Tuple<BlockPos, BlockPos> exitStairCorners;
	private Tuple<BlockPos, BlockPos> entranceStairCorners;
	private int sideLength = 1;
	private int yPos;
	private File entranceStair = null;
	private boolean exitStairIsBossRoom = false;
	private boolean isFirstFloor = false;

	public StrongholdFloorOpen(GeneratorStrongholdOpen generator, int roomCount, Random rand) {
		this(generator, roomCount, rand.nextInt(roomCount), rand.nextInt(roomCount), rand);
	}

	public StrongholdFloorOpen(GeneratorStrongholdOpen generator, int roomCount, int entranceStairIndexX, int entranceStairIndexZ, Random rand) {
		super(generator);
		this.sideLength = roomCount;
		this.roomGrid = new BlockPos[roomCount][roomCount];
		this.random = rand;

		int iX2;
		int iZ2;
		this.entranceStairIndex = new Tuple<>(entranceStairIndexX, entranceStairIndexZ);
		iX2 = this.random.nextInt(roomCount);
		iZ2 = this.random.nextInt(roomCount);
		while (iX2 == entranceStairIndexX) {
			iX2 = this.random.nextInt(roomCount);
		}
		while (iZ2 == entranceStairIndexZ) {
			iZ2 = this.random.nextInt(roomCount);
		}
		this.exitStairIndex = new Tuple<>(iX2, iZ2);
	}

	public Tuple<Integer, Integer> getExitStairIndexes() {
		return this.exitStairIndex;
	}

	public void setEntranceStairPosition(@Nonnull File entranceStair, int x, int y, int z) {
		this.entranceStair = entranceStair;
		this.yPos = y;
		this.entranceStairBlockPosition = new Tuple<>(x, z);
		this.roomGrid[this.entranceStairIndex.getA()][this.entranceStairIndex.getB()] = new BlockPos(x, y, z);
	}

	public void setIsFirstFloor(boolean val) {
		this.isFirstFloor = val;
	}

	public void setExitIsBossRoom(boolean newVal) {
		this.exitStairIsBossRoom = newVal;
		if (newVal) {
			this.exitStairCorners = null;
		}
	}

	public Tuple<Integer, Integer> getExitCoordinates() {
		return this.exitStairBlockPosition;
	}

	@Override
	public void preProcess(Level world, CQRStructurePiece.Builder dungeonBuilder, DungeonInhabitant mobType) {
		Vector3i v = new Vector3i(this.generator.getDungeon().getRoomSizeX() / 2, 0, this.generator.getDungeon().getRoomSizeZ() / 2);
		for (int iX = 0; iX < this.sideLength; iX++) {
			for (int iZ = 0; iZ < this.sideLength; iZ++) {
				if (((iX != this.entranceStairIndex.getA()) || (iZ != this.entranceStairIndex.getB()))) {
					int multiplierX = iX - this.entranceStairIndex.getA();
					int multiplierZ = iZ - this.entranceStairIndex.getB();

					BlockPos pos = new BlockPos(this.entranceStairBlockPosition.getA() + (multiplierX * this.generator.getDungeon().getRoomSizeX()), this.yPos, this.entranceStairBlockPosition.getB() + (multiplierZ * this.generator.getDungeon().getRoomSizeZ()));

					this.roomGrid[iX][iZ] = pos;
					if (iX == this.exitStairIndex.getA() && iZ == this.exitStairIndex.getB()) {
						BlockPos p1 = pos.subtract(v);
						BlockPos p2 = pos.offset(v);
						this.exitStairCorners = new Tuple<>(p1, p2);
						this.exitStairBlockPosition = new Tuple<>(pos.getX(), pos.getZ());
					}
				} else {
					BlockPos p = new BlockPos(this.entranceStairBlockPosition.getA(), this.yPos, this.entranceStairBlockPosition.getB());
					this.entranceStairCorners = new Tuple<>(p.subtract(v), p.offset(v));
				}
			}
		}
		BlockPos exitPos = this.roomGrid[this.exitStairIndex.getA()][this.exitStairIndex.getB()];
		this.entranceStairBlockPosition = new Tuple<>(exitPos.getX(), exitPos.getZ());
	}

	@Override
	public void generate(Level world, CQRStructurePiece.Builder dungeonBuilder, DungeonInhabitant mobType) {
		for (int x = 0; x < this.sideLength; x++) {
			for (int z = 0; z < this.sideLength; z++) {
				BlockPos pos = this.roomGrid[x][z];
				File file = null;
				if (((x != this.exitStairIndex.getA()) || (z != this.exitStairIndex.getB()))) {
					if (x == this.entranceStairIndex.getA() && z == this.entranceStairIndex.getB()) {
						if (this.entranceStair != null) {
							file = this.entranceStair;
						} else if (this.isFirstFloor) {
							file = this.generator.getDungeon().getEntranceStair(this.random);
						} else {
							file = this.generator.getDungeon().getStairRoom(this.random);
						}
					} else {
						file = this.generator.getDungeon().getRoom(this.random);
					}
				} else if (this.exitStairIsBossRoom) {
					file = this.generator.getDungeon().getBossRoom(this.random);
					this.exitStairIsBossRoom = false;
				}

				if (pos != null && file != null) {
					CQStructure structure = CQStructure.createFromFile(file);
					structure.addAll(dungeonBuilder, pos, Offset.CENTER);
				}
			}
		}
	}

	@Override
	public void generatePost(Level world, CQRStructurePiece.Builder dungeonBuilder, DungeonInhabitant mobType) {
		if (this.generator.getDungeon().getWallBlock() == null) {
			return;
		}
		Map<BlockPos, BlockState> stateMap = new HashMap<>();
		int dimX = this.generator.getDungeon().getRoomSizeX() / 2;
		int dimZ = this.generator.getDungeon().getRoomSizeZ() / 2;
		BlockPos p1 = this.roomGrid[this.sideLength - 1][this.sideLength - 1].offset(1, 0, 1).offset(dimX, -1, dimZ);
		BlockPos p2 = this.roomGrid[this.sideLength - 1][0].offset(1, 0, -1).offset(dimX, -1, -dimZ);
		BlockPos p3 = this.roomGrid[0][this.sideLength - 1].offset(-1, 0, 1).offset(-dimX, -1, dimZ);
		BlockPos p4 = this.roomGrid[0][0].offset(-1, 0, -1).offset(-dimX, -1, -dimZ);

		BlockState state = this.generator.getDungeon().getWallBlock();
		int addY = 2 + this.generator.getDungeon().getRoomSizeY();

		// 1-2
		for (BlockPos p12 : BlockPos.betweenClosed(p1, p2.offset(0, addY, 0))) {
			stateMap.put(p12, state);
		}
		// 1-3
		for (BlockPos p13 : BlockPos.betweenClosed(p1, p3.offset(0, addY, 0))) {
			stateMap.put(p13, state);
		}
		// 4-2
		for (BlockPos p42 : BlockPos.betweenClosed(p4, p2.offset(0, addY, 0))) {
			stateMap.put(p42, state);
		}
		// 4-3
		for (BlockPos p43 : BlockPos.betweenClosed(p4, p3.offset(0, addY, 0))) {
			stateMap.put(p43, state);
		}
		// Top
		for (BlockPos pT : BlockPos.betweenClosed(p1.offset(0, 2 + this.generator.getDungeon().getRoomSizeY(), 0), p4.offset(0, addY, 0))) {
			if (((((pT.getX() < this.entranceStairCorners.getA().getX()) || (pT.getX() > this.entranceStairCorners.getB().getX())) || (pT.getZ() < this.entranceStairCorners.getA().getZ())) || (pT.getZ() > this.entranceStairCorners.getB().getZ()))) {
				stateMap.put(pT, state);
			}
		}
		// Bottom
		for (BlockPos pB : BlockPos.betweenClosed(p1, p4)) {
			if (this.exitStairIsBossRoom
					|| (pB != null && this.exitStairCorners != null && ((((pB.getX() < this.exitStairCorners.getA().getX()) || (pB.getX() > this.exitStairCorners.getB().getX())) || (pB.getZ() < this.exitStairCorners.getA().getZ())) || (pB.getZ() > this.exitStairCorners.getB().getZ())))) {
				stateMap.put(pB, state);
			}
		}

		for (Map.Entry<BlockPos, BlockState> entry : stateMap.entrySet()) {
			dungeonBuilder.getLevel().setBlockState(entry.getKey(), entry.getValue());
		}
	}

}
