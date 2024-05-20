package team.cqr.cqrepoured.protection;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.HitResult;
import team.cqr.cqrepoured.common.services.CQRServices;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;

public class ProtectionIndicatorHelper {

	public static void addOrResetProtectedRegionIndicator(Level world, UUID uuid, BoundingBox boundingBox, BlockPos pos, @Nullable ServerPlayer player) {
		BlockPos p1 = new BlockPos(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ());
		BlockPos p2 = new BlockPos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ());
		
		addOrResetProtectedRegionIndicator(world, uuid, p1, p2, pos, player);
	}
	
	public static void addOrResetProtectedRegionIndicator(Level world, UUID uuid, BlockPos start, BlockPos end, BlockPos pos, @Nullable ServerPlayer player) {
		if (world.isClientSide) {
			HitResult result = Minecraft.getInstance().hitResult; //Old: objectMouseOver
			if (result != null && result.getLocation() != null) {
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, result.getLocation().x, result.getLocation().y, result.getLocation().z, 0.0D, 0.0D, 0.0D);
			}
		} else if (player != null) {
			CQRServices.NETWORK.sendToServer(new SPacketAddOrResetProtectedRegionIndicator(uuid, start, end, pos)); //Correct?
		}
	}

}
