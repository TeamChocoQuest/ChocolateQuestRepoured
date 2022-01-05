package team.cqr.cqrepoured.network;

import net.minecraft.network.PacketBuffer;

public abstract class AbstractPacket<T extends Object> implements IMessage<T> {
	
	public IMessage<T> cast() {
		return (IMessage<T>)this;
	}
	
	@Override
	public abstract T fromBytes(PacketBuffer buffer);
	
	@Override
	public abstract void toBytes(T packet, PacketBuffer buffer);

}
