package com.teamcqr.chocolatequestrepoured.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ItemStackSyncPacket implements IMessage {

	private int entityId;
	private int slotIndex;
	private ItemStack stack;

	public ItemStackSyncPacket() {

	}

	public ItemStackSyncPacket(int entityId, int slotIndex, ItemStack stack) {
		this.entityId = entityId;
		this.slotIndex = slotIndex;
		this.stack = stack;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.slotIndex = buf.readInt();
		this.stack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeInt(slotIndex);
		ByteBufUtils.writeItemStack(buf, stack);
	}

	public int getEntityId() {
		return entityId;
	}

	public int getSlotIndex() {
		return slotIndex;
	}

	public ItemStack getStack() {
		return stack;
	}

}
