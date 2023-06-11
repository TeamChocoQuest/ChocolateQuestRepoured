package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class DataEntryString extends DataEntryObject<String> {

	public DataEntryString(String name, @Nonnull String defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public Tag write() {
		return StringTag.valueOf(this.value);
	}

	@Override
	protected void readInternal(Tag nbt) {
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
