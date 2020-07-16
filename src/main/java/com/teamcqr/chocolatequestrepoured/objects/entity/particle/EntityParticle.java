package com.teamcqr.chocolatequestrepoured.objects.entity.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class EntityParticle extends Particle {

	public EntityParticle(final World worldIn, final double posXIn, final double posYIn, final double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);
	}

	public EntityParticle(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
	}

	public void setMotionX(final double motionX) {
		this.motionX = motionX;
	}

	public void setMotionY(final double motionY) {
		this.motionY = motionY;
	}

	public void setMotionZ(final double motionZ) {
		this.motionZ = motionZ;
	}

	public double getX() {
		return this.posX;
	}

	public double getY() {
		return this.posY;
	}

	public double getZ() {
		return this.posZ;
	}

	public double getMotionX() {
		return this.motionX;
	}

	public double getMotionY() {
		return this.motionY;
	}

	public double getMotionZ() {
		return this.motionZ;
	}

}
