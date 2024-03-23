package team.cqr.cqrepoured.faction.entity.ai.sensor;

import java.util.Comparator;
import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import team.cqr.cqrepoured.faction.init.FactionMemoryModuleTypes;
import team.cqr.cqrepoured.faction.init.FactionSensors;

public class NearbyEnemiesSensor<E extends LivingEntity> extends NearbyLivingEntitySensor<E> {
	private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(FactionMemoryModuleTypes.NEAREST_ENEMIES.get(), FactionMemoryModuleTypes.NEAREST_VISIBLE_ENEMIES.get());
	
	@Override
	public List<MemoryModuleType<?>> memoriesUsed() {
		return MEMORIES;
	}
	
	@Override
	public SensorType<? extends ExtendedSensor<?>> type() {
		return FactionSensors.NEARBY_ALLIES.get();
	}
	
	@Override
	protected void doTick(ServerLevel level, E entity) {
		SquareRadius radius = this.radius;

		if (radius == null) {
			double dist = entity.getAttributeValue(Attributes.FOLLOW_RANGE);

			radius = new SquareRadius(dist, dist);
		}

		List<LivingEntity> entities = EntityRetrievalUtil.getEntities(level, entity.getBoundingBox().inflate(radius.xzRadius(), radius.yRadius(), radius.xzRadius()), obj -> obj instanceof LivingEntity livingEntity && predicate().test(livingEntity, entity));

		entities.sort(Comparator.comparingDouble(entity::distanceToSqr));

		BrainUtils.setMemory(entity, FactionMemoryModuleTypes.NEAREST_ENEMIES.get(), entities);
		BrainUtils.setMemory(entity, FactionMemoryModuleTypes.NEAREST_VISIBLE_ENEMIES.get(), new NearestVisibleLivingEntities(entity, entities));
	}

}

