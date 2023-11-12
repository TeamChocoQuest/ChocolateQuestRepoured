package team.cqr.cqrepoured.init;

import java.util.function.Supplier;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.entity.ai.sensor.FactionBasedAttackTargetSensor;
import team.cqr.cqrepoured.entity.ai.sensor.NearbyAlliesSensor;
import team.cqr.cqrepoured.entity.ai.sensor.undead.BuriedSensor;
import team.cqr.cqrepoured.entity.ai.sensor.undead.GraveSpotSensor;

public final class CQRSensors {

	public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, CQRConstants.MODID);

	// Vanilla sensors
	public static final RegistryObject<SensorType<FactionBasedAttackTargetSensor<?>>> FACTION_ATTACK_TARGET = register("faction_attack_target", FactionBasedAttackTargetSensor::new);
	public static final RegistryObject<SensorType<NearbyAlliesSensor<?>>> NEARBY_ALLIES = register("nearby_allies", NearbyAlliesSensor::new);
	
	// Undead
	public static final RegistryObject<SensorType<BuriedSensor<?>>> BURIED = register("buried", BuriedSensor::new);
	public static final RegistryObject<SensorType<GraveSpotSensor<?>>> GRAVE_SPOT = register("grave_spot", GraveSpotSensor::new);

	private static <T extends ExtendedSensor<?>> RegistryObject<SensorType<T>> register(String id, Supplier<T> sensor) {
		return SENSORS.register(id, () -> new SensorType<>(sensor));
	}
	
}
