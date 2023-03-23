package team.cqr.cqrepoured.util;

import javax.annotation.Nullable;

import meldexun.reflectionutil.ReflectionMethod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;

public class StructureHelper {

	private static final ReflectionMethod<Boolean> METHOD_CAN_SPAWN_STRUCTURE_AT_COORDS = new ReflectionMethod<>(Structure.class, "func_75047_a", "canSpawnStructureAtCoords", Integer.TYPE, Integer.TYPE);

	public static boolean isStructureInRange(World world, BlockPos pos, int radius, String name) {
		return false;
		/*if (!world.getWorldInfo().isMapFeaturesEnabled()) {
			return false;
		}

		if (name.equals("AW2")) {
			// check for aw2 structures
			if (!CQRMain.isAW2Installed) {
				return false;
			}

			return AW2Integration.isAW2StructureInRange(world, pos, radius);
		}

		ChunkGenerator chunkGenerator = ((ServerWorld) world).getChunkProvider().chunkGenerator;

		if (chunkGenerator instanceof OverworldChunkGenerator || chunkGenerator instanceof NetherChunkGenerator || chunkGenerator instanceof EndChunkGenerator || chunkGenerator instanceof FlatChunkGenerator || chunkGenerator instanceof DebugChunkGenerator) {
			// vanilla chunk generator
			Structure structureGenerator = getStructureGenerator(world, name);

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

		return false;*/
	}

	@Nullable
	private static Structure getStructureGenerator(World world, String name) {
		/*ChunkGenerator chunkGenerator = ((ServerWorld) world).getChunkProvider().chunkGenerator;

		if (chunkGenerator instanceof OverworldChunkGenerator) {
			if (!((OverworldChunkGenerator) chunkGenerator).mapFeaturesEnabled) {
				return null;
			}
			ChunkGeneratorSettings settings = ((OverworldChunkGenerator) chunkGenerator).settings;
			switch (name) {
			case "Stronghold":
				if (!settings.useStrongholds) {
					return null;
				}
				return ((OverworldChunkGenerator) chunkGenerator).strongholdGenerator;
			case "Village":
				if (!settings.useVillages) {
					return null;
				}
				return ((OverworldChunkGenerator) chunkGenerator).villageGenerator;
			case "Mineshaft":
				if (!settings.useMineShafts) {
					return null;
				}
				return ((OverworldChunkGenerator) chunkGenerator).mineshaftGenerator;
			case "Temple":
				if (!settings.useTemples) {
					return null;
				}
				return ((OverworldChunkGenerator) chunkGenerator).scatteredFeatureGenerator;
			case "Monument":
				if (!settings.useMonuments) {
					return null;
				}
				return ((OverworldChunkGenerator) chunkGenerator).oceanMonumentGenerator;
			case "Mansion":
				if (!settings.useMansions) {
					return null;
				}
				return ((OverworldChunkGenerator) chunkGenerator).woodlandMansionGenerator;
			default:
				break;
			}
		} else if (chunkGenerator instanceof NetherChunkGenerator) {
			if (!((NetherChunkGenerator) chunkGenerator).generateStructures) {
				return null;
			}
			if (name.equals("Fortress")) {
				return ((NetherChunkGenerator) chunkGenerator).genNetherBridge;
			}
		} else if (chunkGenerator instanceof EndChunkGenerator) {
			if (!((EndChunkGenerator) chunkGenerator).mapFeaturesEnabled) {
				return null;
			}
			if (name.equals("EndCity")) {
				return ((EndChunkGenerator) chunkGenerator).endCityGen;
			}
		} else if (chunkGenerator instanceof FlatChunkGenerator) {
			return ((FlatChunkGenerator) chunkGenerator).structureGenerators.get(name);
		}*/

		return null;
	}

	private static boolean isStructureInRange(World world, Structure structureType, BlockPos pos, int radius) {
		int x = pos.getX() >> 4;
		int z = pos.getZ() >> 4;
		/*Random random = structureType.rand;

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

					if (structureType instanceof net.minecraft.world.gen.feature.structure.MineshaftStructure) {
						random.setSeed(x2 ^ z2 ^ world.getSeed());
						random.nextInt();
					}

					if (METHOD_CAN_SPAWN_STRUCTURE_AT_COORDS.invoke(structureType, x2, z2)) {
						return true;
					}
				}
			}
		}*/

		return false;
	}

}
