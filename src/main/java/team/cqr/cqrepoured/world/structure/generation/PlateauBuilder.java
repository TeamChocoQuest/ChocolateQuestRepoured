package team.cqr.cqrepoured.world.structure.generation;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.util.Perlin3D;

public class PlateauBuilder {

	// TODO Create custom DungeonPart class because Perlin3D is to slow to calculate immediately
	public static Set<Tuple<BlockPos, BlockState>> makeRandomBlob(Block fillBlock, BlockPos startPos, BlockPos endPos, int wallSize, long seed) {
		Set<Tuple<BlockPos, BlockState>> output = new HashSet<>();
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

					float noise = Math.max(0.0F, 2.0F - (sizeY - iY) / 4.0F);
					noise += Math.max(0.0F, wallSize - iX / 2.0F);
					noise += Math.max(0.0F, wallSize - (sizeX - iX) / 2.0F);
					noise += Math.max(0.0F, wallSize - iZ / 2.0F);
					noise += Math.max(0.0F, wallSize - (sizeZ - iZ) / 2.0F);

					if (noise >= 0.5F) {
						double perlin1 = perlinNoise1.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);

						if (perlin1 * noise >= 0.5D) {
							double perlin2 = perlinNoise2.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);

							if (perlin1 * perlin2 * noise >= 0.5D) {
								continue;
							}
						}
					}
					output.add(new Tuple<>(new BlockPos(iX, iY, iZ), fillBlock.defaultBlockState()));
				}
			}
		}

		return output;
	}

	public static Set<Tuple<BlockPos, BlockState>> makeRandomBlob2(Block fillBlock, BlockPos startPos, BlockPos endPos, int wallSize, long seed) {
		Set<Tuple<BlockPos, BlockState>> output = new HashSet<>();
		Perlin3D perlinNoise1 = new Perlin3D(seed, 8);
		Perlin3D perlinNoise2 = new Perlin3D(seed, 32);

		int sizeX = endPos.getX() - startPos.getX() + 1 + wallSize * 2;
		int sizeZ = endPos.getZ() - startPos.getZ() + 1 + wallSize * 2;
		int sizeY = endPos.getY() - startPos.getY() + 1 + wallSize * 2;

		for (int iX = 0; iX < sizeX; ++iX) {
			for (int iY = 0; iY < sizeY; ++iY) {
				for (int iZ = 0; iZ < sizeZ; ++iZ) {

					float noise = Math.max(0.0F, wallSize - iY * 0.5F);
					noise += Math.max(0.0F, wallSize - (sizeY - iY) * 0.5F);
					noise += Math.max(0.0F, wallSize - iX * 0.5F);
					noise += Math.max(0.0F, wallSize - (sizeX - iX) * 0.5F);
					noise += Math.max(0.0F, wallSize - iZ * 0.5F);
					noise += Math.max(0.0F, wallSize - (sizeZ - iZ) * 0.5F);

					if (noise >= 0.5F) {
						double perlin1 = perlinNoise1.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);

						if (perlin1 * noise >= 0.5D) {
							double perlin2 = perlinNoise2.getNoiseAt(startPos.getX() + iX, startPos.getY() + iY, startPos.getZ() + iZ);

							if (perlin1 * perlin2 * noise >= 0.5D) {
								continue;
							}
						}
					}

					output.add(new Tuple<>(new BlockPos(iX, iY, iZ), fillBlock.defaultBlockState()));
				}
			}
		}

		return output;
	}

}
