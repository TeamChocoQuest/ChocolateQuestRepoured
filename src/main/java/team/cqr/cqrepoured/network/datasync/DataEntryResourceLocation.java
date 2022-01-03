package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
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
		return StringNBT.valueOf(this.value.toString());
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof StringNBT) {
			this.value = new ResourceLocation(((StringNBT) nbt).getAsString());
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		ByteBufUtil.writeUtf8(buf, this.value.toString());
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
	}

}
