package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class DataEntryBlockPos extends DataEntryObject<BlockPos> {

	public DataEntryBlockPos(String name, @Nonnull BlockPos defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue.immutable(), isClientModificationAllowed);
	}

	@Override
	public Tag write() {
		return DungeonGenUtils.writePosToList(this.value);
	}

	@Override
	protected void readInternal(Tag nbt) {
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
