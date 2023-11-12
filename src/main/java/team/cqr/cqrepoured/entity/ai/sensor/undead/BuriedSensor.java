package team.cqr.cqrepoured.entity.ai.sensor.undead;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import team.cqr.cqrepoured.entity.bases.UndeadEntityBase;
import team.cqr.cqrepoured.init.CQRMemoryModuleTypes;
import team.cqr.cqrepoured.init.CQRSensors;

public class BuriedSensor<E extends UndeadEntityBase<?>> extends ExtendedSensor<E> {
	
	private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(CQRMemoryModuleTypes.BURIED.get());

	@Override
	public List<MemoryModuleType<?>> memoriesUsed() {
		return MEMORIES;
	}

	@Override
	public SensorType<? extends ExtendedSensor<?>> type() {
		return CQRSensors.BURIED.get();
	}
	
	
	
	@Override
	protected void doTick(ServerLevel level, E entity) {
		boolean entityVal = entity.isBuried();
		if (BrainUtils.getMemory(entity, CQRMemoryModuleTypes.BURIED.get()) != entityVal) {
			BrainUtils.setMemory(entity, CQRMemoryModuleTypes.BURIED.get(), entityVal);
		}
	}

}
