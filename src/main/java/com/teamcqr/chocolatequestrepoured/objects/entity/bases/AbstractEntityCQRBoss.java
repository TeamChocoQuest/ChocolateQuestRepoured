package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
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
	public boolean isNonBoss() {
		return false;
	}
	

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		bossInfoServer.setPercent(this.getHealth() / this.getMaxHealth());
	}
	
	@Override
	public void updateReputationOnDeath(DamageSource cause) {
		if (cause.getTrueSource() instanceof EntityPlayer && this.hasFaction()) {
			EntityPlayer player = (EntityPlayer) cause.getTrueSource();
			int range = Reference.CONFIG_HELPER_INSTANCE.getFactionRepuChangeRadius();
			double x1 = player.posX - range;
			double y1 = player.posY - range;
			double z1 = player.posZ - range;
			double x2 = player.posX + range;
			double y2 = player.posY + range;
			double z2 = player.posZ + range;
			AxisAlignedBB aabb = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);

			for (AbstractEntityCQR cqrentity : this.world.getEntitiesWithinAABB(AbstractEntityCQR.class, aabb)) {
				if (cqrentity.hasFaction() && (this.canEntityBeSeen(cqrentity) || cqrentity.canEntityBeSeen(player) || player.canEntityBeSeen(cqrentity))) {
					if (this.getFaction().equals(cqrentity.getFaction())) {
						// DONE decrement the players repu on this entity's faction
						this.getFaction().decrementReputation(player, EFaction.REPU_DECREMENT_ON_MEMBER_KILL *100);
					} else if (this.getFaction().isEnemy(cqrentity.getFaction())) {
						// DONE increment the players repu at CQREntity's faction
						cqrentity.getFaction().incrementReputation(player, EFaction.REPU_DECREMENT_ON_ENEMY_KILL *100);
					} else if (this.getFaction().isAlly(cqrentity.getFaction())) {
						// DONE decrement the players repu on CQREntity's faction
						cqrentity.getFaction().decrementReputation(player, EFaction.REPU_DECREMENT_ON_ALLY_KILL *100);
					}
					break;
				}
			}
		}
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
		
		//TOOD: Destroy protected region
	}
	
	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfoServer.setName(getDisplayName());
	}

}
