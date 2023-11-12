package team.cqr.cqrepoured.entity.bases;

import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;

public class UndeadEntityBase<T extends UndeadEntityBase<T>> extends WalkingEntityBase<T> implements ISynchedDataHelper {
	
	protected static final EntityDataAccessor<Boolean> BURIED = SynchedEntityData.defineId(UndeadEntityBase.class, EntityDataSerializers.BOOLEAN);
	protected final DataHolder<Boolean> buriedHolder = new DataHolder<>(false);
	
	@SuppressWarnings("rawtypes")
	private final Map<EntityDataAccessor, DataHolder> dataAccessorMapping = this.createAccessHolderMap();

	public UndeadEntityBase(EntityType<? extends VariantEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Override
	public BrainActivityGroup<? extends T> getCoreTasks() {
		return super.getCoreTasks();
	}

	@Override
	public BrainActivityGroup<? extends T> getIdleTasks() {
		return super.getIdleTasks();
	}
	
	@Override
	public BrainActivityGroup<? extends T> getFightTasks() {
		return super.getFightTasks();
	}

	public void setBuried(boolean value) {
		if (this.level().isClientSide()) {
			return;
		}
		this.getEntityData().set(BURIED, value);
	}
	
	public boolean isBuried() {
		return this.getSided(BURIED, false);
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		super.onSyncedDataUpdated(pKey);
		
		this.callOnSyncedDataUpdated(pKey);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void registerMappings(BiConsumer<EntityDataAccessor, DataHolder> registerFunction) {
		registerFunction.accept(BURIED, buriedHolder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<EntityDataAccessor, DataHolder> accessHolderMap() {
		return dataAccessorMapping;
	}

	
}
