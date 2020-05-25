package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.util.Constants;

public class EntityInfo implements IGeneratable {

	private NBTTagCompound entityData;

	public EntityInfo(BlockPos structurePos, Entity entity) {
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

	public EntityInfo(NBTTagCompound compound) {
		this.entityData = compound;
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, EDungeonMobType dungeonMob, ProtectedRegion protectedRegion) {
		Entity entity;
		try {
			entity = EntityList.createEntityFromNBT(this.entityData, world);
		} catch (Exception e) {
			entity = null;
		}
		if (entity != null) {
			float transformedYaw = this.getTransformedYaw(entity.rotationYaw, settings.getMirror(), settings.getRotation());
			Vec3d vec;
			if (entity instanceof EntityHanging) {
				vec = new Vec3d(this.entityData.getInteger("TileX"), this.entityData.getInteger("TileY"), this.entityData.getInteger("TileZ"));
			} else {
				vec = DungeonGenUtils.readVecFromList(this.entityData.getTagList("Pos", Constants.NBT.TAG_DOUBLE));
			}
			Vec3d transformedVec = DungeonGenUtils.transformedVec3d(vec, settings).addVector(dungeonPartPos.getX(), dungeonPartPos.getY(), dungeonPartPos.getZ());
			entity.setLocationAndAngles(transformedVec.x, transformedVec.y, transformedVec.z, transformedYaw, entity.rotationPitch);
			entity.setRenderYawOffset(transformedYaw);
			entity.setRotationYawHead(transformedYaw);
			world.spawnEntity(entity);
		}
	}

	public NBTTagCompound getEntityData() {
		return this.entityData;
	}

	public BlockPos getPos() {
		NBTTagList nbtTagList = this.entityData.getTagList("pos", Constants.NBT.TAG_DOUBLE);
		return new BlockPos(nbtTagList.getDoubleAt(0), nbtTagList.getDoubleAt(1), nbtTagList.getDoubleAt(2));
	}

	private float getTransformedYaw(float rotationYaw, Mirror mirror, Rotation rotation) {
		float f = MathHelper.wrapDegrees(rotationYaw);
		switch (mirror) {
		case LEFT_RIGHT:
			f = 180.0F - f;
			break;
		case FRONT_BACK:
			f = -f;
			break;
		default:
			break;
		}
		switch (rotation) {
		case CLOCKWISE_90:
			f += 90.0F;
			break;
		case CLOCKWISE_180:
			f += 180.0F;
			break;
		case COUNTERCLOCKWISE_90:
			f -= 90.0F;
			break;
		default:
			break;
		}
		return f;
	}

}
