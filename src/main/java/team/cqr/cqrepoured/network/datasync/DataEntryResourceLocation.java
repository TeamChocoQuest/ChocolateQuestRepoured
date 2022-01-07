package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class DataEntryResourceLocation extends DataEntryObject<ResourceLocation> {

	public DataEntryResourceLocation(String name, @Nonnull ResourceLocation defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public INBT write() {
		return StringNBT.valueOf(this.value.toString());
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof StringNBT) {
			this.value = new ResourceLocation(((StringNBT) nbt).getAsString());
		}
	}

	@Override
	public void writeChanges(PacketBuffer buf) {
		buf.writeUtf(this.value.toString());
	}

	@Override
	protected void readChangesInternal(PacketBuffer buf) {
		this.value = new ResourceLocation(buf.readUtf());
	}

}
