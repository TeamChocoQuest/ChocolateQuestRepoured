package team.cqr.cqrepoured.network;

import net.minecraft.network.PacketBuffer;

public interface IMessage<S extends Object> {
	
	public Class<S> getPacketClass();
	
	public S fromBytes(PacketBuffer buffer);
	public void toBytes(S packet, PacketBuffer buffer);
	
}
