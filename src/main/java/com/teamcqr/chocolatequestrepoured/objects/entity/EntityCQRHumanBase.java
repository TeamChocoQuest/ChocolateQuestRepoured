package com.teamcqr.chocolatequestrepoured.objects.entity;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityCQRHumanBase extends EntityCreature
		implements ICQREntity, IEntityAdditionalSpawnData, IEntityOwnable {

	private boolean hasExisted = false;

	public BlockPos home;
	public EntityLivingBase leader;

	private int intelligence;
	private boolean isDefending;
	private boolean sitting;
	private double attackDistance;
	private int healingPotions = 2;

	public EntityCQRHumanBase(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public UUID getOwnerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		// TODO Auto-generated method stub

	}

	@Override
	public EFaction getFaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBoss() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRideable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFriendlyTowardsPlayer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasFaction() {
		return true;
	}

	@Override
	public float getBaseHealth() {
		return 20.0F;
	}

	@Override
	public BlockPos getHome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void spawnAt(int x, int y, int z) {
		// TODO Auto-generated method stub

	}
	
	public boolean isSitting() {
		return sitting;
	}

	public void setSitting(boolean flag) {
		sitting = flag;
	}

	public int getInteligence() {
		return intelligence;
	}
	
	public void setDefending(boolean flag) {
		isDefending = flag;
	}

	public double getAttackDistance() {
		return attackDistance;
	}
	public boolean getIsDefending() {
		return isDefending;
	}

	public boolean isSuitableTargetAlly(EntityLivingBase entityliving) {
		return false;
	}

	public void removePotion(int i) {
		this.healingPotions--;
		
	}

	@Override
	public int getRemainingHealingPotions() {
		return healingPotions;
	}

	@Override
	public void setHome(BlockPos home) {
		this.home = home;
	}

	@Override
	public BlockPos getHome() {
		return home;
	}

	@Override
	public void setLeader(EntityLivingBase leader) {
		this.leader = leader;
	}

	@Override
	public EntityLivingBase getLeader() {
		return leader;
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateGround(this, worldIn) {
			@Override
			public float getPathSearchRange() {
				return 128.0F;
			}
		};
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(5, new EntityAIMoveToHome(this));
		this.tasks.addTask(6, new EntityAIMoveToLeader(this));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		boolean hasHome = this.home != null;
		compound.setBoolean("hasHome", hasHome);
		if (hasHome) {
			compound.setTag("home", NBTUtil.BlockPosToNBTTag(this.home));
		}

		boolean hasLeader = this.leader != null;
		compound.setBoolean("hasLeader", hasLeader);
		if (hasLeader) {
			compound.setInteger("leader", this.leader.getEntityId());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		this.hasExisted = true;

		super.readEntityFromNBT(compound);

		if (compound.getBoolean("hasHome")) {
			this.home = NBTUtil.BlockPosFromNBT(compound.getCompoundTag("home"));
		}

		if (compound.getBoolean("hasLeader")) {
			this.leader = (EntityLivingBase) this.world.getEntityByID(compound.getInteger("leader"));
		}
	}
}
