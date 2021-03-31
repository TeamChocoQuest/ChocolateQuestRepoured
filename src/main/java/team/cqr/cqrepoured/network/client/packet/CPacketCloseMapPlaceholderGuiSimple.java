package team.cqr.cqrepoured.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class CPacketCloseMapPlaceholderGuiSimple implements IMessage {

	private BlockPos pos;
	private EnumFacing facing;
	private int scale;
	private EnumFacing orientation;
	private boolean lockOrientation;
	private int sizeUp;
	private int sizeDown;
	private int sizeRight;
	private int sizeLeft;
	private boolean fillMap;
	private int fillRadius;

	public CPacketCloseMapPlaceholderGuiSimple() {

	}

	public CPacketCloseMapPlaceholderGuiSimple(BlockPos pos, EnumFacing facing, int scale, EnumFacing orientation, boolean lockOrientation, int sizeUp, int sizeDown, int sizeRight, int sizeLeft, boolean fillMap, int fillRadius) {
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
	public void fromBytes(ByteBuf buf) {
		this.pos = ByteBufUtil.readBlockPos(buf);
		this.facing = EnumFacing.byHorizontalIndex(buf.readByte());
		this.scale = buf.readByte();
		this.orientation = EnumFacing.byHorizontalIndex(buf.readByte());
		this.lockOrientation = buf.readBoolean();
		this.sizeUp = buf.readByte();
		this.sizeDown = buf.readByte();
		this.sizeRight = buf.readByte();
		this.sizeLeft = buf.readByte();
		this.fillMap = buf.readBoolean();
		this.fillRadius = buf.readShort();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtil.writeBlockPos(buf, this.pos);
		buf.writeByte(this.facing.getHorizontalIndex());
		buf.writeByte(this.scale);
		buf.writeByte(this.orientation.getHorizontalIndex());
		buf.writeBoolean(this.lockOrientation);
		buf.writeByte(this.sizeUp);
		buf.writeByte(this.sizeDown);
		buf.writeByte(this.sizeRight);
		buf.writeByte(this.sizeLeft);
		buf.writeBoolean(this.fillMap);
		buf.writeShort(this.fillRadius);
	}

	public BlockPos getPos() {
		return pos;
	}

	public EnumFacing getFacing() {
		return facing;
	}

	public int getScale() {
		return scale;
	}

	public EnumFacing getOrientation() {
		return orientation;
	}

	public boolean isLockOrientation() {
		return lockOrientation;
	}

	public int getSizeUp() {
		return sizeUp;
	}

	public int getSizeDown() {
		return sizeDown;
	}

	public int getSizeRight() {
		return sizeRight;
	}

	public int getSizeLeft() {
		return sizeLeft;
	}

	public boolean isFillMap() {
		return fillMap;
	}

	public int getFillRadius() {
		return fillRadius;
	}

}
