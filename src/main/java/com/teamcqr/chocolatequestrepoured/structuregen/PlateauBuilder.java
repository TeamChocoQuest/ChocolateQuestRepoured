package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.Perlin3D;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class PlateauBuilder {

	// Copied directly from old mod, should be ok, because it is just an
	// implementation of perlin3D

	public PlateauBuilder() {
	}

	Block structureBlock = Blocks.STONE;
	Block structureTopBlock = Blocks.GRASS;
	private static final int TOP_LAYER_HEIGHT = 1;

	public int wallSize = Reference.CONFIG_HELPER_INSTANCE.getSupportHillWallSize();

	public void load(Block support, Block top) {
		this.structureBlock = support;
		this.structureTopBlock = top;
	}

	public void createSupportHill(Random random, World world, BlockPos startPos, int sizeX, int sizeZ, EPosType posType) {
		BlockPos pos = startPos;
		switch (posType) {
		case CENTER_XZ_LAYER:
			pos = startPos.subtract(new Vec3i(sizeX / 2, 0, sizeZ / 2));
			break;
		case CORNER_NE:
			pos = startPos.subtract(new Vec3i(sizeX, 0, 0));
			break;
		case CORNER_SE:
			pos = startPos.subtract(new Vec3i(sizeX, 0, sizeZ));
			break;
		case CORNER_SW:
			pos = startPos.subtract(new Vec3i(0, 0, sizeZ));
			break;
		default:
			break;
		}
		this.generateSupportHill(random, world, pos.getX(), pos.getY(), pos.getZ(), sizeX, sizeZ);
	}

	// Coordinates are the N_W Corner!!
	/*
	 * DONE: Write also a method, that digs a cave with two corners
	 * 
	 * Future ideas:
	 * - Pass calculation of block positions to a daemon thread OR to a entity that places blocks every tick like old cqc
	 * - Let the daemon thread call a method that runs the block placement on the main Thread
	 * - Rewrite generators in that way, that they get a method that gets called by the calcThread to start block placement
	 * 
	 * Note: Forge allows async threads modifying things of main thread
	 */
	private void generateSupportHill(Random random, World world, int startX, int startY, int startZ, int sizeX, int sizeZ) {
		System.out.println("Trying to construct support platform...");

		Perlin3D p = new Perlin3D(world.getSeed(), this.wallSize, random);
		Perlin3D p2 = new Perlin3D(world.getSeed(), this.wallSize * 4, random);

		sizeX += this.wallSize * 2;
		sizeZ += this.wallSize * 2;

		startX -= this.wallSize;
		startZ -= this.wallSize;

		for (int x = 0; x < sizeX; ++x) {
			for (int z = 0; z < sizeZ; ++z) {
				int maxHeight = startY - TOP_LAYER_HEIGHT - world.getTopSolidOrLiquidBlock(new BlockPos(x + startX, 0, z + startZ)).getY();// DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)), x + i,
																																			// z + k, true);
				int posY = world.getTopSolidOrLiquidBlock(new BlockPos(x + startX, 0, z + startZ)).getY();// DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)),x + i, z + k, true);
				for (int y = 0; y <= maxHeight; ++y) {
					// This generates the "cube" that goes under the structure
					if ((x > this.wallSize) && (z > this.wallSize) && (x < sizeX - this.wallSize) && (z < sizeZ - this.wallSize)) {
						world.setBlockState(new BlockPos(startX + x, posY + y, startZ + z), this.structureBlock.getDefaultState());
					}
					// This generates the fancy "curved" walls around the cube
					else {
						float noiseVar = (float) (y - maxHeight) / ((float) Math.max(1, maxHeight) * 1.5F);

						noiseVar += Math.max(0.0F, (this.wallSize - x) / 8.0F);
						noiseVar += Math.max(0.0F, (this.wallSize - (sizeX - x)) / 8.0F);

						noiseVar += Math.max(0.0F, (this.wallSize - z) / 8.0F);
						noiseVar += Math.max(0.0F, (this.wallSize - (sizeZ - z)) / 8.0F);
						double value = (p.getNoiseAt(x + startX, y, z + startZ) + p2.getNoiseAt(x + startX, y, z + startZ) + noiseVar) / 3.0D + y / (maxHeight == 0 ? 1 : maxHeight) * 0.25D;
						if (value < 0.5D) {
							world.setBlockState(new BlockPos(startX + x, posY + y, startZ + z), this.structureBlock.getDefaultState());
						}
					}
				}
				// This places the top layer blocks
				maxHeight = world.getTopSolidOrLiquidBlock(new BlockPos(x + startX, 0, z + startZ)).getY();// DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)),x + i, z + k, true);//
																											// world.getTopSolidOrLiquidBlock(new BlockPos(x + i, 0, z + k)).getY();
				if (maxHeight <= startY) {
					world.setBlockState(new BlockPos(startX + x, maxHeight - 1, startZ + z), this.structureTopBlock.getDefaultState());
				}
			}
		}
	}

	// These methods are used to dig out random caves
	public void createCave(Random random, BlockPos startPos, BlockPos endPos, long seed, World world) {
		this.makeRandomBlob(random, Blocks.AIR, startPos, endPos, seed, world);
	}

	public void createCave(Random random, BlockPos startPos, BlockPos endPos, int wallSize, long seed, World world) {
		this.makeRandomBlob(random, Blocks.AIR, startPos, endPos, wallSize, seed, world);
	}

	public void makeRandomBlob(Random random, Block fillBlock, BlockPos startPos, BlockPos endPos, long seed, World world) {
		this.makeRandomBlob(random, fillBlock, startPos, endPos, 4, seed, world);
	}

	public void makeRandomBlob(Random random, Block fillBlock, BlockPos startPos, BlockPos endPos, int wallSize, long seed, World world) {
		Perlin3D perlinNoise1 = new Perlin3D(seed, 8, random);
		Perlin3D perlinNoise2 = new Perlin3D(seed, 32, random);

		int sizeX = endPos.getX() - startPos.getX();
		int sizeZ = endPos.getZ() - startPos.getZ();
		int sizeY = endPos.getY() - startPos.getY();

		sizeX *= 1.25;
		sizeZ *= 1.25;
		sizeY *= 1.35;

		for (int iX = 0; iX < sizeX; ++iX) {
			for (int iY = 0; iY < sizeY; ++iY) {
				for (int iZ = 0; iZ < sizeZ; ++iZ) {

					float noise = Math.max(0.0F, 2.0F - (float) (sizeY - iY) / 4.0F);
					noise += Math.max(0.0F, (float) wallSize - (float) iX / 2.0F);
					noise += Math.max(0.0F, (float) wallSize - (float) (sizeX - iX) / 2.0F);
					noise += Math.max(0.0F, (float) wallSize - (float) iZ / 2.0F);
					noise += Math.max(0.0F, (float) wallSize - (float) (sizeZ - iZ) / 2.0F);

					double perlin1 = perlinNoise1.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);
					double perlin2 = perlinNoise2.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);

					if ((perlin1 * perlin2 * (double) noise) < 0.5D) {
						if (!Block.isEqualTo(world.getBlockState(startPos.add(iX, iY, iZ)).getBlock(), fillBlock)) {
							if (Block.isEqualTo(fillBlock, Blocks.AIR)) {
								world.setBlockToAir(startPos.add(iX, iY, iZ));
							} else {
								world.setBlockState(startPos.add(iX, iY, iZ), fillBlock.getDefaultState());
							}
						}
					}
				}
			}
		}
	}

}
