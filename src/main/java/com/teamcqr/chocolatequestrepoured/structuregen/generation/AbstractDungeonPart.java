package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class AbstractDungeonPart {

	protected final World world;
	protected final DungeonGenerator dungeonGenerator;
	protected BlockPos partPos;
	protected BlockPos minPos;
	protected BlockPos maxPos;

	public AbstractDungeonPart(World world, DungeonGenerator dungeonGenerator, BlockPos partPos) {
		this.world = world;
		this.dungeonGenerator = dungeonGenerator;
		this.partPos = partPos;
		this.minPos = partPos;
		this.maxPos = partPos;
	}

	public AbstractDungeonPart(World world, DungeonGenerator dungeonGenerator, NBTTagCompound compound) {
		this.world = world;
		this.dungeonGenerator = dungeonGenerator;
		this.readFromNBT(compound);
	}

	public static AbstractDungeonPart createDungeonPart(World world, DungeonGenerator dungeonGenerator, NBTTagCompound compound) {
		if (compound.hasKey("id", Constants.NBT.TAG_STRING)) {
			String id = compound.getString("id");
			try {
				switch (id) {
				case DungeonPartBlock.ID:
					return new DungeonPartBlock(world, dungeonGenerator, compound);
				case DungeonPartBlockSpecial.ID:
					return new DungeonPartBlockSpecial(world, dungeonGenerator, compound);
				case DungeonPartCover.ID:
					return new DungeonPartCover(world, dungeonGenerator, compound);
				case DungeonPartEntity.ID:
					return new DungeonPartEntity(world, dungeonGenerator, compound);
				case DungeonPartLight.ID:
					return new DungeonPartLight(world, dungeonGenerator, compound);
				case DungeonPartPlateau.ID:
					return new DungeonPartPlateau(world, dungeonGenerator, compound);
				default:
					return null;
				}
			} catch (Exception e) {
				CQRMain.logger.error("Failed to create dungeon part for id " + id, e);
			}
		}

		return null;
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("id", this.getId());
		compound.setTag("partPos", NBTUtil.createPosTag(this.partPos));
		compound.setTag("minPos", NBTUtil.createPosTag(this.minPos));
		compound.setTag("maxPos", NBTUtil.createPosTag(this.maxPos));
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		if (!compound.hasKey("id") || !compound.getString("id").equals(this.getId())) {
			throw new IllegalArgumentException();
		}
		this.partPos = NBTUtil.getPosFromTag(compound.getCompoundTag("partPos"));
		this.minPos = NBTUtil.getPosFromTag(compound.getCompoundTag("minPos"));
		this.maxPos = NBTUtil.getPosFromTag(compound.getCompoundTag("maxPos"));
	}

	public abstract String getId();

	public abstract void generateNext();

	public abstract boolean isGenerated();

	public BlockPos getPartPos() {
		return this.partPos;
	}

	public BlockPos getMinPos() {
		return this.minPos;
	}

	public BlockPos getMaxPos() {
		return this.maxPos;
	}

	protected void updateMinAndMaxPos(BlockPos pos) {
		this.minPos = DungeonGenUtils.getValidMinPos(pos, this.minPos);
		this.maxPos = DungeonGenUtils.getValidMaxPos(pos, this.maxPos);
	}

}
