package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityBubble extends Entity {

	private static final int FLY_TIME_MAX = 160;

	private int flyTicks = 0;

	public EntityBubble(World worldIn) {
		super(worldIn);
		this.isImmuneToFire = true;
		this.setNoGravity(true);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.world.isRemote) {
			if (!this.isBeingRidden() || this.isInLava() || (this.collidedVertically && !this.onGround) || this.flyTicks > FLY_TIME_MAX) {
				if (this.isBeingRidden()) {
					Entity entity = this.getPassengers().get(0);
					entity.dismountRidingEntity();
					entity.setPositionAndUpdate(this.posX, this.posY + 0.5D * (double) (this.height - entity.height), this.posZ);
				}
				this.setDead();
				return;
			}

			this.flyTicks++;
		}

		this.move(MoverType.SELF, 0.0D, 0.05D, 0.0D);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		this.flyTicks += 40;
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.flyTicks = compound.getInteger("flyTicks");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("flyTicks", this.flyTicks);
	}

	@Override
	public double getMountedYOffset() {
		if (this.isBeingRidden()) {
			Entity entity = this.getPassengers().get(0);
			return 0.5D * (double) (this.height - entity.height) - entity.getYOffset();
		}
		return 0.0D;
	}

	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		float size = Math.max(passenger.width, passenger.height) + 0.1F;
		this.setSize(size, size);
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return !this.isBeingRidden();
	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}

}
