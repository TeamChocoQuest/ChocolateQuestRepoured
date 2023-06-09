package team.cqr.cqrepoured.network.datasync;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import team.cqr.cqrepoured.util.DungeonGenUtils;

import javax.annotation.Nonnull;

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
		if (nbt instanceof ListTag) {
			this.value = DungeonGenUtils.readPosFromList((ListTag) nbt);
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.value);
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
		this.value = buf.readBlockPos();
	}

	@Override
	protected void setInternal(BlockPos value) {
		super.setInternal(value.immutable());
	}

}
