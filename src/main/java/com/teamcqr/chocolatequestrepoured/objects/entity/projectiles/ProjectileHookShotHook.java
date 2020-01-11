package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileHookShotHook extends ProjectileBase {
	private static final float PULL_SPEED = 1.8f; // speed that the chain retracts (larger = faster)
	public static final double HOOK_RANGE = 20.0; // Max range of the hook before it stops extending
	public static final double STOP_PULL_DISTANCE = 1.5; // If layer gets within this range of hook, stop pulling
	private boolean pulling = false;
	private Vec3d impactLocation = null;

	private Vec3d lastShooterPos = null;
	private int lastMovementCheckTick = 0;

	public ProjectileHookShotHook(World worldIn) {
		super(worldIn);
	}

	public ProjectileHookShotHook(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileHookShotHook(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public void onUpdate() {
		if (this.getThrower() != null && this.getThrower().isDead) {
			this.pulling = false;
			this.setDead();
		} else if (!this.world.isRemote && this.getThrower() instanceof EntityPlayer) {
			EntityPlayer shootingPlayer = (EntityPlayer) this.getThrower();
			Vec3d playerPos = shootingPlayer.getPositionVector();
			double distanceToHook = playerPos.distanceTo(this.getPositionVector());

			if (this.pulling) {
				this.checkForBlockedPath(shootingPlayer);

				if (distanceToHook < STOP_PULL_DISTANCE) {
					this.pulling = false;
					this.setDead();
					this.setShooterVelocity(shootingPlayer, new Vec3d(0, 0, 0));
				} else {
					Vec3d hookDirection = this.impactLocation.subtract(playerPos);

					Vec3d pullVector = hookDirection.normalize().scale(PULL_SPEED);
					this.setShooterVelocity(shootingPlayer, pullVector);
				}
			} else if (distanceToHook > HOOK_RANGE) {
				this.zeroizeVelocity();
				this.setDead();
			}

			this.onUpdateInAir();
			super.onUpdate();
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			IBlockState state = this.world.getBlockState(result.getBlockPos());

			if (!state.getBlock().isPassable(this.world, result.getBlockPos())) {
				// System.out.println("Hit " + state.getBlock() + " block at " + result.getBlockPos());
				this.zeroizeVelocity();
				this.impactLocation = this.getPositionVector(); // should this use the impact block position instead?
				this.pulling = true;
			}
		}
	}

	private void zeroizeVelocity() {
		//this.setVelocity(0, 0, 0);
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.velocityChanged = true;
	}

	private void setShooterVelocity(EntityPlayer shootingPlayer, Vec3d velocityVec) {
		//shootingPlayer.setVelocity(velocityVec.x, velocityVec.y, velocityVec.z);
		shootingPlayer.motionX = velocityVec.x;
		shootingPlayer.motionY = velocityVec.y;
		shootingPlayer.motionZ = velocityVec.z;
		shootingPlayer.velocityChanged = true;
	}

	private void checkForBlockedPath(EntityPlayer shootingPlayer) {
		if (this.ticksExisted - this.lastMovementCheckTick >= 4) // once every 4 ticks ~ 0.2 seconds
		{
			Vec3d currentPos = shootingPlayer.getPositionVector();
			if (this.lastShooterPos != null) {
				double distanceTraveled = currentPos.distanceTo(this.lastShooterPos);
				if (distanceTraveled < 0.2) {
					// System.out.println("Cancelling hook because shooter was blocked (dist = " + distanceTraveled + ")");
					this.zeroizeVelocity();
					this.setDead();
					this.setShooterVelocity(shootingPlayer, new Vec3d(0, 0, 0));
				}
			}

			this.lastMovementCheckTick = this.ticksExisted;
			this.lastShooterPos = shootingPlayer.getPositionVector();
		}
	}
}