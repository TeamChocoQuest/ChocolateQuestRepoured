package team.cqr.cqrepoured.network.client.handler;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateCQRMultipart;

public class CPacketHandlerUpdateCQRMultipart extends AbstractPacketHandler<SPacketUpdateCQRMultipart> {

	@Override
	protected void execHandlePacket(SPacketUpdateCQRMultipart packet, Supplier<Context> context, World world, PlayerEntity player) {
		try {
			if (world == null) {
				return;
			}
			Entity ent = world.getEntity(packet.getId());
			if (ent != null && ent.isMultipartEntity()) {
				PartEntity<?>[] parts = ent.getParts();
				if (parts == null)
					return;
				for (PartEntity<?> part : parts) {
					if (part instanceof CQRPartEntity) {
						CQRPartEntity<?> tfPart = (CQRPartEntity<?>) part;
						tfPart.readData(packet.getBuffer());
						if (packet.getBuffer().readBoolean()) {
							List<EntityDataManager.DataEntry<?>> data = EntityDataManager.unpack(packet.getBuffer());
							if (data != null)
								tfPart.getEntityData().assignValues(data);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
