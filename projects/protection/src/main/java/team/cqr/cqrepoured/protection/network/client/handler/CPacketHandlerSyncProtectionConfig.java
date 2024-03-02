//package team.cqr.cqrepoured.network.client.handler;
//
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.network.NetworkEvent.Context;
//import team.cqr.cqrepoured.config.CQRConfig;
//import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
//import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectionConfig;
//import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionHelper;
//
//import java.util.function.Supplier;
//
//public class CPacketHandlerSyncProtectionConfig extends AbstractPacketHandler<SPacketSyncProtectionConfig> {
//
//	@Override
//	protected void execHandlePacket(SPacketSyncProtectionConfig message, Supplier<Context> context, World world, PlayerEntity player) {
//		//TODO: Implement again
//		CQRConfig.SERVER_CONFIG.dungeonProtection = message.getProtectionConfig();
//		ProtectedRegionHelper.updateWhitelists();
//	}
//
//}
