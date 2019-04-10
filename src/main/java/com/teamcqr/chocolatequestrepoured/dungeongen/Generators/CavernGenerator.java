package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CavernDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class CavernGenerator implements IDungeonGenerator {
	
	public CavernGenerator() {};
	
	public CavernGenerator(CavernDungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	private int sizeX;
	private int sizeZ;
	private int height;
	
	private BlockPos center;
	
	private CavernDungeon dungeon;
	private Set<BlockPos> airBlocks = Sets.newHashSet();
	private Set<BlockPos> floorBlocks = Sets.newHashSet();
	
	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//DONE: calculate air blocks
		Perlin3D perlin1 = new Perlin3D(world.getSeed(), 4, new Random());
		Perlin3D perlin2 = new Perlin3D(world.getSeed(), 32, new Random());
		
		this.center = new BlockPos(x, y, z);
		
		int centerX = this.sizeX / 2;
		int centerY = this.height / 2;
		int centerZ = this.sizeZ / 2;
		
		int centerDistSquared = centerX*centerX + centerY*centerY + centerZ*centerZ;
		
		float scaleX= 1.0F;
		float scaleY= 1.0F;
		float scaleZ= 1.0F;
		
		float maxSize = Math.max(this.sizeX, Math.max(this.height, this.sizeZ));
		
		scaleX += ((maxSize - this.sizeX) / maxSize);
		scaleY += ((maxSize - this.height) / maxSize);
		scaleZ += ((maxSize - this.sizeZ) / maxSize);
		
		for(int iX = 0; iX < this.sizeX; iX++) {
			for(int iY = 0; iY < this.height; iY++) {
				for(int iZ = 0; iZ < this.sizeZ; iZ++) {
					float noiseAtPos = (iX - centerX) * (iX - centerX) * scaleX * scaleX;
					noiseAtPos += ((iY - centerY) * (iY - centerY) * scaleY * scaleY);
					noiseAtPos += ((iZ - centerZ) * (iZ - centerZ) * scaleZ * scaleZ);
					
					noiseAtPos /= centerDistSquared;
					
					double noise = (perlin1.getNoiseAt(x + iX, y + iY, z + iZ) + perlin2.getNoiseAt(x + iX, y + iY, z + iZ)) / 2.0D * (noiseAtPos * 2.5D);
					
					if(noise < 0.5D) {
						BlockPos block = new BlockPos(x + iX, y + iY, z + iZ);
						this.airBlocks.add(block);
						if(iY == 0) {
							this.floorBlocks.add(block.down());
						}
					}
				}
			}
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		for(BlockPos bp : this.airBlocks) {
			if(!Block.isEqualTo(world.getBlockState(bp).getBlock(), this.dungeon.getAirBlock())) {
				world.setBlockState(bp, this.dungeon.getAirBlock().getDefaultState());
			}
		}
		for(BlockPos bp : this.floorBlocks) {
			if(!Block.isEqualTo(world.getBlockState(bp).getBlock(), this.dungeon.getFloorBlock())) {
				world.setBlockState(bp, this.dungeon.getFloorBlock().getDefaultState());
			}
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		System.out.println("Generated " + this.dungeon.getDungeonName() + " at X: " + x + "  Y: " + y + "  Z: " + z);
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		BlockPos start = new BlockPos(x, y, z);
		world.setBlockState(start, Blocks.CHEST.getDefaultState());
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(start);
		int eltID = new Random().nextInt(14) +4;
		chest.setLootTable(ELootTable.valueOf(eltID).getLootTable(), world.getSeed());
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		BlockPos spawnerPos = new BlockPos(x, y +1, z);
		world.setBlockState(spawnerPos, Blocks.MOB_SPAWNER.getDefaultState());
		
		TileEntityMobSpawner spawner = (TileEntityMobSpawner)world.getTileEntity(spawnerPos);
		
		spawner.getSpawnerBaseLogic().setEntityId(this.dungeon.getBossMob());
	}
	
	public void generateTunnel(BlockPos start, BlockPos end, World world) {
		Block airBlock = this.dungeon.getAirBlock();
		Block floorMaterial = this.dungeon.getFloorBlock();
		int vX = end.getX() - start.getX();
		int vZ = end.getZ() - start.getZ();
		
		start.add(0, 1, 0);
		
		double length = Math.sqrt(vX * vX + vZ * vZ);
		Vec3d v = new Vec3d(vX /length , 0, vZ /length);
		
		//Fills the tunnel with air using multiple 3x3x3 cubes and sets the floor material
		for(int i = 0; i < ((Double)length).intValue(); i++) {
			start = start.add(v.x * i, 0, v.z * i);
			
			world.setBlockState(start, airBlock.getDefaultState());
			world.setBlockState(start.down(), airBlock.getDefaultState());
			world.setBlockState(start.down().down(), floorMaterial.getDefaultState());
			world.setBlockState(start.up(), airBlock.getDefaultState());
			
			world.setBlockState(start.north(), airBlock.getDefaultState());
			world.setBlockState(start.north().down(), airBlock.getDefaultState());
			world.setBlockState(start.north().down().down(), floorMaterial.getDefaultState());
			world.setBlockState(start.north().up(), airBlock.getDefaultState());
			
			world.setBlockState(start.east(), airBlock.getDefaultState());
			world.setBlockState(start.east().down(), airBlock.getDefaultState());
			world.setBlockState(start.east().down().down(), floorMaterial.getDefaultState());
			world.setBlockState(start.east().up(), airBlock.getDefaultState());
			
			world.setBlockState(start.south(), airBlock.getDefaultState());
			world.setBlockState(start.south().down(), airBlock.getDefaultState());
			world.setBlockState(start.south().down().down(), floorMaterial.getDefaultState());
			world.setBlockState(start.south().up(), airBlock.getDefaultState());
			
			world.setBlockState(start.west(), airBlock.getDefaultState());
			world.setBlockState(start.west().down(), airBlock.getDefaultState());
			world.setBlockState(start.west().down().down(), floorMaterial.getDefaultState());
			world.setBlockState(start.west().up(), airBlock.getDefaultState());
		}
	}
	
	public void setSizeAndHeight(int sX, int sZ, int h) {
		this.sizeX = sX;
		this.sizeZ = sZ;
		this.height = h;
	}
	
	public enum EStairDirection {
		NORTH,
		EAST,
		SOUTH,
		WEST;
	}
	
	public void buildLadder(EStairDirection direction, World world) {
		switch (direction) {
		case EAST:
			buildStairE(world);
			break;
		case NORTH:
			buildStairN(world);
			break;
		case SOUTH:
			buildStairS(world);
			break;
		case WEST:
			buildStairW(world);
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void buildStairN(World world) {
		BlockPos start = this.center.north(this.sizeZ - 2);
		int highestY = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(start), start.getX(), start.getZ(), true);
		while(start.getY() <= highestY) {
			/*if(Block.isEqualTo(world.getBlockState(start.south()).getBlock(), Blocks.AIR)) {
				
			}*/
			world.setBlockState(start, Blocks.LADDER.getStateFromMeta(0));
			
			start = start.up();
		}
	}
	@SuppressWarnings("deprecation")
	private void buildStairE(World world) {
		BlockPos start = this.center.north(this.sizeX - 2);
		int highestY = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(start), start.getX(), start.getZ(), true);
		while(start.getY() <= highestY) {
			/*if(Block.isEqualTo(world.getBlockState(start.west()).getBlock(), Blocks.AIR)) {
				
			}*/
			world.setBlockState(start, Blocks.LADDER.getStateFromMeta(5));
			
			start = start.up();
		}
	}
	@SuppressWarnings("deprecation")
	private void buildStairS(World world) {
		BlockPos start = this.center.north(this.sizeZ - 2);
		int highestY = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(start), start.getX(), start.getZ(), true);
		while(start.getY() <= highestY) {
			/*if(Block.isEqualTo(world.getBlockState(start.north()).getBlock(), Blocks.AIR)) {
				
			}*/
			world.setBlockState(start, Blocks.LADDER.getStateFromMeta(3));
			
			start = start.up();
		}
	}
	@SuppressWarnings("deprecation")
	private void buildStairW(World world) {
		BlockPos start = this.center.north(this.sizeX - 2);
		int highestY = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(start), start.getX(), start.getZ(), true);
		while(start.getY() <= highestY) {
			/*if(Block.isEqualTo(world.getBlockState(start.east()).getBlock(), Blocks.AIR)) {
				
			}*/
			world.setBlockState(start, Blocks.LADDER.getStateFromMeta(10));
			
			start = start.up();
		}
	}

}
