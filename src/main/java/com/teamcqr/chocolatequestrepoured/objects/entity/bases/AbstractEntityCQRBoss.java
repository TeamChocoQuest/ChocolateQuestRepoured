package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class AbstractEntityCQRBoss extends AbstractEntityCQR {

	protected int bossSize;
	protected String assignedRegionID = null;

	protected final BossInfoServer bossInfoServer = new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10);

	public AbstractEntityCQRBoss(World worldIn, int size) {
		super(worldIn);
		this.bossSize = size;
	}

	public int getSize() {
		return this.bossSize;
	}

	public void setSize(int newSize) {
		this.bossSize = newSize;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasKey("assignedRegion")) {
			this.assignedRegionID = compound.getString("assignedRegion");
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if (this.assignedRegionID != null) {
			compound.setString("assignedRegion", this.assignedRegionID);
		}
	}

	public void assignRegion(String regionID) {
		if (regionID != null) {
			this.assignedRegionID = regionID;
		}
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		this.bossInfoServer.setPercent(this.getHealth() / this.getMaxHealth());
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

	public void enableBossBar(boolean enabled) {
		this.bossInfoServer.setVisible(enabled);
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);

		// TOOD: Destroy protected region
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfoServer.setName(this.getDisplayName());
	}

}
