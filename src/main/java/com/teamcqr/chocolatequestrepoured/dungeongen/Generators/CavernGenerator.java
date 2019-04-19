package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CavernDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
	
	private int centerX;
	private int centerZ;
	private int centerY;
	
	private BlockPos center;
	
	private CavernDungeon dungeon;
	private List<BlockPos> airBlocks = new ArrayList<BlockPos>();
	private List<BlockPos> floorBlocks = new ArrayList<BlockPos>();
	
	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//DONE: calculate air blocks
		Perlin3D perlin1 = new Perlin3D(world.getSeed(), 4, new Random());
		Perlin3D perlin2 = new Perlin3D(world.getSeed(), 32, new Random());
		
		this.center = new BlockPos(x, y, z);
		
		int centerX = this.sizeX / 2;
		int centerY = this.height / 2;
		int centerZ = this.sizeZ / 2;
		
		this.centerX = centerX + x;
		this.centerY = centerY + y;
		this.centerZ = centerZ + z;
		
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
					
					if(noise < 0.75D) {
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
		if(chest != null) {
			ResourceLocation resLoc = null;
			try {
				resLoc = ELootTable.valueOf(eltID).getLootTable();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			if(resLoc != null) {
				chest.setLootTable(resLoc, world.getSeed());
			}
		}
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		BlockPos spawnerPos = new BlockPos(x, y, z);
		world.setBlockState(spawnerPos, Blocks.MOB_SPAWNER.getDefaultState());
		
		TileEntityMobSpawner spawner = (TileEntityMobSpawner)world.getTileEntity(spawnerPos);
		
		spawner.getSpawnerBaseLogic().setEntityId(this.dungeon.getMob());
		//System.out.println("Spawner Mob: " + this.dungeon.getMob().toString());
		spawner.updateContainingBlockInfo();
		
		spawner.update();
	}
	
	public void generateTunnel(BlockPos start, BlockPos end, World world) {
		generateTunnel(new Random().nextBoolean(), start, end, world);
	}
	
	private void generateTunnel(boolean xFirst, BlockPos start, BlockPos target, World world) {
		if(start.getX() == target.getX() && start.getZ() == target.getZ()) {
			return;
		}
		else if(start.getX() == target.getX() && xFirst) {
			generateTunnel(false, start, target, world);
		} 
		else if(start.getZ() == target.getZ() && !xFirst) {
			generateTunnel(true, start, target, world);
		} 
		/*else if(DungeonGenUtils.PercentageRandom(25, world.getSeed()) && !(start.getX() == target.getX() || start.getZ() == target.getZ())) {
			generateTunnel(!xFirst, start, target, world);
		} */
		else {
			int v = 0;
			buildSegment(start, world);
			if(xFirst) {
				v = start.getX() < target.getX() ? 1 : -1;
				if(start.getX() == target.getX()) {
					v = 0;
				}
				start = start.add(v, 0, 0);
			} else {
				v = start.getZ() < target.getZ() ? 1 : -1;
				if(start.getZ() == target.getZ()) {
					v = 0;
				}
				start = start.add(0, 0, v);
			}
			generateTunnel(xFirst, start, target, world);
		}
	}
	
	private void buildSegment(BlockPos pos, World world) {
		Block airBlock = this.dungeon.getAirBlock();
		Block floorMaterial = this.dungeon.getFloorBlock();
		
		world.setBlockState(pos, airBlock.getDefaultState());
		world.setBlockState(pos.down(), airBlock.getDefaultState());
		world.setBlockState(pos.down().down(), floorMaterial.getDefaultState());
		world.setBlockState(pos.up(), airBlock.getDefaultState());
		
		world.setBlockState(pos.north(), airBlock.getDefaultState());
		world.setBlockState(pos.north().down(), airBlock.getDefaultState());
		world.setBlockState(pos.north().down().down(), floorMaterial.getDefaultState());
		world.setBlockState(pos.north().up(), airBlock.getDefaultState());
		
		world.setBlockState(pos.north().east(), airBlock.getDefaultState());
		world.setBlockState(pos.north().east().down(), airBlock.getDefaultState());
		world.setBlockState(pos.north().east().down().down(), floorMaterial.getDefaultState());
		world.setBlockState(pos.north().east().up(), airBlock.getDefaultState());
		
		world.setBlockState(pos.north().west(), airBlock.getDefaultState());
		world.setBlockState(pos.north().west().down(), airBlock.getDefaultState());
		world.setBlockState(pos.north().west().down().down(), floorMaterial.getDefaultState());
		world.setBlockState(pos.north().west().up(), airBlock.getDefaultState());
		
		world.setBlockState(pos.east(), airBlock.getDefaultState());
		world.setBlockState(pos.east().down(), airBlock.getDefaultState());
		world.setBlockState(pos.east().down().down(), floorMaterial.getDefaultState());
		world.setBlockState(pos.east().up(), airBlock.getDefaultState());
		
		world.setBlockState(pos.south(), airBlock.getDefaultState());
		world.setBlockState(pos.south().down(), airBlock.getDefaultState());
		world.setBlockState(pos.south().down().down(), floorMaterial.getDefaultState());
		world.setBlockState(pos.south().up(), airBlock.getDefaultState());
		
		world.setBlockState(pos.south().east(), airBlock.getDefaultState());
		world.setBlockState(pos.south().east().down(), airBlock.getDefaultState());
		world.setBlockState(pos.south().east().down().down(), floorMaterial.getDefaultState());
		world.setBlockState(pos.south().east().up(), airBlock.getDefaultState());
		
		world.setBlockState(pos.south().west(), airBlock.getDefaultState());
		world.setBlockState(pos.south().west().down(), airBlock.getDefaultState());
		world.setBlockState(pos.south().west().down().down(), floorMaterial.getDefaultState());
		world.setBlockState(pos.south().west().up(), airBlock.getDefaultState());
		
		world.setBlockState(pos.west(), airBlock.getDefaultState());
		world.setBlockState(pos.west().down(), airBlock.getDefaultState());
		world.setBlockState(pos.west().down().down(), floorMaterial.getDefaultState());
		world.setBlockState(pos.west().up(), airBlock.getDefaultState());
	}
	
	public void setSizeAndHeight(int sX, int sZ, int h) {
		this.sizeX = sX;
		this.sizeZ = sZ;
		this.height = h;
	}
	
	public void buildLadder(World world) {
		System.out.println("Building exit at X: " + this.center.getX() + "  Y: " + this.center.getY() + "  Z: " + this.center.getZ() + "...");
		
		BlockPos start = this.center.north(this.sizeZ - 2);
		int y = start.getY();
		int highestY = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(start), start.getX(), start.getZ(), true);
		while(y <= highestY) {
			world.setBlockState(start, Blocks.LADDER.getDefaultState());
			
			start = start.up();
			y++;
		}
	}
	
	public BlockPos getCenter() {
		return new BlockPos(this.centerX, this.centerY, this.centerZ);
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		//MAKES NO SENSE FOR A CAVE
	}

}
