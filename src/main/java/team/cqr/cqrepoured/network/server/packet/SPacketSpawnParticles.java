package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketSpawnParticles implements IMessage {

	private int particleId;
	private double xCoord;
	private double yCoord;
	private double zCoord;
	private double xSpeed;
	private double ySpeed;
	private double zSpeed;
	private int count;
	private double xOffset;
	private double yOffset;
	private double zOffset;
	private int[] optionalArguments;

	public SPacketSpawnParticles() {

	}

	public SPacketSpawnParticles(int particleId, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... optionalArguments) {
		this.particleId = particleId;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.zSpeed = zSpeed;
		this.count = 1;
		this.optionalArguments = optionalArguments;
	}

	public SPacketSpawnParticles(int particleId, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int count, double xOffset, double yOffset, double zOffset, int... optionalArguments) {
		this.particleId = particleId;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.zSpeed = zSpeed;
		this.count = count;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.optionalArguments = optionalArguments;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.particleId = ByteBufUtils.readVarInt(buf, 5);
		this.xCoord = buf.readFloat();
		this.yCoord = buf.readFloat();
		this.zCoord = buf.readFloat();
		this.xSpeed = buf.readFloat();
		this.ySpeed = buf.readFloat();
		this.zSpeed = buf.readFloat();
		this.count = ByteBufUtils.readVarInt(buf, 5);
		if (this.count > 1) {
			this.xOffset = buf.readFloat();
			this.yOffset = buf.readFloat();
			this.zOffset = buf.readFloat();
		}
		this.optionalArguments = new int[ByteBufUtils.readVarInt(buf, 5)];
		for (int i = 0; i < this.optionalArguments.length; i++) {
			this.optionalArguments[i] = ByteBufUtils.readVarInt(buf, 5);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, this.particleId, 5);
		buf.writeFloat((float) this.xCoord);
		buf.writeFloat((float) this.yCoord);
		buf.writeFloat((float) this.zCoord);
		buf.writeFloat((float) this.xSpeed);
		buf.writeFloat((float) this.ySpeed);
		buf.writeFloat((float) this.zSpeed);
		ByteBufUtils.writeVarInt(buf, this.count, 5);
		if (this.count > 1) {
			buf.writeFloat((float) this.xOffset);
			buf.writeFloat((float) this.yOffset);
			buf.writeFloat((float) this.zOffset);
		}
		ByteBufUtils.writeVarInt(buf, this.optionalArguments.length, 5);
		for (int i = 0; i < this.optionalArguments.length; i++) {
			ByteBufUtils.writeVarInt(buf, this.optionalArguments[i], 5);
		}
	}

	public int getParticleId() {
		return particleId;
	}

	public double getxCoord() {
		return xCoord;
	}

	public double getyCoord() {
		return yCoord;
	}

	public double getzCoord() {
		return zCoord;
	}

	public double getxSpeed() {
		return xSpeed;
	}

	public double getySpeed() {
		return ySpeed;
	}

	public double getzSpeed() {
		return zSpeed;
	}

	public int getCount() {
		return count;
	}

	public double getxOffset() {
		return xOffset;
	}

	public double getyOffset() {
		return yOffset;
	}

	public double getzOffset() {
		return zOffset;
	}

	public int[] getOptionalArguments() {
		return optionalArguments;
	}

}
