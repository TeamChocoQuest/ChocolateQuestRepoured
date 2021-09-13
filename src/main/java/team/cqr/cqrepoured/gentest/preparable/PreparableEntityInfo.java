package team.cqr.cqrepoured.gentest.preparable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.gentest.DungeonPlacement;
import team.cqr.cqrepoured.gentest.DungeonPlacement.MutableVec3d;
import team.cqr.cqrepoured.gentest.generatable.GeneratableEntityInfo;

public class PreparableEntityInfo implements IPreparable {

	private final NBTTagCompound entityData;

	public PreparableEntityInfo(BlockPos structurePos, Entity entity) {
		this.entityData = new NBTTagCompound();
		entity.writeToNBTOptional(this.entityData);
		this.entityData.removeTag("UUIDMost");
		this.entityData.removeTag("UUIDLeast");
		NBTTagList nbtTagList = this.entityData.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
		nbtTagList.set(0, new NBTTagDouble(entity.posX - structurePos.getX()));
		nbtTagList.set(1, new NBTTagDouble(entity.posY - structurePos.getY()));
		nbtTagList.set(2, new NBTTagDouble(entity.posZ - structurePos.getZ()));
		if (entity instanceof EntityHanging) {
			BlockPos blockpos = ((EntityHanging) entity).getHangingPosition();
			this.entityData.setInteger("TileX", blockpos.getX() - structurePos.getX());
			this.entityData.setInteger("TileY", blockpos.getY() - structurePos.getY());
			this.entityData.setInteger("TileZ", blockpos.getZ() - structurePos.getZ());
		}
	}

	public PreparableEntityInfo(NBTTagCompound entityData) {
		this.entityData = entityData;
	}

	@Override
	public GeneratableEntityInfo prepare(World world, DungeonPlacement placement) {
		Entity entity = EntityList.createEntityFromNBT(this.entityData, world);
		double x;
		double y;
		double z;

		if (entity instanceof EntityHanging) {
			x = this.entityData.getInteger("TileX");
			y = this.entityData.getInteger("TileY");
			z = this.entityData.getInteger("TileZ");
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
			NBTTagList tagList = this.entityData.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
			MutableVec3d vec = placement.transform(tagList.getDoubleAt(0), tagList.getDoubleAt(1), tagList.getDoubleAt(2));
			x = vec.x;
			y = vec.y;
			z = vec.z;
		}

		float transformedYaw = placement.transform(entity);
		entity.setLocationAndAngles(x, y, z, transformedYaw, entity.rotationPitch);
		entity.setRenderYawOffset(transformedYaw);
		entity.setRotationYawHead(transformedYaw);
		return new GeneratableEntityInfo(entity);
	}

	public NBTTagCompound getEntityData() {
		return this.entityData;
	}

}
