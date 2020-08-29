package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.factories.GearedMobFactory;
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
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfoSpawner;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.WeightedItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class GeneratorVolcano extends AbstractDungeonGenerator<DungeonVolcano> {

	private final int volcanoHeight;
	private final double steepness;
	private final int minRadius;

	private final int caveHeight;
	private final int caveDepth;

	private final EStairSection startStairSection = StairCaseHelper.getRandomStartSection();

	private final List<WeightedItem<IBlockState>> volcanoBlocks;
	private final int volcanoBlocksTotalWeight;
	private final List<WeightedItem<IBlockState>> volcanoBlocksWithLava;
	private final int volcanoBlocksTotalWeightWithLava;

	public GeneratorVolcano(World world, BlockPos pos, DungeonVolcano dungeon) {
		super(world, pos, dungeon);

		this.volcanoHeight = DungeonGenUtils.randomBetween(dungeon.getMinHeight(), dungeon.getMaxHeight());
		this.steepness = dungeon.getSteepness();
		this.minRadius = dungeon.getInnerRadius();

		this.caveHeight = (int) (this.volcanoHeight * 0.6D);
		this.caveDepth = 30;

		this.volcanoBlocks = new ArrayList<>(this.dungeon.getVolcanoBlocks());
		this.volcanoBlocksTotalWeight = WeightedRandom.getTotalWeight(this.volcanoBlocks);
		this.volcanoBlocksWithLava = new ArrayList<>(this.dungeon.getVolcanoBlocks());
		this.volcanoBlocksWithLava.add(new WeightedItem<>(this.dungeon.getLavaBlock(), this.dungeon.getLavaWeight()));
		this.volcanoBlocksTotalWeightWithLava = WeightedRandom.getTotalWeight(this.volcanoBlocksWithLava);
	}

	@Override
	public void preProcess() {

	}

	@Override
	public void buildStructure() {
		int[] outerRadiusArray = new int[this.volcanoHeight + this.caveDepth];
		int[] innerRadiusArray = new int[this.volcanoHeight + this.caveDepth];
		double d = Math.cbrt((this.volcanoHeight + 80.0D) / this.steepness);
		int baseRadius = this.minRadius * 2 + (int) (d - Math.cbrt((80.0D - 20.0D) / this.steepness));
		for (int iY = -this.caveDepth; iY < this.volcanoHeight; iY++) {
			if (iY > -20) {
				outerRadiusArray[iY + this.caveDepth] = this.minRadius * 2 + (int) (d - Math.cbrt((iY + 80.0D) / this.steepness));
			} else {
				outerRadiusArray[iY + this.caveDepth] = baseRadius + (-20 - iY) / 5;
			}
			innerRadiusArray[iY + this.caveDepth] = this.minRadius + (int) Math.sqrt(((double) this.caveHeight - (double) iY) / (3000.0D * this.steepness));
		}

		final int r = outerRadiusArray[0];
		BlockPos referenceLoc = this.pos.add(-r, -this.caveDepth, -r);
		IBlockState[][][] blocks = new IBlockState[r * 2 + 1][this.volcanoHeight + this.caveDepth + 2][r * 2 + 1];
		List<BlockPos> spawnerAndChestList = new ArrayList<>();

		// Support platform
		if (this.dungeon.doBuildSupportPlatform()) {
			this.dungeonGenerator.add(new DungeonPartPlateau(this.world, this.dungeonGenerator, this.pos.getX() - r, this.pos.getZ() - r, this.pos.getX() + r, this.pos.getY() - this.caveDepth, this.pos.getZ() + r, this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock(), 8));
		}

		// Volcano part
		for (int iY = 0; iY < this.volcanoHeight + this.caveDepth; iY++) {
			int outerRadius = outerRadiusArray[iY];
			int innerRadius = innerRadiusArray[iY];

			for (int iX = -outerRadius; iX <= outerRadius; iX++) {
				for (int iZ = -outerRadius; iZ <= outerRadius; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, outerRadius) && !DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius)) {
						if (DungeonGenUtils.percentageRandom(0.15D)) {
							this.forEachSpherePosition(new BlockPos(iX, iY, iZ), 1 + this.random.nextInt(3), p -> {
								try {
									if (blocks[p.getX() + r][p.getY()][p.getZ() + r] == null) {
										if (!DungeonGenUtils.isInsideCircle(p.getX(), p.getZ(), innerRadius + 2)) {
											blocks[p.getX() + r][p.getY()][p.getZ() + r] = this.getRandomVolcanoBlockWithLava();
										} else {
											blocks[p.getX() + r][p.getY()][p.getZ() + r] = this.getRandomVolcanoBlock();
										}
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									// ignore
								}
							});
						} else if (!DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius + 2)) {
							blocks[iX + r][iY][iZ + r] = this.getRandomVolcanoBlockWithLava();
						} else {
							blocks[iX + r][iY][iZ + r] = this.getRandomVolcanoBlock();
						}
					}
				}
			}
		}

		// Holes
		this.generateHoles(blocks);

		// Cave part
		for (int iY = 0; iY < this.volcanoHeight + this.caveDepth; iY++) {
			int innerRadius = innerRadiusArray[iY];

			for (int iX = -innerRadius; iX <= innerRadius; iX++) {
				for (int iZ = -innerRadius; iZ <= innerRadius; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius)) {
						if (iY < 2) {
							blocks[iX + r][iY][iZ + r] = this.dungeon.getLavaBlock();
						} else {
							blocks[iX + r][iY][iZ + r] = Blocks.AIR.getDefaultState();
						}

						if (!DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius - 1) && DungeonGenUtils.percentageRandom(5)) {
							this.forEachSpherePosition(new BlockPos(iX, iY, iZ), 2 + this.random.nextInt(3), p -> {
								try {
									if (blocks[p.getX() + r][p.getY()][p.getZ() + r] == null) {
										blocks[p.getX() + r][p.getY()][p.getZ() + r] = this.getRandomVolcanoBlock();
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									// ignore
								}
							});
						}
					}
				}
			}
		}

		// Infamous nether staircase
		if (this.dungeon.doBuildStairs()) {
			EStairSection stairSection = this.startStairSection;

			for (int iY = -1; iY < (this.caveHeight + this.caveDepth) * 0.9D; iY++) {
				int y = Math.max(iY, 1);
				int outerStairRadius = innerRadiusArray[y];
				int innerStairRadius = outerStairRadius / 2;

				for (int iX = -outerStairRadius; iX <= outerStairRadius; iX++) {
					for (int iZ = -outerStairRadius; iZ <= outerStairRadius; iZ++) {
						if (DungeonGenUtils.isInsideCircle(iX, iZ, outerStairRadius) && !DungeonGenUtils.isInsideCircle(iX, iZ, innerStairRadius) && StairCaseHelper.isLocationFine(stairSection, iX, iZ, outerStairRadius)) {
							blocks[iX + r][y][iZ + r] = this.dungeon.getRampBlock();

							if (DungeonGenUtils.isInsideCircle(iX, iZ, outerStairRadius - 2) && !DungeonGenUtils.isInsideCircle(iX, iZ, innerStairRadius + 2) && DungeonGenUtils.percentageRandom(this.dungeon.getChestChance())) {
								spawnerAndChestList.add(new BlockPos(iX, y - this.caveDepth + 1, iZ));
							}
						}
					}
				}

				stairSection = stairSection.getSuccessor();
			}

			this.generatePillars(new BlockPos(r + innerRadiusArray[0] / 2, 0, r), 2, (int) ((this.caveHeight + this.caveDepth) * 0.95D), blocks, this.dungeon.getPillarBlock());
			this.generatePillars(new BlockPos(r - innerRadiusArray[0] / 2, 0, r), 2, (int) ((this.caveHeight + this.caveDepth) * 0.95D), blocks, this.dungeon.getPillarBlock());
			this.generatePillars(new BlockPos(r, 0, r + innerRadiusArray[0] / 2), 2, (int) ((this.caveHeight + this.caveDepth) * 0.95D), blocks, this.dungeon.getPillarBlock());
			this.generatePillars(new BlockPos(r, 0, r - innerRadiusArray[0] / 2), 2, (int) ((this.caveHeight + this.caveDepth) * 0.95D), blocks, this.dungeon.getPillarBlock());
		}

		String mobType = dungeon.getDungeonMob();
		if (mobType.equalsIgnoreCase(DungeonInhabitantManager.DEFAULT_INHABITANT_IDENT)) {
			mobType = DungeonInhabitantManager.getInhabitantDependingOnDistance(world, this.pos.getX(), this.pos.getZ()).getName();
		}

		// Add block state array to dungeonGenerator
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>(blocks.length * blocks[0].length * blocks[0][0].length / 2);
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				for (int k = 0; k < blocks[i][j].length; k++) {
					if (blocks[i][j][k] != null) {
						blockInfoList.add(new BlockInfo(i, j, k, blocks[i][j][k], null));
					}
				}
			}
		}
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, referenceLoc, blockInfoList, new PlacementSettings(), mobType));

		// Spawners and Chests
		this.generateSpawnersAndChests(spawnerAndChestList, mobType);

		// Stronghold
		this.generateStronghold(innerRadiusArray[0], mobType);

		// Cover blocks
		if (this.dungeon.isCoverBlockEnabled()) {
			this.dungeonGenerator.add(new DungeonPartCover(this.world, this.dungeonGenerator, this.pos.getX() - r, this.pos.getZ() - r, this.pos.getX() + r, this.pos.getZ() + r, this.dungeon.getCoverBlock()));
		}
	}

	@Override
	public void postProcess() {

	}

	private void generateHoles(IBlockState[][][] blockArray) {
		if (this.dungeon.isVolcanoDamaged()) {
			List<BlockPos> list = new ArrayList<>((int) (this.volcanoHeight * 1.6D));

			for (int i = 0; i < this.volcanoHeight * 1.6D; i++) {
				for (int j = 0; j < 100; j++) {
					int x = this.random.nextInt(blockArray.length);
					int y = this.random.nextInt(blockArray[x].length);
					int z = this.random.nextInt(blockArray[x][y].length);

					if (blockArray[x][y][z] != null && blockArray[x][y][z] != Blocks.AIR.getDefaultState()) {
						list.add(new BlockPos(x, y, z));
						break;
					}
				}
			}

			for (BlockPos pos : list) {
				this.forEachSpherePosition(pos, DungeonGenUtils.randomBetween(2, this.dungeon.getMaxHoleSize()), p -> {
					try {
						if (blockArray[p.getX()][p.getY()][p.getZ()] != null && blockArray[p.getX()][p.getY()][p.getZ()] != Blocks.AIR.getDefaultState()) {
							blockArray[p.getX()][p.getY()][p.getZ()] = Blocks.AIR.getDefaultState();
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						// ignore
					}
				});
			}
		}
	}

	private void generatePillars(BlockPos pos, int radius, int height, IBlockState[][][] blocks, IBlockState pillarBlock) {
		for (int iY = 0; iY < height; iY++) {
			for (int iX = -radius; iX <= radius; iX++) {
				for (int iZ = -radius; iZ <= radius; iZ++) {
					try {
						blocks[iX + pos.getX()][iY + pos.getY()][iZ + pos.getZ()] = pillarBlock;
					} catch (ArrayIndexOutOfBoundsException ex) {
						// ignore
					}
				}
			}
		}
	}

	private void generateSpawnersAndChests(List<BlockPos> spawnerAndChestList, String mobType) {
		if (!spawnerAndChestList.isEmpty()) {
			ResourceLocation[] lootTables = this.dungeon.getChestIDs();
			GearedMobFactory mobFactory = new GearedMobFactory(spawnerAndChestList.size(), this.dungeon.getRampMob(), this.random);
			int floor = spawnerAndChestList.size();
			List<AbstractBlockInfo> blockInfoList1 = new ArrayList<>();

			for (BlockPos pos : spawnerAndChestList) {
				if (this.random.nextBoolean()) {
					blockInfoList1.add(new BlockInfoLootChest(pos.getX(), pos.getY(), pos.getZ(), lootTables[this.random.nextInt(lootTables.length)], EnumFacing.NORTH));
				}

				int entityCount = 2 + this.random.nextInt(3);
				List<Entity> entityList = new ArrayList<>(entityCount);
				for (int i = 0; i < entityCount; i++) {
					entityList.add(mobFactory.getGearedEntityByFloor(floor, this.world));
				}
				blockInfoList1.add(new BlockInfoSpawner(pos.getX(), pos.getY() + 1, pos.getZ(), ModBlocks.SPAWNER.getDefaultState(), entityList));
				floor--;
			}
			this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.pos, blockInfoList1, new PlacementSettings(), mobType));
		}
	}

	private void generateStronghold(int radius, String mobType) {
		if (this.dungeon.doBuildStronghold()) {
			EStairSection entranceDirection = this.startStairSection.getSuccessor();
			int entranceDistToWall = radius / 3;
			int wideness = radius - entranceDistToWall;
			BlockPos entranceStartPos;

			switch (entranceDirection) {
			case EAST:
			case EAST_SEC:
				entranceStartPos = this.pos.add(wideness, 1 - this.caveDepth, 0);
				break;
			case NORTH:
			case NORTH_SEC:
				entranceStartPos = this.pos.add(0, 1 - this.caveDepth, -wideness);
				break;
			case SOUTH:
			case SOUTH_SEC:
				entranceStartPos = this.pos.add(0, 1 - this.caveDepth, wideness);
				break;
			case WEST:
			case WEST_SEC:
			default:
				entranceStartPos = this.pos.add(-wideness, 1 - this.caveDepth, 0);
				break;
			}

			StrongholdBuilder entranceBuilder = new StrongholdBuilder(this, this.dungeonGenerator, entranceStartPos, entranceDistToWall, this.dungeon, entranceDirection.getAsSkyDirection(), this.world);
			entranceBuilder.generate(this.pos.getX(), this.pos.getZ(), mobType);
			this.dungeonGenerator.addAll(entranceBuilder.getStrongholdParts());
		}
	}

	private void forEachSpherePosition(BlockPos center, int radius, Consumer<BlockPos.MutableBlockPos> action) {
		for (BlockPos.MutableBlockPos p : BlockPos.getAllInBoxMutable(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius)) {
			if (DungeonGenUtils.isInsideSphere(p.getX(), p.getY(), p.getZ(), radius)) {
				action.accept(p);
			}
		}
	}

	private IBlockState getRandomVolcanoBlock() {
		WeightedItem<IBlockState> weightedItem = WeightedRandom.getRandomItem(this.random, this.volcanoBlocks, this.volcanoBlocksTotalWeight);
		return weightedItem != null ? weightedItem.item : Blocks.STONE.getDefaultState();
	}

	private IBlockState getRandomVolcanoBlockWithLava() {
		WeightedItem<IBlockState> weightedItem = WeightedRandom.getRandomItem(this.random, this.volcanoBlocksWithLava, this.volcanoBlocksTotalWeightWithLava);
		return weightedItem != null ? weightedItem.item : Blocks.STONE.getDefaultState();
	}

}
