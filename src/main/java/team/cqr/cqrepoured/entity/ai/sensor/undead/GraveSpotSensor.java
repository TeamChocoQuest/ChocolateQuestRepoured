package team.cqr.cqrepoured.entity.ai.sensor.undead;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import team.cqr.cqrepoured.entity.bases.UndeadEntityBase;
import team.cqr.cqrepoured.init.CQRMemoryModuleTypes;
import team.cqr.cqrepoured.init.CQRSensors;

/*
 * Sensor for identifying and memorising nearby positions that can be used as a grave.
 * Positions are stored in {@link CQRMemoryModuleTypes#GRAVE_SPOTS}
 * Only positions where the entity fits above are added
 * The block at that position has to be tagged as dirt
 */
public class GraveSpotSensor<E extends UndeadEntityBase<?>> extends NearbyBlocksSensor<E> {

	private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(CQRMemoryModuleTypes.GRAVE_SPOTS.get());
	
	public GraveSpotSensor() {
		setPredicate((state, entity) -> {
			if (state.isAir()) {
				return false;
			}
			return (state.is(BlockTags.DIRT));
		});
	}
	
	@Override
	public List<MemoryModuleType<?>> memoriesUsed() {
		return MEMORIES;
	}

	@Override
	public SensorType<? extends ExtendedSensor<?>> type() {
		return CQRSensors.GRAVE_SPOT.get();
	}
	
	public GraveSpotSensor<E> setRadius(double radius) {
		return setRadius(radius, radius);
	}

	public GraveSpotSensor<E> setRadius(double xz, double y) {
		super.setRadius(xz, y);

		return this;
	}
	
	@Override
	protected void doTick(ServerLevel level, E entity) {
		List<BlockPos> positions = new ObjectArrayList<>();
		
		for (BlockPos pos : BlockPos.betweenClosed(entity.blockPosition().subtract(this.radius.toVec3i()), entity.blockPosition().offset(this.radius.toVec3i()))) {
			boolean collided = false;
			Iterable<VoxelShape> collisions = level.getBlockCollisions(entity, entity.getBoundingBox().move(pos.above()));
			for (VoxelShape vs : collisions) {
				if (!vs.isEmpty()) {
					collided = true;
					break;
				}
			}
			if (!collided) {
				positions.add(pos);
			}
		}

		if (positions.isEmpty()) {
			BrainUtils.clearMemory(entity, CQRMemoryModuleTypes.GRAVE_SPOTS.get());
		}
		else {
			BrainUtils.setMemory(entity, CQRMemoryModuleTypes.GRAVE_SPOTS.get(), positions);
		}
	}
	
	

}
