package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

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
import team.cqr.cqrepoured.world.structure.generation.generation.CQRLevel;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement.MutableVec3d;
import team.cqr.cqrepoured.world.structure.generation.generation.IEntityFactory;

public class PreparableEntityInfo {

	private final CompoundTag entityData;

	public PreparableEntityInfo(BlockPos templatePos, Entity entity) {
		this.entityData = IEntityFactory.save(entity);
		this.entityData.remove("UUIDMost");
		this.entityData.remove("UUIDLeast");
		ListTag nbtTagList = this.entityData.getList("Pos", Tag.TAG_DOUBLE);
		nbtTagList.set(0, DoubleTag.valueOf(entity.getX() - templatePos.getX()));
		nbtTagList.set(1, DoubleTag.valueOf(entity.getY() - templatePos.getY()));
		nbtTagList.set(2, DoubleTag.valueOf(entity.getZ() - templatePos.getZ()));
		if (entity instanceof HangingEntity) {
			BlockPos blockpos = ((HangingEntity) entity).getPos();
			this.entityData.putInt("TileX", blockpos.getX() - templatePos.getX());
			this.entityData.putInt("TileY", blockpos.getY() - templatePos.getY());
			this.entityData.putInt("TileZ", blockpos.getZ() - templatePos.getZ());
		}
	}

	public PreparableEntityInfo(CompoundTag entityData) {
		this.entityData = entityData;
	}

	public void prepare(CQRLevel level, DungeonPlacement placement) {
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
