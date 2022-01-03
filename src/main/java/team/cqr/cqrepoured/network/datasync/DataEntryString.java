package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;

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
	public void writeChanges(ByteBuf buf) {
		ByteBufUtil.writeUtf8(buf, this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = ByteBufUtils.readUTF8String(buf);
	}

}
