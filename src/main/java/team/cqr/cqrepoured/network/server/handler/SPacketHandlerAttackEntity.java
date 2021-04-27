package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.packet.CPacketAttackEntity;

public class SPacketHandlerAttackEntity implements IMessageHandler<CPacketAttackEntity, IMessage> {

	@Override
	public IMessage onMessage(CPacketAttackEntity message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity != null && player.getDistanceSq(entity) < getEntityReachDistanceSqr(player, entity)) {
					player.attackTargetEntityWithCurrentItem(entity);
				}
			});
		}
		return null;
	}

	private static double getEntityReachDistanceSqr(EntityPlayer player, Entity entity) {
		double d = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		if (!player.isCreative()) {
			d -= 0.5D;
		}
		d -= 1.5D;
		d += Math.max(entity.width * 0.5D, entity.height);
		return d * d;
	}

}
