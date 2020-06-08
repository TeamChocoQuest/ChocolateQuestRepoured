package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class PlateauBuilder {

	// Copied directly from old mod, should be ok, because it is just an
	// implementation of perlin3D

	public PlateauBuilder() {
	}

	Block structureBlock = Blocks.STONE;
	Block structureTopBlock = Blocks.GRASS;
	public static final int TOP_LAYER_HEIGHT = 1;

	public int wallSize = CQRConfig.general.supportHillWallSize;

	public void load(Block support, Block top) {
		this.structureBlock = support;
		this.structureTopBlock = top;
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
								world.setBlockState(startPos.add(iX, iY, iZ), fillBlock.getDefaultState(), 2);
							}
						}
					}
				}
			}
		}
	}
	
	public static DungeonPartBlock makeRandomBlob2(Block fillBlock, BlockPos startPos, BlockPos endPos, int wallSize, long seed, World world, DungeonGenerator dungeonGenerator) {
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		Perlin3D perlinNoise1 = new Perlin3D(seed, 8, new Random());
		Perlin3D perlinNoise2 = new Perlin3D(seed, 32, new Random());

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

					if (noise >= 0.5F) {
						double perlin1 = perlinNoise1.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);

						if (perlin1 * (double) noise >= 0.5D) {
							double perlin2 = perlinNoise2.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);

							if (perlin1 * perlin2 * (double) noise >= 0.5D) {
								continue;
							}
						}
					}

					blockInfoList.add(new BlockInfo(new BlockPos(iX, iY, iZ), fillBlock.getDefaultState(), null));
				}
			}
		}

		return new DungeonPartBlock(world, dungeonGenerator, startPos, blockInfoList, new PlacementSettings(), EDungeonMobType.DEFAULT);
	}

}
