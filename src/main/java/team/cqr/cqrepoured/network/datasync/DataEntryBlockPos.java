package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.util.ByteBufUtil;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class DataEntryBlockPos extends DataEntryObject<BlockPos> {

	public DataEntryBlockPos(String name, @Nonnull BlockPos defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue.immutable(), isClientModificationAllowed);
	}

	@Override
	public INBT write() {
		return DungeonGenUtils.writePosToList(this.value);
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof ListNBT) {
			this.value = DungeonGenUtils.readPosFromList((ListNBT) nbt);
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		ByteBufUtil.writeBlockPos(buf, this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = ByteBufUtil.readBlockPos(buf);
	}

	@Override
	protected void setInternal(BlockPos value) {
		super.setInternal(value.immutable());
	}

}
