package team.cqr.cqrepoured.network;

import net.minecraft.network.FriendlyByteBuf;

public interface IMessage<S extends Object> {
	
	public Class<S> getPacketClass();
	
	public S fromBytes(FriendlyByteBuf buffer);
	public void toBytes(S packet, FriendlyByteBuf buffer);
	
}
