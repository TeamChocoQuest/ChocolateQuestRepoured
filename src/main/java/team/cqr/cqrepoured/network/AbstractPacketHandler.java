package team.cqr.cqrepoured.network;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

public abstract class AbstractPacketHandler<P extends Object> implements IMessageHandler<P> {

	public IMessageHandler<P> cast() {
		return (IMessageHandler<P>)this;
	}
	
	@Override
	public final void handlePacket(P packet, Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			Player sender = null;
			Level world = null;
			if(context.get().getNetworkManager().getPacketListener() instanceof ServerPacketListener) {
				sender = context.get().getSender();
				if(sender != null) {
					world = sender.level();
				}
			}
			if(context.get().getNetworkManager().getPacketListener() instanceof ClientPacketListener) {
				sender = ClientOnlyMethods.getClientPlayer();
				world = ClientOnlyMethods.getWorld();
			}
			
			
			this.execHandlePacket(packet, context, world, sender);
		});
		context.get().setPacketHandled(true);
	}
	
	/*
	 * Params:
	 * packet: The packet
	 * context: Network context
	 * world: Optional, set when player is not null or the packet is received clientside, then it is the currently opened world
	 * player: Either the sender of the packet or the local player. Is null for packets recepted during login
	 */
	protected abstract void execHandlePacket(P packet, Supplier<Context> context, @Nullable Level world, @Nullable Player player);

}
