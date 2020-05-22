package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class DungeonPartLight extends AbstractDungeonPart {

	public static final String ID = "dungeon_part_light";
	private int chunkX;
	private int chunkZ;
	private int x1;
	private int y1;
	private int z1;
	private int x2;
	private int y2;
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
		this.y2 = this.minPos.getY();
		this.z2 = this.minPos.getZ();
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
		compound.setInteger("y2", this.y2);
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
		this.y2 = compound.getInteger("y2");
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
			BlockPos pos = new BlockPos(this.x2, this.y2, this.z2);
			IBlockState state = this.world.getBlockState(pos);

			if (state.getBlock() instanceof BlockLiquid) {
				BlockPos pos2 = pos.getY() > 0 ? pos.down() : pos.up();
				IBlockState state2 = this.world.getBlockState(pos2);
				state.neighborChanged(this.world, pos, state2.getBlock(), pos2);
			}

			this.z2++;
			if (this.z2 > this.maxPos.getZ()) {
				this.z2 = this.minPos.getZ();
				this.y2++;
				if (this.y2 > this.maxPos.getY()) {
					this.y2 = this.minPos.getY();
					this.x2++;
				}
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return this.x2 > this.maxPos.getX();
	}

}
