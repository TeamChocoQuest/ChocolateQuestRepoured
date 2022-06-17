package team.cqr.cqrepoured.init;

import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class CQRConfiguredStructures {
	
	public static StructureFeature<?, ?> CONFIGURED_WALL_IN_THE_NORTH = CQRStructures.WALL_IN_THE_NORTH.get().configured(IFeatureConfig.NONE);
    /* public static StructureFeature<?, ?> CONFIGURED_SKY_BATTLE_TOWER = BTStructures
            .SKY_BATTLE_TOWER.get().configured(IFeatureConfig.NONE); */
	
	// Config holds data that will be available to the structure class during generation => Use custom class for this!
	// Codec => Used to save and load the config

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        
        Registry.register(registry, new ResourceLocation(CQRMain.MODID, "wall_in_the_north"), CONFIGURED_WALL_IN_THE_NORTH);

        /* Ok so, this part may be hard to grasp but basically, just add your structure to this to
         * prevent any sort of crash or issue with other mod's custom ChunkGenerators. If they use
         * FlatGenerationSettings.STRUCTURE_FEATURES in it and you don't add your structure to it, the game
         * could crash later when you attempt to add the StructureSeparationSettings to the dimension.
         *
         * (It would also crash with superflat worldtype if you omit the below line
         * and attempt to add the structure's StructureSeparationSettings to the world)
         *
         * Note: If you want your structure to spawn in superflat, remove the FlatChunkGenerator check
         * in StructureTutorialMain.addDimensionalSpacing and then create a superflat world, exit it,
         * and re-enter it and your structures will be spawning. I could not figure out why it needs
         * the restart but honestly, superflat is really buggy and shouldn't be your main focus in my opinion.
         *
         * Requires AccessTransformer ( see resources/META-INF/accesstransformer.cfg )
         */
        FlatGenerationSettings.STRUCTURE_FEATURES.put(CQRStructures.WALL_IN_THE_NORTH.get(), CONFIGURED_WALL_IN_THE_NORTH);
        // FlatGenerationSettings.STRUCTURE_FEATURES.put(BTStructures.SKY_BATTLE_TOWER.get(), CONFIGURED_SKY_BATTLE_TOWER);
        
        //Now, parse all structures (dungeons) in CQRSTructures.DUNGEON_STRUCTURES and register those, the dungeon class will receive a method to generate teh structure feature to be registered as well as a getter for the structure object
        for(Map.Entry<DungeonBase, RegistryObject<Structure<?>>> entry : CQRStructures.DUNGEON_ENTRIES.entrySet()) {
        	//TODO: Check if we need that featureconfig at all, if yes => properly implement it!
        	StructureFeature<?, ?> sf = entry.getValue().get().configured(IFeatureConfig.NONE);
        	
        	Registry.register(registry, CQRMain.prefix(entry.getValue().getId().getPath()), sf);
        	FlatGenerationSettings.STRUCTURE_FEATURES.put(entry.getValue().get(), sf);
        	CQRStructures.DUNGEON_CONFIGURED_ENTRIES.putIfAbsent(entry.getKey(), sf);
        }
    }

}
