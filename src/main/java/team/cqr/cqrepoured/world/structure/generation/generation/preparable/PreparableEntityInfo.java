package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement.MutableVec3d;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableEntityInfo;

public class PreparableEntityInfo implements IPreparable<GeneratableEntityInfo> {

	private final CompoundNBT entityData;

	public PreparableEntityInfo(BlockPos structurePos, Entity entity) {
		this.entityData = new CompoundNBT();
		entity.writeToNBTOptional(this.entityData);
		this.entityData.removeTag("UUIDMost");
		this.entityData.removeTag("UUIDLeast");
		ListNBT nbtTagList = this.entityData.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
		nbtTagList.set(0, new DoubleNBT(entity.posX - structurePos.getX()));
		nbtTagList.set(1, new DoubleNBT(entity.posY - structurePos.getY()));
		nbtTagList.set(2, new DoubleNBT(entity.posZ - structurePos.getZ()));
		if (entity instanceof HangingEntity) {
			BlockPos blockpos = ((HangingEntity) entity).getHangingPosition();
			this.entityData.setInteger("TileX", blockpos.getX() - structurePos.getX());
			this.entityData.setInteger("TileY", blockpos.getY() - structurePos.getY());
			this.entityData.setInteger("TileZ", blockpos.getZ() - structurePos.getZ());
		}
	}

	public PreparableEntityInfo(CompoundNBT entityData) {
		this.entityData = entityData;
	}

	@Override
	public GeneratableEntityInfo prepareNormal(World world, DungeonPlacement placement) {
		Entity entity = EntityList.createEntityFromNBT(this.entityData, world);
		double x;
		double y;
		double z;

		if (entity instanceof HangingEntity) {
			x = this.entityData.getInteger("TileX");
			y = this.entityData.getInteger("TileY");
			z = this.entityData.getInteger("TileZ");
			if (entity instanceof PaintingEntity && placement.getMirror() != Mirror.NONE) {
				int n = ((((PaintingEntity) entity).art.sizeX >> 4) + 1) & 1;
				switch (((PaintingEntity) entity).facingDirection.rotateYCCW()) {
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
			ListNBT tagList = this.entityData.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
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

	@Override
	public GeneratableEntityInfo prepareDebug(World world, DungeonPlacement placement) {
		return this.prepareNormal(world, placement);
	}

	public CompoundNBT getEntityData() {
		return this.entityData;
	}

}
