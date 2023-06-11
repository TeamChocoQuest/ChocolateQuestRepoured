package team.cqr.cqrepoured.world.structure.generation.generation;

import org.apache.commons.lang3.Validate;

import net.minecraft.nbt.CompoundNBT;

public class EntityContainer {

	private Entity entity;
	private CompoundNBT entityNbt;

	public EntityContainer(Entity entity) {
		Validate.notNull(entity);
		this.entity = entity;
	}

	public EntityContainer(CompoundNBT entityNbt) {
		Validate.notNull(entityNbt);
		this.entityNbt = entityNbt;
	}

	public Entity getEntity(IEntityFactory entityFactory) {
		if (entity == null) {
			entity = entityFactory.createEntity(entityNbt);
		}
		return entity;
	}

	public CompoundNBT getEntityNbt() {
		if (entityNbt == null) {
			entityNbt = IEntityFactory.save(entity);
		}
		return entityNbt;
	}

}
