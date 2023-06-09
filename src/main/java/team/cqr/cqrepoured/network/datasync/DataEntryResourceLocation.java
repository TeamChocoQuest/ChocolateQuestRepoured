package team.cqr.cqrepoured.network.datasync;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class DataEntryResourceLocation extends DataEntryObject<ResourceLocation> {

	public DataEntryResourceLocation(String name, @Nonnull ResourceLocation defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public INBT write() {
		return StringTag.valueOf(this.value.toString());
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof StringTag) {
			this.value = new ResourceLocation(((StringTag) nbt).getAsString());
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeUtf(this.value.toString());
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
		this.value = new ResourceLocation(buf.readUtf());
	}

}
