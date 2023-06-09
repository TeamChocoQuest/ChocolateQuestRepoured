package team.cqr.cqrepoured.network.datasync;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.FriendlyByteBuf;

public class DataEntryFacing extends DataEntryObject<Direction> {

	public DataEntryFacing(String name, Direction defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public INBT write() {
		return ByteNBT.valueOf((byte) this.value.ordinal());
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof ByteNBT) {
			this.value = Direction.values()[((ByteNBT) nbt).getAsByte()];
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeByte(this.value.ordinal());
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
		this.value = Direction.values()[buf.readByte()];
	}

}
