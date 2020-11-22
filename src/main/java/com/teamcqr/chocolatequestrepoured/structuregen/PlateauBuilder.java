package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class PlateauBuilder {

	// TODO Create custom DungeonPart class because Perlin3D is to slow to calculate immediately
	public static DungeonPartBlock makeRandomBlob(Block fillBlock, BlockPos startPos, BlockPos endPos, int wallSize, long seed, World world, DungeonGenerator dungeonGenerator) {
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		Perlin3D perlinNoise1 = new Perlin3D(seed, 8);
		Perlin3D perlinNoise2 = new Perlin3D(seed, 32);

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

					blockInfoList.add(new BlockInfo(iX, iY, iZ, fillBlock.getDefaultState(), null));
				}
			}
		}

		return new DungeonPartBlock(world, dungeonGenerator, startPos, blockInfoList, new PlacementSettings(), DungeonInhabitantManager.DEFAULT_DUNGEON_INHABITANT);
	}

	public static DungeonPartBlock makeRandomBlob2(Block fillBlock, BlockPos startPos, BlockPos endPos, int wallSize, long seed, World world, DungeonGenerator dungeonGenerator) {
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		Perlin3D perlinNoise1 = new Perlin3D(seed, 8);
		Perlin3D perlinNoise2 = new Perlin3D(seed, 32);

		int sizeX = endPos.getX() - startPos.getX() + 1 + wallSize * 2;
		int sizeZ = endPos.getZ() - startPos.getZ() + 1 + wallSize * 2;
		int sizeY = endPos.getY() - startPos.getY() + 1 + wallSize * 2;

		for (int iX = 0; iX < sizeX; ++iX) {
			for (int iY = 0; iY < sizeY; ++iY) {
				for (int iZ = 0; iZ < sizeZ; ++iZ) {

					float noise = Math.max(0.0F, (float) wallSize - (float) iY * 0.5F);
					noise += Math.max(0.0F, (float) wallSize - (float) (sizeY - iY) * 0.5F);
					noise += Math.max(0.0F, (float) wallSize - (float) iX * 0.5F);
					noise += Math.max(0.0F, (float) wallSize - (float) (sizeX - iX) * 0.5F);
					noise += Math.max(0.0F, (float) wallSize - (float) iZ * 0.5F);
					noise += Math.max(0.0F, (float) wallSize - (float) (sizeZ - iZ) * 0.5F);

					if (noise >= 0.5F) {
						double perlin1 = perlinNoise1.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);

						if (perlin1 * (double) noise >= 0.5D) {
							double perlin2 = perlinNoise2.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);

							if (perlin1 * perlin2 * (double) noise >= 0.5D) {
								continue;
							}
						}
					}

					blockInfoList.add(new BlockInfo(iX, iY, iZ, fillBlock.getDefaultState(), null));
				}
			}
		}

		return new DungeonPartBlock(world, dungeonGenerator, startPos.add(-wallSize, -wallSize, -wallSize), blockInfoList, new PlacementSettings(), DungeonInhabitantManager.DEFAULT_DUNGEON_INHABITANT);
	}

}
