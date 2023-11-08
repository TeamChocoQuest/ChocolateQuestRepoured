package team.cqr.cqrepoured.init;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRConstants;

public class CQRMemoryModuleTypes {
	
	public static void init() {}
	public static final DeferredRegister<MemoryModuleType<?>> MEMORY_TYPES = DeferredRegister.create(ForgeRegistries.Keys.MEMORY_MODULE_TYPES, CQRConstants.MODID);
	

	public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEAREST_ALLIES = register("nearest_allies");
	public static final RegistryObject<MemoryModuleType<NearestVisibleLivingEntities>> NEAREST_VISIBLE_ALLIES = register("nearest_visible_allies");

	private static <T> RegistryObject<MemoryModuleType<T>> register(String id) {
		return register(id, null);
	}

	private static <T> RegistryObject<MemoryModuleType<T>> register(String id, @Nullable Codec<T> codec) {
		return MEMORY_TYPES.register(id, () -> new MemoryModuleType<T>(Optional.ofNullable(codec)));
	}

}
