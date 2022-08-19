package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;

public interface IEntityFactory {

	<T extends Entity> T createEntity(EntityType<T> entityType);

	<T extends Entity> T createEntity(Function<World, T> entityConstructor);

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
	default Entity createEntity(CompoundNBT nbt) {
		Entity entity = EntityType.by(nbt).map(this::createEntity).orElse(null);
		if (entity == null) {
			CQRMain.logger.warn("Skipping Entity with id {}", nbt.getString("id"));
			return null;
		}
		entity.load(nbt);
		return entity;
	}

	@Nullable
	static CompoundNBT save(Entity entity) {
		CompoundNBT nbt = new CompoundNBT();
		return entity.save(nbt) ? nbt : null;
	}

}
