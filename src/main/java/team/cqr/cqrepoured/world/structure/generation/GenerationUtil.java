package team.cqr.cqrepoured.world.structure.generation;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.util.Perlin3D;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.ICQRLevel;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBossInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableForceFieldNexusInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableLootChestInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableSpawnerInfo;

public class GenerationUtil {

	private static DungeonPlacement placement;

	public static void init(DungeonPlacement placement) {
		GenerationUtil.placement = placement;
	}

	public static void reset() {
		placement = null;
	}

	public static void setBoss(ICQRLevel level, BlockPos pos) {
		new PreparableBossInfo((CompoundNBT) null).prepare(level, pos, placement);
	}

	public static void setNexus(ICQRLevel level, BlockPos pos) {
		new PreparableForceFieldNexusInfo().prepare(level, pos, placement);
	}

	public static void setLootChest(ICQRLevel level, BlockPos pos, ResourceLocation lootTable, Direction facing) {
		new PreparableLootChestInfo(lootTable, facing).prepare(level, pos, placement);
	}

	public static void setSpawner(ICQRLevel level, BlockPos pos, Entity... entities) {
		new PreparableSpawnerInfo(entities).prepare(level, pos, placement);
	}

	public static void setSpawner(ICQRLevel level, BlockPos pos, Collection<Entity> entities) {
		new PreparableSpawnerInfo(entities).prepare(level, pos, placement);
	}

	public static void makeRandomBlob2(final ICQRLevel level, Block fillBlock, BlockPos startPos, BlockPos endPos, int wallSize, long seed, final BlockPos offset) {
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
	
					level.setBlockState(offset.offset(iX, iY, iZ), fillBlock.defaultBlockState());
				}
			}
		}
	}

	// TODO Create custom DungeonPart class because Perlin3D is to slow to calculate immediately
	public static void makeRandomBlob(final ICQRLevel level, Block fillBlock, BlockPos startPos, BlockPos endPos, int wallSize, long seed, final BlockPos offset) {
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
					level.setBlockState(offset.offset(iX, iY, iZ), fillBlock.defaultBlockState());
				}
			}
		}
	}

}
