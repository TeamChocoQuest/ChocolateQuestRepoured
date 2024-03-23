package team.cqr.cqrepoured.faction.init;

import java.util.List;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.services.CQRServices;

public class FactionMemoryModuleTypes {
	
	public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEAREST_ALLIES = CQRServices.ENTITY_AI.registerMemoryModuleType("nearest_allies");
	public static final RegistryObject<MemoryModuleType<NearestVisibleLivingEntities>> NEAREST_VISIBLE_ALLIES = CQRServices.ENTITY_AI.registerMemoryModuleType("nearest_visible_enemies");
	public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEAREST_ENEMIES = CQRServices.ENTITY_AI.registerMemoryModuleType("nearest_allies");
	public static final RegistryObject<MemoryModuleType<NearestVisibleLivingEntities>> NEAREST_VISIBLE_ENEMIES = CQRServices.ENTITY_AI.registerMemoryModuleType("nearest_visible_enemies");

}
