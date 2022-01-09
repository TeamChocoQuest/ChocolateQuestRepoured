package team.cqr.cqrepoured.init;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.thewall.StructureWall;

public class CQRWorldGenFeatures {
	
	public static final DeferredRegister<Feature<?>> DEFERRED_REGISTRY_FEATURE = DeferredRegister.create(ForgeRegistries.FEATURES, CQRMain.MODID);
	
	public static final RegistryObject<Feature<?>> WALL_IN_THE_NORTH = DEFERRED_REGISTRY_FEATURE
			.register("wall-in-the-north", () -> new StructureWall(NoFeatureConfig.CODEC)))); 
			
	public static void register(IEventBus bus) {
		DEFERRED_REGISTRY_FEATURE.register(bus);
	}

}
