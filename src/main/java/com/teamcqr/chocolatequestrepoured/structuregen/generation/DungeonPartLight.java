package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.EnumSet;

import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;

public class DungeonPartLight extends AbstractDungeonPart {

	private int chunkX;
	private int chunkZ;
	private BlockPos.MutableBlockPos mutablePos1;
	private BlockPos.MutableBlockPos mutablePos2;
	private BlockPos.MutableBlockPos mutablePos3;

	public DungeonPartLight(World world, DungeonGenerator dungeonGenerator) {
		this(world, dungeonGenerator, BlockPos.ORIGIN, BlockPos.ORIGIN);
	}

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
			this.mutablePos3 = new BlockPos.MutableBlockPos(this.minPos);
		}
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
		return DUNGEON_PART_LIGHT_ID;
	}

	@Override
	public void generateNext() {
		if (this.chunkX <= this.maxPos.getX() >> 4) {
			this.world.getChunk(this.chunkX, this.chunkZ).generateSkylightMap();

			this.chunkZ++;
			if (this.chunkZ > this.maxPos.getZ() >> 4) {
				this.chunkZ = this.minPos.getZ() >> 4;
				this.chunkX++;
			}
		} else if (this.mutablePos1.getX() <= this.maxPos.getX()) {
			this.world.checkLight(this.mutablePos1);

			if (this.mutablePos1.getZ() < this.maxPos.getZ()) {
				this.mutablePos1.setPos(this.mutablePos1.getX(), this.mutablePos1.getY(), this.mutablePos1.getZ() + 1);
			} else if (this.mutablePos1.getY() < this.maxPos.getY()) {
				this.mutablePos1.setPos(this.mutablePos1.getX(), this.mutablePos1.getY() + 1, this.minPos.getZ());
			} else {
				this.mutablePos1.setPos(this.mutablePos1.getX() + 1, this.minPos.getY(), this.minPos.getZ());
			}
		} else if (this.mutablePos2.getX() <= this.maxPos.getX()) {
			((WorldServer) this.world).getPlayerChunkMap().markBlockForUpdate(this.mutablePos2);

			if (this.mutablePos2.getZ() < this.maxPos.getZ()) {
				this.mutablePos2.setPos(this.mutablePos2.getX(), this.mutablePos2.getY(), this.mutablePos2.getZ() + 1);
			} else if (this.mutablePos2.getY() < this.maxPos.getY()) {
				this.mutablePos2.setPos(this.mutablePos2.getX(), this.mutablePos2.getY() + 1, this.minPos.getZ());
			} else {
				this.mutablePos2.setPos(this.mutablePos2.getX() + 1, this.minPos.getY(), this.minPos.getZ());
			}
		} else if (this.mutablePos3.getX() <= this.maxPos.getX()) {
			//IBlockState state = this.world.getBlockState(this.mutablePos3);
			//this.world.notifyNeighborsRespectDebug(this.mutablePos3, state.getBlock(), false);
			notifyNeighborsRespectDebug(world, mutablePos3);

			if (this.mutablePos3.getZ() < this.maxPos.getZ()) {
				this.mutablePos3.setPos(this.mutablePos3.getX(), this.mutablePos3.getY(), this.mutablePos3.getZ() + 1);
			} else if (this.mutablePos3.getY() < this.maxPos.getY()) {
				this.mutablePos3.setPos(this.mutablePos3.getX(), this.mutablePos3.getY() + 1, this.minPos.getZ());
			} else {
				this.mutablePos3.setPos(this.mutablePos3.getX() + 1, this.minPos.getY(), this.minPos.getZ());
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return this.mutablePos3.getX() > this.maxPos.getX();
	}

	private void notifyNeighborsRespectDebug(World world, BlockPos pos) {
		if (world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			return;
		}
		
		IBlockState state = world.getBlockState(pos);

		if (ForgeEventFactory.onNeighborNotify(world, pos, state, EnumSet.allOf(EnumFacing.class), false).isCanceled()) {
			return;
		}

		Block block = state.getBlock();
		BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
		world.neighborChanged(p.setPos(pos.getX() + 1, pos.getY(), pos.getZ()), block, pos);
		world.neighborChanged(p.setPos(pos.getX() - 1, pos.getY(), pos.getZ()), block, pos);
		world.neighborChanged(p.setPos(pos.getX(), pos.getY() + 1, pos.getZ()), block, pos);
		world.neighborChanged(p.setPos(pos.getX(), pos.getY() - 1, pos.getZ()), block, pos);
		world.neighborChanged(p.setPos(pos.getX(), pos.getY(), pos.getZ() + 1), block, pos);
		world.neighborChanged(p.setPos(pos.getX(), pos.getY(), pos.getZ() - 1), block, pos);
	}

}
