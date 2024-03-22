package team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement.MutableVec3d;

public class PreparableEntityInfo {

	private final CompoundTag entityData;

	public PreparableEntityInfo(Entity entity, BoundingBox boundingBox) {
		this.entityData = EntityFactory.save(entity);
		this.entityData.remove("UUIDMost");
		this.entityData.remove("UUIDLeast");
		ListTag nbtTagList = this.entityData.getList("Pos", Tag.TAG_DOUBLE);
		nbtTagList.set(0, DoubleTag.valueOf(entity.getX() - boundingBox.minX()));
		nbtTagList.set(1, DoubleTag.valueOf(entity.getY() - boundingBox.minY()));
		nbtTagList.set(2, DoubleTag.valueOf(entity.getZ() - boundingBox.minZ()));
		if (entity instanceof HangingEntity) {
			BlockPos blockpos = ((HangingEntity) entity).getPos();
			this.entityData.putInt("TileX", blockpos.getX() - boundingBox.minX());
			this.entityData.putInt("TileY", blockpos.getY() - boundingBox.minY());
			this.entityData.putInt("TileZ", blockpos.getZ() - boundingBox.minZ());
		}
	}

	public PreparableEntityInfo(CompoundTag entityData) {
		this.entityData = entityData;
	}

	public void prepare(StructureLevel level, DungeonPlacement placement) {
		Entity entity = placement.entityFactory().createEntity(this.entityData);
		double x;
		double y;
		double z;
		
		// TODO: Create registry <EntityType<?>, Function<Entity, Vec3> to make this more extendable and customizable
		if (entity instanceof HangingEntity) {
			x = this.entityData.getInt("TileX");
			y = this.entityData.getInt("TileY");
			z = this.entityData.getInt("TileZ");
			if (entity instanceof Painting && placement.mirror() != Mirror.NONE) {
				int n = ((((Painting) entity).getWidth() >> 4) + 1) & 1;
				switch (((Painting) entity).getDirection().getCounterClockWise()) {
				case NORTH:
					z -= n;
					break;
				case EAST:
					x += n;
					break;
				case SOUTH:
					z += n;
					break;
				case WEST:
					x -= n;
					break;
				default:
					break;
				}
			}
			BlockPos pos = placement.transform((int) x, (int) y, (int) z);
			x = pos.getX();
			y = pos.getY();
			z = pos.getZ();
		} else {
			ListTag tagList = this.entityData.getList("Pos", Tag.TAG_DOUBLE);
			MutableVec3d vec = placement.transform(tagList.getDouble(0), tagList.getDouble(1), tagList.getDouble(2));
			x = vec.x;
			y = vec.y;
			z = vec.z;
		}

		float transformedYaw = placement.transform(entity);
		entity.moveTo(x, y, z, transformedYaw, entity.getXRot());
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).setYBodyRot(transformedYaw);
			((LivingEntity) entity).setYHeadRot(transformedYaw);
		}
		
		if (entity instanceof Mob mob) {
			placement.inhabitant().prepare(mob, placement.random());
		}

		level.addEntity(entity);
	}

	public CompoundTag getEntityData() {
		return this.entityData;
	}

}
