package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.open;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlockSpecial;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartEntity;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.GeneratorStrongholdOpen;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class StrongholdFloorOpen {

	private GeneratorStrongholdOpen generator;
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

	public StrongholdFloorOpen(GeneratorStrongholdOpen generator, int roomCount) {
		this(generator, roomCount, new Random().nextInt(roomCount), new Random().nextInt(roomCount));
	}

	public StrongholdFloorOpen(GeneratorStrongholdOpen generator, int roomCount, int entranceStairIndexX, int entranceStairIndexZ) {
		this.generator = generator;
		this.sideLength = roomCount;
		this.roomGrid = new BlockPos[roomCount][roomCount];

		int iX2, iZ2;
		this.entranceStairIndex = new Tuple<>(entranceStairIndexX, entranceStairIndexZ);
		Random rdm = new Random();
		iX2 = rdm.nextInt(roomCount);
		iZ2 = rdm.nextInt(roomCount);
		while (iX2 == entranceStairIndexX) {
			iX2 = rdm.nextInt(roomCount);
		}
		while (iZ2 == entranceStairIndexZ) {
			iZ2 = rdm.nextInt(roomCount);
		}
		this.exitStairIndex = new Tuple<>(iX2, iZ2);
	}

	public Tuple<Integer, Integer> getExitStairIndexes() {
		return exitStairIndex;
	}

	public void setEntranceStairPosition(@Nonnull File entranceStair, int x, int y, int z) {
		this.entranceStair = entranceStair;
		this.yPos = y;
		this.entranceStairBlockPosition = new Tuple<Integer, Integer>(x, z);
		this.roomGrid[this.entranceStairIndex.getFirst()][this.entranceStairIndex.getSecond()] = new BlockPos(x, y, z);
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

	public void calculatePositions() {
		Vec3i v = new Vec3i(this.generator.getDungeon().getRoomSizeX() / 2, 0, this.generator.getDungeon().getRoomSizeZ() / 2);
		for (int iX = 0; iX < this.sideLength; iX++) {
			for (int iZ = 0; iZ < this.sideLength; iZ++) {
				if (!(iX == this.entranceStairIndex.getFirst() && iZ == this.entranceStairIndex.getSecond())) {
					int multiplierX = iX - this.entranceStairIndex.getFirst();
					int multiplierZ = iZ - this.entranceStairIndex.getSecond();

					BlockPos pos = new BlockPos(this.entranceStairBlockPosition.getFirst() + (multiplierX * this.generator.getDungeon().getRoomSizeX()), this.yPos, this.entranceStairBlockPosition.getSecond() + (multiplierZ * this.generator
							.getDungeon().getRoomSizeZ()));

					this.roomGrid[iX][iZ] = pos;
					if (iX == this.exitStairIndex.getFirst() && iZ == this.exitStairIndex.getSecond()) {
						BlockPos p1 = pos.subtract(v);
						BlockPos p2 = pos.add(v);
						this.exitStairCorners = new Tuple<>(p1, p2);
						this.exitStairBlockPosition = new Tuple<>(pos.getX(), pos.getZ());
					}
				} else {
					BlockPos p = new BlockPos(this.entranceStairBlockPosition.getFirst(), this.yPos, this.entranceStairBlockPosition.getSecond());
					this.entranceStairCorners = new Tuple<>(p.subtract(v), p.add(v));
				}
			}
		}
		BlockPos exitPos = this.roomGrid[this.exitStairIndex.getFirst()][this.exitStairIndex.getSecond()];
		this.entranceStairBlockPosition = new Tuple<>(exitPos.getX(), exitPos.getZ());
	}

	public void generateRooms(World world, DungeonGenerator dungeonGenerator, String mobType) {
		for (int x = 0; x < this.sideLength; x++) {
			for (int z = 0; z < this.sideLength; z++) {
				BlockPos pos = this.roomGrid[x][z];
				File file = null;
				if (!(x == this.exitStairIndex.getFirst() && z == this.exitStairIndex.getSecond())) {
					if (x == this.entranceStairIndex.getFirst() && z == this.entranceStairIndex.getSecond()) {
						if (this.entranceStair != null) {
							file = this.entranceStair;
						} else if (this.isFirstFloor) {
							file = this.generator.getDungeon().getEntranceStair();
						} else {
							file = this.generator.getDungeon().getStairRoom();
						}
					} else {
						file = this.generator.getDungeon().getRoom();
					}
				} else if (this.exitStairIsBossRoom) {
					file = this.generator.getDungeon().getBossRoom();
					this.exitStairIsBossRoom = false;
				}

				if (pos != null && file != null) {
					CQStructure structure = this.generator.loadStructureFromFile(file);
					BlockPos p = DungeonGenUtils.getCentralizedPosForStructure(pos, structure, this.generator.getPlacementSettings());
					dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, p, structure.getBlockInfoList(), this.generator.getPlacementSettings(), mobType));
					dungeonGenerator.add(new DungeonPartEntity(world, dungeonGenerator, p, structure.getEntityInfoList(), this.generator.getPlacementSettings(), mobType));
					dungeonGenerator.add(new DungeonPartBlockSpecial(world, dungeonGenerator, p, structure.getSpecialBlockInfoList(), this.generator.getPlacementSettings(), mobType));
				}
			}
		}
	}

	public void buildWalls(World world, DungeonGenerator dungeonGenerator) {
		if (this.generator.getDungeon().getWallBlock() == null) {
			return;
		}
		Map<BlockPos, IBlockState> stateMap = new HashMap<>();
		int dimX = this.generator.getDungeon().getRoomSizeX() / 2;
		int dimZ = this.generator.getDungeon().getRoomSizeZ() / 2;
		BlockPos p1 = this.roomGrid[this.sideLength - 1][this.sideLength - 1].add(1, 0, 1).add(dimX, -1, dimZ);
		BlockPos p2 = this.roomGrid[this.sideLength - 1][0].add(1, 0, -1).add(dimX, -1, -dimZ);
		BlockPos p3 = this.roomGrid[0][this.sideLength - 1].add(-1, 0, 1).add(-dimX, -1, dimZ);
		BlockPos p4 = this.roomGrid[0][0].add(-1, 0, -1).add(-dimX, -1, -dimZ);

		IBlockState state = this.generator.getDungeon().getWallBlock().getDefaultState();
		int addY = 2 + this.generator.getDungeon().getRoomSizeY();

		// 1-2
		for (BlockPos p12 : BlockPos.getAllInBoxMutable(p1, p2.add(0, addY, 0))) {
			// world.setBlockState(p12, block);
			stateMap.put(p12, state);
		}
		// 1-3
		for (BlockPos p13 : BlockPos.getAllInBoxMutable(p1, p3.add(0, addY, 0))) {
			// world.setBlockState(p13, block);
			stateMap.put(p13, state);
		}
		// 4-2
		for (BlockPos p42 : BlockPos.getAllInBoxMutable(p4, p2.add(0, addY, 0))) {
			// world.setBlockState(p42, block);
			stateMap.put(p42, state);
		}
		// 4-3
		for (BlockPos p43 : BlockPos.getAllInBoxMutable(p4, p3.add(0, addY, 0))) {
			// world.setBlockState(p43, block);
			stateMap.put(p43, state);
		}
		// Top
		for (BlockPos pT : BlockPos.getAllInBoxMutable(p1.add(0, 2 + this.generator.getDungeon().getRoomSizeY(), 0), p4.add(0, addY, 0))) {
			if (!(pT.getX() >= this.entranceStairCorners.getFirst().getX() && pT.getX() <= this.entranceStairCorners.getSecond().getX() && pT.getZ() >= this.entranceStairCorners.getFirst().getZ() && pT.getZ() <= this.entranceStairCorners.getSecond()
					.getZ())) {
				// world.setBlockState(pT, block);
				stateMap.put(pT, state);
			}
		}
		// Bottom
		for (BlockPos pB : BlockPos.getAllInBoxMutable(p1, p4)) {
			if (this.exitStairIsBossRoom || (pB != null && this.exitStairCorners != null && !(pB.getX() >= this.exitStairCorners.getFirst().getX() && pB.getX() <= this.exitStairCorners.getSecond().getX() && pB.getZ() >= this.exitStairCorners
					.getFirst().getZ() && pB.getZ() <= this.exitStairCorners.getSecond().getZ()))) {
				// world.setBlockState(pB, block);
				stateMap.put(pB, state);
			}
		}
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
			blockInfoList.add(new BlockInfo(entry.getKey().subtract(this.generator.getPos()), entry.getValue(), null));
		}
		dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, this.generator.getPos(), blockInfoList, new PlacementSettings(), "ZOMBIE"));
	}

}
