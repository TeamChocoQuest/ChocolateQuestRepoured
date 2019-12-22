package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.open;

import java.io.File;
import java.util.Random;

import javax.annotation.Nonnull;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdOpenGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class StrongholdFloorOpen {

	private StrongholdOpenGenerator generator;
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
	
	public StrongholdFloorOpen(StrongholdOpenGenerator generator) {
		this.generator = generator;
		int rgd = generator.getDungeon().getRandomRoomCountForFloor();
		if(rgd < 2) {
			rgd = 2;
		}
		if(rgd % 2 != 0) {
			rgd++;
		}
		rgd = (new Double(Math.ceil(Math.sqrt(rgd)))).intValue();
		sideLength = rgd;
		roomGrid = new BlockPos[rgd][rgd];
		
		int iX, iZ, iX2, iZ2;
		Random rdm = new Random();
		iX = rdm.nextInt(rgd);
		iZ = rdm.nextInt(rgd);
		entranceStairIndex = new Tuple<>(iX, iZ);
		iX2 = rdm.nextInt(rgd);
		iZ2 = rdm.nextInt(rgd);
		while(iX2 == iX) {
			iX2 = rdm.nextInt(rgd);
		}
		while(iZ2 == iZ) {
			iZ2 = rdm.nextInt(rgd);
		}
		exitStairIndex = new Tuple<>(iX2, iZ2);
	}
	
	public void setEntranceStairPosition(@Nonnull File entranceStair, int x, int y, int z) {
		this.entranceStair = entranceStair;
		this.yPos = y;
		this.entranceStairBlockPosition = new Tuple<Integer, Integer>(x, z);
		this.roomGrid[entranceStairIndex.getFirst()][entranceStairIndex.getSecond()] = new BlockPos(x,y,z);
	}
	
	public void setIsFirstFloor(boolean val) {
		this.isFirstFloor = val;
	}
	
	public void setExitIsBossRoom(boolean newVal) {
		this.exitStairIsBossRoom = newVal;
		if(newVal) {
			exitStairCorners = null;
		}
	}
	
	public Tuple<Integer, Integer> getExitCoordinates() {
		return exitStairBlockPosition;
	}
	
	public void calculatePositions() {
		Vec3i v = new Vec3i(generator.getDungeon().getRoomSizeX() /2, 0, generator.getDungeon().getRoomSizeZ() /2);
		for(int iX = 0; iX < sideLength; iX++) {
			for(int iZ = 0; iZ < sideLength; iZ++) {
				if(!(iX == entranceStairIndex.getFirst() && iZ == entranceStairIndex.getSecond())) {
					int multiplierX = iX - this.entranceStairIndex.getFirst();
					int multiplierZ = iZ - this.entranceStairIndex.getSecond();
					
					BlockPos pos = new BlockPos(
						entranceStairBlockPosition.getFirst() + (multiplierX * this.generator.getDungeon().getRoomSizeX()), 
						this.yPos,
						entranceStairBlockPosition.getSecond() + (multiplierZ * this.generator.getDungeon().getRoomSizeZ())
					);
					
					this.roomGrid[iX][iZ] = pos;
					if(iX == exitStairIndex.getFirst() && iZ == exitStairIndex.getSecond()) {
						BlockPos p1 = pos.subtract(v);
						BlockPos p2 = pos.add(v);
						exitStairCorners = new Tuple<>(p1,p2);
						exitStairBlockPosition = new Tuple<>(pos.getX(), pos.getZ());
					}
				} else {
					BlockPos p = new BlockPos(entranceStairBlockPosition.getFirst(), this.yPos, entranceStairBlockPosition.getSecond());
					entranceStairCorners = new Tuple<>(p.subtract(v), p.add(v));
				}
			}
		}
		BlockPos exitPos = this.roomGrid[exitStairIndex.getFirst()][exitStairIndex.getSecond()];
		this.entranceStairBlockPosition = new Tuple<>(exitPos.getX(), exitPos.getZ());
	}
	
	public void generateRooms(World world) {
		for(int x = 0; x < sideLength; x++) {
			for(int z = 0; z < sideLength; z++) {
				BlockPos p = this.roomGrid[x][z];
				File structure = null;
				if(x != exitStairIndex.getFirst() && z != exitStairIndex.getSecond()) {
					if(x == entranceStairIndex.getFirst() && z == entranceStairIndex.getSecond()) {
						if(this.entranceStair != null) {
							structure = this.entranceStair;
						} else if(isFirstFloor) {
							structure = this.generator.getDungeon().getEntranceStair();
						} else {
							structure = this.generator.getDungeon().getStairRoom();
						}
					} else {
						structure = this.generator.getDungeon().getRoom();
					}
				} else if(exitStairIsBossRoom) {
					structure = this.generator.getDungeon().getBossRoom();
				}
				
				if(p != null && structure != null) {
					CQStructure struct = new CQStructure(structure, this.generator.getDungeon(), this.generator.getDunX(), this.generator.getDunZ(), this.generator.getDungeon().isProtectedFromModifications());
					struct.placeBlocksInWorld(world, p, this.generator.getPlacementSettings(), EPosType.CENTER_XZ_LAYER);
				}
			}
		}
	}
	
	public void buildWalls(World world) {
		if(this.generator.getDungeon().getWallBlock() == null) {
			return;
		}
		int dimX = generator.getDungeon().getRoomSizeX();
		int dimZ = generator.getDungeon().getRoomSizeZ();
		BlockPos p1 = roomGrid[sideLength -1][sideLength -1].add(1,-1,1).add(dimX, 0, dimZ);
		BlockPos p2 = roomGrid[sideLength -1][0].add(1,-1,-1).add(dimX, 0, -dimZ);
		BlockPos p3 = roomGrid[0][sideLength -1].add(-1,-1,1).add(-dimX, 0, dimZ);
		BlockPos p4 = roomGrid[0][0].add(-1,-1,-1).add(-dimX, 0, -dimZ);
		
		IBlockState block = generator.getDungeon().getWallBlock().getDefaultState();
		int addY = 2 + this.generator.getDungeon().getRoomSizeY();
		
		//1-2
		for(BlockPos p12 : BlockPos.getAllInBoxMutable(p1, p2.add(0, addY, 0))) {
			world.setBlockState(p12, block);
		}
		//1-3
		for(BlockPos p13 : BlockPos.getAllInBoxMutable(p1, p3.add(0, addY, 0))) {
			world.setBlockState(p13, block);
		}
		//4-2
		for(BlockPos p42 : BlockPos.getAllInBoxMutable(p4, p2.add(0, addY, 0))) {
			world.setBlockState(p42, block);
		}
		//4-3
		for(BlockPos p43 : BlockPos.getAllInBoxMutable(p4, p3.add(0, addY, 0))) {
			world.setBlockState(p43, block);
		}
		//Top
		for(BlockPos pT : BlockPos.getAllInBoxMutable(p1.add(0, 2 + this.generator.getDungeon().getRoomSizeY(), 0), p4.add(0, addY, 0))) {
			if(!(pT.getX() >= entranceStairCorners.getFirst().getX() && pT.getX() <= entranceStairCorners.getSecond().getX())
					&& !(pT.getZ() >= entranceStairCorners.getFirst().getZ() && pT.getZ() <= entranceStairCorners.getSecond().getZ())) {
				world.setBlockState(pT, block);
			}
		}
		//Bottom
		for(BlockPos pB : BlockPos.getAllInBoxMutable(p1, p4)) {
			if(exitStairIsBossRoom || 
					(!(pB.getX() >= exitStairCorners.getFirst().getX() && pB.getX() <= exitStairCorners.getSecond().getX())
							&& !(pB.getZ() >= exitStairCorners.getFirst().getZ() && pB.getZ() <= exitStairCorners.getSecond().getZ()))) {
				world.setBlockState(pB, block);
			}
		}
	}

}
