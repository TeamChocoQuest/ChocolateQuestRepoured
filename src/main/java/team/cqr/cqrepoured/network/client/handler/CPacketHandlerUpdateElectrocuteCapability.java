package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;

public class CPacketHandlerUpdateElectrocuteCapability extends AbstractPacketHandler<SPacketUpdateElectrocuteCapability> {

	@Override
	protected void execHandlePacket(SPacketUpdateElectrocuteCapability message, Supplier<Context> context, World world, PlayerEntity player) {
		if (world == null) {
			return;
		}

		Entity entity = world.getEntity(message.getEntityId());
		if (entity == null) {
			return;
		}

		LazyOptional<CapabilityElectricShock> lOpCap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
		if (!lOpCap.isPresent()) {
			return;
		}
		
		CapabilityElectricShock cap = lOpCap.resolve().get();

		// int charge = message.getElectroCharge();
		int charge = message.isElectrocutionActive() ? 20 : 0;
		cap.setRemainingTicks(charge);
		if (message.hasTarget()) {
			Entity target = world.getEntity(message.getEntityIdTarget());
			cap.setTarget(target);
		} else {
			cap.setTarget(null);
		}
	}

}
