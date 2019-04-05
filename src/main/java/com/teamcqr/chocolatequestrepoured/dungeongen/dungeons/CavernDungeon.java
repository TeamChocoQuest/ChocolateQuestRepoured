package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.CavernGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootTableList;

public class CavernDungeon extends DungeonBase {
	
	private int minRooms = 1;
	private int maxRooms = 8;
	private int minY = 30;
	private int maxY = 60;
	private int minCaveSize = 5;
	private int maxCaveSize = 15;
	private int minHeight = 4;
	private int maxHeight = 8;
	private int maxRoomDistance = 18;
	private int minRoomDistance = 10;
	private int chestChancePerRoom = 20;
	
	private boolean placeSpawners = false;
	private boolean placeBoss = true;
	private boolean lootChests = false;
	private String mobName = "minecraft:zombie";
	private String bossMobName = "minecraft:wither";
	private Block floorMaterial = Blocks.STONE;
	private Block airBlock = Blocks.AIR;

	@Override
	public IDungeonGenerator getGenerator() {
		return new CavernGenerator();
	}
	
	//One block below starts y is the floor...
	@Override
	protected void generate(int x, int z, World world, Chunk chunk) {
		super.generate(x, z, world, chunk);
		
		List<CavernGenerator> caves = new ArrayList<CavernGenerator>();
		HashMap<CavernGenerator, Integer> xMap = new HashMap<CavernGenerator, Integer>();
		HashMap<CavernGenerator, Integer> zMap = new HashMap<CavernGenerator, Integer>();
		
		int rooms = maxRooms <= minRooms ? minRooms : DungeonGenUtils.getIntBetweenBorders(minRooms, maxRooms, world.getSeed());
		int y = DungeonGenUtils.getIntBetweenBorders(minY, maxY, world.getSeed());
		int roomIndex = 1;
		
		int OrigX = new Integer(x);
		int OrigZ = new Integer(z);
		
		Vec3i distance = new Vec3i(0, 0, 0);
		
		do {
			x += distance.getX();
			z +=distance.getZ();
					
			CavernGenerator cave = new CavernGenerator(this);
			cave.preProcess(world, chunk, x + distance.getX(), y, z + distance.getZ());
			
			int vLength = DungeonGenUtils.getIntBetweenBorders(minRoomDistance, maxRoomDistance, world.getSeed());
			distance = new Vec3i(vLength, 0, 0);
			double angle = ((Integer)new Random().nextInt(360)).doubleValue();
			distance = VectorUtil.rotateVectorAroundY(distance, angle);
			
			caves.add(cave);
			xMap.put(cave, x);
			zMap.put(cave, z);
		} while(roomIndex < rooms);
		
		int currX = new Integer(OrigX);
		int currZ = new Integer(OrigZ);
		for(int i = 0; i < caves.size(); i++) {
			
			CavernGenerator cave = caves.get(i);
			
			BlockPos start = new BlockPos(currX, y, currZ);
			BlockPos end = new BlockPos(xMap.get(cave), y, zMap.get(cave));
			
			//Dig out the cave...
			cave.buildStructure(world, chunk, xMap.get(cave), y, zMap.get(cave));
			
			//connect the tunnels
			generateTunnel(start, end, world);
			
			//Place a loot chest....
			if(lootChests && DungeonGenUtils.PercentageRandom(this.chestChancePerRoom, world.getSeed())) {
				cave.fillChests(world, chunk, xMap.get(cave), y, zMap.get(cave));
			}
			
			//Place a spawner...
			if(placeSpawners) {
				cave.placeSpawners(world, chunk, xMap.get(cave), y +1, zMap.get(cave));
				/*world.setBlockState(start.add(0, 1, 0), Blocks.MOB_SPAWNER.getDefaultState());
				TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(start.add(0, 1, 0));
				//TODO: set spawner mob*/
			}
			
			currX = new Integer(end.getX());
			currZ = new Integer(end.getZ());
		}
		
		if(placeBoss) {
			int caveIndx = new Random().nextInt(caves.size());
			BlockPos bossPos = new BlockPos(xMap.get(caves.get(caveIndx)), y +1, zMap.get(caves.get(caveIndx)));
			world.setBlockToAir(bossPos.down());
			
			//BOSS CHEST
			world.setBlockState(bossPos.down(), Blocks.CHEST.getDefaultState());
			TileEntityChest bossChest = (TileEntityChest) world.getTileEntity(bossPos.down());
			bossChest.setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, world.getSeed());
			
			//BOSS SPAWNER
		}
		
	}
	
	private void generateTunnel(BlockPos start, BlockPos end, World world) {
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
	
	int getMinCaveHeight() {
		return this.minHeight;
	}
	int getMaxCaveHeight() {
		return this.maxHeight;
	}
	int getMinCaveSize() {
		return this.minCaveSize;
	}
	int getMaxCaveSize() {
		return this.maxCaveSize;
	}
	public Block getAirBlock() {
		return this.airBlock;
	}
	public Block getFloorBlock() {
		return this.floorMaterial;
	}
}
