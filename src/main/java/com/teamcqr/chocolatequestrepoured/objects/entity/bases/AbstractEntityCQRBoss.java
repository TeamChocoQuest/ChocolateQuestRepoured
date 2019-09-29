package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class AbstractEntityCQRBoss extends AbstractEntityCQR {
	
	protected int bossSize;
	protected String assignedRegionID = null;
	
	protected final BossInfoServer bossInfoServer = new BossInfoServer(getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10);

	public AbstractEntityCQRBoss(World worldIn, int size) {
		super(worldIn);
		this.bossSize = size;
		// TODO Auto-generated constructor stub
	}

	public int getSize() {
		return bossSize;
	}
	
	public void setSize(int newSize) {
		this.bossSize = newSize;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		super.readEntityFromNBT(compound);
		if(compound.hasKey("assignedRegion")) {
			this.assignedRegionID = compound.getString("assignedRegion");
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		super.writeEntityToNBT(compound);
		if(this.assignedRegionID != null) {
			compound.setString("assignedRegion", assignedRegionID);
		}
	}
	
	public void assignRegion(String regionID) {
		if(regionID != null) {
			this.assignedRegionID = regionID;
		}
	}
	
	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfoServer.setName(getDisplayName());
	}
	
	@Override
	public boolean isNonBoss() {
		return false;
	}
	

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		bossInfoServer.setPercent(this.getHealth() / this.getMaxHealth());
	}
	
	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfoServer.addPlayer(player);
	}
	
	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfoServer.removePlayer(player);
	}
	
	public void setBarColor(BossInfo.Color color) {
		this.bossInfoServer.setColor(color);
	}
	
	public void setBarText(ITextComponent text) {
		this.bossInfoServer.setName(text);
	}
	
	public void setVarStyle(BossInfo.Overlay style) {
		this.bossInfoServer.setOverlay(style);
	}

}
