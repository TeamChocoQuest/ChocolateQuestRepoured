package com.teamcqr.chocolatequestrepoured.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ParticlesMessageToClient implements IMessage {
	private Vec3d targetCoordinates;
	private int numberOfParticles;
	private int particleId;
	private boolean messageIsValid;

	public ParticlesMessageToClient(Vec3d targetCoordinates, int particleId, int numberOfParticles) {
		this.targetCoordinates = targetCoordinates;
		this.particleId = particleId;
		this.numberOfParticles = numberOfParticles;
		this.messageIsValid = true;
	}

	public ParticlesMessageToClient() {
		this.messageIsValid = false;
	}

	public TargetPoint getTargetPoint(EntityLivingBase target) {
		return new NetworkRegistry.TargetPoint(target.dimension, target.posX, target.posY, target.posZ, 64);
	}

	public Vec3d getTargetCoordinates() {
		return this.targetCoordinates;
	}

	public EnumParticleTypes getParticleType() {
		return EnumParticleTypes.getParticleFromId(this.particleId);
	}

	public int getNumberOfParticles() {
		return this.numberOfParticles;
	}

	public boolean isMessageValid() {
		return this.messageIsValid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			int p = buf.readInt();
			int n = buf.readInt();
			this.targetCoordinates = new Vec3d(x, y, z);
			this.particleId = p;
			this.numberOfParticles = n;

		} catch (IndexOutOfBoundsException ioe) {
			System.err.println("Exception while reading ParticlesMessageToClient: " + ioe);
			return;
		}
		this.messageIsValid = true;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (!this.messageIsValid) {
			return;
		}

		buf.writeDouble(this.targetCoordinates.x);
		buf.writeDouble(this.targetCoordinates.y);
		buf.writeDouble(this.targetCoordinates.z);
		buf.writeInt(this.particleId);
		buf.writeInt(this.numberOfParticles);
	}

	@Override
	public String toString() {
		return "TargetEffectMessageToClient[targetCoordinates=" + String.valueOf(this.targetCoordinates) + "]";
	}
}