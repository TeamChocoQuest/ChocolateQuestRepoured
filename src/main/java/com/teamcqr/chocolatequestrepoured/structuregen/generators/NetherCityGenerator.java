package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.ClassicNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;

import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class NetherCityGenerator implements IDungeonGenerator {

	private ClassicNetherCity dungeon;
	
	private Set<BlockPos> gridPositions = new HashSet<>();
	private Set<BlockPos> bridgeBuilderStartPositionsX = new HashSet<>();
	private Set<BlockPos> bridgeBuilderStartPositionsZ = new HashSet<>();
	
	private Set<BlockPos> bridgeBlocks = new HashSet<>();
	private Set<BlockPos> lavaBlocks = new HashSet<>();
	
	private int minX;
	private int maxX;
	private int minZ;
	private int maxZ;
	
	private static int tunnelHeight = 3;
	
	public NetherCityGenerator(ClassicNetherCity dungeon) {
		this.dungeon = dungeon;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//Calculate the grid for the buildings
		int rowsX = dungeon.getXRows();
		int rowsZ = dungeon.getZRows();
		
		rowsX /= 2;
		rowsZ /= 2;
		
		minX = x -(rowsX * dungeon.getDistanceBetweenBuildingCenters());
		minZ = z -(rowsZ * dungeon.getDistanceBetweenBuildingCenters());
		maxX = x +(rowsX * dungeon.getDistanceBetweenBuildingCenters());
		maxZ = z +(rowsZ * dungeon.getDistanceBetweenBuildingCenters());
		
		//This constructs the grid around the center
		for(int iX = -rowsX; iX <= rowsX; iX++) {
			for(int iZ = -rowsZ; iZ <= rowsZ; iZ++) {
				
				boolean noAddFlag = false;
				if(dungeon.centralBuildingIsSpecial() && iX == 0 && iZ == 0) {
					noAddFlag = true;
				}
				
				BlockPos p = new BlockPos(x + (iX * dungeon.getDistanceBetweenBuildingCenters()), y, z +(iZ * dungeon.getDistanceBetweenBuildingCenters()));
				if(!noAddFlag) {
					gridPositions.add(p);
				}
				//Bridge starter positions, in total there will be rowsX +rowsZ -1 bridges
				if(iX == 0) {
					bridgeBuilderStartPositionsZ.add(p);
				}
				if(iZ == 0) {
					bridgeBuilderStartPositionsX.add(p);
				}
			}
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		//Dig out the big air pocket or the small ones
		if(dungeon.makeSpaceForBuildings()) {
			if(dungeon.useSingleAirPocketsForHouses()) {
				for(BlockPos pocketCenter : gridPositions) {
					BlockPos cLower = new BlockPos(pocketCenter.getX() - dungeon.getLongestSide() /2, y +1, pocketCenter.getZ() - dungeon.getLongestSide());
					BlockPos cUpper = new BlockPos(pocketCenter.getX() + dungeon.getLongestSide() /2, y +dungeon.getCaveHeight(), pocketCenter.getZ() + dungeon.getLongestSide());
					
					PlateauBuilder pB = new PlateauBuilder();
					pB.makeRandomBlob(new Random(), dungeon.getAirPocketBlock(), cLower, cUpper, WorldDungeonGenerator.getSeed(world, pocketCenter.getX(), pocketCenter.getZ()), world);
				}
			} else {
				BlockPos cLower = new BlockPos(minX, y +1, minZ).add(-dungeon.getDistanceBetweenBuildingCenters(), 0, -dungeon.getDistanceBetweenBuildingCenters());;
				BlockPos cUpper = new BlockPos(maxX, y +dungeon.getCaveHeight(), maxZ).add(-dungeon.getDistanceBetweenBuildingCenters() /4, 0, -dungeon.getDistanceBetweenBuildingCenters() /4);
				
				PlateauBuilder pB = new PlateauBuilder();
				pB.makeRandomBlob(new Random(), dungeon.getAirPocketBlock(), cLower, cUpper, WorldDungeonGenerator.getSeed(world, minX, maxZ), world);
			}
		}
		
		
		//Build the roads / bridges and the floors
		for(BlockPos lavaPos : BlockPos.getAllInBox(minX - dungeon.getDistanceBetweenBuildingCenters(), y, minZ- dungeon.getDistanceBetweenBuildingCenters(), maxX + dungeon.getDistanceBetweenBuildingCenters(), y, maxZ + dungeon.getDistanceBetweenBuildingCenters())) {
			lavaBlocks.add(lavaPos);
		}
		//Bridges from south to north
		for(BlockPos pX : bridgeBuilderStartPositionsX) {
			for(int iZ = minZ; iZ <= maxZ; iZ++) {
				BlockPos pC = new BlockPos(pX.getX(), pX.getY(), iZ);
				BlockPos pCE = pC.east();
				BlockPos pCW = pC.west();
				bridgeBlocks.add(pC);
				bridgeBlocks.add(pCE);
				bridgeBlocks.add(pCW);
				
				//Tunnels if not big air pocket
				if(dungeon.makeSpaceForBuildings() && dungeon.useSingleAirPocketsForHouses()) {
					for(int n = 1; n <= tunnelHeight; n++) {
						world.setBlockToAir(pC.up(n));
						world.setBlockToAir(pCE.up(n));
						world.setBlockToAir(pCW.up(n));
					}
				}
			}
		}
		//Bridges from west to east
		for(BlockPos pZ : bridgeBuilderStartPositionsZ) {
			for(int iX = minX; iX <= maxX; iX++) {
				BlockPos pC = new BlockPos(iX, pZ.getY(), pZ.getZ());
				BlockPos pCN = pC.north();
				BlockPos pCS = pC.south();
				bridgeBlocks.add(pC);
				bridgeBlocks.add(pCN);
				bridgeBlocks.add(pCS);
				
				//Tunnels if not big air pocket
				if(dungeon.makeSpaceForBuildings() && dungeon.useSingleAirPocketsForHouses()) {
					for(int n = 1; n <= tunnelHeight; n++) {
						world.setBlockToAir(pC.up(n));
						world.setBlockToAir(pCN.up(n));
						world.setBlockToAir(pCS.up(n));
					}
				}
			}
		}
		
		for(BlockPos p : lavaBlocks) {
			world.setBlockState(p, dungeon.getFloorBlock().getDefaultState());
		}
		for(BlockPos p : bridgeBlocks) {
			world.setBlockState(p, dungeon.getBridgeBlock().getDefaultState());
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		//Place the buildings
		Random rdm = new Random();
		
		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);
		
		if(dungeon.centralBuildingIsSpecial()) {
			//DONE: Choose a central building, figure out the size, then build the platform and then the building
			CQStructure centralStructure = null;
			if(dungeon.getCentralBuildingFolder().exists() && dungeon.getCentralBuildingFolder().isDirectory() && dungeon.getCentralBuildingFolder().listFiles().length > 0) {
				centralStructure = new CQStructure(dungeon.getCentralBuildingFolder().listFiles()[rdm.nextInt(dungeon.getCentralBuildingFolder().listFiles().length)], dungeon.isProtectedFromModifications());
			} else if(dungeon.getCentralBuildingFolder().exists() && dungeon.getCentralBuildingFolder().isFile()) {
				centralStructure = new CQStructure(dungeon.getCentralBuildingFolder(), dungeon.isProtectedFromModifications());
			}
			
			if(centralStructure != null) {
				BlockPos cL = new BlockPos(x - (centralStructure.getSizeX() /2 +2), y, z - (centralStructure.getSizeZ() /2 +2));
				BlockPos cU = cL.add(centralStructure.getSizeX() +4, 0, centralStructure.getSizeZ() +4);
				BlockPos.getAllInBox(cL, cU).forEach(new Consumer<BlockPos>() {

					@Override
					public void accept(BlockPos t) {
						world.setBlockState(t, dungeon.getBridgeBlock().getDefaultState());
					}
				});
				
				centralStructure.placeBlocksInWorld(world, new BlockPos(x, y+1, z), settings, EPosType.CENTER_XZ_LAYER);
			}
		}
		CQStructure structure = null;
		int filesInFolder = dungeon.getBuildingFolder().exists() && dungeon.getBuildingFolder().isDirectory() ? dungeon.getBuildingFolder().listFiles().length : -1;
		for(BlockPos centerPos : gridPositions) {
			//DONE: Choose a building, figure out the size, then build the platform and then the building
			if(dungeon.getBuildingFolder().exists() && dungeon.getBuildingFolder().isDirectory() && filesInFolder > 1) {
				structure = new CQStructure(dungeon.getBuildingFolder().listFiles()[rdm.nextInt(filesInFolder)], dungeon.isProtectedFromModifications());
			} else if(dungeon.getBuildingFolder().exists() && dungeon.getBuildingFolder().isFile()) {
				structure = new CQStructure(dungeon.getBuildingFolder(), dungeon.isProtectedFromModifications());
			}
			
			if(structure != null) {
				BlockPos cL = centerPos.subtract(new Vec3i(structure.getSizeX() /2  +2, 0, structure.getSizeZ() /2  +2));
				BlockPos cU = centerPos.add(structure.getSizeX() /2  +2, 0, structure.getSizeZ() /2  +2);
				BlockPos.getAllInBox(cL, cU).forEach(new Consumer<BlockPos>() {

					@Override
					public void accept(BlockPos t) {
						world.setBlockState(t, dungeon.getBridgeBlock().getDefaultState());
					}
				});
				
				structure.placeBlocksInWorld(world, centerPos.add(0,1,0), settings, EPosType.CENTER_XZ_LAYER);
			}
		}
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//UNUSED HERE
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		//Maybe place some ghast spawners over the city and a nether dragon? -> Complete factory first
		if(dungeon.placeSpawnersAboveBuildings()) {
			int spawnerY = y + (new Double(dungeon.getCaveHeight() *0.9).intValue());
			
			//DONE: Place CQ spawners !!!
			if(dungeon.centralBuildingIsSpecial()) {
				BlockPos spawnerPosCentral = new BlockPos(x, spawnerY, z);
				try {
					if(dungeon.centralSpawnerIsSingleUse()) {
						SpawnerFactory.placeSpawnerForMob(EntityList.createEntityByIDFromName(dungeon.getCentralSpawnerMob(), world), false, null, world, spawnerPosCentral);
					} else {
						SpawnerFactory.createSimpleMultiUseSpawner(world, spawnerPosCentral, dungeon.getCentralSpawnerMob());
					}
				} catch(NullPointerException ex) {
					world.setBlockToAir(spawnerPosCentral);
				}
			}
			
			for(BlockPos p : gridPositions) {
				BlockPos spawnerPos = new BlockPos(p.getX(), spawnerY, p.getZ());
				
				try {
					if(dungeon.spawnersAreSingleUse()) {
						SpawnerFactory.placeSpawnerForMob(EntityList.createEntityByIDFromName(dungeon.getSpawnerMob(), world), false, null, world, spawnerPos);
					} else {
						SpawnerFactory.createSimpleMultiUseSpawner(world, spawnerPos, dungeon.getSpawnerMob());
					}
				} catch(NullPointerException ex) {
					world.setBlockToAir(spawnerPos);
				}
			}
		}
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		//UNUSED HERE, it would place blocks above the nether
	}

}
