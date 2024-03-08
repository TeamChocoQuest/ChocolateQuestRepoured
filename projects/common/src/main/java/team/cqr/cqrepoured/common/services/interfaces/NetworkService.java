package team.cqr.cqrepoured.common.services.interfaces;

import java.util.Optional;

import de.dertoaster.multihitboxlib.api.network.IMessage;
import de.dertoaster.multihitboxlib.api.network.IMessageHandler;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import net.minecraftforge.network.simple.SimpleChannel;

public interface NetworkService {
	
	public SimpleChannel network();
	
	default public <T extends Object> void send(T packet, PacketTarget target) {
		network().send(target, packet);
	}
	
	default public <T extends Object> void send(PacketTarget target, T packet) {
		network().send(target, packet);
	}

	default public <T extends Object> void sendToServer(T packet) {
		network().sendToServer(packet);
	}
	
	default <MSG> void registerClientToServer(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler) {
		NetworkMethods.registerClientToServer(clsMessage, clsHandler, this);
	}

	default <MSG> void registerServerToClient(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler) {
		NetworkMethods.registerServerToClient(clsMessage, clsHandler, this);
	}

	default <MSG> void register(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler) {
		NetworkMethods.register(clsMessage, clsHandler, this);
	}

	default <MSG> void register(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler, final Optional<NetworkDirection> networkDirection) {
		NetworkMethods.register(clsMessage, clsHandler, networkDirection, this);
	}

	default <MSG> void register(IMessage<MSG> message, IMessageHandler<MSG> handler) {
		NetworkMethods.register(message, handler, this);
	}

	default <MSG> void register(IMessage<MSG> message, IMessageHandler<MSG> handler, final Optional<NetworkDirection> networkDirection) {
		NetworkMethods.register(message, handler, networkDirection, this);
	}

}
