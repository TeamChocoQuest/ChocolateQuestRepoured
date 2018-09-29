package com.tiviacz.chocolatequestrepoured.network;

import com.tiviacz.chocolatequestrepoured.tileentity.TileEntityTable;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateTable implements IMessage {

	private BlockPos pos;
	private int dimension;
	
	public PacketRequestUpdateTable(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public PacketRequestUpdateTable(TileEntityTable te) {
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	public PacketRequestUpdateTable() {
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeInt(dimension);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		dimension = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<PacketRequestUpdateTable, PacketUpdateTable> 
	{
	
		@Override
		public PacketUpdateTable onMessage(PacketRequestUpdateTable message, MessageContext ctx) {
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
			TileEntityTable te = (TileEntityTable)world.getTileEntity(message.pos);
			if (te != null) {
				return new PacketUpdateTable(te);
			} else {
				return null;
			}
		}
	
	}
}