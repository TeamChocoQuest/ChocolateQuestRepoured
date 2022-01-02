package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class DataEntryResourceLocation extends DataEntryObject<ResourceLocation> {

	public DataEntryResourceLocation(String name, @Nonnull ResourceLocation defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public INBT write() {
		return new StringNBT(this.value.toString());
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof StringNBT) {
			this.value = new ResourceLocation(((StringNBT) nbt).getString());
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.value.toString());
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
	}

}
