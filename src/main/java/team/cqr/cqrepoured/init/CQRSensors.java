package team.cqr.cqrepoured.init;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.services.CQRServices;
import team.cqr.cqrepoured.entity.ai.sensor.FactionBasedAttackTargetSensor;
import team.cqr.cqrepoured.entity.ai.sensor.undead.BuriedSensor;
import team.cqr.cqrepoured.entity.ai.sensor.undead.GraveSpotSensor;

public final class CQRSensors {

	// Vanilla sensors
	public static final RegistryObject<SensorType<FactionBasedAttackTargetSensor<?>>> FACTION_ATTACK_TARGET = CQRServices.ENTITY_AI.registerSensor("faction_attack_target", FactionBasedAttackTargetSensor::new);
	
	// Undead
	public static final RegistryObject<SensorType<BuriedSensor<?>>> BURIED = CQRServices.ENTITY_AI.registerSensor("buried", BuriedSensor::new);
	public static final RegistryObject<SensorType<GraveSpotSensor<?>>> GRAVE_SPOT = CQRServices.ENTITY_AI.registerSensor("grave_spot", GraveSpotSensor::new);
	
}
