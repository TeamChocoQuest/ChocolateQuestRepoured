package com.teamcqr.chocolatequestrepoured.tileentity;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.CQSaveStructureRequestPacket;
import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityExporter extends TileEntitySyncClient implements ITickable
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
	}
	
	public void setUser(EntityPlayer player) {
		this.user = player;
	}
	
	@Override
	public void update() {
		//TODO: make fancy size display thingy
		if(!world.isRemote && isPlayerInRange(pos.getX(), pos.getY(), pos.getZ(), getMaxRenderDistanceSquared())) {
			//TODO: Use Tesselator to draw a box that contains the structure (Box texture like worldborder, but not that bold?)
			//https://www.minecraftforge.net/forum/topic/66168-1122-using-minecrafts-tessellator-and-bufferbuilder/
			//https://www.minecraftforge.net/forum/topic/41255-question-regarding-the-vertexbuffer-and-the-old-tesselator/
			//http://jabelarminecraft.blogspot.com/p/minecraft-forge-172-quick-tips-gl11-and.html
		}
	}
	
	public void saveStructure(World world, BlockPos startPos, BlockPos endPos, String authorName) 
	{
		if(!world.isRemote) {
			CQStructure structure = new CQStructure(this.structureName, true);
			structure.setAuthor(authorName);
			
			structure.save(world, startPos, endPos, this.partModeUsing, this.user);
		}
		System.out.println("Sending packet...");
		CQRMain.NETWORK.sendToServer(new CQSaveStructureRequestPacket(startPos, endPos, authorName, this.structureName, true, this.partModeUsing));
		System.out.println("Packet sent!");
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return (128 + (endX - startX) + (endY - startY) + (endZ - startZ))^2; 
	}
	
	private boolean isPlayerInRange(double x, double y, double z, double range) 
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
    }
	
	public AxisAlignedBB getDimensionIndicatorBoudingBox() {
		AxisAlignedBB aabb = super.getRenderBoundingBox();
		aabb = aabb.offset(new BlockPos(startX, startY, startZ));
		aabb = aabb.expand(endX, endY, endZ);
		
		return aabb;
	}
}