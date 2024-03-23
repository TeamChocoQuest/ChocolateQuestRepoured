package team.cqr.cqrepoured.faction.init;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.services.CQRServices;
import team.cqr.cqrepoured.faction.entity.ai.sensor.NearbyAlliesSensor;
import team.cqr.cqrepoured.faction.entity.ai.sensor.NearbyEnemiesSensor;

public class FactionSensors {

	public static final RegistryObject<SensorType<NearbyAlliesSensor<?>>> NEARBY_ALLIES = CQRServices.ENTITY_AI.registerSensor("nearby_allies", NearbyAlliesSensor::new);
	public static final RegistryObject<SensorType<NearbyEnemiesSensor<?>>> NEARBY_ENEMIES = CQRServices.ENTITY_AI.registerSensor("nearby_enemies", NearbyEnemiesSensor::new);
	
}
