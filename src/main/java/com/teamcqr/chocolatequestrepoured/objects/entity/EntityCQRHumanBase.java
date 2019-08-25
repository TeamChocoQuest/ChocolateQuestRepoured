package com.teamcqr.chocolatequestrepoured.objects.entity;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityCQRHumanBase extends EntityCreature
		implements ICQREntity, IEntityAdditionalSpawnData, IEntityOwnable {
	
	private int intelligence;
	private boolean isDefending;
	private boolean sitting;
	private EntityLivingBase leader;
	private double attackDistance;

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
	public void spawnAt(int x, int y, int z) {
		// TODO Auto-generated method stub

	}
	
	public boolean isSitting() {
		return sitting;
	}
	public void setSitting(boolean flag) {
		sitting = flag;
	}

	public EntityLivingBase getLeader() {
		return leader;
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

}
