package team.cqr.cqrepoured.network.datasync;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

public class DataEntryString extends DataEntryObject<String> {

	public DataEntryString(String name, @Nonnull String defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public INBT write() {
		return StringTag.valueOf(this.value);
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof StringTag) {
			this.value = ((StringTag) nbt).getAsString();
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeUtf(this.value);
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
		this.value = buf.readUtf();
	}

}
