package team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import org.apache.commons.lang3.Validate;

public class EntityContainer {

	private Entity entity;
	private CompoundTag entityNbt;

	public EntityContainer(Entity entity) {
		Validate.notNull(entity);
		this.entity = entity;
	}

	public EntityContainer(CompoundTag entityNbt) {
		Validate.notNull(entityNbt);
		this.entityNbt = entityNbt;
	}

	public Entity getEntity(EntityFactory entityFactory) {
		if (entity == null) {
			entity = entityFactory.createEntity(entityNbt);
		}
		return entity;
	}

	public CompoundTag getEntityNbt() {
		if (entityNbt == null) {
			entityNbt = EntityFactory.save(entity);
		}
		return entityNbt;
	}

}
