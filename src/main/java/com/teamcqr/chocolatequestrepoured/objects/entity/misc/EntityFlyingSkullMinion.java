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
import net.minecraft.nbt.NBTUtil;
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
		this.setSize(0.5F, 0.5F);
		this.setNoGravity(true);
		this.setHealth(1F);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1F);
		this.navigator = new PathNavigateFlying(this, worldIn);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.getImmediateSource() instanceof EntitySpectralArrow) {
			Entity summonerTmp = this.summoner;
			this.summoner = source.getTrueSource();
			this.target = summonerTmp;
			this.explode(10F);
			return true;
		}
		if (this.getRNG().nextInt(10) == 9) {
			Entity summonerTmp = this.summoner;
			this.summoner = source.getTrueSource();
			this.target = summonerTmp;
			return true;
		}
		this.explode();
		return true;
	}

	@Override
	public PathNavigate getNavigator() {
		return this.navigator;
	}

	public void setSummoner(Entity ent) {
		this.summoner = ent;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		// If we hit a wall we explode
		if (!this.isInsideOfMaterial(Material.AIR)) {
			this.explode();
		}
		if (this.attacking) {
			if (this.target != null && !this.target.isDead) {
				this.updateDirection();
			}
			Vec3d v = this.direction;
			v = v.normalize();
			this.setVelocity(v.x * 0.4F, v.y * 0.25F, v.z * 0.4F);

			this.getLookHelper().setLookPositionWithEntity(this.target, 30, 30);

		} else if (this.summoner != null) {
			Vec3d v = this.summoner.getLookVec();
			v = new Vec3d(v.x, 2.25D, v.z);
			v = v.normalize();
			v = v.scale(2.5D);
			v = VectorUtil.rotateVectorAroundY(v, this.isLeftSkull ? 270 : 90);
			Vec3d targetPos = this.summoner.getPositionVector().add(v);
			this.getLookHelper().setLookPositionWithEntity(this.summoner, 30, 30);
			if (this.getDistance(targetPos.x, targetPos.y, targetPos.z) > 1) {
				Vec3d velo = targetPos.subtract(this.getPositionVector());
				velo = velo.normalize();
				velo = velo.scale(0.2);
				this.setVelocity(velo.x, velo.y * 1.5, velo.z);
			}
		}
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn != this.summoner) {
			super.collideWithEntity(entityIn);
			this.explode(0.75F);
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		this.explode(1.25F);
	}

	private void explode() {
		this.explode(1F);
		this.setDead();
	}

	private void explode(float strengthMultiplier) {
		if (this.world != null) {
			if (this.summoner != null) {
				this.world.newExplosion(this.summoner, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), 0.5F * strengthMultiplier, true, false);
			}
			this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPosition().getX(), this.getPosition().getY() + 0.02, this.getPosition().getZ(), 0.5F, 0.0F, 0.5F, 1);
			this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPosition().getX(), this.getPosition().getY() + 0.02, this.getPosition().getZ(), 0.5F, 0.0F, -0.5F, 1);
			this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPosition().getX(), this.getPosition().getY() + 0.02, this.getPosition().getZ(), -0.5F, 0.0F, -0.5F, 1);
			this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPosition().getX(), this.getPosition().getY() + 0.02, this.getPosition().getZ(), -0.5F, 0.0F, 0.5F, 1);
		}
	}

	public void setTarget(Entity target) {
		this.target = target;
		this.updateDirection();
	}

	public void startAttacking() {
		this.attacking = true;
	}

	private void updateDirection() {
		this.direction = this.target.getPositionVector().subtract(this.getPositionVector());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("attacking", this.attacking);
		compound.setDouble("vX", this.direction == null ? 0D : this.direction.x);
		compound.setDouble("vY", this.direction == null ? 0D : this.direction.y);
		compound.setDouble("vZ", this.direction == null ? 0D : this.direction.z);
		if (this.summoner != null && !this.summoner.isDead) {
			compound.setTag("summonerID", NBTUtil.createUUIDTag(this.summoner.getPersistentID()));
		}
		if (this.target != null && !this.target.isDead) {
			compound.setTag("targetID", net.minecraft.nbt.NBTUtil.createUUIDTag(this.target.getPersistentID()));
		}
	}

	public boolean isAttacking() {
		return this.attacking;
	}

	public boolean hasTarget() {
		return this.target != null && !this.target.isDead;
	}

	public void setSide(boolean left) {
		this.isLeftSkull = left;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.attacking = compound.getBoolean("attacking");
		double x, y, z;
		x = compound.getDouble("vX");
		y = compound.getDouble("vY");
		z = compound.getDouble("vZ");
		this.direction = new Vec3d(x, y, z);
		if (compound.hasKey("targetID")) {
			UUID id = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("targetID"));
			if (this.world != null) {
				for (Entity ent : this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(this.getPosition().add(10, 10, 10), this.getPosition().add(-10, -10, -10)), TargetUtil.PREDICATE_LIVING)) {
					if (ent.getPersistentID().equals(id)) {
						this.target = ent;
					}
				}
			}
		}
		if (compound.hasKey("summonerID")) {
			UUID id = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("summonerID"));
			if (this.world != null) {
				for (Entity ent : this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(this.getPosition().add(10, 10, 10), this.getPosition().add(-10, -10, -10)), TargetUtil.PREDICATE_LIVING)) {
					if (ent.getPersistentID().equals(id)) {
						this.summoner = ent;
					}
				}
			}
		}
	}

}
