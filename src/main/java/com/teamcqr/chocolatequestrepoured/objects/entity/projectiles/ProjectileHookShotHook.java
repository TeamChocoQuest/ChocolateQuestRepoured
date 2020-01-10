package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.HookShotPullPacket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileHookShotHook extends ProjectileBase {
	private static final double PULL_SPEED = 1.8f; //speed that the chain retracts (larger = faster)
	public static final double STOP_PULL_DISTANCE = 2.0; //If layer gets within this range of hook, stop pulling
	private boolean pulling = false; //if the hook has impacted and is currently pulling the player
	private Vec3d impactLocation = null; //where the hook intersects a block
	private double hookRange = 20.0; //Max range of the hook before it stops extending

	private Vec3d lastShooterPos = null; //last recorded position of the shooter - used to detect blocked path
	private int lastMovementCheckTick = 0; //tick count of last time shooter position was recorded

	public ProjectileHookShotHook(World worldIn) {
		super(worldIn);
	}

	public ProjectileHookShotHook(World worldIn, double x, double y, double z, double range) {
		super(worldIn, x, y, z);
		this.hookRange = range;
	}

	public ProjectileHookShotHook(World worldIn, EntityLivingBase shooter, double range) {
		super(worldIn, shooter);
		this.hookRange = range;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public void onUpdate() {
		if (this.getThrower() != null && this.getThrower().isDead) {
			pulling = false;
			setDead();
		} else if (!this.world.isRemote && this.getThrower() instanceof EntityPlayerMP) {
			EntityPlayerMP shootingPlayer = (EntityPlayerMP) this.getThrower();
			Vec3d playerPos = shootingPlayer.getPositionVector();
			double distanceToHook = playerPos.distanceTo(this.getPositionVector());

			if (pulling) {
				checkForBlockedPath(shootingPlayer);

				if (distanceToHook < STOP_PULL_DISTANCE) {
					pulling = false;
					sendClientStopPacket(shootingPlayer);
					setDead();
				}
				else {
					sendClientPullPacket(shootingPlayer);
				}
			} else if (distanceToHook > hookRange) {
				zeroizeHookVelocity();
				setDead();
			}

			onUpdateInAir();
			super.onUpdate();
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			IBlockState state = this.world.getBlockState(result.getBlockPos());

			if (!state.getBlock().isPassable(this.world, result.getBlockPos())) {
				System.out.println("Hit " + state.getBlock() + " block at " + result.getBlockPos());
				this.zeroizeHookVelocity();
				this.impactLocation = this.getPositionVector(); // should this use the impact block position instead?
				this.pulling = true;
			}
		}
	}

	private void zeroizeHookVelocity() {
		this.setVelocity(0, 0, 0);
	}

	private void sendClientPullPacket(EntityPlayerMP shootingPlayer) {
		HookShotPullPacket pullPacket = new HookShotPullPacket(true, PULL_SPEED, impactLocation);
		CQRMain.NETWORK.sendTo(pullPacket, shootingPlayer);
	}

	private void sendClientStopPacket(EntityPlayerMP shootingPlayer) {
		HookShotPullPacket pullPacket = new HookShotPullPacket(false, 0.0, impactLocation);
		CQRMain.NETWORK.sendTo(pullPacket, shootingPlayer);
	}

	private void checkForBlockedPath(EntityPlayer shootingPlayer) {
		if (this.ticksExisted - this.lastMovementCheckTick >= 4) // once every 4 ticks ~ 0.2 seconds
		{
			Vec3d currentPos = shootingPlayer.getPositionVector();
			if (this.lastShooterPos != null) {
				double distanceTraveled = currentPos.distanceTo(this.lastShooterPos);
				if (distanceTraveled < 0.4) {
					// System.out.println("Cancelling hook because shooter was blocked (dist = " + distanceTraveled + ")");
					this.zeroizeHookVelocity();
					this.setDead();
				}
			}

			this.lastMovementCheckTick = this.ticksExisted;
			this.lastShooterPos = shootingPlayer.getPositionVector();
		}
	}
}