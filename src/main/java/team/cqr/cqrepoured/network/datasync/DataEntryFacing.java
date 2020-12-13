package team.cqr.cqrepoured.network.datasync;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;

public class DataEntryFacing extends DataEntryObject<EnumFacing> {

	public DataEntryFacing(String name, EnumFacing defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public NBTBase write() {
		return new NBTTagByte((byte) this.value.ordinal());
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof NBTTagByte) {
			this.value = EnumFacing.values()[((NBTTagByte) nbt).getByte()];
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		buf.writeByte(this.value.ordinal());
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = EnumFacing.values()[buf.readByte()];
	}

}
