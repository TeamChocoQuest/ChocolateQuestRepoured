package team.cqr.cqrepoured.common.services.interfaces;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.RegistryObject;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

public interface EntityAIService {
	
	public <T extends ExtendedSensor<?>> RegistryObject<SensorType<T>> registerSensor(String id, Supplier<T> sensor);
	
	public default <T> RegistryObject<MemoryModuleType<T>> registerMemoryModuleType(String id) {
		return registerMemoryModuleType(id, null);
	}
	public <T> RegistryObject<MemoryModuleType<T>> registerMemoryModuleType(String id, @Nullable Codec<T> codec);

}
