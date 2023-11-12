package team.cqr.cqrepoured.entity.bases;

import java.util.Map;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

public interface ISynchedDataHelper {
	
	public static class DataHolder<T> {
		private T value;
		
		public DataHolder(T initialValue) {
			this.value = initialValue;
		}
		
		public T get() {
			return this.value;
		}
		
		public void set(T value) {
			this.value = value;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public default Map<EntityDataAccessor, DataHolder> createAccessHolderMap() {
		Map<EntityDataAccessor, DataHolder> result = new Object2ObjectArrayMap<>();
		this.registerMappings(result::put);
		return result;
	}
	
	public void registerMappings(@SuppressWarnings("rawtypes") BiConsumer<EntityDataAccessor, DataHolder> registerFunction);
	
	@SuppressWarnings("rawtypes")
	public Map<EntityDataAccessor, DataHolder> accessHolderMap();
	
	public default <T> void setSided(EntityDataAccessor<T> accessor, DataHolder<T> clientHolder, T value) {
		Entity self = (Entity)this;
		
		if (self.level().isClientSide()) {
			clientHolder.set(value);
		} else {
			self.getEntityData().set(accessor, value);
		}
	}
	
	public default <T> T getSided(EntityDataAccessor<T> accessor, T defaultValue) {
		@SuppressWarnings("unchecked")
		DataHolder<T> holder = accessHolderMap().get(accessor);
		if (holder != null) {
			return holder.get();
		}
		return defaultValue;
	}
	
	@Nullable
	public default <T> T getSided(EntityDataAccessor<T> accessor) {
		return this.getSided(accessor, null);
	}
	
	public default void callOnSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		Entity self = (Entity)this;
		if (!self.level().isClientSide()) {
			return;
		}
		@SuppressWarnings("unchecked")
		DataHolder<Object> holder = accessHolderMap().getOrDefault(pKey, null);
		if (holder != null) {
			holder.set((Object)self.getEntityData().get(pKey));
		}
	}

}
