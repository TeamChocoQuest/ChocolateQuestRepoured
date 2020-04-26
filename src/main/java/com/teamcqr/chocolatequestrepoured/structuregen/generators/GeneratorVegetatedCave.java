package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVegetatedCave;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class GeneratorVegetatedCave implements IDungeonGenerator {

	private DungeonVegetatedCave dungeon;
	
	private List<BlockPos> spawners;
	private List<BlockPos> chests;
	private List<BlockPos> floorBlocks;
	private HashMap<BlockPos, IBlockState> blocks;
	private BlockPos center;
	private EDungeonMobType mobtype;

	public GeneratorVegetatedCave(DungeonVegetatedCave dungeon) {
		this.dungeon = dungeon;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		if(this.dungeon.getDungeonMob() == EDungeonMobType.DEFAULT) {
			dungeon.getDungeonMob();
			this.mobtype = EDungeonMobType.getMobTypeDependingOnDistance(world, x, z);
		} else {
			this.mobtype = dungeon.getDungeonMob();
		}
		Random random = new Random(WorldDungeonGenerator.getSeed(world, x / 16, z / 16));
		Block[][][] blocks = getRandomBlob(dungeon.getAirBlock(), dungeon.getCentralCaveSize(), random);
		getFloorBlocksOfBlob(blocks, new BlockPos(x,y,z), random);
		//lists.add(ExtendedBlockStatePart.split(new BlockPos(x,y,z), blocks, 32));
		storeBlockArrayInMap(blocks, new BlockPos(x,y,z));
		Vec3d center = new Vec3d(x,y,z);
		Vec3d rad = new Vec3d(dungeon.getCentralCaveSize(), 0, 0);
		double angle = 360D / dungeon.getCaveCount();
		for(int i = 0; i < dungeon.getCaveCount(); i++) {
			Vec3d v = VectorUtil.rotateVectorAroundY(rad, angle * i);
			Vec3d startPos = center.add(v);
			createTunnel(startPos, angle * i, dungeon.getCentralCaveSize() / (dungeon.getCaveCount() -1), dungeon.getCaveSegmentCount(), random);
		}
		lists.add(ExtendedBlockStatePart.splitBlockStateMap(new BlockPos(x,y,z), this.blocks));
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		if(dungeon.placeBuilding()) {
			BlockPos pastePos = new BlockPos(x , y, z);
			File file = dungeon.getRandomCentralBuilding();
			if(file != null) {
				CQStructure structure = new CQStructure(file);
				structure.setDungeonMob(this.mobtype);
				PlacementSettings settings = new PlacementSettings();
				settings.setMirror(Mirror.NONE);
				settings.setRotation(Rotation.NONE);
				settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
				settings.setIntegrity(1.0F);
				for (List<? extends IStructure> list : structure.addBlocksToWorld(world, pastePos, settings, EPosType.CENTER_XZ_LAYER, this.dungeon, chunk.x, chunk.z)) {
					lists.add(list);
				}
			}
		}
		// DONE: Paste the building
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		//TODO: Place giant shrooms
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		//TODO: Place and fill chests
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		//TODO: Place spawners
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// UNUSED
	}

	@Override
	public DungeonBase getDungeon() {
		return dungeon;
	}
	
	private void createTunnel(Vec3d startPos, double initAngle, int startSize, int initLength, Random random) {
		double angle = 67.5D;
		Vec3d expansionDir = VectorUtil.rotateVectorAroundY(new Vec3d(startSize, 0, 0), initAngle);
		for(int i = 0; i < initLength; i++) {
			Block[][][] blob = getRandomBlob(dungeon.getAirBlock(), startSize, random);
			getFloorBlocksOfBlob(blob, new BlockPos(startPos.x, startPos.y, startPos.z), random);
			storeBlockArrayInMap(blob, new BlockPos(startPos.x, startPos.y, startPos.z));
			expansionDir = VectorUtil.rotateVectorAroundY(expansionDir, angle);
			startPos = startPos.add(expansionDir);
		}
		startSize -= 2;
		if(startSize > 2) {
			//TODO
		}
	}
	
	private void storeBlockArrayInMap(Block[][][] blob, BlockPos blobCenter) {
		int radius = blob.length / 2;
		for(int iX = 0; iX < blob.length; iX++) {
			for(int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for(int iY = 1; iY < blob[0].length; iY++) {
					if(blob[iX][iY][iZ] != null) {
						IBlockState state = blob[iX][iY][iZ].getDefaultState();
						BlockPos bp = new BlockPos(iX - radius, iY - radius, iZ - radius);
						this.blocks.put(bp, state);
					}
				}
			}
		}
	}
	
	private List<BlockPos> getFloorBlocksOfBlob(Block[][][] blob, BlockPos blobCenter, Random random) {
		List<BlockPos> floorBlocks = new ArrayList<>();
		int radius = blob.length / 2;
		for(int iX = 0; iX < blob.length; iX++) {
			for(int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for(int iY = 1; iY < blob[0].length; iY++) {
					if(blob[iX][iY][iZ] != null && blob[iX][iY -1][iZ] == null) {
						blob[iX][iY][iZ] = dungeon.getFloorBlock(random);
						floorBlocks.add(new BlockPos(iX - radius, iY - radius, iZ - radius));
					}
				}
			}
		}
		return floorBlocks;
	}
	
	private Block[][][] getRandomBlob(Block block, int radius, Random random) {
		Block[][][] blocks = new Block[radius * 4][radius * 4][radius * 4];
		int subSphereCount = radius * 3;
		double sphereSurface = 4 * Math.PI * (radius * radius);
		double counter = sphereSurface / subSphereCount;
		double cI = 0;
		for(int iX = -radius; iX <= radius; iX++) {
			for(int iY = -radius; iY <= radius; iY++) {
				for(int iZ = -radius; iZ <= radius; iZ++) {
					double distance = iX * iX + iZ * iZ + iY * iY;
					distance = Math.sqrt(distance);
					if(distance < radius) {
						blocks[iX + (radius * 2)][iY + (radius * 2)][iZ + (radius * 2)] = block;
					} else if(distance <= radius +1) {
						cI++;
						if(cI < counter) {
							continue;
						}
						cI = 0;
						int r1 = radius / 2;
						int r2 = (int) (radius * 0.75);
						int rSub = DungeonGenUtils.getIntBetweenBorders(r1, r2, random);
						for(int jX = iX - rSub; jX <= iX + rSub; jX++) {
							for(int jY = iY - rSub; jY <= iY + rSub; jY++) {
								for(int jZ = iZ - rSub; jZ <= iZ + rSub; jZ++) {
									double distanceSub = (jX -iX) * (jX -iX) + (jY -iY) * (jY -iY) + (jZ -iZ) * (jZ -iZ);
									distanceSub = Math.sqrt(distanceSub);
									if(distanceSub < rSub) {
										try {
											//blocks[jX + (radius * 2)][jY + (radius * 2)][jZ + (radius * 2)] = block;
											if(blocks[jX + (radius * 2)][jY + (radius * 2)][jZ + (radius * 2)] != block) {
												blocks[jX + (radius * 2)][jY + (radius * 2)][jZ + (radius * 2)] = block;
											}
										} catch(ArrayIndexOutOfBoundsException ex) {
											//Ignore
										}
									}
								}
							}
						}
						subSphereCount--;
					}
				}
			}
		}
		return blocks;
	}

}
