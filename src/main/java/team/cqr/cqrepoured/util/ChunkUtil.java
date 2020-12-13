package team.cqr.cqrepoured.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

public class ChunkUtil {

	private static final ReflectionField<Integer> FIELD_MAX_DEPTH = new ReflectionField<>(ForgeChunkManager.Ticket.class, "maxDepth", "maxDepth");

	public static ForgeChunkManager.Ticket getTicket(World world, BlockPos pos1, BlockPos pos2) {
		return getTicket(world, pos1.getX() >> 4, pos1.getZ() >> 4, pos2.getX() >> 4, pos2.getZ() >> 4, false);
	}

	public static ForgeChunkManager.Ticket getTicket(World world, BlockPos pos1, BlockPos pos2, boolean overwriteChunkLimit) {
		return getTicket(world, pos1.getX() >> 4, pos1.getZ() >> 4, pos2.getX() >> 4, pos2.getZ() >> 4, overwriteChunkLimit);
	}

	public static ForgeChunkManager.Ticket getTicket(World world, int chunkStartX, int chunkStartZ, int chunkEndX, int chunkEndZ) {
		return getTicket(world, chunkStartX, chunkStartZ, chunkEndX, chunkEndZ, false);
	}

	public static ForgeChunkManager.Ticket getTicket(World world, int chunkStartX, int chunkStartZ, int chunkEndX, int chunkEndZ, boolean overwriteChunkLimit) {
		ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestTicket(CQRMain.INSTANCE, world, ForgeChunkManager.Type.NORMAL);
		if (ticket == null) {
			return null;
		}
		if (CQRConfig.advanced.overwriteForgeChunkLoadingLimit && overwriteChunkLimit) {
			FIELD_MAX_DEPTH.set(ticket, 0);
		}
		for (int x = chunkStartX; x <= chunkEndX; x++) {
			for (int z = chunkStartZ; z <= chunkEndZ; z++) {
				ForgeChunkManager.forceChunk(ticket, new ChunkPos(x, z));
			}
		}
		return ticket;
	}

}
