package team.cqr.cqrepoured.client.world.structure.protection;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;

public class ProtectionIndicatorHelper {

	public static void addOrResetProtectedRegionIndicator(World world, UUID uuid, BlockPos start, BlockPos end, BlockPos pos, @Nullable EntityPlayerMP player) {
		if (world.isRemote) {
			RayTraceResult result = Minecraft.getMinecraft().objectMouseOver;
			if (result != null && result.hitVec != null) {
				world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, result.hitVec.x, result.hitVec.y, result.hitVec.z, 0.0D, 0.0D, 0.0D);
			}
		} else if (player != null) {
			CQRMain.NETWORK.sendTo(new SPacketAddOrResetProtectedRegionIndicator(uuid, start, end, pos), player);
		}
	}

}
