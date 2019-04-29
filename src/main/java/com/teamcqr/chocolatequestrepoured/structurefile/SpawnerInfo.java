package com.teamcqr.chocolatequestrepoured.structurefile;

import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockSpawner;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.NBTUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

class SpawnerInfo {

	private BlockPos position;
	private TileEntitySpawner spawnerTile;
	private NBTTagCompound spawnerData = null;
	
	public SpawnerInfo(BlockSpawner spawnerBlock, BlockPos pos, World world) {
		this.position = pos;
		if(world.getTileEntity(pos) != null) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileEntitySpawner) {
				this.spawnerTile = (TileEntitySpawner)tile;
			}
		}
	}
	
	public SpawnerInfo(NBTTagCompound nbtTag) {
		if(nbtTag.getString("type").equalsIgnoreCase("cq_spawner")) {
			this.position = NBTUtil.BlockPosFromNBT(nbtTag.getCompoundTag("position"));
			this.spawnerData = nbtTag.getCompoundTag("data");
		}
	}
	
	public NBTTagCompound getSpawnerData() {
		return this.spawnerData;
	}
	
	public NBTTagCompound getAsNBTTag() {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setString("type", "cq_spawner");
		
		appendPos(tag);
		
		NBTTagCompound spawnerTag = new NBTTagCompound();
		spawnerTag = this.spawnerTile.writeToNBT(spawnerTag);
		
		tag.setTag("data", spawnerTag);
		
		return tag;
	}
	
	private void appendPos(NBTTagCompound compound) {
		compound.setTag("position", NBTUtil.BlockPosToNBTTag(this.position));
	}
	
	public BlockPos getPos() {
		return this.position;
	}

}
