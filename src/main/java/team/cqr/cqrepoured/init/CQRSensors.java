package team.cqr.cqrepoured.init;

import java.util.function.Supplier;

import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.ai.sensor.FactionBasedAttackTargetSensor;

public final class CQRSensors {

	public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, CQRMain.MODID);

	// Vanilla sensors
	public static final RegistryObject<SensorType<FactionBasedAttackTargetSensor<?>>> FACTION_ATTACK_TARGET = register("faction_attack_target", FactionBasedAttackTargetSensor::new);

	private static <T extends ExtendedSensor<?>> RegistryObject<SensorType<T>> register(String id, Supplier<T> sensor) {
		return SENSORS.register(id, () -> new SensorType<>(sensor));
	}
	
}
