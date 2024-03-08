package team.cqr.cqrepoured.service.impl;

import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.services.interfaces.EntityAIService;

public class EntityAIServiceImpl implements EntityAIService {
	
	public static final DeferredRegister<MemoryModuleType<?>> MEMORY_TYPES = DeferredRegister.create(ForgeRegistries.Keys.MEMORY_MODULE_TYPES, CQRepoured.MODID);
	public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, CQRepoured.MODID);

	@Override
	public <T extends ExtendedSensor<?>> RegistryObject<SensorType<T>> registerSensor(String id, Supplier<T> sensor) {
		return SENSORS.register(id, () -> new SensorType<>(sensor));
	}

	@Override
	public <T> RegistryObject<MemoryModuleType<T>> registerMemoryModuleType(String id, Codec<T> codec) {
		return MEMORY_TYPES.register(id, () -> new MemoryModuleType<T>(Optional.ofNullable(codec)));
	}

}
