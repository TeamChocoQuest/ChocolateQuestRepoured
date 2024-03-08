package team.cqr.cqrepoured.faction.init;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.services.CQRServices;
import team.cqr.cqrepoured.faction.entity.ai.sensor.NearbyAlliesSensor;

public class FactionSensors {

	public static final RegistryObject<SensorType<NearbyAlliesSensor<?>>> NEARBY_ALLIES = CQRServices.ENTITY_AI.registerSensor("nearby_allies", NearbyAlliesSensor::new);
	
}
