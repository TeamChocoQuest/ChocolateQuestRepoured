package team.cqr.cqrepoured.world.structure.generation.generators.volcano;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.GearedMobFactory;
import team.cqr.cqrepoured.world.structure.generation.GenerationUtil;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonVolcano;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generators.LegacyDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.generators.stronghold.spiral.EntranceBuilderHelper;
import team.cqr.cqrepoured.world.structure.generation.generators.stronghold.spiral.StrongholdBuilder;
import team.cqr.cqrepoured.world.structure.generation.generators.volcano.StairCaseHelper.EStairSection;

public class GeneratorVolcano extends LegacyDungeonGenerator<DungeonVolcano> {

	private final int volcanoHeight;
	private final double steepness;
	private final int minRadius;

	private final int caveHeight;
	private final int caveDepth;

	private final EStairSection startStairSection = StairCaseHelper.getRandomStartSection();

	private final CQRWeightedRandom<BlockState> volcanoBlocks;
	private final CQRWeightedRandom<BlockState> volcanoBlocksWithLava;

	public GeneratorVolcano(ChunkGenerator world, BlockPos pos, DungeonVolcano dungeon, Random rand) {
		super(world, pos, dungeon, rand);
		
		int yTmp = world.getFirstFreeHeight(this.pos.getX(), this.pos.getZ(), Type.WORLD_SURFACE_WG);
		this.pos = new BlockPos(this.pos.getX(), yTmp, this.pos.getZ());

		this.volcanoHeight = DungeonGenUtils.randomBetween(dungeon.getMinHeight(), dungeon.getMaxHeight(), this.random);
		this.steepness = dungeon.getSteepness();
		this.minRadius = dungeon.getInnerRadius();

		this.caveHeight = (int) (this.volcanoHeight * 0.6D);
		this.caveDepth = 30;

		this.volcanoBlocks = this.dungeon.getVolcanoBlocks().copy();
		this.volcanoBlocksWithLava = this.dungeon.getVolcanoBlocks().copy();
		this.volcanoBlocksWithLava.add(new CQRWeightedRandom.WeightedObject<>(this.dungeon.getLavaBlock(), this.dungeon.getLavaWeight()));
	}

	@Override
	public void preProcess() {

	}

	@Override
	public void buildStructure() {
		final int[] outerRadiusArray = new int[this.volcanoHeight + this.caveDepth];
		final int[] innerRadiusArray = new int[this.volcanoHeight + this.caveDepth];
		//Calculates the inner and outer bounds for the base shape and the cave later
		this.calcInnerAndOutherRadii(outerRadiusArray, innerRadiusArray);

		//Biggest radius of the overall shape (not counting the added rougness via spheres)
		final int r = outerRadiusArray[0];
		//Location in the middle of the lava lake
		final BlockPos referenceLoc = this.pos.offset(-r, -this.caveDepth, -r);
		final DungeonPlacement dp = this.dungeonBuilder.getPlacement(referenceLoc);
		GenerationUtil.init(dp);
		final int sizeX = r * 2 + 1;
		final int sizeY = this.volcanoHeight + this.caveHeight + 2;
		final int sizeZ = r * 2 + 1;
		final List<BlockPos> spawnerAndChestList = new ArrayList<>();

		// basic volcano shape with air inside
		this.generateBaseShape(outerRadiusArray, innerRadiusArray, referenceLoc, r);

		// add spheres at the outer radius
		this.roughenOuterWalls(outerRadiusArray, innerRadiusArray, referenceLoc, r);

		// add lava and spheres at the inner radius
		this.roughenInnerWalls(innerRadiusArray, referenceLoc, r);
		
		// Holes
		this.damageBaseShape(referenceLoc, sizeX, sizeY, sizeZ);

		// Infamous nether staircase
		this.decorateInnerCave(innerRadiusArray, outerRadiusArray, referenceLoc, spawnerAndChestList, r);
		
		// Spawners and Chests
		this.generateSpawnersAndChests(spawnerAndChestList);

		// Stronghold
		this.generateStronghold(innerRadiusArray[0]);
	}

	protected void decorateInnerCave(int[] innerRadiusArray, int[] outerRadiusArray, BlockPos referenceLoc, final List<BlockPos> spawnerAndChestList, final int r) {
		if (this.dungeon.doBuildStairs()) {
			EStairSection stairSection = this.startStairSection;
			int sectionMinX = 0;
			int sectionMaxX = 0;
			int sectionMinZ = 0;
			int sectionMaxZ = 0;
			final int highestPlatformY = (int) (((this.caveHeight + this.caveDepth) * 0.9D) - 1);
			for (int iY = -1; iY <= highestPlatformY; iY++) {
				int y = Math.max(iY, 1);
				//Grab inner border of baseshape as our outer radius
				int outerStairRadius = innerRadiusArray[y];
				int innerStairRadius = outerStairRadius / 2;

				// First, reset the section mins and maxs, otherwise the result will be faulty
				sectionMinX = 0;
				sectionMaxX = 0;
				sectionMinZ = 0;
				sectionMaxZ = 0;

				for (int iX = -outerStairRadius; iX <= outerStairRadius; iX++) {
					for (int iZ = -outerStairRadius; iZ <= outerStairRadius; iZ++) {
						if (DungeonGenUtils.isInsideCircle(iX, iZ, outerStairRadius) && !DungeonGenUtils.isInsideCircle(iX, iZ, innerStairRadius) && StairCaseHelper.isLocationFine(stairSection, iX, iZ, outerStairRadius)) {
							//blocks[iX + r][y][iZ + r] = this.dungeon.getRampBlock();
							this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(iX + r, y, iZ + r), this.dungeon.getRampBlock());

							if (iX < sectionMinX) {
								sectionMinX = iX;
							}
							if (iX > sectionMaxX) {
								sectionMaxX = iX;
							}
							if (iZ < sectionMinZ) {
								sectionMinZ = iZ;
							}
							if (iZ > sectionMaxZ) {
								sectionMaxZ = iZ;
							}
							//TODO: Allow for some decorations?

							//TODO: Move magic numbers to config option
							if (DungeonGenUtils.isInsideCircle(iX, iZ, outerStairRadius - 2) && !DungeonGenUtils.isInsideCircle(iX, iZ, innerStairRadius + 2) && DungeonGenUtils.percentageRandom(this.dungeon.getChestChance(), this.random)) {
								spawnerAndChestList.add(new BlockPos(iX + r, y + 1, iZ + r));
							}
						}
					}
				}

				stairSection = stairSection.getSuccessor();
				
				//If we built the last section, let's see if we should construct the entrance tunnel
				if(iY == highestPlatformY) {
					//And after that generate a small tunnel to enter the volcano
					if (this.dungeon.constructEntranceTunnel()) {
						int segmentCenterX = (sectionMaxX - sectionMinX) / 2 + sectionMinX;
						int segmentCenterZ = (sectionMaxZ - sectionMinZ) / 2 + sectionMinZ;
						this.generateEntranceTunnel(stairSection, segmentCenterX, segmentCenterZ, outerRadiusArray, innerRadiusArray, highestPlatformY, referenceLoc, r);
					}
				}
			}
			//Finally, place the pillars
			this.generatePillars(innerRadiusArray, referenceLoc, r);
		}
	}

	protected void calcInnerAndOutherRadii(int[] outerRadiusArray, int[] innerRadiusArray) {
		final double d = Math.cbrt((this.volcanoHeight + 80.0D) / this.steepness);
		final int baseRadius = this.minRadius * 2 + (int) (d - Math.cbrt((80.0D - 20.0D) / this.steepness));
		for (int iY = -this.caveDepth; iY < this.volcanoHeight; iY++) {
			if (iY > -20) {
				outerRadiusArray[iY + this.caveDepth] = this.minRadius * 2 + (int) (d - Math.cbrt((iY + 80.0D) / this.steepness));
			} else {
				outerRadiusArray[iY + this.caveDepth] = baseRadius + (-20 - iY) / 5;
			}
			innerRadiusArray[iY + this.caveDepth] = this.minRadius + (int) Math.sqrt(((double) this.caveHeight - (double) iY) / (3000.0D * this.steepness));
		}
	}

	protected void generateEntranceTunnel(EStairSection stairSection, int segmentCenterX, int segmentCenterZ, int[] outerRadiusArray, int[] innerRadiusArray, int highestPlatformY, BlockPos referenceLoc, int r) {
//		System.out.println("Highestplatform y: " + highestPlatformY);
//		System.out.println("Cave depth: " + caveDepth);
//		System.out.println("Cave height: " + caveHeight);
//		System.out.println("Y: " + this.pos.getY());
		EStairSection stairSectionPrev = stairSection.getPredeccessor();
		if (stairSectionPrev != null) {
			Direction direction = stairSectionPrev.getAsSkyDirection();
			// direction = direction.rotateYCCW();
			switch (direction) {
			case EAST:
				segmentCenterX += 6;
				break;
			case NORTH:
				segmentCenterZ -= 6;
				break;
			case SOUTH:
				segmentCenterZ += 6;
				break;
			case WEST:
				segmentCenterX -= 6;
				break;
			default:
				break;
			}
			int endX = segmentCenterX;
			int endZ = segmentCenterZ;

			int tunnelLength = outerRadiusArray[highestPlatformY] - innerRadiusArray[highestPlatformY];
			tunnelLength *= 1.5D;
			// System.out.println("TL before modification: " + tunnelLength);
			switch (direction) {
			case EAST:
				endX += tunnelLength;
				break;
			case NORTH:
				endZ -= tunnelLength;
				break;
			case SOUTH:
				endZ += tunnelLength;
				break;
			case WEST:
				endX -= tunnelLength;
				break;
			default:
				break;
			}
			/*
			 * System.out.println("Direction: " + direction.name());
			 * System.out.println("px: " + segmentCenterX + "  pz: " + segmentCenterZ);
			 * System.out.println("ex: " + endX + "  ez: " + endZ);
			 * System.out.println("R: " + r);
			 */

			// First: air sphere around the entrance
			forEachSpherePosition(new BlockPos(endX, highestPlatformY + 3, endZ), 4 + this.random.nextInt(2), p -> {
				BlockState stateAt = this.dungeonBuilder.getLevel().getBlockState(referenceLoc.offset(p.offset(r, 0 , r)));
				if(stateAt != Blocks.AIR.defaultBlockState()) {
					this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(p.offset(r, 0 , r)), Blocks.AIR.defaultBlockState());
				}
			});

			// Second: Place the segments
			int segmentCount = tunnelLength / EntranceBuilderHelper.SEGMENT_LENGTH;
			BlockPos segmentPos = new BlockPos(endX, highestPlatformY - this.caveDepth, endZ);
			segmentPos = segmentPos.relative(direction.getOpposite(), (int) -Math.round(EntranceBuilderHelper.SEGMENT_LENGTH * 1.5D));

			for (int i = 0; i < segmentCount; i++) {
				EntranceBuilderHelper.buildEntranceSegment(this.pos.offset(segmentPos), this.dungeonBuilder, direction);

				segmentPos = segmentPos.relative(direction.getOpposite(), EntranceBuilderHelper.SEGMENT_LENGTH);
			}
		}
	}

	protected void generatePillars(int[] innerRadiusArray, BlockPos referenceLoc, int r) {
		this.generatePillar(new BlockPos(r + innerRadiusArray[0] / 2, 0, r), 2, (int) ((this.caveHeight + this.caveDepth) * 0.95D), referenceLoc, this.dungeon.getPillarBlock());
		this.generatePillar(new BlockPos(r - innerRadiusArray[0] / 2, 0, r), 2, (int) ((this.caveHeight + this.caveDepth) * 0.95D), referenceLoc, this.dungeon.getPillarBlock());
		this.generatePillar(new BlockPos(r, 0, r + innerRadiusArray[0] / 2), 2, (int) ((this.caveHeight + this.caveDepth) * 0.95D), referenceLoc, this.dungeon.getPillarBlock());
		this.generatePillar(new BlockPos(r, 0, r - innerRadiusArray[0] / 2), 2, (int) ((this.caveHeight + this.caveDepth) * 0.95D), referenceLoc, this.dungeon.getPillarBlock());		
	}

	protected void generateBaseShape(int[] outerRadiusArray, int[] innerRadiusArray, BlockPos referenceLoc, final int r) {
		for (int iY = 0; iY < this.volcanoHeight + this.caveDepth; iY++) {
			int outerRadius = outerRadiusArray[iY];
			int innerRadius = innerRadiusArray[iY];

			for (int iX = -outerRadius; iX <= outerRadius; iX++) {
				for (int iZ = -outerRadius; iZ <= outerRadius; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius)) {
						this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(iX + r, iY, iZ + r), Blocks.AIR.defaultBlockState());
					} else if (DungeonGenUtils.isInsideCircle(iX, iZ, outerRadius)) {
						if (!DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius + 2)) {
							this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(iX + r, iY, iZ + r), this.getRandomVolcanoBlockWithLava());
						} else {
							this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(iX + r, iY, iZ + r), this.getRandomVolcanoBlock());
						}
					}
				}
			}
		}
	}

	protected void roughenOuterWalls(int[] outerRadiusArray, int[] innerRadiusArray, BlockPos referenceLoc, final int r) {
		for (int iY = 0; iY < this.volcanoHeight + this.caveDepth; iY++) {
			int outerRadius = outerRadiusArray[iY];

			for (int iX = -outerRadius; iX <= outerRadius; iX++) {
				for (int iZ = -outerRadius; iZ <= outerRadius; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, outerRadius) && (!DungeonGenUtils.isInsideCircle(iX, iZ, outerRadius - 2) || (iY == this.volcanoHeight + this.caveDepth - 1 && !DungeonGenUtils.isInsideCircle(iX, iZ, innerRadiusArray[iY])))) {
						if (DungeonGenUtils.percentageRandom(0.05D, this.random)) {
							forEachSpherePosition(new BlockPos(iX, iY, iZ), 2 + this.random.nextInt(3), p -> {
								this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(p.getX() + r, p.getY(), p.getZ() + r), this.getRandomVolcanoBlockWithLava());
							});
						}
					}
				}
			}
		}
	}

	protected void roughenInnerWalls(int[] innerRadiusArray, BlockPos referenceLoc, final int r) {
		for (int iY = 0; iY < this.volcanoHeight + this.caveDepth; iY++) {
			int innerRadius = innerRadiusArray[iY];

			for (int iX = -innerRadius; iX <= innerRadius; iX++) {
				for (int iZ = -innerRadius; iZ <= innerRadius; iZ++) {
					if (iY < 2 && DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius)) {
						this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(iX + r, iY, iZ + r), this.dungeon.getLavaBlock());
					}

					if (DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius + 2) && !DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius)) {
						if (DungeonGenUtils.percentageRandom(0.05D, this.random)) {
							forEachSpherePosition(new BlockPos(iX, iY, iZ), 1 + this.random.nextInt(3), p -> {
								BlockState stateAt = this.dungeonBuilder.getLevel().getBlockState(referenceLoc.offset(p.getX() + r, p.getY(), p.getZ() + r));
								if(stateAt == Blocks.AIR.defaultBlockState()) {
									this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(p.getX() + r, p.getY(), p.getZ() + r), this.getRandomVolcanoBlock());
								}
							});
						}
					}
				}
			}
		}
		
	}

	@Override
	public void postProcess() {

	}
	
	protected void damageBaseShape(BlockPos referenceLoc, int sizeX, int sizeY, int sizeZ) {
		if (this.dungeon.isVolcanoDamaged()) {
			List<BlockPos> list = new ArrayList<>((int) (this.volcanoHeight * 1.6D));

			for (int i = 0; i < this.volcanoHeight * 1.6D; i++) {
				for (int j = 0; j < 100; j++) {
					int x = this.random.nextInt(sizeX);
					int y = this.random.nextInt(sizeY);
					int z = this.random.nextInt(sizeZ);

					if(this.dungeonBuilder.getLevel().getBlockState(referenceLoc.offset(x, y, z)) != Blocks.AIR.defaultBlockState()) {
						list.add(new BlockPos(x, y, z));
						break;
					}
				}
			}

			for (BlockPos pos : list) {
				forEachSpherePosition(pos, DungeonGenUtils.randomBetween(2, this.dungeon.getMaxHoleSize(), this.random), p -> {
					BlockState atState = this.dungeonBuilder.getLevel().getBlockState(referenceLoc.offset(p));
					if (atState != null && atState != Blocks.AIR.defaultBlockState()) {
						this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(p), Blocks.AIR.defaultBlockState());
					}
				});
			}
		}
	}

	protected void generatePillar(BlockPos pos, int radius, int height, BlockPos referenceLoc, BlockState pillarBlock) {
		for (int iY = 0; iY < height; iY++) {
			for (int iX = -radius; iX <= radius; iX++) {
				for (int iZ = -radius; iZ <= radius; iZ++) {
					try {
						this.dungeonBuilder.getLevel().setBlockState(referenceLoc.offset(pos).offset(iX, iY, iZ), pillarBlock);
					} catch (ArrayIndexOutOfBoundsException ex) {
						// ignore
					}
				}
			}
		}
	}

	protected void generateSpawnersAndChests(List<BlockPos> spawnerAndChestList) {
		if (!spawnerAndChestList.isEmpty()) {
			ResourceLocation[] lootTables = this.dungeon.getChestIDs();
			GearedMobFactory mobFactory = new GearedMobFactory(spawnerAndChestList.size(), this.dungeon.getRampMob(), this.random);
			int floor = spawnerAndChestList.size();

			for (BlockPos pos : spawnerAndChestList) {
				if (this.random.nextBoolean()) {
					GenerationUtil.setLootChest(this.dungeonBuilder.getLevel(), pos, lootTables[this.random.nextInt(lootTables.length)], Direction.Plane.HORIZONTAL.getRandomDirection(this.random));
				}

				int entityCount = 2 + this.random.nextInt(3);
				List<Entity> entityList = new ArrayList<>(entityCount);
				for (int i = 0; i < entityCount; i++) {
					entityList.add(mobFactory.getGearedEntityByFloor(floor, this.dungeonBuilder.getEntityFactory()));
				}
				GenerationUtil.setSpawner(this.dungeonBuilder.getLevel(), pos.above(), entityList);
				floor--;
			}
		}
	}
	
	protected void generateStronghold(int radius) {
		if (this.dungeon.doBuildStronghold()) {
			EStairSection entranceDirection = this.startStairSection.getSuccessor();
			int entranceDistToWall = radius / 3;
			int wideness = radius - entranceDistToWall;
			BlockPos entranceStartPos;

			switch (entranceDirection) {
			case EAST:
			case EAST_SEC:
				entranceStartPos = this.pos.offset(wideness, 1 - this.caveDepth, 0);
				break;
			case NORTH:
			case NORTH_SEC:
				entranceStartPos = this.pos.offset(0, 1 - this.caveDepth, -wideness);
				break;
			case SOUTH:
			case SOUTH_SEC:
				entranceStartPos = this.pos.offset(0, 1 - this.caveDepth, wideness);
				break;
			case WEST:
			case WEST_SEC:
			default:
				entranceStartPos = this.pos.offset(-wideness, 1 - this.caveDepth, 0);
				break;
			}

			StrongholdBuilder entranceBuilder = new StrongholdBuilder(this.dungeonBuilder, entranceStartPos, entranceDistToWall, this.dungeon, entranceDirection.getAsSkyDirection(), this.dungeonBuilder, this.random);
			entranceBuilder.generate();
		}
	}

	public static void forEachSpherePosition(BlockPos center, int radius, Consumer<BlockPos> action) {
		for (BlockPos p : BlockPos.betweenClosed(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius)) {
			if (DungeonGenUtils.isInsideSphere(p.getX() - center.getX(), p.getY() - center.getY(), p.getZ() - center.getZ(), radius)) {
				action.accept(p);
			}
		}
	}

	protected BlockState getRandomVolcanoBlock() {
		BlockState state = this.volcanoBlocks.next(this.random);
		return state != null ? state : Blocks.STONE.defaultBlockState();
	}

	protected BlockState getRandomVolcanoBlockWithLava() {
		BlockState state = this.volcanoBlocksWithLava.next(this.random);
		return state != null ? state : Blocks.STONE.defaultBlockState();
	}

}
