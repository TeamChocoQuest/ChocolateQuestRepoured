package com.teamcqr.chocolatequestrepoured.tileentity;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.gui.GuiExporter;
import com.teamcqr.chocolatequestrepoured.network.CQSaveStructureRequestPacket;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityExporter extends TileEntity {

	public int startX = 0;
	public int startY = 0;
	public int startZ = 0;
	public int endX = 0;
	public int endY = 0;
	public int endZ = 0;
	public String structureName = "NoName";
	public boolean partModeUsing = false;
	public boolean relativeMode = false;

	private BlockPos minPos = new BlockPos(0, 0, 0);
	private BlockPos maxPos = new BlockPos(0, 0, 0);

	private EntityPlayer user = null;

	public NBTTagCompound getExporterData(NBTTagCompound compound) {
		compound.setInteger("StartX", startX);
		compound.setInteger("StartY", startY);
		compound.setInteger("StartZ", startZ);
		compound.setInteger("EndX", endX);
		compound.setInteger("EndY", endY);
		compound.setInteger("EndZ", endZ);
		compound.setString("StructureName", structureName);
		compound.setBoolean("PartMode", partModeUsing);
		compound.setBoolean("RelativeMode", relativeMode);
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
		partModeUsing = compound.getBoolean("PartMode");
		relativeMode = compound.getBoolean("RelativeMode");

		this.onPositionsChanged();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.getExporterData(compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.setExporterData(compound);
	}

	public void setValues(int sX, int sY, int sZ, int eX, int eY, int eZ, String structName, boolean usePartMode,
			boolean useRelativeMode) {
		startX = sX;
		startY = sY;
		startZ = sZ;
		endX = eX;
		endY = eY;
		endZ = eZ;
		structureName = structName;
		partModeUsing = usePartMode;
		relativeMode = useRelativeMode;

		this.onPositionsChanged();

		this.markDirty();
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 1, this.getExporterData(new NBTTagCompound()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.setExporterData(pkt.getNbtCompound());

		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if (screen instanceof GuiExporter) {
			((GuiExporter) screen).sync();
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound data = super.getUpdateTag();
		data.setTag("data", getExporterData(new NBTTagCompound()));
		return data;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		setExporterData(tag.getCompoundTag("data"));
	}

	public void setUser(EntityPlayer player) {
		this.user = player;
	}

	public void saveStructure(World world, BlockPos startPos, BlockPos endPos, String authorName) {
		if (this.relativeMode) {
			startPos = this.pos.subtract(startPos);
			endPos = this.pos.subtract(endPos);
		}
		if (!world.isRemote) {
			CQStructure structure = new CQStructure(this.structureName, true);
			structure.setAuthor(authorName);
			System.out.println("Server is saving structure...");
			structure.save(world, startPos, endPos, this.partModeUsing, this.user);
			System.out.println("Done!");
		} else {
			System.out.println("Sending structure save request packet...");
			CQRMain.NETWORK.sendToServer(new CQSaveStructureRequestPacket(startPos, endPos, authorName,
					this.structureName, true, this.partModeUsing));
			System.out.println("Packet sent!");
		}
	}

	public void onPositionsChanged() {
		this.minPos = new BlockPos(Math.min(this.startX, this.endX), Math.min(this.startY, this.endY),
				Math.min(this.startZ, this.endZ));
		this.maxPos = new BlockPos(Math.max(this.startX, this.endX), Math.max(this.startY, this.endY),
				Math.max(this.startZ, this.endZ));
	}

	public BlockPos getMinPos() {
		return this.minPos;
	}

	public BlockPos getMaxPos() {
		return this.maxPos;
	}

	public BlockPos getRenderMinPos() {
		return this.relativeMode ? this.minPos : this.minPos.subtract(this.pos);
	}

	public BlockPos getRenderMaxPos() {
		return this.relativeMode ? this.maxPos : this.maxPos.subtract(this.pos);
	}

}
