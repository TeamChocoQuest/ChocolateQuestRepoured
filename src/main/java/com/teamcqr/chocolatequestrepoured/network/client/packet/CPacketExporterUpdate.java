package com.teamcqr.chocolatequestrepoured.network.client.packet;

import java.io.IOException;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/*
 * Developed by MrMarnic on 28.08.2019
 * GitHub: https://github.com/MrMarnic
 */
public class CPacketExporterUpdate implements IMessage {

	private TileEntityExporter entityExporter;
	private NBTTagCompound data;
	private BlockPos pos;

	public CPacketExporterUpdate() {

	}

	public CPacketExporterUpdate(TileEntityExporter entityExporter) {
		this.entityExporter = entityExporter;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer buffer = new PacketBuffer(buf);
		try {
			this.pos = buffer.readBlockPos();
			this.data = buffer.readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		buffer.writeBlockPos(this.entityExporter.getPos());
		buffer.writeCompoundTag(this.entityExporter.getExporterData(new NBTTagCompound()));
		buf.writeBytes(buffer);
	}

	public NBTTagCompound getExporterData() {
		return this.data;
	}

	public BlockPos getPos() {
		return this.pos;
	}

}
