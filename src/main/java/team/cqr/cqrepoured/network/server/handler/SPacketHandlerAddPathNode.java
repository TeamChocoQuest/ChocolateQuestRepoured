package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.packet.CPacketAddPathNode;
import team.cqr.cqrepoured.objects.entity.pathfinding.Path;
import team.cqr.cqrepoured.objects.items.ItemPathTool;

public class SPacketHandlerAddPathNode implements IMessageHandler<CPacketAddPathNode, IMessage> {

	@Override
	public IMessage onMessage(CPacketAddPathNode message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				ItemStack stack = player.getHeldItem(message.getHand());

				if (stack.getItem() instanceof ItemPathTool) {
					Path path = ItemPathTool.getPath(stack);

					if (path != null) {
						Path.PathNode rootNode = path.getNode(message.getRootNode());
						BlockPos pos = message.getPos();
						int waitingTimeMin = message.getWaitingTimeMin();
						int waitingTimeMax = message.getWaitingTimeMax();
						float waitingRotaiton = message.getWaitingRotation();
						int weight = message.getWeight();
						int timeMin = message.getTimeMin();
						int timeMax = message.getTimeMax();
						boolean bidirectional = message.isBidirectional();

						if (path.addNode(rootNode, pos, waitingTimeMin, waitingTimeMax, waitingRotaiton, weight, timeMin, timeMax, bidirectional)) {
							Path.PathNode addedNode = path.getNode(path.getSize() - 1);
							for (int index : message.getBlacklistedPrevNodes()) {
								Path.PathNode blacklistedPrevNode = path.getNode(index);
								if (blacklistedPrevNode != null) {
									addedNode.addBlacklistedPrevNode(blacklistedPrevNode);
								}
							}
							ItemPathTool.setSelectedNode(stack, path.getNode(pos));
							player.sendMessage(new TextComponentString("Added node!"));
						}
					}
				}
			});
		}
		return null;
	}

}
