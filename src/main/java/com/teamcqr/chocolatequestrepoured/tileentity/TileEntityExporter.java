package com.teamcqr.chocolatequestrepoured.tileentity;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.CQSaveStructureRequestPacket;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityExporter /*extends TileEntitySyncClient*/ extends TileEntity
{
	public int startX = 0;
	public int startY = 0;
	public int startZ = 0;
	public int endX = 0;
	public int endY = 0;
	public int endZ = 0;
	public String structureName = "NoName";
	public boolean partModeUsing = false;
	
	private EntityPlayer user = null;
	
	public TileEntityExporter() {
	}

	public NBTTagCompound getExporterData() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("StartX", startX);
		compound.setInteger("StartY", startY);
		compound.setInteger("StartZ", startZ);
		compound.setInteger("EndX", endX);
		compound.setInteger("EndY", endY);
		compound.setInteger("EndZ", endZ);
		compound.setString("StructureName", structureName);

		return compound;
	}

	public void setExporterData(NBTTagCompound compound) {
		startX = compound.getInteger("StartX");
		startY = compound.getInteger("StartY");
		startZ = compound.getInteger("StartZ");
		endX = compound.getInteger("EndX");
		endY = compound.getInteger("EndY");
		endZ = compound.getInteger("EndZ");
		structureName = compound.getString("StructureName");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		 super.writeToNBT(compound);
		 compound.setInteger("StartX", startX);
		 compound.setInteger("StartY", startY);
		 compound.setInteger("StartZ", startZ);
		 compound.setInteger("EndX", endX);
		 compound.setInteger("EndY", endY);
		 compound.setInteger("EndZ", endZ);
		 compound.setString("StructureName", structureName);
		 return compound;
	} 

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		startX = compound.getInteger("StartX");
		startY = compound.getInteger("StartY");
		startZ = compound.getInteger("StartZ");
		endX = compound.getInteger("EndX");
		endY = compound.getInteger("EndY");
		endZ = compound.getInteger("EndZ");
		structureName = compound.getString("StructureName");
	}
	
	public void setValues(int sX, int sY, int sZ, int eX, int eY, int eZ, String structName, boolean usePartMode)
	{
		startX = sX;
		startY = sY;
		startZ = sZ;
		endX = eX;
		endY = eY;
		endZ = eZ;
		partModeUsing = usePartMode;
		structureName = structName;
		
		//if(!world.isRemote) {
			markDirty();
		//} else {
			world.notifyBlockUpdate(this.getPos(), this.getBlockType().getDefaultState(), this.getBlockType().getDefaultState(), 2);
		//}
	}

	//Not used anymore
	/*
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		 return new SPacketUpdateTileEntity(this.pos, 7, this.getUpdateTag());
	}
	 */

	//Not used anymore
	/*
	@Override
	public NBTTagCompound getUpdateTag() {
		if(world.isRemote) {
			return writeToNBT(new NBTTagCompound());
		}
		return super.getUpdateTag();
	}
	 */
	
	//Not needed anymore succeeded by getExporterData
	/*
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		if(!world.isRemote) {
			super.handleUpdateTag(tag);

			startX = tag.getInteger("sX");
			startY = tag.getInteger("sY");
			startZ = tag.getInteger("sZ");

			endX = tag.getInteger("eX");
			endY = tag.getInteger("eY");
			endZ = tag.getInteger("eZ");

			partModeUsing = tag.getBoolean("partmode");

			structureName = tag.getString("sName");
		}
	}
	 */

	//Not needed anymore succeeded by setExporterData
	/*
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		
		NBTTagCompound tag = pkt.getNbtCompound();
		
		startX = tag.getInteger("sX");
		startY = tag.getInteger("sY");
		startZ = tag.getInteger("sZ");

		endX = tag.getInteger("eX");
		endY = tag.getInteger("eY");
		endZ = tag.getInteger("eZ");
		
		partModeUsing = tag.getBoolean("partmode");
		
		structureName = tag.getString("sName");
	}
	*/

	
	public void setUser(EntityPlayer player) {
		this.user = player;
	}
	
	public void saveStructure(World world, BlockPos startPos, BlockPos endPos, String authorName) 
	{
		if(!world.isRemote) {
			CQStructure structure = new CQStructure(this.structureName, true);
			structure.setAuthor(authorName);
			
			structure.save(world, startPos, endPos, this.partModeUsing, this.user);
		}
		System.out.println("Sending structure save request packet...");
		CQRMain.NETWORK.sendToServer(new CQSaveStructureRequestPacket(startPos, endPos, authorName, this.structureName, true, this.partModeUsing));
		System.out.println("Packet sent!");
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return (128 + (endX - startX) + (endY - startY) + (endZ - startZ))^2; 
	}
	
	/*private boolean isPlayerInRange(double x, double y, double z, double range) 
    {
        for(int i = 0; i < this.world.playerEntities.size(); ++i) 
        {
            EntityPlayer player = this.world.playerEntities.get(i);
 
                double playerDistance = player.getDistanceSq(x, y, z);
 
                if(range < 0 || playerDistance < range * range) 
                {
                    return true;
                }
        }
        return false;
    }*/
	
	public AxisAlignedBB getDimensionIndicatorBoudingBox() {
		AxisAlignedBB aabb = super.getRenderBoundingBox();
		aabb = aabb.offset(new BlockPos(startX, startY, startZ));
		aabb = aabb.expand(endX, endY, endZ);
		
		return aabb;
	}
}