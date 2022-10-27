package team.cqr.cqrepoured.world.structure.generation.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HugeMushroomBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.GearedMobFactory;
import team.cqr.cqrepoured.util.VectorUtil;
import team.cqr.cqrepoured.world.structure.generation.GenerationUtil;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonVegetatedCave;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

public class GeneratorVegetatedCave extends LegacyDungeonGenerator<DungeonVegetatedCave> {

	private List<BlockPos> spawners = new ArrayList<>();
	private List<BlockPos> chests = new ArrayList<>();
	private Set<BlockPos> ceilingBlocks = new HashSet<>();
	private Set<BlockPos> giantMushrooms = new HashSet<>();
	private Map<BlockPos, Integer> heightMap = new ConcurrentHashMap<>();
	private Set<BlockPos> floorBlocks = new HashSet<>();
	private Map<BlockPos, BlockState> blocks = new ConcurrentHashMap<>();
	private BlockState[][][] centralCaveBlocks;
	private DungeonInhabitant mobtype;

	public GeneratorVegetatedCave(ChunkGenerator world, BlockPos pos, DungeonVegetatedCave dungeon, Random rand) {
		super(world, pos, dungeon, rand);
	}

	@Override
	public void preProcess() {
		this.mobtype = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(this.dungeon.getDungeonMob(), this.level, this.pos.getX(), this.pos.getZ());
		Random random = new Random(WorldDungeonGenerator.getSeed(this.level.getSeed(), this.pos.getX() / 16, this.pos.getZ() / 16));
		BlockState[][][] blocks = this.getRandomBlob(this.dungeon.getAirBlock(), this.dungeon.getCentralCaveSize(), random);
		this.centralCaveBlocks = blocks;
		// if (this.dungeon.placeVines()) {
		this.ceilingBlocks.addAll(this.getCeilingBlocksOfBlob(blocks, this.pos, random));
		// }
		this.floorBlocks.addAll(this.getFloorBlocksOfBlob(blocks, this.pos, random));
		this.storeBlockArrayInMap(blocks, this.pos);
		final BlockPos centerBP = this.pos.below(this.dungeon.getCentralCaveSize() / 2);
		Vector3d center = new Vector3d(centerBP.getX(), centerBP.getY(), centerBP.getZ());
		Vector3d rad = new Vector3d(this.dungeon.getCentralCaveSize() * 1.75, 0, 0);
		int tunnelCount = this.dungeon.getTunnelCount(random);
		double angle = 360D / tunnelCount;
		for (int i = 0; i < tunnelCount; i++) {
			Vector3d v = VectorUtil.rotateVectorAroundY(rad, angle * i);
			Vector3d startPos = center.add(v);
			this.createTunnel(startPos, angle * i, this.dungeon.getTunnelStartSize(), this.dungeon.getCaveSegmentCount(), random);
		}
		// Filter floorblocks
		this.filterFloorBlocks();

		/*
		 * this.ceilingBlocks.forEach(new Consumer<BlockPos>() {
		 * 
		 * @Override public void accept(BlockPos t) { GeneratorVegetatedCave.this.blocks.put(t, GeneratorVegetatedCave.this.dungeon.getVineLatchBlock()); } });
		 */

		// Filter ceiling blocks
		if (this.dungeon.placeVines()) {
			this.filterCeilingBlocks(this.level);
		}

		// Flowers, Mushrooms and Weed
		if (this.dungeon.placeVegetation()) {
			this.createVegetation(random);
		}
		// Vines
		if (this.dungeon.placeVines()) {
			this.createVines(random);
		}

		// Build
		// BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
		for (Map.Entry<BlockPos, BlockState> entry : this.blocks.entrySet()) {
			// partBuilder.add(new PreparableBlockInfo(entry.getKey().subtract(this.pos), entry.getValue(), null));
			this.dungeonBuilder.getLevel().setBlockState(entry.getKey().subtract(this.pos), entry.getValue());
		}
		// this.dungeonBuilder.add(partBuilder);
	}

	@Override
	public void buildStructure() {
		// DONE: Paste the building
	}

	private int getLowestY(BlockState[][][] blocks, int rX, int rZ, int origY) {
		int y = 255;

		int cX = blocks.length / 2;
		int radX = rX < cX ? rX : cX;
		if (cX + radX >= blocks.length) {
			radX = blocks.length - cX;
		}
		int cZ = blocks[0][0].length / 2;
		int radZ = rZ < cZ ? rZ : cZ;
		if (cZ + radZ >= blocks.length) {
			radZ = blocks.length - cZ;
		}

		for (int iX = cX - radX; iX <= cX + radX; iX++) {
			for (int iZ = cZ - radZ; iZ <= cZ + radZ; iZ++) {
				if (iX < 0 || iX >= blocks.length || iZ < 0 || iZ >= blocks[0][0].length) {
					continue;
				}
				for (int iY = 0; iY < blocks[iX].length; iY++) {
					if (blocks[iX][iY][iZ] != null) {
						if (y > iY) {
							y = iY;
						}
						break;
					}
				}
			}
		}

		int radius = blocks.length / 2;
		y -= radius;
		y += origY;
		return y;
	}

	@Override
	public void postProcess() {
		// DONE: Place giant shrooms
		Map<BlockPos, BlockState> stateMap = new HashMap<>();
		Random random = new Random(WorldDungeonGenerator.getSeed(this.dungeonBuilder.getLevel().getSeed(), this.pos.getX() / 16, this.pos.getZ() / 16));
		for (BlockPos mushroompos : this.giantMushrooms) {
			// Place shroom
			if (random.nextBoolean()) {
				this.generateGiantMushroom(mushroompos, random, stateMap);
			}

			if (random.nextInt(3) == 0) {
				// Spawner
				BlockPos spawner = new BlockPos(mushroompos.getX() + (random.nextBoolean() ? -1 : 1), mushroompos.getY() + 1, mushroompos.getZ() + (random.nextBoolean() ? -1 : 1));
				this.spawners.add(spawner);
				if (random.nextInt(3) >= 1) {
					// Chest
					this.chests.add(spawner.below());
				}
			}
		}
		// BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
		for (Map.Entry<BlockPos, BlockState> entry : stateMap.entrySet()) {
			// partBuilder.add(new PreparableBlockInfo(entry.getKey().subtract(this.pos), entry.getValue(), null));
			this.dungeonBuilder.getLevel().setBlockState(entry.getKey().subtract(this.pos), entry.getValue());
		}
		// this.dungeonBuilder.add(partBuilder);

		this.placeSpawners();
		this.fillChests();
		this.generateCenterStructure();
	}

	public void fillChests() {
		// DONE: Place and fill chests
		// BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
		Random random = new Random(WorldDungeonGenerator.getSeed(this.dungeonBuilder.getLevel().getSeed(), this.pos.getX() / 16, this.pos.getZ() / 16));
		ResourceLocation[] chestIDs = this.dungeon.getChestIDs();
		for (BlockPos chestpos : this.chests) {
			ResourceLocation resLoc = chestIDs[random.nextInt(chestIDs.length)];
			GenerationUtil.setLootChest(this.dungeonBuilder.getLevel(), chestpos.subtract(this.pos), resLoc, Direction.Plane.HORIZONTAL.getRandomDirection(this.random));
			/*
			 * this.dungeonBuilder.getLevel().setBlockState(chestpos.subtract(this.pos), state, (te) -> { if(te != null && te instanceof ChestTileEntity) { ChestTileEntity chest = (ChestTileEntity)te;
			 * 
			 * if (chest != null) { if (resLoc != null) { long seed = WorldDungeonGenerator.getSeed(this.dungeonBuilder.getLevel().getSeed(), this.pos.getX() + chestpos.getX() + chestpos.getY(), this.pos.getZ() + chestpos.getZ() + chestpos.getY());
			 * chest.setLootTable(resLoc, seed); } } } });
			 */
			// partBuilder.add(new PreparableBlockInfo(chestpos.subtract(this.pos), state, nbt));
		}
		// this.dungeonBuilder.add(partBuilder);
	}

	private static final int FLOORS = 100;

	public void placeSpawners() {
		// DONE: Place spawners
		// BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();

		GearedMobFactory mobFactory = new GearedMobFactory(FLOORS, this.mobtype.getEntityID(), this.random);
		for (BlockPos spawnerpos : this.spawners) {
			int entityCount = 1 + this.random.nextInt(3);
			List<Entity> entityList = new ArrayList<>(entityCount);
			for (int i = 0; i < entityCount; i++) {
				int floor = this.random.nextInt(FLOORS);
				entityList.add(mobFactory.getGearedEntityByFloor(floor, this.dungeonBuilder.getEntityFactory()));
			}
			GenerationUtil.setSpawner(this.dungeonBuilder.getLevel(), spawnerpos.subtract(this.pos), entityList);
		}
	}

	public void generateCenterStructure() {
		// Well we need to place the building now to avoid that it gets overrun by mushrooms
		if (this.dungeon.placeBuilding()) {
			File file = this.dungeon.getRandomCentralBuilding(this.random);
			if (file != null) {
				CQStructure structure = CQStructure.createFromFile(file);
				int pY = this.getLowestY(this.centralCaveBlocks, structure.getSize().getX() / 2, structure.getSize().getZ() / 2, this.pos.getY());
				// DONE: Support platform -> not needed
				structure.addAll(this.dungeonBuilder, new BlockPos(this.pos.getX(), pY, this.pos.getZ()), Offset.CENTER);
			}
		}
	}

	private void createTunnel(Vector3d startPos, double initAngle, int startSize, int initLength, Random random) {
		double angle = 90D;
		angle /= initLength;
		angle /= (startSize - 2) / 2;
		Vector3d expansionDir = VectorUtil.rotateVectorAroundY(new Vector3d(startSize, 0, 0), initAngle);
		for (int i = 0; i < initLength; i++) {
			BlockState[][][] blob = this.getRandomBlob(this.dungeon.getAirBlock(), startSize, (int) (startSize * 0.8), random);
			// if (this.dungeon.placeVines()) {
			this.ceilingBlocks.addAll(this.getCeilingBlocksOfBlob(blob, new BlockPos(startPos.x, startPos.y, startPos.z), random));
			// }
			this.floorBlocks.addAll(this.getFloorBlocksOfBlob(blob, new BlockPos(startPos.x, startPos.y, startPos.z), random));

			this.storeBlockArrayInMap(blob, new BlockPos(startPos.x, startPos.y, startPos.z));
			expansionDir = VectorUtil.rotateVectorAroundY(expansionDir, angle);
			startPos = startPos.add(expansionDir);
		}
		int szTmp = startSize;
		startSize -= 2;
		if (startSize > 3) {
			this.createTunnel(startPos, initAngle + angle * initLength - 90, startSize, initLength * (szTmp / startSize), random);
			this.createTunnel(startPos, initAngle + angle * initLength, startSize, initLength * (szTmp / startSize), random);
		}
	}

	private List<BlockPos> getCeilingBlocksOfBlob(BlockState[][][] blob, BlockPos blobCenter, Random random) {
		List<BlockPos> ceilingBlocks = new ArrayList<>();
		int radius = blob.length / 2;
		for (int iX = 0; iX < blob.length; iX++) {
			for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for (int iY = blob[0].length - 1; iY >= 1; iY--) {
					if (blob[iX][iY - 1][iZ] != null && blob[iX][iY][iZ] == null) {
						// blob[iX][iY][iZ] = dungeon.getFloorBlock(random);
						BlockPos p = blobCenter.offset(new BlockPos(iX - radius, iY - radius - 1, iZ - radius));
						ceilingBlocks.add(p);
						int height = 0;
						int yTmp = iY - 1;
						while (blob[iX][yTmp][iZ] != null && yTmp >= 0) {
							yTmp--;
							height++;
						}
						this.heightMap.put(p, height);
						break;
					}
				}
			}
		}
		return ceilingBlocks;
	}

	private void storeBlockArrayInMap(BlockState[][][] blob, BlockPos blobCenter) {
		int radius = blob.length / 2;
		for (int iX = 0; iX < blob.length; iX++) {
			for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for (int iY = 1; iY < blob[0].length; iY++) {
					if (blob[iX][iY][iZ] != null) {
						BlockState state = blob[iX][iY][iZ];
						BlockPos bp = new BlockPos(iX - radius, iY - radius, iZ - radius);
						this.blocks.put(blobCenter.offset(bp), state);
					}
				}
			}
		}
	}

	private List<BlockPos> getFloorBlocksOfBlob(BlockState[][][] blob, BlockPos blobCenter, Random random) {
		List<BlockPos> floorBlocks = new ArrayList<>();
		int radius = blob.length / 2;
		for (int iX = 0; iX < blob.length; iX++) {
			for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for (int iY = 1; iY < blob[0].length; iY++) {
					if (blob[iX][iY][iZ] != null && blob[iX][iY - 1][iZ] == null) {
						blob[iX][iY][iZ] = this.dungeon.getFloorBlock(random);
						floorBlocks.add(blobCenter.offset(new BlockPos(iX - radius, iY - radius, iZ - radius)));
						break;
					}
				}
			}
		}
		return floorBlocks;
	}

	private BlockState[][][] getRandomBlob(BlockState block, int radius, Random random) {
		return this.getRandomBlob(block, radius, (int) (radius * 0.75), random);
	}

	private BlockState[][][] getRandomBlob(BlockState block, int radius, int radiusY, Random random) {
		BlockState[][][] blocks = new BlockState[radius * 4][radiusY * 4][radius * 4];
		int subSphereCount = radius * 3;
		double sphereSurface = 4 * Math.PI * (radius * radius);
		double counter = sphereSurface / subSphereCount;
		double cI = 0;
		for (int iX = -radius; iX <= radius; iX++) {
			for (int iY = -radiusY; iY <= radiusY; iY++) {
				for (int iZ = -radius; iZ <= radius; iZ++) {
					double distance = iX * iX + iZ * iZ + iY * iY;
					distance = Math.sqrt(distance);
					if (distance < radius) {
						blocks[iX + (radius * 2)][iY + (radiusY * 2)][iZ + (radius * 2)] = block;
					} else if (distance <= radius + 1) {
						cI++;
						if (cI < counter) {
							continue;
						}
						cI = 0;
						int r1 = radius / 2;
						int r1Y = radiusY / 2;
						int r2 = (int) (radius * 0.75);
						int r2Y = (int) (radiusY * 0.75);
						int rSub = DungeonGenUtils.randomBetween(r1, r2, random);
						int rSubY = DungeonGenUtils.randomBetween(r1Y, r2Y, random);
						for (int jX = iX - rSub; jX <= iX + rSub; jX++) {
							for (int jY = iY - rSubY; jY <= iY + rSubY; jY++) {
								for (int jZ = iZ - rSub; jZ <= iZ + rSub; jZ++) {
									double distanceSub = (jX - iX) * (jX - iX) + (jY - iY) * (jY - iY) + (jZ - iZ) * (jZ - iZ);
									distanceSub = Math.sqrt(distanceSub);
									if (distanceSub < rSub) {
										try {
											if (blocks[jX + (radius * 2)][jY + (radiusY * 2)][jZ + (radius * 2)] != block) {
												blocks[jX + (radius * 2)][jY + (radiusY * 2)][jZ + (radius * 2)] = block;
											}
										} catch (ArrayIndexOutOfBoundsException ex) {
											// Ignore
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

	private void filterFloorBlocks() {
		this.floorBlocks.removeIf(floorPos -> {
			BlockPos lower = floorPos.below();
			if (GeneratorVegetatedCave.this.blocks.containsKey(lower)) {
				GeneratorVegetatedCave.this.blocks.put(floorPos, GeneratorVegetatedCave.this.dungeon.getAirBlock());
				return true;
			}
			return false;
		});
	}

	private void filterCeilingBlocks(World world) {
		this.ceilingBlocks.removeIf(arg0 -> {
			BlockPos upper = arg0.above();
			if (GeneratorVegetatedCave.this.blocks.containsKey(upper)) {
				GeneratorVegetatedCave.this.blocks.put(arg0, GeneratorVegetatedCave.this.dungeon.getAirBlock());
				GeneratorVegetatedCave.this.heightMap.remove(arg0);
				return true;
			}
			if (!GeneratorVegetatedCave.this.dungeon.skipCeilingFiltering()) {
				return world.getHeight(Type.WORLD_SURFACE_WG, arg0.getX(), arg0.getZ()) <= arg0.getY() || world.getHeightmapPos(Type.WORLD_SURFACE_WG, arg0).getY() <= arg0.getY() || world.canSeeSky(arg0);
			}
			return false;
		});
	}

	private void createVegetation(Random random) {
		for (BlockPos floorPos : this.floorBlocks) {
			int number = random.nextInt(300);
			BlockState state = null;
			if (number >= 295) {
				// Giant mushroom
				boolean flag = true;
				for (BlockPos shroom : this.giantMushrooms) {
					if (shroom.distSqr(floorPos.getX(), floorPos.getY(), floorPos.getZ(), true) < 25 /* 5^2 */) {
						flag = false;
						break;
					}
				}
				if (flag) {
					this.giantMushrooms.add(floorPos.above());
				}
			} else if (number >= 290) {
				// Lantern
				state = this.dungeon.getPumpkinBlock();
			} else if (number <= 150) {
				if (number <= 100) {
					// Grass
					state = this.dungeon.getGrassBlock(random);
				} else {
					// Flower or mushroom
					if (random.nextBoolean()) {
						// Flower
						state = this.dungeon.getFlowerBlock(random);
					} else {
						// Mushroom
						state = this.dungeon.getMushroomBlock(random);
					}
				}
			}
			if (state != null) {
				this.blocks.put(floorPos.above(), state);
			}
		}
		// System.out.println("Floor blocks: " + floorBlocks.size());
		// System.out.println("Giant mushrooms: " + giantMushrooms.size());
	}

	private void createVines(Random random) {
		for (BlockPos vineStart : this.ceilingBlocks) {
			if (random.nextInt(300) >= (300 - this.dungeon.getVineChance())) {
				int vineLength = this.heightMap.get(vineStart);
				vineLength = (int) (vineLength / this.dungeon.getVineLengthModifier());
				BlockPos vN = vineStart.north();
				BlockPos vE = vineStart.east();
				BlockPos vS = vineStart.south();
				BlockPos vW = vineStart.west();
				if (this.dungeon.isVineShapeCross()) {
					this.blocks.put(vineStart, this.dungeon.getVineLatchBlock());
				}
				BlockState airState = this.dungeon.getAirBlock();
				BlockState sState = this.dungeon.isVineShapeCross() ? this.dungeon.getVineBlock().setValue(VineBlock.NORTH, true) : null;
				BlockState wState = this.dungeon.isVineShapeCross() ? this.dungeon.getVineBlock().setValue(VineBlock.EAST, true) : null;
				BlockState nState = this.dungeon.isVineShapeCross() ? this.dungeon.getVineBlock().setValue(VineBlock.SOUTH, true) : null;
				BlockState eState = this.dungeon.isVineShapeCross() ? this.dungeon.getVineBlock().setValue(VineBlock.WEST, true) : null;
				while (vineLength >= 0) {
					if (this.dungeon.isVineShapeCross()) {
						this.blocks.put(vN, nState);
						this.blocks.put(vE, eState);
						this.blocks.put(vS, sState);
						this.blocks.put(vW, wState);
						vN = vN.below();
						vE = vE.below();
						vS = vS.below();
						vW = vW.below();
						if (this.blocks.getOrDefault(vN, airState) != airState || this.blocks.getOrDefault(vE, airState) != airState || this.blocks.getOrDefault(vS, airState) != airState || this.blocks.getOrDefault(vW, airState) != airState) {
							break;
						}
					} else {
						this.blocks.put(vineStart, this.dungeon.getVineBlock());
						vineStart = vineStart.below();
						if (this.blocks.getOrDefault(vineStart, airState) != airState) {
							break;
						}
					}
					vineLength--;
				}
			}
		}
	}

	private void generateGiantMushroom(BlockPos position, Random rand, Map<BlockPos, BlockState> stateMap) {
		final boolean red = rand.nextBoolean();
		int shroomHeight = rand.nextInt(3) + 4;
		int foliageRad = red ? 2 : 3;
		shroomHeight *= rand.nextInt(12) == 0 ? 2 : 1;
		final BlockState stemState = Blocks.MUSHROOM_STEM.defaultBlockState();
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int i = 0; i < shroomHeight; ++i) {
			mutable.set(position).move(Direction.UP, i);
			stateMap.put(new BlockPos(mutable), stemState);
		}

		if (red) {
			for (int i = shroomHeight - 3; i <= shroomHeight; ++i) {
				int j = i < shroomHeight ? foliageRad : foliageRad - 1;
				int k = foliageRad - 2;

				for (int l = -j; l <= j; ++l) {
					for (int i1 = -j; i1 <= j; ++i1) {
						boolean flag = l == -j;
						boolean flag1 = l == j;
						boolean flag2 = i1 == -j;
						boolean flag3 = i1 == j;
						boolean flag4 = flag || flag1;
						boolean flag5 = flag2 || flag3;
						if (i >= shroomHeight || flag4 != flag5) {
							mutable.setWithOffset(position, l, i, i1);
							BlockState state = Blocks.RED_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.DOWN, Boolean.valueOf(false)).setValue(HugeMushroomBlock.UP, Boolean.valueOf(i >= shroomHeight - 1)).setValue(
									HugeMushroomBlock.WEST, Boolean.valueOf(l < -k)).setValue(HugeMushroomBlock.EAST, Boolean.valueOf(l > k)).setValue(HugeMushroomBlock.NORTH, Boolean.valueOf(i1 < -k)).setValue(HugeMushroomBlock.SOUTH, Boolean
											.valueOf(i1 > k));
							stateMap.put(new BlockPos(mutable), state);
						}
					}
				}
			}
		} else {
			for (int j = -foliageRad; j <= foliageRad; ++j) {
				for (int k = -foliageRad; k <= foliageRad; ++k) {
					boolean flag = j == -foliageRad;
					boolean flag1 = j == foliageRad;
					boolean flag2 = k == -foliageRad;
					boolean flag3 = k == foliageRad;
					boolean flag4 = flag || flag1;
					boolean flag5 = flag2 || flag3;
					if (!flag4 || !flag5) {
						mutable.setWithOffset(position, j, shroomHeight, k);
						boolean flag6 = flag || flag5 && j == 1 - foliageRad;
						boolean flag7 = flag1 || flag5 && j == foliageRad - 1;
						boolean flag8 = flag2 || flag4 && k == 1 - foliageRad;
						boolean flag9 = flag3 || flag4 && k == foliageRad - 1;
						BlockState state = Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.valueOf(true)).setValue(HugeMushroomBlock.DOWN, Boolean.valueOf(false)).setValue(HugeMushroomBlock.WEST, Boolean
								.valueOf(flag6)).setValue(HugeMushroomBlock.EAST, Boolean.valueOf(flag7)).setValue(HugeMushroomBlock.NORTH, Boolean.valueOf(flag8)).setValue(HugeMushroomBlock.SOUTH, Boolean.valueOf(flag9));
						stateMap.put(new BlockPos(mutable), state);
					}
				}
			}
		}
	}

}
