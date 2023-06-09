package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class DataEntryResourceLocation extends DataEntryObject<ResourceLocation> {

	public DataEntryResourceLocation(String name, @Nonnull ResourceLocation defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public Tag write() {
		return StringTag.valueOf(this.value.toString());
	}

	@Override
	protected void readInternal(Tag nbt) {
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
