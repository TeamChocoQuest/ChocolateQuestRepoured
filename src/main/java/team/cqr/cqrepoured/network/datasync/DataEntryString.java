package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class DataEntryString extends DataEntryObject<String> {

	public DataEntryString(String name, @Nonnull String defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public NBTBase write() {
		return new StringNBT(this.value);
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof StringNBT) {
			this.value = ((StringNBT) nbt).getString();
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = ByteBufUtils.readUTF8String(buf);
	}

}
