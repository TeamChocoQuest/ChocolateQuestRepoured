package com.teamcqr.chocolatequestrepoured.network.datasync;

import javax.annotation.Nonnull;

import com.teamcqr.chocolatequestrepoured.util.ByteBufUtil;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class DataEntryBlockPos extends DataEntryObject<BlockPos> {

	public DataEntryBlockPos(String name, @Nonnull BlockPos defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue.toImmutable(), isClientModificationAllowed);
	}

	@Override
	public NBTBase write() {
		return DungeonGenUtils.writePosToList(this.value);
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof NBTTagList) {
			this.value = DungeonGenUtils.readPosFromList((NBTTagList) nbt);
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
		super.setInternal(value.toImmutable());
	}

}
