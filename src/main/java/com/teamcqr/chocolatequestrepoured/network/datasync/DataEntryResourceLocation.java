package com.teamcqr.chocolatequestrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class DataEntryResourceLocation extends DataEntryObject<ResourceLocation> {

	public DataEntryResourceLocation(String name, @Nonnull ResourceLocation defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue, isClientModificationAllowed);
	}

	@Override
	public NBTBase write() {
		return new NBTTagString(this.value.toString());
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof NBTTagString) {
			this.value = new ResourceLocation(((NBTTagString) nbt).getString());
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
