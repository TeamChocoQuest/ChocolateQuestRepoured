package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.factories.GearedMobFactory;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVolcano;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartCover;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartPlateau;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.spiral.StrongholdBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.StairCaseHelper.EStairSection;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfoLootChest;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class GeneratorVolcano extends AbstractDungeonGenerator<DungeonVolcano> {

	// GENERATION TIME TOTAL: ~15-30 seconds
	/**
	 * Generate: Given height, given base radius, given top inner radius
	 * steepness: In %
	 * level begins at 0!!
	 * DONE: Outer Radius -> make new function, current function dows not really work out
	 * Outer Radius --> y=(level * steepness) * level^2 --> y = steepness *level^3 --> RADIUS = baseRAD - (level/steepness)^1/3
	 * Inner Radius --> given, OR if (OuterRadius - (maxHeight - currHeight)) > given && (OuterRadius - (maxHeight - currHeight)) < maxInnerRad then use
	 * (OuterRadius - (maxHeight - currHeight))
	 * Block placing: generate circles. Per outer layer, the probability to place a block is reduced, same for height. A block can only be placed if it
	 * has a support block
	 * probability for blocks: max rad +1: 0% minRad -1: 100% --> P(x) = 1- [ (x-MIN)/(MAX-MIN) -steepNess * level] x = radius of block -> distance to
	 * center - innerRadius
	 * 
	 * 
	 * Config Values:
	 * > Steepness
	 * > minRadius
	 * > maxHeight/topY
	 * 
	 * Calculate first: minY in affected region
	 * difference between minY and topY --> height
	 * 
	 * then: calculate base radius
	 * baseRad = minRadius + ((maxHeight+1)/steepness)^1/3
	 *
	 *
	 * MOVE CALCULATION OF BASERADIUS TO THE DUNGEON OBJECT!!!! -> NO, the maxHeight can be different as it is a random value....
	 * 
	 */

	private int baseRadius = 1;
	private int maxHeight = 10;
	private int minRadius = 1;
	private int minY = 1;
	private int entranceDistToWall = 10;
	private double steepness = 0.0D;
	private List<BlockPos> spawnersNChestsOnPath = new ArrayList<>();
	private BlockPos centerLoc = null;
	private BlockPos entranceStartPos = null;
	private EStairSection entranceDirection = null;

	public GeneratorVolcano(World world, BlockPos pos, DungeonVolcano dungeon) {
		super(world, pos.down(20), dungeon);

		this.maxHeight = DungeonGenUtils.randomBetween(dungeon.getMinHeight(), dungeon.getMaxHeight()) + 20;
		this.minRadius = dungeon.getInnerRadius();
		this.steepness = dungeon.getSteepness();

		this.baseRadius = this.minRadius * 2 + (int) Math.cbrt(((double) this.maxHeight + 60.0D) / this.steepness);
	}

	// DONE: Lower ore gen
	// DONE: add noise in crater like in new version that is too slow

	@Override
	public void preProcess() {
		// X Y Z mark the C E N T E R / Middle of the crater!!!!

		this.centerLoc = this.pos;
	}

	private void calculateNextStairDirection(int yStairCase, int wideness) {
		switch (this.entranceDirection) {
		case EAST:
		case EAST_SEC:
			this.entranceStartPos = new BlockPos(this.centerLoc.getX(), yStairCase, this.centerLoc.getZ()).add(wideness, 0, 0);
			break;
		case NORTH:
		case NORTH_SEC:
			this.entranceStartPos = new BlockPos(this.centerLoc.getX(), yStairCase, this.centerLoc.getZ()).add(0, 0, -wideness);
			break;
		case SOUTH:
		case SOUTH_SEC:
			this.entranceStartPos = new BlockPos(this.centerLoc.getX(), yStairCase, this.centerLoc.getZ()).add(0, 0, wideness);
			break;
		case WEST:
		case WEST_SEC:
			this.entranceStartPos = new BlockPos(this.centerLoc.getX(), yStairCase, this.centerLoc.getZ()).add(-wideness, 0, 0);
			break;
		default:
			break;

		}
	}

	@Override
	public void buildStructure() {
		// System.out.println("Calculating minY...");
		// 1) Calculate MinY
		// 2) Calculate the base radius
		// 4) calculate all block positions
		// 5) Create a new SimpleThread that places all blocks
		// 6) Place cover blocks -> calculate positions and let the thread place the blocks
		// 7) Calculate the blocks for the spire
		// 8) Build the dungeon

		// System.out.println("Creating lists...");
		List<BlockPos> blockList = new ArrayList<BlockPos>();
		List<BlockPos> pillarCenters = new ArrayList<BlockPos>();
		// int lowYMax = y + (new Double(0.1 * this.maxHeight).intValue());
		this.minY = this.pos.getY();// this.getMinY(this.centerLoc, this.baseRadius, world) /*- (new Double(0.1 * maxHeight).intValue())*/;
		int lowYMax = this.minY + (new Double(0.1 * this.maxHeight).intValue());
		int rMax = (int) (baseRadius * 4 + dungeon.getMaxHoleSize());
		final int r = rMax / 2;
		BlockPos referenceLoc = this.centerLoc.subtract(new Vec3i(r, this.centerLoc.getY(), r));
		IBlockState[][][] blocks = new IBlockState[rMax][256][rMax];
		// DONE: indexes CAN be negative -> recalculate coordinates
		// DONE: Rewrite ore gen code
		// DONE: Rewrite hole gen code
		// TODO: Merge all 3 for y(for x(for z))) loops

		// int yMax = ((y + this.maxHeight) < 256 ? this.maxHeight : (255 - y));
		int yMax = ((this.minY + this.maxHeight) < 256 ? this.maxHeight : (255 - this.minY));

		if (this.dungeon.doBuildSupportPlatform()) {
			this.dungeonGenerator.add(new DungeonPartPlateau(this.world, this.dungeonGenerator, this.pos.getX() - r, this.pos.getZ() - r, this.pos.getX() + r, this.minY + 1, this.pos.getZ() + r, this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock(), 8));
		}

		// Upper volcano part
		for (int iY = 0; iY < yMax; iY++) {
			// RADIUS = baseRAD - (level/steepness)^1/3
			int radiusOuter = this.baseRadius - (int) Math.cbrt(((double) iY + 60.0D) / this.steepness);
			int innerRadius = this.minRadius; // DONE calculate minRadius

			for (int iX = -radiusOuter; iX <= radiusOuter; iX++) {
				for (int iZ = -radiusOuter; iZ <= radiusOuter; iZ++) {
					// First check if it is within the base radius...
					if (DungeonGenUtils.isInsideCircle(iX, iZ, radiusOuter)) {
						// If it is at the bottom and also inside the inner radius -> lava
						if (!DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius)) {
							// Else it is a wall block
							// SO now we decide what the wall is gonna be...
							if (DungeonGenUtils.PercentageRandom(this.dungeon.getLavaChance(), this.random.nextLong()) && !DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius + 2, this.centerLoc)) {
								// It is lava :D
								blocks[iX + r][iY + this.minY][iZ + r] = dungeon.getLavaBlock();
							} else if (DungeonGenUtils.PercentageRandom(this.dungeon.getMagmaChance(), this.random.nextLong())) {
								// It is magma
								blocks[iX + r][iY + this.minY][iZ + r] = dungeon.getMagmaBlock();
							} else {
								// It is stone or ore
								if (DungeonGenUtils.getIntBetweenBorders(0, 101) > 95) {
									blockList.addAll(this.getSphereBlocks(new BlockPos(iX + this.pos.getX(), iY + this.minY, iZ + this.pos.getZ()), this.random.nextInt(3) + 1));
									for (BlockPos bp : this.getSphereBlocks(new BlockPos(iX + this.pos.getX(), iY + this.minY, iZ + this.pos.getZ()), this.random.nextInt(3) + 1)) {
										BlockPos v = bp.subtract(referenceLoc);
										if (bp.getY() < 256) {
											try {
												blocks[v.getX()][bp.getY()][v.getZ()] = this.dungeon.getUpperMainBlock();
											} catch (ArrayIndexOutOfBoundsException ex) {
												// IGNORE
											}
										}
									}
								} else {
									blockList.add(new BlockPos(iX + this.pos.getX(), iY + this.minY, iZ + this.pos.getZ()));
									blocks[iX + r][iY + this.minY][iZ + r] = this.dungeon.getUpperMainBlock();
								}
							}
						} else {
							blocks[iX + r][iY + this.minY][iZ + r] = Blocks.AIR.getDefaultState();
						}
					}
				}
			}
		}

		// Lower "cave" part
		int[] radiusArr = new int[(int) (lowYMax * 0.9)];
		for (int iY = 0; iY <= lowYMax + 5; iY++) {
			int radius = new Double(Math.sqrt(-1.0 * new Double((iY - lowYMax) / (10.0 * this.dungeon.getSteepness()))) + (double) this.minRadius).intValue();
			if (iY < radiusArr.length) {
				radiusArr[iY] = radius;
			}
			for (int iX = -radius - 2; iX <= radius + 2; iX++) {
				for (int iZ = -radius - 2; iZ <= radius + 2; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, radius, this.centerLoc)) {
						if (DungeonGenUtils.isInsideCircle(iX, iZ, (radius - 1), this.centerLoc)) {
							if (iY < 2) {
								// We're low enough, place lava
								blocks[iX + r][iY + 6][iZ + r] = dungeon.getLavaBlock();
							} else {
								// We're over the lava -> air
								blocks[iX + r][iY + 6][iZ + r] = Blocks.AIR.getDefaultState();
							}
						} else {
							// We are in the outer wall -> random spheres to make it more cave
							if (DungeonGenUtils.getIntBetweenBorders(0, 101) > 95) {
								for (BlockPos bp : this.getSphereBlocks(new BlockPos(iX + this.pos.getX(), iY + 6, iZ + this.pos.getZ()), this.random.nextInt(3) + 2)) {
									BlockPos v = bp.subtract(referenceLoc);
									int chanceForSecondary = new Double((this.dungeon.getMagmaChance() * 100.0D) * 2.0D).intValue();
									IBlockState block = DungeonGenUtils.getIntBetweenBorders(0, 101) >= (100 - chanceForSecondary) ? this.dungeon.getMagmaBlock() : this.dungeon.getLowerMainBlock();
									if (bp.getY() < 256) {
										try {
											blocks[v.getX()][bp.getY()][v.getZ()] = block;
										} catch (ArrayIndexOutOfBoundsException ex) {
											// IGNORE
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// Infamous nether staircase
		EStairSection currStairSection = StairCaseHelper.getRandomStartSection();
		this.entranceDirection = currStairSection.getSuccessor();
		if (this.dungeon.doBuildStairs()) {
			int yStairCase, stairRadius = 1;
			for (int i = -3; i < radiusArr.length; i++) {
				yStairCase = i >= 0 ? i + 7 : 7;
				stairRadius = i >= 0 ? radiusArr[i] : radiusArr[0];

				// Calculates the position of the entrance to the stronghold
				if (this.dungeon.doBuildDungeon() && i == 0) {
					this.entranceDistToWall = (radiusArr[i] / 3);
					int vecI = radiusArr[i] - this.entranceDistToWall;
					calculateNextStairDirection(yStairCase, vecI);
				}

				for (int iX = -stairRadius; iX <= stairRadius; iX++) {
					for (int iZ = -stairRadius; iZ <= stairRadius; iZ++) {
						// Pillars
						if (this.dungeon.doBuildDungeon() && i == -3 && StairCaseHelper.isPillarCenterLocation(iX, iZ, stairRadius)) {
							pillarCenters.add(new BlockPos(iX + r, yStairCase - 3, iZ + r));
						}
						// Stairwell -> check if it is in the volcano
						if (DungeonGenUtils.isInsideCircle(iX, iZ, stairRadius + 1, this.centerLoc) && !DungeonGenUtils.isInsideCircle(iX, iZ, stairRadius / 2, this.centerLoc)) {
							// Check that it is outside of the middle circle
							if (this.dungeon.doBuildStairs() && StairCaseHelper.isLocationFine(currStairSection, iX, iZ, stairRadius)) {
								BlockPos pos = new BlockPos(iX + this.pos.getX(), yStairCase, iZ + this.pos.getZ());
								blocks[iX + r][yStairCase][iZ + r] = dungeon.getRampBlock();
								// Spawners and chets, spawn only in a certain radius and only with 1% chance
								if (DungeonGenUtils.isInsideCircle(iX, iZ, (stairRadius / 2) + (stairRadius / 4) + (stairRadius / 6), this.centerLoc)) {
									if (new Random().nextInt(this.dungeon.getChestChance() + 1) >= (this.dungeon.getChestChance() - 1)) {
										this.spawnersNChestsOnPath.add(pos.add(0, 1, 0));
									}
								}

							}
						}
					}
				}
				currStairSection = currStairSection.getSuccessor();
			}
		}

		if (this.dungeon.isVolcanoDamaged()) {
			generateHoles(blockList, blocks, r);
		}

		if (this.dungeon.generateOres()) {
			this.generateOres(blockList, blocks, r);
		}

		if (this.dungeon.doBuildDungeon()) {
			for (BlockPos center : pillarCenters) {
				this.generatePillars(center, 2, lowYMax + 10, blocks, dungeon.getPillarBlock());
			}
		}

		String mobType = dungeon.getDungeonMob();
		if (mobType.equalsIgnoreCase(DungeonInhabitantManager.DEFAULT_INHABITANT_IDENT)) {
			mobType = DungeonInhabitantManager.getInhabitantDependingOnDistance(world, this.pos.getX(), this.pos.getZ()).getName();
		}

		// Generate parts for generation
		// lists.add(ExtendedBlockStatePart.split(referenceLoc, blocks, 32));
		List<AbstractBlockInfo> list = new ArrayList<>();
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				for (int k = 0; k < blocks[i][j].length; k++) {
					if (blocks[i][j][k] != null) {
						list.add(new BlockInfo(new BlockPos(i, j, k), blocks[i][j][k], null));
					}
				}
			}
		}
		this.dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, referenceLoc, list, new PlacementSettings(), mobType));

		// BlockPos lowerCorner = new BlockPos(x - (this.baseRadius * 2), 0, z - (this.baseRadius * 2));
		// BlockPos upperCorner = new BlockPos(2 * (this.baseRadius * 2), yMax + y, 2 * (this.baseRadius * 2));
		// TODO Add bosses
		// CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, lowerCorner, upperCorner, world, new
		// ArrayList<String>());
		// MinecraftForge.EVENT_BUS.post(event);

		// TIME
		// All: About 20 seconds

		if (this.dungeon.doBuildDungeon()) {
			final StrongholdBuilder entranceBuilder = new StrongholdBuilder(this, this.dungeonGenerator, this.entranceStartPos, this.entranceDistToWall, this.dungeon, this.entranceDirection.getAsSkyDirection(), world);
			entranceBuilder.generate(this.pos.getX(), this.pos.getZ(), mobType);
			this.dungeonGenerator.addAll(entranceBuilder.getStrongholdParts());
		}

		// Generates the stronghold

		// IMPORTANT: Entrance + Staircase: Same as original

		// 1) Build entrance
		// 2) figure out direction
		// 3) choose a random number of rooms for layer
		// 4) create a "map" that knows the rooms locations
		// 5) check via warshall algorithm
		// 6) choose place for random staircase
		// 7) if there are still layers to build, goto 3)
		// 9) if all layers all build, generate a final layer with one to three rooms with weapons / healing / food and a final hallway that leads to the boss chamber

		final ResourceLocation[] chestIDs = this.dungeon.getChestIDs();
		List<AbstractBlockInfo> lootChests = new ArrayList<>();
		for (BlockPos pos : this.spawnersNChestsOnPath) {
			if (this.random.nextBoolean()) {
				lootChests.add(new BlockInfoLootChest(pos.subtract(this.pos), chestIDs[this.random.nextInt(chestIDs.length)], EnumFacing.NORTH));
			}
		}
		this.dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, this.pos, lootChests, new PlacementSettings(), mobType));

		List<AbstractBlockInfo> mobSpawners = new ArrayList<>();
		int floor = this.spawnersNChestsOnPath.size();
		GearedMobFactory mobFactory = new GearedMobFactory(this.spawnersNChestsOnPath.size(), dungeon.getRampMob(), this.random);
		for (BlockPos pos : this.spawnersNChestsOnPath) {
			Block block = ModBlocks.SPAWNER;
			IBlockState state = block.getDefaultState();
			TileEntitySpawner spawner = (TileEntitySpawner) block.createTileEntity(world, state);
			int ec = 2 + this.random.nextInt(3);
			for (int i = 0; i < ec; i++) {
				Entity ent = mobFactory.getGearedEntityByFloor(floor, world);
				spawner.inventory.setStackInSlot(i, SpawnerFactory.getSoulBottleItemStackForEntity(ent));
			}
			NBTTagCompound data = spawner.writeToNBT(new NBTTagCompound());
			mobSpawners.add(new BlockInfo(pos.subtract(this.pos).add(0, 1, 0), state, data));
			floor--;
		}
		this.dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, this.pos, mobSpawners, new PlacementSettings(), mobType));
	}

	@Override
	public void postProcess() {
		if(this.dungeon.isCoverBlockEnabled()) {
			int rMax = (int) (this.baseRadius * 4 + this.dungeon.getMaxHoleSize());
			BlockPos startPos = this.pos.add((rMax / 2) + 8, 0, (rMax /2) + 8);
			BlockPos endPos = this.pos.add(-(rMax / 2) - 8, 0, -(rMax /2) - 8);
			BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
			BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
			this.dungeonGenerator.add(new DungeonPartCover(this.world, this.dungeonGenerator, pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getZ(), this.dungeon.getCoverBlock()));
		}
	}

	private List<BlockPos> getSphereBlocks(BlockPos center, int radius) {
		List<BlockPos> posList = new ArrayList<>();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					BlockPos p = center.add(x, y, z);
					if (DungeonGenUtils.isInsideSphere(p, center, radius)) {
						posList.add(p);
					}
				}
			}
		}
		return posList;
	}

	private void generateOres(List<BlockPos> blocks, IBlockState[][][] blockArray, int r) {
		Random rdm = new Random();

		List<Integer> usedIndexes = new ArrayList<>();
		Double divisor = new Double((double) this.dungeon.getOreChance() / 100.0D);
		for (int i = 0; i < (new Double(divisor * blocks.size()).intValue()); i++) {
			int blockIndex = rdm.nextInt(blocks.size());
			while (usedIndexes.contains(blockIndex)) {
				blockIndex = rdm.nextInt(blocks.size());
			}
			BlockPos p = blocks.get(blockIndex);
			BlockPos v = p.subtract(centerLoc);

			if (blockArray[v.getX() + r][p.getY()][v.getZ() + r] == null || blockArray[v.getX() + r][p.getY()][v.getZ() + r].getBlock() == Blocks.AIR) {
				continue;
			}
			IBlockState ore;
			int indx = this.dungeon.getOres().length;
			indx = rdm.nextInt(indx);
			ore = this.dungeon.getOres()[indx];
			blockArray[v.getX() + r][p.getY()][v.getZ() + r] = ore;
		}
	}

	private void generateHoles(List<BlockPos> blocks, IBlockState[][][] blockArray, int r) {
		Random rdm = new Random();
		// Makes random holes
		for (int holeCount = 0; holeCount < this.maxHeight * 1.5; holeCount++) {
			BlockPos center = blocks.get(rdm.nextInt(blocks.size()));

			int radius = DungeonGenUtils.randomBetween(2, this.dungeon.getMaxHoleSize());

			for (BlockPos p : this.getSphereBlocks(center, radius)) {
				BlockPos v = p.subtract(centerLoc);
				try {
					blockArray[v.getX() + r][p.getY()][v.getZ() + r] = Blocks.AIR.getDefaultState();
				} catch (ArrayIndexOutOfBoundsException ex) {
					// Ignore
				}
			}

		}
	}

	private void generatePillars(BlockPos centerAsIndexes, int radius, int height, IBlockState[][][] blocks, IBlockState pillarBlock) {
		for (int iY = 0; iY < height; iY++) {
			for (int iX = -radius; iX <= radius; iX++) {
				for (int iZ = -radius; iZ <= radius; iZ++) {
					try {
						blocks[iX + centerAsIndexes.getX()][iY + centerAsIndexes.getY()][iZ + centerAsIndexes.getZ()] = pillarBlock;
					} catch (ArrayIndexOutOfBoundsException ex) {
						continue;
					}
				}
			}
		}
	}

}
