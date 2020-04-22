package com.teamcqr.chocolatequestrepoured.network.packets.toClient;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketSyncProtectedRegions implements IMessage {

	private final List<NBTTagCompound> protectedRegions = new ArrayList<>();

	public SPacketSyncProtectedRegions() {

	}

	public SPacketSyncProtectedRegions(List<ProtectedRegion> protectedRegions) {
		for (ProtectedRegion protectedRegion : protectedRegions) {
			if (protectedRegion != null) {
				this.protectedRegions.add(protectedRegion.writeToNBT());
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readShort();
		for (int i = 0; i < size; i++) {
			this.protectedRegions.add(ByteBufUtils.readTag(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeShort(this.protectedRegions.size());
		for (NBTTagCompound compound : this.protectedRegions) {
			ByteBufUtils.writeTag(buf, compound);
		}
	}

	public List<NBTTagCompound> getProtectedRegions() {
		return this.protectedRegions;
	}

}
