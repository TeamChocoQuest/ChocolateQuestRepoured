package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.pathfinding.Path;
import team.cqr.cqrepoured.item.ItemPathTool;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.IMessageHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketAddPathNode;

public class SPacketHandlerAddPathNode extends AbstractPacketHandler<CPacketAddPathNode> {


	@Override
	public IMessageHandler<CPacketAddPathNode> cast() {
		return (IMessageHandler<CPacketAddPathNode>)this;
	}

	@Override
	public void handlePacket(CPacketAddPathNode packet, Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();
			ItemStack stack = player.getItemInHand(packet.getHand());

			if (stack.getItem() instanceof ItemPathTool) {
				Path path = ItemPathTool.getPath(stack);

				if (path != null) {
					Path.PathNode rootNode = path.getNode(packet.getRootNode());
					BlockPos pos = packet.getPos();
					int waitingTimeMin = packet.getWaitingTimeMin();
					int waitingTimeMax = packet.getWaitingTimeMax();
					float waitingRotaiton = packet.getWaitingRotation();
					int weight = packet.getWeight();
					int timeMin = packet.getTimeMin();
					int timeMax = packet.getTimeMax();
					boolean bidirectional = packet.isBidirectional();

					if (path.addNode(rootNode, pos, waitingTimeMin, waitingTimeMax, waitingRotaiton, weight, timeMin, timeMax, bidirectional)) {
						Path.PathNode addedNode = path.getNode(path.getSize() - 1);
						for (int index : packet.getBlacklistedPrevNodes()) {
							Path.PathNode blacklistedPrevNode = path.getNode(index);
							if (blacklistedPrevNode != null) {
								addedNode.addBlacklistedPrevNode(blacklistedPrevNode);
							}
						}
						ItemPathTool.setSelectedNode(stack, path.getNode(pos));
						player.sendMessage(new StringTextComponent("Added node!"), null);
					}
				}
			}
		});
		context.get().setPacketHandled(true);
	}

}
