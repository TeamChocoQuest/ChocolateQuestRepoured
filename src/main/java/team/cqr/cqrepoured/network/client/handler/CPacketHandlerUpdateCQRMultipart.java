package team.cqr.cqrepoured.network.client.handler;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateCQRMultipart;

public class CPacketHandlerUpdateCQRMultipart extends AbstractPacketHandler<SPacketUpdateCQRMultipart> {

	@Override
	protected void execHandlePacket(SPacketUpdateCQRMultipart packet, Supplier<Context> context, Level world, Player player) {
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
							List<SynchedEntityData.DataEntry<?>> data = SynchedEntityData.unpack(packet.getBuffer());
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
