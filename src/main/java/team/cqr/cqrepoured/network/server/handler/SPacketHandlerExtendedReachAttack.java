package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.packet.CPacketExtendedReachAttack;
import team.cqr.cqrepoured.objects.items.spears.ItemSpearBase;

public class SPacketHandlerExtendedReachAttack implements IMessageHandler<CPacketExtendedReachAttack, IMessage> {
	@Override
	public IMessage onMessage(final CPacketExtendedReachAttack message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer attackingPlayer = CQRMain.proxy.getPlayer(ctx);
				World world = CQRMain.proxy.getWorld(ctx);
				Entity attackTarget = world.getEntityByID(message.getEntityId());

				if (attackTarget == null) {
					return;
				}
				if (!(attackingPlayer.getHeldItemMainhand().getItem() instanceof ItemSpearBase)) {
					return;
				}
				if (attackingPlayer.getDistanceSq(attackTarget) < 144.0D) {
					attackingPlayer.attackTargetEntityWithCurrentItem(attackTarget);
				}
			});
		}
		return null;
	}
}
