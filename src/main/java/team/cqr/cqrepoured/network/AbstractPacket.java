package team.cqr.cqrepoured.network;

import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractPacket<T extends Object> implements IMessage<T> {
	
	public IMessage<T> cast() {
		return (IMessage<T>)this;
	}
	
	@Override
	public abstract T fromBytes(FriendlyByteBuf buffer);
	
	@Override
	public abstract void toBytes(T packet, FriendlyByteBuf buffer);

}
