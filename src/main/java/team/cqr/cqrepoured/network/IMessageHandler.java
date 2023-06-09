package team.cqr.cqrepoured.network;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMessageHandler<T extends Object> {
	
	public void handlePacket(T packet, Supplier<NetworkEvent.Context> context);

}
