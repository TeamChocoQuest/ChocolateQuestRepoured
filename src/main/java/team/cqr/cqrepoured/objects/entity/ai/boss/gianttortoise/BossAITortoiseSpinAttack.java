package team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileBubble;

public class BossAITortoiseSpinAttack extends AbstractCQREntityAI<EntityCQRGiantTortoise> {

	private Vec3d movementVector;

	private static final int COOLDOWN = 10;
	private int cooldown = COOLDOWN / 2;
	private int previousBlocks = 0;
	private static final int MAX_BLOCKED_SPINS = 1;

	private final int AFTER_IDLE_TIME = 5;
	private final int BUBBLE_SHOOT_DURATION = 20;

	static final int MAX_DISTANCE_TO_BEGIN_SPIN = 16;
	static final int MAX_DISTANCE_TO_TARGET = 20;

	private int explosionCooldown = 0;
	private static final int MAX_EXPLOSION_COOLDOWN = 20;

	public BossAITortoiseSpinAttack(EntityCQRGiantTortoise entity) {
		super(entity);
		// setMutexBits(8);
	}

	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public boolean shouldExecute() {
		this.cooldown--;
		if (!this.getBoss().isStunned() && this.getBoss().getAttackTarget() != null && !this.getBoss().getAttackTarget().isDead) {
			if (this.getBoss().getDistance(this.getBoss().getAttackTarget()) > MAX_DISTANCE_TO_BEGIN_SPIN) {
				return false;
			}
		} else {
			return false;
		}
		if (this.cooldown <= 0 && !this.getBoss().isHealing()) {
			this.getBoss().setWantsToSpin(true);
			this.cooldown = 0;
			this.previousBlocks = 0;
			if (this.getBoss().isInShell() && this.getBoss().isReadyToSpin()) {
				this.getBoss().setCanBeStunned(false);
				this.getBoss().setSpinning(true);
				this.getBoss().setWantsToSpin(false);
				return true;
			} else {
				this.getBoss().targetNewState(EntityCQRGiantTortoise.TARGET_MOVE_IN);
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.getBoss() != null && this.getBoss().getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_SPINNING && !this.getBoss().isStunned() && this.getBoss().getSpinsBlocked() <= MAX_BLOCKED_SPINS && super.shouldContinueExecuting() && !this.getBoss().isDead && this.getBoss().getAttackTarget() != null
				&& !this.getBoss().getAttackTarget().isDead && !this.getBoss().isHealing() && this.getBoss().shouldCurrentAnimationBePlaying();
	}

	private void calculateVelocity() {
		this.movementVector = this.getBoss().getAttackTarget().getPositionVector().subtract(this.getBoss().getPositionVector());
		if (this.movementVector.y >= 2) {
			this.movementVector = this.movementVector.subtract(0, this.movementVector.y, 0);
		}
		this.movementVector = this.movementVector.normalize();
		this.movementVector = this.movementVector.scale(1.125D);
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.getBoss().setSpinning(true);
		this.getBoss().setCanBeStunned(false);
		this.getBoss().setInShell(true);
		this.getBoss().setReadyToSpin(false);
		this.getBoss().setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_SPINNING);
	}

	@Override
	public void updateTask() {
		super.updateTask();
		// this.getBoss().setSpinning(false);
		if (this.getBoss().getSpinsBlocked() >= MAX_BLOCKED_SPINS) {
			this.getBoss().setSpinning(false);
			this.getBoss().setStunned(true);
		} else if (this.getBoss().getCurrentAnimationTick() > this.BUBBLE_SHOOT_DURATION && EntityCQRGiantTortoise.ANIMATIONS[EntityCQRGiantTortoise.ANIMATION_ID_SPINNING].getAnimationDuration() - this.getBoss().getCurrentAnimationTick() > this.AFTER_IDLE_TIME) {
			//Spinning phase
			if (this.explosionCooldown > 0) {
				this.explosionCooldown--;
			}
			if (this.getBoss().collidedHorizontally || this.movementVector == null || this.getBoss().getDistance(this.getBoss().getAttackTarget()) >= MAX_DISTANCE_TO_TARGET || this.previousBlocks != this.getBoss().getSpinsBlocked()) {
				if (this.getBoss().collidedHorizontally && !this.getBoss().getWorld().isRemote && this.explosionCooldown <= 0) {
					this.explosionCooldown = MAX_EXPLOSION_COOLDOWN;
					this.getBoss().getWorld().newExplosion(this.getBoss(), this.entity.getPositionVector().x, this.entity.getPositionVector().y, this.entity.getPositionVector().z, 2, false, false);
				}

				if (this.hitHardBlock(this.movementVector)) {
					this.getBoss().setSpinning(false);
					this.getBoss().setStunned(true);
				}

				this.calculateVelocity();
				float damage = 1F;
				if (this.previousBlocks != this.getBoss().getSpinsBlocked()) {
					this.previousBlocks = this.getBoss().getSpinsBlocked();
					damage *= 1.5F;
					damage /= Math.max(1, this.getBoss().getWorld().getDifficulty().getId());
					this.getBoss().attackEntityFrom(DamageSource.IN_WALL, damage, true);
				}

				/*
				 * damage /= Math.max(1, getBoss().getWorld().getDifficulty().getDifficultyId()); if(getBoss().collidedHorizontally) {
				 * getBoss().attackEntityFrom(DamageSource.IN_WALL, damage, true); }
				 */
			}
			this.getBoss().setSpinning(true);
			this.getBoss().setCanBeStunned(false);
			this.getBoss().setInShell(true);
			this.getBoss().motionX = this.movementVector.x;
			this.getBoss().motionZ = this.movementVector.z;
			this.getBoss().motionY = this.entity.collidedHorizontally ? this.movementVector.y : 0.5 * this.movementVector.y;
			this.getBoss().velocityChanged = true;
		} else if (this.getBoss().getCurrentAnimationTick() <= this.BUBBLE_SHOOT_DURATION) {
			//Shooting bubbles
			this.getBoss().setSpinning(false);
			if (this.getBoss().getCurrentAnimationTick() % 5 == 0) {
				this.getBoss().playSound(CQRSounds.BUBBLE_BUBBLE, 1, 0.75F + (0.5F * this.getBoss().getRNG().nextFloat()));
			}
			Vec3d v = new Vec3d(this.entity.getRNG().nextDouble() - 0.5D, 0.125D * (this.entity.getRNG().nextDouble() - 0.5D), this.entity.getRNG().nextDouble() - 0.5D);
			v = v.normalize();
			v = v.scale(1.4);
			this.entity.faceEntity(this.entity.getAttackTarget(), 30, 30);
			ProjectileBubble bubble = new ProjectileBubble(this.entity.world, this.entity);
			bubble.motionX = v.x;
			bubble.motionY = v.y;
			bubble.motionZ = v.z;
			bubble.velocityChanged = true;
			this.entity.world.spawnEntity(bubble);

		} else {
			this.getBoss().setSpinning(false);
			this.getBoss().resetSpinsBlocked();
		}
	}

	private boolean hitHardBlock(Vec3d velocity) {
		AxisAlignedBB aabb = this.getBoss().getCollisionBoundingBox();
		if (aabb == null) {
			return false;
		}
		aabb = aabb.grow(0.5).offset(velocity.normalize().scale(this.getBoss().width / 2));
		World world = this.getBoss().getWorld();

		int x1 = MathHelper.floor(aabb.minX);
		int y1 = MathHelper.floor(aabb.minY);
		int z1 = MathHelper.floor(aabb.minZ);
		int x2 = MathHelper.floor(aabb.maxX);
		int y2 = MathHelper.floor(aabb.maxY);
		int z2 = MathHelper.floor(aabb.maxZ);

		for (int k1 = x1; k1 <= x2; ++k1) {
			for (int l1 = y1; l1 <= y2; ++l1) {
				for (int i2 = z1; i2 <= z2; ++i2) {
					BlockPos blockpos = new BlockPos(k1, l1, i2);
					IBlockState iblockstate = world.getBlockState(blockpos);
					Block block = iblockstate.getBlock();

					if (EntityCQRGiantTortoise.isHardBlock(block.getRegistryName())) {
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.getBoss().setSpinning(false);
		this.getBoss().setReadyToSpin(true);
		this.getBoss().setCanBeStunned(true);
		this.cooldown = COOLDOWN;
		if (!(this.getBoss().getAttackTarget() != null && !this.getBoss().getAttackTarget().isDead)) {
			this.cooldown /= 3;
		}
		//this.getBoss().setAnimationTick(0);
		if (this.getBoss().getSpinsBlocked() >= MAX_BLOCKED_SPINS) {
			this.cooldown *= 1.5;
			this.getBoss().setStunned(true);
		}
		this.getBoss().resetSpinsBlocked();
	}

}
