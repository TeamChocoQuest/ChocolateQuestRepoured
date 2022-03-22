package team.cqr.cqrepoured.network.datasync;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

public class DataEntryString extends DataEntryObject<String> {

	public DataEntryString(String name, @Nonnull String defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public INBT write() {
		return StringNBT.valueOf(this.value);
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof StringNBT) {
			this.value = ((StringNBT) nbt).getAsString();
		}
	}

	@Override
	public void writeChanges(PacketBuffer buf) {
		buf.writeUtf(this.value);
	}

	@Override
	protected void readChangesInternal(PacketBuffer buf) {
		this.value = buf.readUtf();
	}

}
