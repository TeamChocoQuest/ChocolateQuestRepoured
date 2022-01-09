package team.cqr.cqrepoured.network.client.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.network.AbstractPacket;

public class CPacketCloseMapPlaceholderGuiSimple extends AbstractPacket<CPacketCloseMapPlaceholderGuiSimple> {

	private BlockPos pos;
	private Direction facing;
	private int scale;
	private Direction orientation;
	private boolean lockOrientation;
	private int sizeUp;
	private int sizeDown;
	private int sizeRight;
	private int sizeLeft;
	private boolean fillMap;
	private int fillRadius;

	public CPacketCloseMapPlaceholderGuiSimple() {

	}

	public CPacketCloseMapPlaceholderGuiSimple(BlockPos pos, Direction facing, int scale, Direction orientation, boolean lockOrientation, int sizeUp, int sizeDown, int sizeRight, int sizeLeft, boolean fillMap, int fillRadius) {
		this.pos = pos;
		this.facing = facing;
		this.scale = scale;
		this.orientation = orientation;
		this.lockOrientation = lockOrientation;
		this.sizeUp = sizeUp;
		this.sizeDown = sizeDown;
		this.sizeRight = sizeRight;
		this.sizeLeft = sizeLeft;
		this.fillMap = fillMap;
		this.fillRadius = fillRadius;
	}

	@Override
	public CPacketCloseMapPlaceholderGuiSimple fromBytes(PacketBuffer buf) {
		CPacketCloseMapPlaceholderGuiSimple result = new CPacketCloseMapPlaceholderGuiSimple();
		
		result.pos = buf.readBlockPos();
		result.facing = Direction.from2DDataValue(buf.readByte());
		result.scale = buf.readByte();
		result.orientation = Direction.from2DDataValue(buf.readByte());
		result.lockOrientation = buf.readBoolean();
		result.sizeUp = buf.readByte();
		result.sizeDown = buf.readByte();
		result.sizeRight = buf.readByte();
		result.sizeLeft = buf.readByte();
		result.fillMap = buf.readBoolean();
		result.fillRadius = buf.readShort();
		
		return result;
	}

	@Override
	public void toBytes(CPacketCloseMapPlaceholderGuiSimple packet, PacketBuffer buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeByte(packet.facing.get2DDataValue());
		buf.writeByte(packet.scale);
		buf.writeByte(packet.orientation.get2DDataValue());
		buf.writeBoolean(packet.lockOrientation);
		buf.writeByte(packet.sizeUp);
		buf.writeByte(packet.sizeDown);
		buf.writeByte(packet.sizeRight);
		buf.writeByte(packet.sizeLeft);
		buf.writeBoolean(packet.fillMap);
		buf.writeShort(packet.fillRadius);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public Direction getFacing() {
		return this.facing;
	}

	public int getScale() {
		return this.scale;
	}

	public Direction getOrientation() {
		return this.orientation;
	}

	public boolean isLockOrientation() {
		return this.lockOrientation;
	}

	public int getSizeUp() {
		return this.sizeUp;
	}

	public int getSizeDown() {
		return this.sizeDown;
	}

	public int getSizeRight() {
		return this.sizeRight;
	}

	public int getSizeLeft() {
		return this.sizeLeft;
	}

	public boolean isFillMap() {
		return this.fillMap;
	}

	public int getFillRadius() {
		return this.fillRadius;
	}

	@Override
	public Class<CPacketCloseMapPlaceholderGuiSimple> getPacketClass() {
		return CPacketCloseMapPlaceholderGuiSimple.class;
	}

}
