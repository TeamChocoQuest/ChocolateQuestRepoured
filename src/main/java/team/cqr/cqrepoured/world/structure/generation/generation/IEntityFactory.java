package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.CQRMain;

public interface IEntityFactory {

	<T extends Entity> T createEntity(EntityType<T> entityType);

	<T extends Entity> T createEntity(Function<Level, T> entityConstructor);

	@Nullable
	default Entity createEntity(ResourceLocation registryName) {
		Entity entity = EntityType.byString(registryName.toString()).map(this::createEntity).orElse(null);
		if (entity == null) {
			CQRMain.logger.warn("Skipping Entity with id {}", registryName);
			return null;
		}
		return entity;
	}

	@Nullable
	default Entity createEntity(CompoundTag nbt) {
		Entity entity = EntityType.by(nbt).map(this::createEntity).orElse(null);
		if (entity == null) {
			CQRMain.logger.warn("Skipping Entity with id {}", nbt.getString("id"));
			return null;
		}
		entity.load(nbt);
		return entity;
	}

	@Nullable
	static CompoundTag save(Entity entity) {
		CompoundTag nbt = new CompoundTag();
		return entity.save(nbt) ? nbt : null;
	}

}
