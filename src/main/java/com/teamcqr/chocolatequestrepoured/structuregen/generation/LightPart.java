package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class LightPart implements IStructure {

	private BlockPos startPos;
	private BlockPos endPos;

	public LightPart(NBTTagCompound compound) {
		this.readFromNBT(compound);
	}

	public LightPart(BlockPos startPos, BlockPos endPos) {
		this.startPos = startPos;
		this.endPos = endPos;
	}

	@Override
	public void generate(World world) {
		for (BlockPos.MutableBlockPos mutablePos : BlockPos.getAllInBoxMutable(this.startPos, this.endPos)) {
			BlockPos pos = mutablePos.toImmutable();
			world.checkLight(pos);
			Chunk chunk = world.getChunkFromBlockCoords(pos);
			IBlockState state = chunk.getBlockState(pos);
			world.markAndNotifyBlock(pos, chunk, state, state, 2);
		}
	}

	@Override
	public boolean canGenerate(World world) {
		return true;
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setString("id", "lightPart");
		compound.setTag("startPos", NBTUtil.createPosTag(this.startPos));
		compound.setTag("endPos", NBTUtil.createPosTag(this.endPos));

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.startPos = NBTUtil.getPosFromTag(compound.getCompoundTag("startPos"));
		this.endPos = NBTUtil.getPosFromTag(compound.getCompoundTag("endPos"));
	}

	@Override
	public BlockPos getPos() {
		return this.startPos;
	}

	@Override
	public BlockPos getSize() {
		return this.endPos.subtract(this.startPos);
	}

}
