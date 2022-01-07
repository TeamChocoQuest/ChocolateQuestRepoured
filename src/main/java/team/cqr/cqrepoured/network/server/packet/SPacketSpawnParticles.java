package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketSpawnParticles extends AbstractPacket<SPacketSpawnParticles> {

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
	public SPacketSpawnParticles fromBytes(PacketBuffer buf) {
		SPacketSpawnParticles result = new SPacketSpawnParticles();
		
		result.particleId = buf.readVarInt();
		result.xCoord = buf.readFloat();
		result.yCoord = buf.readFloat();
		result.zCoord = buf.readFloat();
		result.xSpeed = buf.readFloat();
		result.ySpeed = buf.readFloat();
		result.zSpeed = buf.readFloat();
		result.count = buf.readVarInt();
		if (result.count > 1) {
			result.xOffset = buf.readFloat();
			result.yOffset = buf.readFloat();
			result.zOffset = buf.readFloat();
		}
		result.optionalArguments = new int[buf.readVarInt()];
		for (int i = 0; i < result.optionalArguments.length; i++) {
			result.optionalArguments[i] = buf.readVarInt();
		}
		
		return result;
	}

	@Override
	public void toBytes(SPacketSpawnParticles packet, PacketBuffer buf) {
		buf.writeVarInt(packet.particleId);
		buf.writeFloat((float) packet.xCoord);
		buf.writeFloat((float) packet.yCoord);
		buf.writeFloat((float) packet.zCoord);
		buf.writeFloat((float) packet.xSpeed);
		buf.writeFloat((float) packet.ySpeed);
		buf.writeFloat((float) packet.zSpeed);
		buf.writeVarInt(packet.count);
		if (this.count > 1) {
			buf.writeFloat((float) packet.xOffset);
			buf.writeFloat((float) packet.yOffset);
			buf.writeFloat((float) packet.zOffset);
		}
		buf.writeVarInt(packet.optionalArguments.length);
		for (int i = 0; i < this.optionalArguments.length; i++) {
			buf.writeVarInt(packet.optionalArguments[i]);
		}
	}

	public int getParticleId() {
		return this.particleId;
	}

	public double getxCoord() {
		return this.xCoord;
	}

	public double getyCoord() {
		return this.yCoord;
	}

	public double getzCoord() {
		return this.zCoord;
	}

	public double getxSpeed() {
		return this.xSpeed;
	}

	public double getySpeed() {
		return this.ySpeed;
	}

	public double getzSpeed() {
		return this.zSpeed;
	}

	public int getCount() {
		return this.count;
	}

	public double getxOffset() {
		return this.xOffset;
	}

	public double getyOffset() {
		return this.yOffset;
	}

	public double getzOffset() {
		return this.zOffset;
	}

	public int[] getOptionalArguments() {
		return this.optionalArguments;
	}

	@Override
	public Class<SPacketSpawnParticles> getPacketClass() {
		return SPacketSpawnParticles.class;
	}

}
