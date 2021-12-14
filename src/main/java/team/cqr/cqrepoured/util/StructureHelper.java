package team.cqr.cqrepoured.util;

import java.util.Random;

import javax.annotation.Nullable;

import meldexun.reflectionutil.ReflectionMethod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenStructure;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.integration.ancientwarfare.AW2Integration;

public class StructureHelper {

	private static final ReflectionMethod<Boolean> METHOD_CAN_SPAWN_STRUCTURE_AT_COORDS = new ReflectionMethod<>(MapGenStructure.class, "func_75047_a", "canSpawnStructureAtCoords", Integer.TYPE, Integer.TYPE);

	public static boolean isStructureInRange(World world, BlockPos pos, int radius, String name) {
		if (!world.getWorldInfo().isMapFeaturesEnabled()) {
			return false;
		}

		if (name.equals("AW2")) {
			// check for aw2 structures
			if (!CQRMain.isAW2Installed) {
				return false;
			}

			return AW2Integration.isAW2StructureInRange(world, pos, radius);
		}

		IChunkGenerator chunkGenerator = ((WorldServer) world).getChunkProvider().chunkGenerator;

		if (chunkGenerator instanceof ChunkGeneratorOverworld || chunkGenerator instanceof ChunkGeneratorHell || chunkGenerator instanceof ChunkGeneratorEnd || chunkGenerator instanceof ChunkGeneratorFlat || chunkGenerator instanceof ChunkGeneratorDebug) {
			// vanilla chunk generator
			MapGenStructure structureGenerator = getStructureGenerator(world, name);

			if (structureGenerator != null) {
				return isStructureInRange(world, structureGenerator, pos, radius);
			}

			return false;
		}

		// modded chunk generator
		BlockPos structurePos;
		try {
			structurePos = chunkGenerator.getNearestStructurePos(world, name, pos, false);
			if (structurePos != null && (Math.abs(structurePos.getX() - pos.getX()) <= radius || Math.abs(structurePos.getZ() - pos.getZ()) <= radius)) {
				return true;
			}
		} catch (NullPointerException e) {
			// ignore
		}
		try {
			structurePos = chunkGenerator.getNearestStructurePos(world, name, pos, true);
			if (structurePos != null && (Math.abs(structurePos.getX() - pos.getX()) <= radius || Math.abs(structurePos.getZ() - pos.getZ()) <= radius)) {
				return true;
			}
		} catch (NullPointerException e) {
			// ignore
		}

		return false;
	}

	@Nullable
	private static MapGenStructure getStructureGenerator(World world, String name) {
		IChunkGenerator chunkGenerator = ((WorldServer) world).getChunkProvider().chunkGenerator;

		if (chunkGenerator instanceof ChunkGeneratorOverworld) {
			if (!((ChunkGeneratorOverworld) chunkGenerator).mapFeaturesEnabled) {
				return null;
			}
			ChunkGeneratorSettings settings = ((ChunkGeneratorOverworld) chunkGenerator).settings;
			switch (name) {
			case "Stronghold":
				if (!settings.useStrongholds) {
					return null;
				}
				return ((ChunkGeneratorOverworld) chunkGenerator).strongholdGenerator;
			case "Village":
				if (!settings.useVillages) {
					return null;
				}
				return ((ChunkGeneratorOverworld) chunkGenerator).villageGenerator;
			case "Mineshaft":
				if (!settings.useMineShafts) {
					return null;
				}
				return ((ChunkGeneratorOverworld) chunkGenerator).mineshaftGenerator;
			case "Temple":
				if (!settings.useTemples) {
					return null;
				}
				return ((ChunkGeneratorOverworld) chunkGenerator).scatteredFeatureGenerator;
			case "Monument":
				if (!settings.useMonuments) {
					return null;
				}
				return ((ChunkGeneratorOverworld) chunkGenerator).oceanMonumentGenerator;
			case "Mansion":
				if (!settings.useMansions) {
					return null;
				}
				return ((ChunkGeneratorOverworld) chunkGenerator).woodlandMansionGenerator;
			default:
				break;
			}
		} else if (chunkGenerator instanceof ChunkGeneratorHell) {
			if (!((ChunkGeneratorHell) chunkGenerator).generateStructures) {
				return null;
			}
			if (name.equals("Fortress")) {
				return ((ChunkGeneratorHell) chunkGenerator).genNetherBridge;
			}
		} else if (chunkGenerator instanceof ChunkGeneratorEnd) {
			if (!((ChunkGeneratorEnd) chunkGenerator).mapFeaturesEnabled) {
				return null;
			}
			if (name.equals("EndCity")) {
				return ((ChunkGeneratorEnd) chunkGenerator).endCityGen;
			}
		} else if (chunkGenerator instanceof ChunkGeneratorFlat) {
			return ((ChunkGeneratorFlat) chunkGenerator).structureGenerators.get(name);
		}

		return null;
	}

	private static boolean isStructureInRange(World world, MapGenStructure structureType, BlockPos pos, int radius) {
		int x = pos.getX() >> 4;
		int z = pos.getZ() >> 4;
		Random random = structureType.rand;

		structureType.world = world;
		for (int r = 0; r <= radius; ++r) {
			for (int x1 = -r; x1 <= r; ++x1) {
				boolean flag = x1 == -r || x1 == r;

				for (int z1 = -r; z1 <= r; ++z1) {
					if (!flag && z1 != -r && z1 != r) {
						continue;
					}

					int x2 = x + x1;
					int z2 = z + z1;

					if (structureType instanceof MapGenMineshaft) {
						random.setSeed(x2 ^ z2 ^ world.getSeed());
						random.nextInt();
					}

					if (METHOD_CAN_SPAWN_STRUCTURE_AT_COORDS.invoke(structureType, x2, z2)) {
						return true;
					}
				}
			}
		}

		return false;
	}

}
