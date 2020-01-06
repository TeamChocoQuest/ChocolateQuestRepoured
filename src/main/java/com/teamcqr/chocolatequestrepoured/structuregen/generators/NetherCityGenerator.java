package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.ClassicNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.MinecraftForge;

public class NetherCityGenerator implements IDungeonGenerator {

	private ClassicNetherCity dungeon;

	// TODO: Dont make this a Set, sets are slow as they need to calculate the hash keys every time you add something to them...
	private Set<BlockPos> gridPositions = new HashSet<>();
	private Set<BlockPos> bridgeBuilderStartPositionsX = new HashSet<>();
	private Set<BlockPos> bridgeBuilderStartPositionsZ = new HashSet<>();

	private Set<BlockPos> bridgeBlocks = new HashSet<>();
	private Set<BlockPos> lavaBlocks = new HashSet<>();

	private int minX;
	private int maxX;
	private int minZ;
	private int maxZ;

	// private static int tunnelHeight = 3;

	public NetherCityGenerator(ClassicNetherCity dungeon) {
		this.dungeon = dungeon;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// Calculate the grid for the buildings
		int rowsX = this.dungeon.getXRows();
		int rowsZ = this.dungeon.getZRows();

		rowsX /= 2;
		rowsZ /= 2;

		this.minX = x - (rowsX * this.dungeon.getDistanceBetweenBuildingCenters());
		this.minZ = z - (rowsZ * this.dungeon.getDistanceBetweenBuildingCenters());
		this.maxX = x + (rowsX * this.dungeon.getDistanceBetweenBuildingCenters());
		this.maxZ = z + (rowsZ * this.dungeon.getDistanceBetweenBuildingCenters());

		// This constructs the grid around the center
		for (int iX = -rowsX; iX <= rowsX; iX++) {
			for (int iZ = -rowsZ; iZ <= rowsZ; iZ++) {

				boolean noAddFlag = false;
				if (this.dungeon.centralBuildingIsSpecial() && iX == 0 && iZ == 0) {
					noAddFlag = true;
				}

				BlockPos p = new BlockPos(x + (iX * this.dungeon.getDistanceBetweenBuildingCenters()), y, z + (iZ * this.dungeon.getDistanceBetweenBuildingCenters()));
				if (!noAddFlag) {
					this.gridPositions.add(p);
				}
				// Bridge starter positions, in total there will be rowsX +rowsZ -1 bridges
				if (iX == 0) {
					this.bridgeBuilderStartPositionsZ.add(p);
				}
				if (iZ == 0) {
					this.bridgeBuilderStartPositionsX.add(p);
				}
			}
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// Dig out the big air pocket or the small ones
		if (this.dungeon.makeSpaceForBuildings()) {
			/*
			 * if(dungeon.useSingleAirPocketsForHouses()) {
			 * for(BlockPos pocketCenter : gridPositions) {
			 * BlockPos cLower = new BlockPos(pocketCenter.getX() - dungeon.getLongestSide() /2, y +1, pocketCenter.getZ() - dungeon.getLongestSide());
			 * BlockPos cUpper = new BlockPos(pocketCenter.getX() + dungeon.getLongestSide() /2, y +dungeon.getCaveHeight(), pocketCenter.getZ() + dungeon.getLongestSide());
			 * 
			 * PlateauBuilder pB = new PlateauBuilder();
			 * pB.makeRandomBlob(new Random(), dungeon.getAirPocketBlock(), cLower, cUpper, WorldDungeonGenerator.getSeed(world, pocketCenter.getX(), pocketCenter.getZ()), world);
			 * }
			 * } else {
			 */
			BlockPos cLower = new BlockPos(this.minX, y + 1, this.minZ).add(-this.dungeon.getDistanceBetweenBuildingCenters(), 0, -this.dungeon.getDistanceBetweenBuildingCenters());
			;
			BlockPos cUpper = new BlockPos(this.maxX, y + this.dungeon.getCaveHeight(), this.maxZ).add(this.dungeon.getDistanceBetweenBuildingCenters() * 0.1, 0, this.dungeon.getDistanceBetweenBuildingCenters() * 0.05);

			PlateauBuilder pB = new PlateauBuilder();
			pB.makeRandomBlob(new Random(), this.dungeon.getAirPocketBlock(), cLower, cUpper, WorldDungeonGenerator.getSeed(world, this.minX, this.maxZ), world);
			// }
		}

		// Build the roads / bridges and the floors
		for (BlockPos lavaPos : BlockPos.getAllInBox(this.minX - this.dungeon.getDistanceBetweenBuildingCenters(), y, this.minZ - this.dungeon.getDistanceBetweenBuildingCenters(),
				this.maxX + this.dungeon.getDistanceBetweenBuildingCenters(), y, this.maxZ + this.dungeon.getDistanceBetweenBuildingCenters())) {
			this.lavaBlocks.add(lavaPos);
		}
		// Bridges from south to north
		for (BlockPos pX : this.bridgeBuilderStartPositionsX) {
			for (int iZ = this.minZ; iZ <= this.maxZ; iZ++) {
				BlockPos pC = new BlockPos(pX.getX(), pX.getY(), iZ);
				BlockPos pCE = pC.east();
				BlockPos pCW = pC.west();
				this.bridgeBlocks.add(pC);
				this.bridgeBlocks.add(pCE);
				this.bridgeBlocks.add(pCW);

				// Tunnels if not big air pocket
				/*
				 * if(dungeon.makeSpaceForBuildings() && dungeon.useSingleAirPocketsForHouses()) {
				 * for(int n = 1; n <= tunnelHeight; n++) {
				 * world.setBlockToAir(pC.up(n));
				 * world.setBlockToAir(pCE.up(n));
				 * world.setBlockToAir(pCW.up(n));
				 * }
				 * }
				 */
			}
		}
		// Bridges from west to east
		for (BlockPos pZ : this.bridgeBuilderStartPositionsZ) {
			for (int iX = this.minX; iX <= this.maxX; iX++) {
				BlockPos pC = new BlockPos(iX, pZ.getY(), pZ.getZ());
				BlockPos pCN = pC.north();
				BlockPos pCS = pC.south();
				this.bridgeBlocks.add(pC);
				this.bridgeBlocks.add(pCN);
				this.bridgeBlocks.add(pCS);

				// Tunnels if not big air pocket
				/*
				 * if(dungeon.makeSpaceForBuildings() && dungeon.useSingleAirPocketsForHouses()) {
				 * for(int n = 1; n <= tunnelHeight; n++) {
				 * world.setBlockToAir(pC.up(n));
				 * world.setBlockToAir(pCN.up(n));
				 * world.setBlockToAir(pCS.up(n));
				 * }
				 * }
				 */
			}
		}

		for (BlockPos p : this.lavaBlocks) {
			world.setBlockState(p, this.dungeon.getFloorBlock().getDefaultState());
		}
		for (BlockPos p : this.bridgeBlocks) {
			world.setBlockState(p, this.dungeon.getBridgeBlock().getDefaultState());
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		// Place the buildings
		// Random rdm = new Random();

		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);

		CQStructure centralStructure = null;
		if (this.dungeon.centralBuildingIsSpecial()) {
			// DONE: Choose a central building, figure out the size, then build the platform and then the building

			/*
			 * if(dungeon.getCentralBuildingFolder().exists() && dungeon.getCentralBuildingFolder().isDirectory() && dungeon.getCentralBuildingFolder().listFiles().length > 0) {
			 * centralStructure = new CQStructure(dungeon.getCentralBuildingFolder().listFiles()[rdm.nextInt(dungeon.getCentralBuildingFolder().listFiles().length)], dungeon, chunk.x, chunk.z, dungeon.isProtectedFromModifications());
			 * } else if(dungeon.getCentralBuildingFolder().exists() && dungeon.getCentralBuildingFolder().isFile()) {
			 * centralStructure = new CQStructure(dungeon.getCentralBuildingFolder(), dungeon, chunk.x, chunk.z, dungeon.isProtectedFromModifications());
			 * }
			 */
			if (this.dungeon.getRandomCentralBuilding() != null) {
				centralStructure = new CQStructure(this.dungeon.getRandomCentralBuilding(), this.dungeon, chunk.x, chunk.z, this.dungeon.isProtectedFromModifications());
			}

			if (centralStructure != null) {
				BlockPos cL = new BlockPos(x - (centralStructure.getSizeX() / 2 + 2), y, z - (centralStructure.getSizeZ() / 2 + 2));
				BlockPos cU = cL.add(centralStructure.getSizeX() + 4, 0, centralStructure.getSizeZ() + 4);
				BlockPos.getAllInBox(cL, cU).forEach(new Consumer<BlockPos>() {

					@Override
					public void accept(BlockPos t) {
						world.setBlockState(t, NetherCityGenerator.this.dungeon.getBridgeBlock().getDefaultState());
					}
				});

				centralStructure.placeBlocksInWorld(world, new BlockPos(x, y + 1, z), settings, EPosType.CENTER_XZ_LAYER);
			}
		}
		CQStructure structure = null;
		// int filesInFolder = dungeon.getBuildingFolder().exists() && dungeon.getBuildingFolder().isDirectory() ? dungeon.getBuildingFolder().listFiles().length : -1;
		List<String> bosses = new ArrayList<>();
		for (BlockPos centerPos : this.gridPositions) {
			// DONE: Choose a building, figure out the size, then build the platform and then the building

			/*
			 * if(dungeon.getBuildingFolder().exists() && dungeon.getBuildingFolder().isDirectory() && filesInFolder > 1) {
			 * structure = new CQStructure(dungeon.getBuildingFolder().listFiles()[rdm.nextInt(filesInFolder)], dungeon, chunk.x, chunk.z, dungeon.isProtectedFromModifications());
			 * } else if(dungeon.getBuildingFolder().exists() && dungeon.getBuildingFolder().isFile()) {
			 * structure = new CQStructure(dungeon.getBuildingFolder(), dungeon, chunk.x, chunk.z, dungeon.isProtectedFromModifications());
			 * }
			 */
			if (this.dungeon.getRandomBuilding() != null) {
				structure = new CQStructure(this.dungeon.getRandomBuilding(), this.dungeon, chunk.x, chunk.z, this.dungeon.isProtectedFromModifications());
			}

			if (structure != null) {
				BlockPos cL = centerPos.subtract(new Vec3i(structure.getSizeX() / 2 + 2, 0, structure.getSizeZ() / 2 + 2));
				BlockPos cU = centerPos.add(structure.getSizeX() / 2 + 2, 0, structure.getSizeZ() / 2 + 2);
				BlockPos.getAllInBox(cL, cU).forEach(new Consumer<BlockPos>() {

					@Override
					public void accept(BlockPos t) {
						world.setBlockState(t, NetherCityGenerator.this.dungeon.getBridgeBlock().getDefaultState());
					}
				});
				for(UUID id : structure.getBossIDs()) {
					bosses.add(id.toString());
				}
				structure.placeBlocksInWorld(world, centerPos.add(0, 1, 0), settings, EPosType.CENTER_XZ_LAYER);
			}
		}
		BlockPos posLower = new BlockPos(this.minX - this.dungeon.getDistanceBetweenBuildingCenters(), y, this.minZ - this.dungeon.getDistanceBetweenBuildingCenters());
		BlockPos posUpper = new BlockPos(this.maxX + this.dungeon.getDistanceBetweenBuildingCenters(), y + this.dungeon.getCaveHeight(), this.maxZ + this.dungeon.getDistanceBetweenBuildingCenters());

		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, posLower, posUpper.subtract(posLower), world, bosses);
		if (centralStructure != null) {
			event.setShieldCorePosition(centralStructure.getShieldCorePosition());
		}
		MinecraftForge.EVENT_BUS.post(event);
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		// UNUSED HERE
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// Maybe place some ghast spawners over the city and a nether dragon? -> Complete factory first
		if (this.dungeon.placeSpawnersAboveBuildings()) {
			int spawnerY = y + (new Double(this.dungeon.getCaveHeight() * 0.9).intValue());

			// DONE: Place CQ spawners !!!
			if (this.dungeon.centralBuildingIsSpecial()) {
				BlockPos spawnerPosCentral = new BlockPos(x, spawnerY, z);
				try {
					if (this.dungeon.centralSpawnerIsSingleUse()) {
						SpawnerFactory.placeSpawner(new Entity[] { EntityList.createEntityByIDFromName(this.dungeon.getCentralSpawnerMob(), world) }, false, null, world, spawnerPosCentral);
					} else {
						SpawnerFactory.createSimpleMultiUseSpawner(world, spawnerPosCentral, this.dungeon.getCentralSpawnerMob());
					}
				} catch (NullPointerException ex) {
					world.setBlockToAir(spawnerPosCentral);
				}
			}

			for (BlockPos p : this.gridPositions) {
				BlockPos spawnerPos = new BlockPos(p.getX(), spawnerY, p.getZ());

				try {
					if (this.dungeon.spawnersAreSingleUse()) {
						SpawnerFactory.placeSpawner(new Entity[] { EntityList.createEntityByIDFromName(this.dungeon.getSpawnerMob(), world) }, false, null, world, spawnerPos);
					} else {
						SpawnerFactory.createSimpleMultiUseSpawner(world, spawnerPos, this.dungeon.getSpawnerMob());
					}
				} catch (NullPointerException ex) {
					world.setBlockToAir(spawnerPos);
				}
			}
		}
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		// UNUSED HERE, it would place blocks above the nether
	}

}
