package team.cqr.cqrepoured.entity.misc;

import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityFlyingSkullMinion extends FlyingEntity implements IDontRenderFire {

	protected Entity summoner;
	protected Entity target;
	protected boolean attacking = false;
	protected boolean isLeftSkull = false;
	protected Vector3d direction = null;

	public EntityFlyingSkullMinion(World worldIn) {
		super(worldIn);
		this.setSize(0.5F, 0.5F);
		this.setNoGravity(true);
		this.setHealth(1F);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1F);
		this.navigator = new FlyingPathNavigator(this, worldIn);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.isExplosion()) {
			return false;
		}
		if (source.getTrueSource() != null && EntityUtil.isEntityFlying(source.getTrueSource())) {
			return false;
		}
		if (source.getImmediateSource() instanceof SpectralArrowEntity) {
			Entity summonerTmp = this.summoner;
			this.summoner = source.getTrueSource();
			this.target = summonerTmp;
			// this.explode(10F);
			// this.setDead();
			return true;
		}
		if (this.getRNG().nextInt(10) == 9) {
			Entity summonerTmp = this.summoner;
			this.summoner = source.getTrueSource();
			this.target = summonerTmp;
			this.setDead();
			return true;
		}
		this.explode(1.25F);
		this.setDead();
		return true;
	}

	@Override
	public PathNavigator getNavigator() {
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
			this.explode(1.25F);
			this.setDead();
		}
		if (this.attacking) {
			if (this.target != null && !this.target.isDead) {
				this.updateDirection();
			}
			Vector3d v = this.direction;
			v = v.normalize();
			// this.setVelocity(v.x * 0.4F, v.y * 0.25F, v.z * 0.4F);
			this.motionX = v.x * 0.4D;
			this.motionY = v.y * 0.25D;
			this.motionZ = v.z * 0.4D;
			this.velocityChanged = true;
			if (this.target != null && !this.target.isDead) {
				this.getLookHelper().setLookPositionWithEntity(this.target, 30, 30);
			}

		} else if (this.summoner != null) {
			Vector3d v = this.summoner.getLookVec();
			v = new Vector3d(v.x, 2.25D, v.z);
			v = v.normalize();
			v = v.scale(2.5D);
			v = VectorUtil.rotateVectorAroundY(v, this.isLeftSkull ? 270 : 90);
			Vector3d targetPos = this.summoner.getPositionVector().add(v);
			this.getLookHelper().setLookPositionWithEntity(this.summoner, 30, 30);
			if (this.getDistance(targetPos.x, targetPos.y, targetPos.z) > 1) {
				Vector3d velo = targetPos.subtract(this.getPositionVector());
				velo = velo.normalize();
				velo = velo.scale(0.2);
				// this.setVelocity(velo.x, velo.y * 1.5, velo.z);
				this.motionX = velo.x;
				this.motionY = velo.y * 2.5D;
				this.motionZ = velo.z;
				this.velocityChanged = true;
			}
		}
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn != this.summoner) {
			super.collideWithEntity(entityIn);

			if (EntityUtil.isEntityFlying(entityIn)) {
				if (this.summoner instanceof LivingEntity && entityIn instanceof LivingEntity) {
					((LivingEntity) this.summoner).heal(((LivingEntity) entityIn).getHealth() / 2);
					((LivingEntity) entityIn).motionY *= -2;
					((LivingEntity) entityIn).velocityChanged = true;
				}
			}
			this.explode(0.75F);
			this.setDead();
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		this.explode(1.25F);
	}

	private void explode(float strengthMultiplier) {
		if (this.world != null) {
			if (this.summoner != null && !this.summoner.isDead && !this.isDead) {
				this.world.newExplosion(this.summoner, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), 2 * strengthMultiplier, false, false);
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
	public void writeEntityToNBT(CompoundNBT compound) {
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
	public void readEntityFromNBT(CompoundNBT compound) {
		super.readEntityFromNBT(compound);
		this.attacking = compound.getBoolean("attacking");
		double x, y, z;
		x = compound.getDouble("vX");
		y = compound.getDouble("vY");
		z = compound.getDouble("vZ");
		this.direction = new Vector3d(x, y, z);
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
