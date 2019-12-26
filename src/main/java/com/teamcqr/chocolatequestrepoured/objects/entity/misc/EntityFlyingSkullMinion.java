package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.TargetUtil;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFlyingSkullMinion extends EntityFlying {
	
	protected Entity summoner;
	protected Entity target;
	protected boolean attacking = false;
	protected boolean isLeftSkull = false;
	protected Vec3d direction = null;

	public EntityFlyingSkullMinion(World worldIn) {
		super(worldIn);
		setSize(0.5F, 0.5F);
		setNoGravity(true);
		setHealth(1F);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1F);
		this.navigator = new PathNavigateFlying(this, worldIn);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(source.getImmediateSource() instanceof EntitySpectralArrow) {
			explode(10F);
			return true;
		}
		if(getRNG().nextInt(20) == 19) {
			Entity summonerTmp = this.summoner;
			this.summoner = source.getTrueSource();
			this.target = summonerTmp;
			return true;
		} 
		explode();
		return true;
	}
	
	@Override
	public PathNavigate getNavigator() {
		return navigator;
	}
	
	public void setSummoner(Entity ent) {
		this.summoner = ent;
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		//If we hit a wall we explode
		if(!isInsideOfMaterial(Material.AIR)) {
			explode();
		}
		if(attacking) {
			if(this.target != null && !this.target.isDead) {
				updateDirection();
			}
			Vec3d v = direction;
			v = v.normalize();
			setVelocity(v.x * 0.4F, v.y * 0.25F, v.z * 0.4F);
			
			getLookHelper().setLookPositionWithEntity(target, 10, 10);
			
		} else if(summoner != null) {
			Vec3d v = summoner.getLookVec();
			v = new Vec3d(v.x, 2.25D, v.z);
			v = v.normalize();
			v = v.scale(2.5D);
			v = VectorUtil.rotateVectorAroundY(v, isLeftSkull ? 270 : 90);
			Vec3d targetPos = summoner.getPositionVector().add(v);
			getLookHelper().setLookPositionWithEntity(summoner, 10, 10);
			if(getDistance(targetPos.x, targetPos.y, targetPos.z) > 1) {
				Vec3d velo = targetPos.subtract(getPositionVector());
				velo = velo.normalize();
				velo = velo.scale(0.05);
				setVelocity(velo.x, velo.y, velo.z);
			}
		}
	}
	
	@Override
	protected void collideWithEntity(Entity entityIn) {
		if(entityIn != summoner) {
			super.collideWithEntity(entityIn);
			explode();
		}
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		explode();
	}
	
	private void explode() {
		explode(1F);
	}
	
	private void explode(float strengthMultiplier) {
		world.newExplosion(this.summoner, getPosition().getX(), getPosition().getY(), getPosition().getZ(), 0.5F * strengthMultiplier, true, false);
		
		world.spawnParticle(EnumParticleTypes.FLAME, getPosition().getX(), getPosition().getY() + 0.02, getPosition().getZ(), 0.5F, 0.0F, 0.5F, 1);
		world.spawnParticle(EnumParticleTypes.FLAME, getPosition().getX(), getPosition().getY() + 0.02, getPosition().getZ(), 0.5F, 0.0F, -0.5F, 1);
		world.spawnParticle(EnumParticleTypes.FLAME, getPosition().getX(), getPosition().getY() + 0.02, getPosition().getZ(), -0.5F, 0.0F, -0.5F, 1);
		world.spawnParticle(EnumParticleTypes.FLAME, getPosition().getX(), getPosition().getY() + 0.02, getPosition().getZ(), -0.5F, 0.0F, 0.5F, 1);
		
		setDead();
	}

	public void setTarget(Entity target) {
		this.target = target;
		updateDirection();
	}
	
	public void startAttacking() {
		this.attacking = true;
	}
	
	private void updateDirection() {
		this.direction = target.getPositionVector().subtract(getPositionVector());
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("attacking", attacking);
		compound.setDouble("vX", direction == null ? 0D : direction.x);
		compound.setDouble("vY", direction == null ? 0D : direction.y);
		compound.setDouble("vZ", direction == null ? 0D : direction.z);
		if(target != null && !target.isDead) {
			compound.setTag("targetID", net.minecraft.nbt.NBTUtil.createUUIDTag(target.getPersistentID()));
		}
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	public boolean hasTarget() {
		return target != null && !target.isDead;
	}
	
	public void setSide(boolean left) {
		this.isLeftSkull = left;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		attacking = compound.getBoolean("attacking");
		double x,y,z;
		x = compound.getDouble("vX");
		y = compound.getDouble("vY");
		z = compound.getDouble("vZ");
		direction = new Vec3d(x, y, z);
		if(compound.hasKey("targetID")) {
			UUID id = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("targetID"));
			if(world != null) {
				for(Entity ent : world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(getPosition().add(10,10,10), getPosition().add(-10, -10, -10)), TargetUtil.LIVING)) {
					if(ent.getPersistentID().equals(id)) {
						target = ent;
					}
				}
			}
		}
	}

}
