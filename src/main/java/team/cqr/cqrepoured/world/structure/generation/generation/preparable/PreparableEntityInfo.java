package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement.MutableVec3d;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableEntityInfo;

public class PreparableEntityInfo {

	private final NBTTagCompound entityData;

	public PreparableEntityInfo(NBTTagCompound entityData) {
		this.entityData = entityData;
	}

	public static Optional<PreparableEntityInfo> create(BlockPos structurePos, Entity entity) {
		NBTTagCompound entityData = new NBTTagCompound();
		if (!entity.writeToNBTOptional(entityData)) {
			return Optional.empty();
		}
		cleanEntityTag(structurePos, entity, entityData);
		return Optional.of(new PreparableEntityInfo(entityData));
	}

	private static void cleanEntityTag(BlockPos structurePos, Entity entity, NBTTagCompound entityTag) {
		entityTag.removeTag("UUIDMost");
		entityTag.removeTag("UUIDLeast");

		NBTTagList nbtTagList = entityTag.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
		nbtTagList.set(0, new NBTTagDouble(entity.posX - structurePos.getX()));
		nbtTagList.set(1, new NBTTagDouble(entity.posY - structurePos.getY()));
		nbtTagList.set(2, new NBTTagDouble(entity.posZ - structurePos.getZ()));
		if (entity instanceof EntityHanging) {
			BlockPos blockpos = ((EntityHanging) entity).getHangingPosition();
			entityTag.setInteger("TileX", blockpos.getX() - structurePos.getX());
			entityTag.setInteger("TileY", blockpos.getY() - structurePos.getY());
			entityTag.setInteger("TileZ", blockpos.getZ() - structurePos.getZ());
		}

		NBTTagList passengers = entityTag.getTagList("Passengers", Constants.NBT.TAG_COMPOUND);
		for (NBTBase passengerTag : passengers) {
			cleanEntityTag(structurePos, entity, (NBTTagCompound) passengerTag);
		}
	}

	@Nullable
	public GeneratableEntityInfo prepare(World world, DungeonPlacement placement) {
		Entity entity = prepareEntity(world, placement, this.entityData);
		if (entity == null) {
			return null;
		}
		return new GeneratableEntityInfo(entity);
	}

	@Nullable
	private static Entity prepareEntity(World world, DungeonPlacement placement, NBTTagCompound entityTag) {
		Entity entity = EntityList.createEntityFromNBT(entityTag, world);

		if (entity == null) {
			return null;
		}

		double x;
		double y;
		double z;

		if (entity instanceof EntityHanging) {
			x = entityTag.getInteger("TileX");
			y = entityTag.getInteger("TileY");
			z = entityTag.getInteger("TileZ");
			if (entity instanceof EntityPainting && placement.getMirror() != Mirror.NONE) {
				int n = ((((EntityPainting) entity).art.sizeX >> 4) + 1) & 1;
				switch (((EntityPainting) entity).facingDirection.rotateYCCW()) {
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
			NBTTagList tagList = entityTag.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
			MutableVec3d vec = placement.transform(tagList.getDoubleAt(0), tagList.getDoubleAt(1), tagList.getDoubleAt(2));
			x = vec.x;
			y = vec.y;
			z = vec.z;
		}

		float transformedYaw = placement.transform(entity);
		entity.setLocationAndAngles(x, y, z, transformedYaw, entity.rotationPitch);
		entity.setRenderYawOffset(transformedYaw);
		entity.setRotationYawHead(transformedYaw);

		NBTTagList passengers = entityTag.getTagList("Passengers", Constants.NBT.TAG_COMPOUND);
		for (NBTBase passengerNBT : passengers) {
			Entity passenger = prepareEntity(world, placement, (NBTTagCompound) passengerNBT);
			if (passenger != null) {
				passenger.startRiding(entity);
			}
		}

		return entity;
	}

	public NBTTagCompound getEntityData() {
		return this.entityData;
	}

}
