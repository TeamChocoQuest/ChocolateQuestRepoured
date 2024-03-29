package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketAddPathNode;

public class SPacketHandlerAddPathNode extends AbstractPacketHandler<CPacketAddPathNode> {

	@Override
	protected void execHandlePacket(CPacketAddPathNode packet, Supplier<Context> context, Level world, Player player) {
		ItemStack stack = player.getItemInHand(packet.getHand());

		/*if (stack.getItem() instanceof ItemPathTool) {
			CQRNPCPath path = ItemPathTool.getPath(stack);

			if (path != null) {
				CQRNPCPath.PathNode rootNode = path.getNode(packet.getRootNode());
				BlockPos pos = packet.getPos();
				int waitingTimeMin = packet.getWaitingTimeMin();
				int waitingTimeMax = packet.getWaitingTimeMax();
				float waitingRotaiton = packet.getWaitingRotation();
				int weight = packet.getWeight();
				int timeMin = packet.getTimeMin();
				int timeMax = packet.getTimeMax();
				boolean bidirectional = packet.isBidirectional();

				if (path.addNode(rootNode, pos, waitingTimeMin, waitingTimeMax, waitingRotaiton, weight, timeMin, timeMax, bidirectional)) {
					CQRNPCPath.PathNode addedNode = path.getNode(path.getSize() - 1);
					for (int index : packet.getBlacklistedPrevNodes()) {
						CQRNPCPath.PathNode blacklistedPrevNode = path.getNode(index);
						if (blacklistedPrevNode != null) {
							addedNode.addBlacklistedPrevNode(blacklistedPrevNode);
						}
					}
					ItemPathTool.setSelectedNode(stack, path.getNode(pos));
					player.sendMessage(new StringTextComponent("Added node!"), null);
				}
			}
		}*/
	}

}
