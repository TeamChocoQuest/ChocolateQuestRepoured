package team.cqr.cqrepoured.client.world.structure.protection;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;

import javax.annotation.Nullable;
import java.util.UUID;

public class ProtectionIndicatorHelper {

	public static void addOrResetProtectedRegionIndicator(Level world, UUID uuid, BlockPos start, BlockPos end, BlockPos pos, @Nullable ServerPlayer player) {
		if (world.isClientSide) {
			HitResult result = Minecraft.getInstance().hitResult; //Old: objectMouseOver
			if (result != null && result.getLocation() != null) {
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, result.getLocation().x, result.getLocation().y, result.getLocation().z, 0.0D, 0.0D, 0.0D);
			}
		} else if (player != null) {
			CQRMain.NETWORK.sendToServer(new SPacketAddOrResetProtectedRegionIndicator(uuid, start, end, pos)); //Correct?
		}
	}

}
