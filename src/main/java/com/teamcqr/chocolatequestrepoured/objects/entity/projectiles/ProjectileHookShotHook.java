package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.HookShotPullPacket;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectileHookShotHook extends ProjectileBase {
	private enum HookPhase
	{
		OUT,
		BACK
	}

	public static final double STOP_PULL_DISTANCE = 2.0; //If layer gets within this range of hook, stop pulling
	private Vec3d impactLocation = null; //where the hook intersects a block
	private double hookRange = 20.0; //Max range of the hook before it stops extending
	private HookPhase phase = HookPhase.OUT; //Out for moving away from shooter, back for coming back
	private Vec3d startLocation = null;

	private Vec3d lastShooterPos = null; //last recorded position of the shooter - used to detect blocked path
	private int lastMovementCheckTick = 0; //tick count of last time shooter position was recorded

	//Impact position is a position instead of Rotations, but the structure stores 3 floats so it works nicely
	protected static final DataParameter<Rotations> IMPACT_POS = EntityDataManager.createKey(ProjectileHookShotHook.class, DataSerializers.ROTATIONS);
	protected static final DataParameter<Boolean> IS_PULLING = EntityDataManager.createKey(ProjectileHookShotHook.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Optional<UUID>> SHOOTER_UUID = EntityDataManager.createKey(ProjectileHookShotHook.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	public ProjectileHookShotHook(World worldIn) {
		super(worldIn);
	}

	public ProjectileHookShotHook(World worldIn, double x, double y, double z, double range) {
		super(worldIn, x, y, z);
		this.hookRange = range;
	}

	public ProjectileHookShotHook(World worldIn, EntityLivingBase shooter, double range) {
		super(worldIn, shooter);
		this.dataManager.set(SHOOTER_UUID, Optional.of(shooter.getPersistentID()));
		this.startLocation = shooter.getPositionVector();
		this.hookRange = range;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(IMPACT_POS, new Rotations(0F, 0F, 0F));
		dataManager.register(IS_PULLING, false);
		dataManager.register(SHOOTER_UUID, Optional.absent());
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.getThrower() != null && this.getThrower().isDead) {
			stopPulling();
			setDead();

		} else if (!this.world.isRemote && this.getThrower() instanceof EntityPlayerMP) {
			EntityPlayerMP shootingPlayer = (EntityPlayerMP) this.getThrower();
			Vec3d playerPos = shootingPlayer.getPositionVector();
			double distanceToHook = playerPos.distanceTo(this.getPositionVector());

			if (isPulling()) {
				checkForBlockedPath(shootingPlayer);

				if (distanceToHook < STOP_PULL_DISTANCE) {
					stopPulling();
					setDead();
				}

			} else if (this.phase == HookPhase.OUT && distanceToHook > hookRange) {
				reverseHookVelocity();
				this.phase = HookPhase.BACK;

			} else if (this.phase == HookPhase.BACK && distanceToHook < STOP_PULL_DISTANCE) {
				zeroizeHookVelocity();
				setDead();
			}

		} else if (isPulling()) {
			if (this.world.isRemote) {
				pullIfClientIsShooter();
			}
		}
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (this.getThrower() == null && this.getShooterUUID() == null) {
			this.setDead();
		} else if(this.getThrower() == null) {
			if(!this.world.isRemote) {
				this.thrower = (EntityLivingBase) ((WorldServer)this.world).getEntityFromUuid(this.getShooterUUID());
			}
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				IBlockState state = this.world.getBlockState(result.getBlockPos());

				if (!state.getBlock().isPassable(this.world, result.getBlockPos())) {
					this.zeroizeHookVelocity();
					this.impactLocation = this.getPositionVector(); // should this use the impact block position instead?
					dataManager.set(SHOOTER_UUID, Optional.of(thrower.getUniqueID()));
					dataManager.set(IMPACT_POS, new Rotations((float) impactLocation.x, (float) impactLocation.y, (float) impactLocation.z));

					startPulling();
				}
			}
		}
	}

	private void zeroizeHookVelocity() {
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.velocityChanged = true;
	}

	private void reverseHookVelocity() {
		this.motionX = -this.motionX;
		this.motionY = -this.motionY;
		this.motionZ = -this.motionZ;
		this.velocityChanged = true;
	}

	private void sendClientPullPacket(EntityPlayerMP shootingPlayer) {
		HookShotPullPacket pullPacket = new HookShotPullPacket(true, getPullSpeed(), impactLocation);
		CQRMain.NETWORK.sendTo(pullPacket, shootingPlayer);
	}

	private void sendClientStopPacket(EntityPlayerMP shootingPlayer) {
		HookShotPullPacket pullPacket = new HookShotPullPacket(false, 0.0, impactLocation);
		CQRMain.NETWORK.sendTo(pullPacket, shootingPlayer);
	}

	// Check for no movement in the shooting player and stop pulling if they are blocked
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

	@SideOnly(Side.CLIENT)
	private void pullIfClientIsShooter()
	{
		//Must be client side to check this
		if (this.world.isRemote) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			Optional<UUID> shooterUUID = dataManager.get(SHOOTER_UUID);
			// If this client's player is the shooting player
			if (shooterUUID.isPresent() && player.getUniqueID() == shooterUUID.get()) {
				// Calculate the vector between this player and the hook
				Vec3d playerPos = player.getPositionVector();
				Vec3d impactLocation = this.getPositionVector();

				double distanceToHook = playerPos.distanceTo(impactLocation);

				// Might want to notify server we stopped pulling here...or just let server handle it all
				if (distanceToHook < ProjectileHookShotHook.STOP_PULL_DISTANCE) {
					player.setVelocity(0, 0, 0);
					player.velocityChanged = true;

					return;
				}

				// Pull the player to the hook by swtting their velocity to point towards the latch location
				Vec3d hookDirection = impactLocation.subtract(playerPos);
				Vec3d pullV = hookDirection.normalize().scale(getPullSpeed());

				player.setVelocity(pullV.x, pullV.y, pullV.z);
				player.velocityChanged = true;
			}
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		float x = compound.getFloat("cqrdata.impactX");
		float y = compound.getFloat("cqrdata.impactY");
		float z = compound.getFloat("cqrdata.impactZ");
		this.dataManager.set(IMPACT_POS, new Rotations(x, y, z));

		this.dataManager.set(IS_PULLING, compound.getBoolean("cqrdata.isPulling"));

		if (compound.hasKey("cqrdata.shooterUUID")) {
			this.dataManager.set(SHOOTER_UUID, Optional.of(compound.getUniqueId("cqrdata.shooterUUID")));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		Rotations impactLocation = this.dataManager.get(IMPACT_POS);
		compound.setFloat("cqrdata.impactX", impactLocation.getX());
		compound.setFloat("cqrdata.impactY", impactLocation.getY());
		compound.setFloat("cqrdata.impactZ", impactLocation.getZ());

		compound.setBoolean("cqrdata.isPulling", this.dataManager.get(IS_PULLING));

		UUID shooter;
		if (this.dataManager.get(SHOOTER_UUID).isPresent())
		{
			compound.setUniqueId("cqrdata.shooterUUID", this.dataManager.get(SHOOTER_UUID).get());
		}
	}

	private void startPulling()
	{
		dataManager.set(IS_PULLING, true);
	}

	private void stopPulling()
	{
		dataManager.set(IS_PULLING, true);
	}

	public boolean isPulling()
	{
		return dataManager.get(IS_PULLING);
	}

	protected double getPullSpeed()
	{
		return 1.8; //determined from trial and error on what felt like a good speed
	}

	@Nullable
	public UUID getShooterUUID()
	{
		if (this.dataManager.get(SHOOTER_UUID).isPresent())
		{
			return this.dataManager.get(SHOOTER_UUID).get();
		}
		else
		{
			return null;
		}
	}

	@Nullable
	public Vec3d getImpactLocation()
	{
		return getImpactLocationVec3d();
	}

	private Vec3d getImpactLocationVec3d()
	{
		Rotations impactLocFloat = dataManager.get(IMPACT_POS);
		return new Vec3d(impactLocFloat.getX(), impactLocFloat.getY(), impactLocFloat.getZ());
	}
}