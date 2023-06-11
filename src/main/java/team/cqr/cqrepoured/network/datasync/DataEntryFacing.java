package team.cqr.cqrepoured.network.datasync;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class DataEntryFacing extends DataEntryObject<Direction> {

	public DataEntryFacing(String name, Direction defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public Tag write() {
		return ByteTag.valueOf((byte) this.value.ordinal());
	}

	@Override
	protected void readInternal(Tag nbt) {
		if (nbt instanceof ByteTag) {
			this.value = Direction.values()[((ByteTag) nbt).getAsByte()];
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
