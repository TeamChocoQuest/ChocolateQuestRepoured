package team.cqr.cqrepoured.util;

import java.util.Random;

import meldexun.reflectionutil.ReflectionMethod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenStructure;

public class VanillaStructureHelper {

	private static final ReflectionMethod<Boolean> METHOD_CAN_SPAWN_STRUCTURE_AT_COORDS = new ReflectionMethod<>(MapGenStructure.class, "func_75047_a",
			"canSpawnStructureAtCoords", Integer.TYPE, Integer.TYPE);

	// ChunkGeneratorFlat
	private static final String[] STRUCTURE_NAMES = { "Stronghold", "Village", "Mineshaft", "Temple", "Monument", "Mansion", "Fortress", "EndCity" };

	public static boolean isStructureInRange(World worldIn, BlockPos startPos, int radius) {
		if (worldIn.isRemote) {
			return false;
		}

		if (!worldIn.getWorldInfo().isMapFeaturesEnabled()) {
			return false;
		}

		IChunkGenerator chunkGenerator = ((WorldServer) worldIn).getChunkProvider().chunkGenerator;

		if (chunkGenerator instanceof ChunkGeneratorOverworld) {
			ChunkGeneratorSettings settings = ((ChunkGeneratorOverworld) chunkGenerator).settings;

			if (settings.useStrongholds && isStructureInRange(worldIn, ((ChunkGeneratorOverworld) chunkGenerator).strongholdGenerator, startPos, radius)) {
				return true;
			}
			if (settings.useVillages && isStructureInRange(worldIn, ((ChunkGeneratorOverworld) chunkGenerator).villageGenerator, startPos, radius)) {
				return true;
			}
			if (settings.useMineShafts && isStructureInRange(worldIn, ((ChunkGeneratorOverworld) chunkGenerator).mineshaftGenerator, startPos, radius)) {
				return true;
			}
			if (settings.useTemples && isStructureInRange(worldIn, ((ChunkGeneratorOverworld) chunkGenerator).scatteredFeatureGenerator, startPos, radius)) {
				return true;
			}
			if (settings.useMonuments && isStructureInRange(worldIn, ((ChunkGeneratorOverworld) chunkGenerator).oceanMonumentGenerator, startPos, radius)) {
				return true;
			}
			if (settings.useMansions && isStructureInRange(worldIn, ((ChunkGeneratorOverworld) chunkGenerator).woodlandMansionGenerator, startPos, radius)) {
				return true;
			}
		} else if (chunkGenerator instanceof ChunkGeneratorHell) {
			return isStructureInRange(worldIn, ((ChunkGeneratorHell) chunkGenerator).genNetherBridge, startPos, radius);
		} else if (chunkGenerator instanceof ChunkGeneratorEnd) {
			return isStructureInRange(worldIn, ((ChunkGeneratorEnd) chunkGenerator).endCityGen, startPos, radius);
		} else if (chunkGenerator instanceof ChunkGeneratorFlat) {
			for (String structureName : STRUCTURE_NAMES) {
				MapGenStructure structureGenerator = ((ChunkGeneratorFlat) chunkGenerator).structureGenerators.get(structureName);

				if (structureGenerator != null && isStructureInRange(worldIn, structureGenerator, startPos, radius)) {
					return true;
				}
			}
		} else {
			// modded chunk generator
			int x = startPos.getX() >> 4;
			int z = startPos.getZ() >> 4;

			for (String structureName : STRUCTURE_NAMES) {
				try {
					BlockPos pos = worldIn.findNearestStructure(structureName, startPos, false);

					if (pos != null && (Math.abs((pos.getX() >> 4) - x) <= radius || Math.abs((pos.getZ() >> 4) - z) <= radius)) {
						return true;
					}
				} catch (NullPointerException e) {
					// ignore
				}
			}
		}

		return false;
	}

	private static boolean isStructureInRange(World worldIn, MapGenStructure structureType, BlockPos startPos, int radius) {
		int x = startPos.getX() >> 4;
		int z = startPos.getZ() >> 4;
		Random random = structureType.rand;

		structureType.world = worldIn;
		for (int i = 0; i <= radius; ++i) {
			for (int x1 = -i; x1 <= i; ++x1) {
				boolean flag = x1 == -i || x1 == i;

				for (int z1 = -i; z1 <= i; ++z1) {
					boolean flag1 = z1 == -i || z1 == i;

					if (flag || flag1) {
						int x2 = x + x1;
						int z2 = z + z1;

						if (structureType instanceof MapGenMineshaft) {
							random.setSeed(x2 ^ z2 ^ worldIn.getSeed());
							random.nextInt();
						}

						if (METHOD_CAN_SPAWN_STRUCTURE_AT_COORDS.invoke(structureType, x2, z2)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

}
