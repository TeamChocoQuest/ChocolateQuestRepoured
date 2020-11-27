package com.teamcqr.chocolatequestrepoured.util;

import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.reflection.ReflectionField;
import com.teamcqr.chocolatequestrepoured.util.reflection.ReflectionMethod;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenEndCity;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.WoodlandMansion;

public class VanillaStructureHelper {

	private static final ReflectionMethod<Boolean> METHOD_CAN_SPAWN_STRUCTURE_AT_COORDS = new ReflectionMethod<>(MapGenStructure.class, "func_75047_a", "canSpawnStructureAtCoords", Integer.TYPE, Integer.TYPE);

	// ChunkGeneratorOverworld
	private static final ReflectionField<ChunkGeneratorSettings> FIELD_SETTINGS = new ReflectionField<>(ChunkGeneratorOverworld.class, "field_186000_s", "settings");
	private static final ReflectionField<MapGenStronghold> FIELD_STRONGHOLD_GENERATOR = new ReflectionField<>(ChunkGeneratorOverworld.class, "field_186004_w", "strongholdGenerator");
	private static final ReflectionField<MapGenVillage> FIELD_VILLAGE_GENERATOR = new ReflectionField<>(ChunkGeneratorOverworld.class, "field_186005_x", "villageGenerator");
	private static final ReflectionField<MapGenMineshaft> FIELD_MINESHAFT_GENERATOR = new ReflectionField<>(ChunkGeneratorOverworld.class, "field_186006_y", "mineshaftGenerator");
	private static final ReflectionField<MapGenScatteredFeature> FIELD_SCATTERED_FEATURE_GENERATOR = new ReflectionField<>(ChunkGeneratorOverworld.class, "field_186007_z", "scatteredFeatureGenerator");
	private static final ReflectionField<StructureOceanMonument> FIELD_OCEAN_MONUMENT_GENERATOR = new ReflectionField<>(ChunkGeneratorOverworld.class, "field_185980_B", "oceanMonumentGenerator");
	private static final ReflectionField<WoodlandMansion> FIELD_WOODLAND_MANSION_GENERATOR = new ReflectionField<>(ChunkGeneratorOverworld.class, "field_191060_C", "woodlandMansionGenerator");

	// ChunkGeneratorHell
	private static final ReflectionField<MapGenNetherBridge> FIELD_GEN_NETHER_BRIDGE = new ReflectionField<>(ChunkGeneratorHell.class, "field_73172_c", "genNetherBridge");

	// ChunkGeneratorEnd
	private static final ReflectionField<MapGenEndCity> FIELD_END_CITY_GEN = new ReflectionField<>(ChunkGeneratorEnd.class, "field_185972_n", "endCityGen");

	// ChunkGeneratorFlat
	private static final String[] STRUCTURE_NAMES = { "Stronghold", "Village", "Mineshaft", "Temple", "Monument", "Mansion", "Fortress", "EndCity" };
	private static final ReflectionField<Map<String, MapGenStructure>> FIELD_STRUCTURE_GENERATORS = new ReflectionField<>(ChunkGeneratorFlat.class, "field_82696_f", "structureGenerators");

	public static boolean isStructureInRange(World worldIn, BlockPos startPos, int radius) {
		if (worldIn.isRemote) {
			return false;
		}

		if (!worldIn.getWorldInfo().isMapFeaturesEnabled()) {
			return false;
		}

		IChunkGenerator chunkGenerator = ((WorldServer) worldIn).getChunkProvider().chunkGenerator;

		if (chunkGenerator instanceof ChunkGeneratorOverworld) {
			ChunkGeneratorSettings settings = FIELD_SETTINGS.get((ChunkGeneratorOverworld) chunkGenerator);

			if (settings.useStrongholds && isStructureInRange(worldIn, FIELD_STRONGHOLD_GENERATOR.get((ChunkGeneratorOverworld) chunkGenerator), startPos, radius)) {
				return true;
			}
			if (settings.useVillages && isStructureInRange(worldIn, FIELD_VILLAGE_GENERATOR.get((ChunkGeneratorOverworld) chunkGenerator), startPos, radius)) {
				return true;
			}
			if (settings.useMineShafts && isStructureInRange(worldIn, FIELD_MINESHAFT_GENERATOR.get((ChunkGeneratorOverworld) chunkGenerator), startPos, radius)) {
				return true;
			}
			if (settings.useTemples && isStructureInRange(worldIn, FIELD_SCATTERED_FEATURE_GENERATOR.get((ChunkGeneratorOverworld) chunkGenerator), startPos, radius)) {
				return true;
			}
			if (settings.useMonuments && isStructureInRange(worldIn, FIELD_OCEAN_MONUMENT_GENERATOR.get((ChunkGeneratorOverworld) chunkGenerator), startPos, radius)) {
				return true;
			}
			if (settings.useMansions && isStructureInRange(worldIn, FIELD_WOODLAND_MANSION_GENERATOR.get((ChunkGeneratorOverworld) chunkGenerator), startPos, radius)) {
				return true;
			}
		} else if (chunkGenerator instanceof ChunkGeneratorHell) {
			return isStructureInRange(worldIn, FIELD_GEN_NETHER_BRIDGE.get((ChunkGeneratorHell) chunkGenerator), startPos, radius);
		} else if (chunkGenerator instanceof ChunkGeneratorEnd) {
			return isStructureInRange(worldIn, FIELD_END_CITY_GEN.get((ChunkGeneratorEnd) chunkGenerator), startPos, radius);
		} else if (chunkGenerator instanceof ChunkGeneratorFlat) {
			Map<String, MapGenStructure> structureGenerators = FIELD_STRUCTURE_GENERATORS.get((ChunkGeneratorFlat) chunkGenerator);

			for (String structureName : STRUCTURE_NAMES) {
				MapGenStructure structureGenerator = structureGenerators.get(structureName);

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
		Random random = new Random();

		for (int i = 0; i <= radius; ++i) {
			for (int x1 = -i; x1 <= i; ++x1) {
				boolean flag = x1 == -i || x1 == i;

				for (int z1 = -i; z1 <= i; ++z1) {
					boolean flag1 = z1 == -i || z1 == i;

					if (flag || flag1) {
						int x2 = x + x1;
						int z2 = z + z1;

						MapGenBase.setupChunkSeed(worldIn.getSeed(), random, x2, z2);
						random.nextInt();

						if (Boolean.TRUE.equals(METHOD_CAN_SPAWN_STRUCTURE_AT_COORDS.invoke(structureType, x2, z2))) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

}
