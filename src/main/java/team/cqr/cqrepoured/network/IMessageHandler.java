package team.cqr.cqrepoured.network;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;

public interface IMessageHandler<T extends Object> {
	
	public void handlePacket(T packet, Supplier<NetworkEvent.Context> context);

}
