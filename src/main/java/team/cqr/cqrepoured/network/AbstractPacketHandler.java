package team.cqr.cqrepoured.network;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent.Context;

public abstract class AbstractPacketHandler<P extends Object> implements IMessageHandler<P> {

	public IMessageHandler<P> cast() {
		return (IMessageHandler<P>)this;
	}
	
	@Override
	public abstract void handlePacket(P packet, Supplier<Context> context);

}
