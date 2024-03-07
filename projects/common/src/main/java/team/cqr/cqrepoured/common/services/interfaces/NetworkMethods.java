package team.cqr.cqrepoured.common.services.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import de.dertoaster.multihitboxlib.api.network.IMessage;
import de.dertoaster.multihitboxlib.api.network.IMessageHandler;
import net.minecraftforge.network.NetworkDirection;

class NetworkMethods {

	static int messageID = 0;
	
	static <MSG> void registerClientToServer(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler, NetworkService service) {
		register(clsMessage, clsHandler, Optional.of(NetworkDirection.PLAY_TO_SERVER), service);
	}

	static <MSG> void registerServerToClient(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler, NetworkService service) {
		register(clsMessage, clsHandler, Optional.of(NetworkDirection.PLAY_TO_CLIENT), service);
	}

	static <MSG> void register(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler, NetworkService service) {
		register(clsMessage, clsHandler, Optional.empty(), service);
	}

	static <MSG> void register(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler, final Optional<NetworkDirection> networkDirection, NetworkService service) {
		IMessage<MSG> message = null;
		IMessageHandler<MSG> handler = null;
		try {
			message = clsMessage.getConstructor(new Class[] {}).newInstance();
			handler = clsHandler.getConstructor(new Class[] {}).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		if (handler != null && message != null)
			register(message, handler, networkDirection, service);
	}

	static <MSG> void register(IMessage<MSG> message, IMessageHandler<MSG> handler, NetworkService service) {
		register(message, handler, Optional.empty(), service);
	}

	static <MSG> void register(IMessage<MSG> message, IMessageHandler<MSG> handler, final Optional<NetworkDirection> networkDirection, NetworkService service) {
		service.network().registerMessage(messageID++, message.getPacketClass(), message::toBytes, message::fromBytes, handler::handlePacket, networkDirection);
	}
	
}
