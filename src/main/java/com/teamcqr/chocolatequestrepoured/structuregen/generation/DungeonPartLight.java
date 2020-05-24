package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class DungeonPartLight extends AbstractDungeonPart {

	public static final String ID = "dungeon_part_light";
	private int chunkX;
	private int chunkZ;
	private int x1;
	private int y1;
	private int z1;
	private int x2;
	private int z2;

	public DungeonPartLight(World world, DungeonGenerator dungeonGenerator, BlockPos minPos, BlockPos maxPos) {
		super(world, dungeonGenerator, DungeonGenUtils.getValidMinPos(minPos, maxPos));
		this.maxPos = DungeonGenUtils.getValidMaxPos(minPos, maxPos);
		this.chunkX = this.minPos.getX() >> 4;
		this.chunkZ = this.minPos.getZ() >> 4;
		this.x1 = this.minPos.getX();
		this.y1 = this.minPos.getY();
		this.z1 = this.minPos.getZ();
		this.x2 = this.minPos.getX();
		this.z2 = this.minPos.getZ();
		if (CQRConfig.advanced.instantLightUpdates) {
			this.chunkX = Integer.MAX_VALUE;
			this.x1 = Integer.MAX_VALUE;
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
		compound.setInteger("x1", this.x1);
		compound.setInteger("y1", this.y1);
		compound.setInteger("z1", this.z1);
		compound.setInteger("x2", this.x2);
		compound.setInteger("z2", this.z2);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.chunkX = compound.getInteger("chunkX");
		this.chunkZ = compound.getInteger("chunkZ");
		this.x1 = compound.getInteger("x1");
		this.y1 = compound.getInteger("y1");
		this.z1 = compound.getInteger("z1");
		this.x2 = compound.getInteger("x2");
		this.z2 = compound.getInteger("z2");
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
			}
		} else if (this.x1 <= this.maxPos.getX()) {
			BlockPos pos = new BlockPos(this.x1, this.y1, this.z1);
			this.world.checkLight(pos);
			Chunk chunk = this.world.getChunkFromBlockCoords(pos);
			IBlockState state = chunk.getBlockState(pos);
			this.world.markAndNotifyBlock(pos, chunk, state, state, 18);

			this.z1++;
			if (this.z1 > this.maxPos.getZ()) {
				this.z1 = this.minPos.getZ();
				this.y1++;
				if (this.y1 > this.maxPos.getY()) {
					this.y1 = this.minPos.getY();
					this.x1++;
				}
			}
		} else if (this.x2 <= this.maxPos.getX()) {
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(this.x2, this.minPos.getY(), this.z2);
			int oldChunkY = minPos.getY() >> 4;
			Chunk chunk = this.world.getChunkFromChunkCoords(this.x2 >> 4, this.z2 >> 4);
			ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[oldChunkY];
			BlockPos.MutableBlockPos oldPos = new BlockPos.MutableBlockPos(this.x2, this.minPos.getY() == 0 ? 1 : this.minPos.getY() - 1, this.z2);
			IBlockState oldState = chunk.getBlockState(oldPos);

			for (int y2 = this.minPos.getY(); y2 <= this.maxPos.getY(); y2++) {
				int chunkY = y2 >> 4;

				if (chunkY != oldChunkY) {
					extendedBlockStorage = chunk.getBlockStorageArray()[chunkY];
					oldChunkY = chunkY;
				}

				if (extendedBlockStorage != Chunk.NULL_BLOCK_STORAGE) {
					IBlockState state = extendedBlockStorage.get(this.x2 & 15, y2 & 15, this.z2 & 15);

					if (state.getBlock() instanceof BlockLiquid) {
						state.neighborChanged(this.world, pos.setPos(this.x2, y2, this.z2), oldState.getBlock(), oldPos);
					}

					oldState = state;
					oldPos.setPos(this.x2, y2, this.z2);
				} else {
					y2 += 15 - (y2 & 15);
					oldState = Blocks.AIR.getDefaultState();
					oldPos.setY(y2);
				}
			}

			this.z2++;
			if (this.z2 > this.maxPos.getZ()) {
				this.z2 = this.minPos.getZ();
				this.x2++;
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return this.x2 > this.maxPos.getX();
	}

}
