package com.teamcqr.chocolatequestrepoured.tileentity;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.gui.GuiExporter;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.network.CQSaveStructureRequestPacket;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	private BlockPos vecLowerCornerOfSelection = new BlockPos(0,0,0);
	private BlockPos selectionSize = new BlockPos(0,0,0);

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
		calculateSelectionVectors();
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
		calculateSelectionVectors();
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
			calculateSelectionVectors();
		//}
	}

	private void calculateSelectionVectors() {
		int vSelX = startX < endX ? startX : endX;
		//System.out.println("vSelX: " + vSelX);
		int vSelSizeX = endX > startX ? endX - startX : startX - endX;
		//System.out.println("vSelSizeX: " + vSelSizeX);
		vSelX -= getPos().getX();
		//System.out.println("vSelX2: " + vSelX);
		//vSelSizeX -= getPos().getX();

		int vSelY = startY < endY ? startY : endY;
		int vSelSizeY = endY > startY ? endY - startY : startY - endY;
		vSelY -= getPos().getY();
		//vSelSizeY -= getPos().getY();

		int vSelZ = startZ < endZ ? startZ : endZ;
		int vSelSizeZ = endZ > startZ ? endZ - startZ : startZ - endZ;
		vSelZ -= getPos().getZ();
		//vSelSizeZ -= getPos().getZ();

		if(vSelSizeX > 0 && vSelSizeY > 0 && vSelSizeZ > 0) {
			vecLowerCornerOfSelection = new BlockPos(vSelX, vSelY, vSelZ);
			selectionSize = new BlockPos(vSelSizeX, vSelSizeY, vSelSizeZ).add(1,1,1);
		}
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = getExporterData();
		return new SPacketUpdateTileEntity(this.pos,1,compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		setExporterData(compound);
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if(screen instanceof GuiExporter) {
			calculateSelectionVectors();
			((GuiExporter)screen).sync();
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound data = super.getUpdateTag();
		data.setTag("data",getExporterData());
		return data;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		setExporterData(tag.getCompoundTag("data"));
	}

	public void requestSync() {
		world.notifyBlockUpdate(this.pos, ModBlocks.EXPORTER.getDefaultState(),ModBlocks.EXPORTER.getDefaultState(),0);
		calculateSelectionVectors();
	}
	
	public void setUser(EntityPlayer player) {
		this.user = player;
	}
	
	public void saveStructure(World world, BlockPos startPos, BlockPos endPos, String authorName) 
	{
		if(!world.isRemote) {
			CQStructure structure = new CQStructure(this.structureName, true);
			structure.setAuthor(authorName);
			System.out.println("Server is saving structure...");
			structure.save(world, startPos, endPos, this.partModeUsing, this.user);
			System.out.println("Done!");
		} else {
			System.out.println("Sending structure save request packet...");
			CQRMain.NETWORK.sendToServer(new CQSaveStructureRequestPacket(startPos, endPos, authorName, this.structureName, true, this.partModeUsing));
			System.out.println("Packet sent!");
		}
	}

	@SideOnly(Side.CLIENT)
	public BlockPos getStructurePosVector() {
		calculateSelectionVectors();

		return vecLowerCornerOfSelection;
	}

	@SideOnly(Side.CLIENT)
	public BlockPos getStructureSize() {
		calculateSelectionVectors();

		return selectionSize;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB aabb = super.getRenderBoundingBox();

		aabb.offset(startX, startY, startZ);
		aabb.expand(endX, endY, endZ);
		
		return aabb;
	}

}