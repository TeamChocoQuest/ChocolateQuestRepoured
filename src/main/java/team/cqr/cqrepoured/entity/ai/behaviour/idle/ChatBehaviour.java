package team.cqr.cqrepoured.entity.ai.behaviour.idle;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import team.cqr.cqrepoured.faction.entity.FactionEntity;
import team.cqr.cqrepoured.faction.init.FactionMemoryModuleTypes;
import team.cqr.cqrepoured.init.CQRMemoryModuleTypes;

public class ChatBehaviour<E extends FactionEntity<?>> extends ExtendedBehaviour<E> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = ObjectArrayList.of(
			Pair.of(CQRMemoryModuleTypes.CHAT_TARGET.get(), MemoryStatus.VALUE_ABSENT),
			Pair.of(FactionMemoryModuleTypes.NEAREST_VISIBLE_ALLIES.get(), MemoryStatus.VALUE_PRESENT)
	);
	
	public ChatBehaviour() {
		super();
		
		this.taskStartCallback = (entity) -> {
			// Select speechbubble
		};
		this.taskStopCallback = (entity) -> {
			// Clear flags, remove speechbubble
		};
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORIES;
	}
	
	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		return super.checkExtraStartConditions(level, entity);
		
		// Check if there is someone we can talk to
	}
	
	@Override
	protected void start(E entity) {
		super.start(entity);
		// Select entity to talk to
		// Rotate
		// Set look target
	}
	
	@Override
	protected void stop(E entity) {
		super.stop(entity);
		// Deselect entity
		// Reset speechbubble
	}

}
