package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;

public class DungeonPartLight extends AbstractDungeonPart {

	public static final String ID = "dungeon_part_light";
	private int chunkX;
	private int chunkZ;
	private BlockPos.MutableBlockPos mutablePos1;
	private BlockPos.MutableBlockPos mutablePos2;
	private BlockPos.MutableBlockPos mutablePos3;

	public DungeonPartLight(World world, DungeonGenerator dungeonGenerator, BlockPos minPos, BlockPos maxPos) {
		super(world, dungeonGenerator, DungeonGenUtils.getValidMinPos(minPos, maxPos));
		this.maxPos = DungeonGenUtils.getValidMaxPos(minPos, maxPos);
		if (!CQRConfig.advanced.instantLightUpdates) {
			this.chunkX = this.minPos.getX() >> 4;
			this.chunkZ = this.minPos.getZ() >> 4;
			this.mutablePos1 = new BlockPos.MutableBlockPos(this.minPos);
			this.mutablePos2 = new BlockPos.MutableBlockPos(this.minPos);
			this.mutablePos3 = new BlockPos.MutableBlockPos(this.minPos);
		} else {
			this.chunkX = Integer.MAX_VALUE;
			this.chunkZ = Integer.MAX_VALUE;
			this.mutablePos1 = new BlockPos.MutableBlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
			this.mutablePos2 = new BlockPos.MutableBlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
			this.mutablePos3 = new BlockPos.MutableBlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		}
	}

	public DungeonPartLight(World world, DungeonGenerator dungeonGenerator, NBTTagCompound compound) {
		super(world, dungeonGenerator, compound);
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = super.writeToNBT();
		compound.setInteger("chunkX", this.chunkX);
		compound.setInteger("chunkZ", this.chunkZ);
		compound.setTag("pos1", DungeonGenUtils.writePosToList(this.mutablePos1));
		compound.setTag("pos2", DungeonGenUtils.writePosToList(this.mutablePos2));
		compound.setTag("pos3", DungeonGenUtils.writePosToList(this.mutablePos3));
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.chunkX = compound.getInteger("chunkX");
		this.chunkZ = compound.getInteger("chunkZ");
		this.mutablePos1 = new BlockPos.MutableBlockPos(DungeonGenUtils.readPosFromList(compound.getTagList("pos1", Constants.NBT.TAG_INT)));
		this.mutablePos2 = new BlockPos.MutableBlockPos(DungeonGenUtils.readPosFromList(compound.getTagList("pos2", Constants.NBT.TAG_INT)));
		this.mutablePos3 = new BlockPos.MutableBlockPos(DungeonGenUtils.readPosFromList(compound.getTagList("pos3", Constants.NBT.TAG_INT)));
	}

	@Override
	public String getId() {
		return DungeonPartLight.ID;
	}

	@Override
	public void generateNext() {
		if (this.chunkX <= this.maxPos.getX() >> 4) {
			this.world.getChunkFromChunkCoords(this.chunkX, this.chunkZ).generateSkylightMap();

			this.chunkZ++;
			if (this.chunkZ > this.maxPos.getZ() >> 4) {
				this.chunkZ = this.minPos.getZ() >> 4;
				this.chunkX++;
				if (this.chunkX > this.maxPos.getX() >> 4) {
					CQRMain.logger.info("1 Light: {}", System.currentTimeMillis() - this.dungeonGenerator.t);
				}
			}
		} else if (this.mutablePos1.getX() <= this.maxPos.getX()) {
			this.world.checkLight(this.mutablePos1);

			if (this.mutablePos1.getZ() < this.maxPos.getZ()) {
				this.mutablePos1.setPos(this.mutablePos1.getX(), this.mutablePos1.getY(), this.mutablePos1.getZ() + 1);
			} else if (this.mutablePos1.getY() < this.maxPos.getY()) {
				this.mutablePos1.setPos(this.mutablePos1.getX(), this.mutablePos1.getY() + 1, this.minPos.getZ());
			} else {
				this.mutablePos1.setPos(this.mutablePos1.getX() + 1, this.minPos.getY(), this.minPos.getZ());
				if (this.mutablePos1.getX() > this.maxPos.getX()) {
					CQRMain.logger.info("2 Light: {}", System.currentTimeMillis() - this.dungeonGenerator.t);
				}
			}
		} else if (this.mutablePos2.getX() <= this.maxPos.getX()) {
			((WorldServer) this.world).getPlayerChunkMap().markBlockForUpdate(this.mutablePos2);

			if (this.mutablePos2.getZ() < this.maxPos.getZ()) {
				this.mutablePos2.setPos(this.mutablePos2.getX(), this.mutablePos2.getY(), this.mutablePos2.getZ() + 1);
			} else if (this.mutablePos2.getY() < this.maxPos.getY()) {
				this.mutablePos2.setPos(this.mutablePos2.getX(), this.mutablePos2.getY() + 1, this.minPos.getZ());
			} else {
				this.mutablePos2.setPos(this.mutablePos2.getX() + 1, this.minPos.getY(), this.minPos.getZ());
				if (this.mutablePos2.getX() > this.maxPos.getX()) {
					CQRMain.logger.info("3 Light: {}", System.currentTimeMillis() - this.dungeonGenerator.t);
				}
			}
		} else if (this.mutablePos3.getX() <= this.maxPos.getX()) {
			IBlockState state = this.world.getBlockState(this.mutablePos3);
			this.world.notifyNeighborsRespectDebug(this.mutablePos3, state.getBlock(), false);

			if (this.mutablePos3.getZ() < this.maxPos.getZ()) {
				this.mutablePos3.setPos(this.mutablePos3.getX(), this.mutablePos3.getY(), this.mutablePos3.getZ() + 1);
			} else if (this.mutablePos3.getY() < this.maxPos.getY()) {
				this.mutablePos3.setPos(this.mutablePos3.getX(), this.mutablePos3.getY() + 1, this.minPos.getZ());
			} else {
				this.mutablePos3.setPos(this.mutablePos3.getX() + 1, this.minPos.getY(), this.minPos.getZ());
				if (this.mutablePos3.getX() > this.maxPos.getX()) {
					CQRMain.logger.info("4 Light: {}", System.currentTimeMillis() - this.dungeonGenerator.t);
				}
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return this.mutablePos3.getX() > this.maxPos.getX();
	}

}
