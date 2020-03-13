package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class AbstractEntityCQRBoss extends AbstractEntityCQR {

	protected int bossSize;
	protected String assignedRegionID = null;

	protected final BossInfoServer bossInfoServer = new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10);
	public int deathTicks = 0;

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
	public float calculateBaseHealth(double x, double z, float health) {
		float hp = super.calculateBaseHealth(x, z, health);

		float multiplier = 1F;

		multiplier += this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - 100, 0, z - 100, x + 100, 255, z + 100)).size() * CQRConfig.mobs.bossHealthMultiplierPerPlayer;

		hp *= multiplier;

		return hp;
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
	
	@Override
	protected void onDeathUpdate() {
		//super.onDeathUpdate();
		++this.deathTicks;
		if (this.deathTicks >= 180 && this.deathTicks <= 200)
        {
            float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double)f, this.posY + 2.0D + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D);
        }
		this.setNoGravity(true);
		 this.move(MoverType.SELF, 0.0D, 0.1, 0.0D);
		if (this.deathTicks == 200 && !this.world.isRemote)
        {
            this.setDead();
        }
	}

}
