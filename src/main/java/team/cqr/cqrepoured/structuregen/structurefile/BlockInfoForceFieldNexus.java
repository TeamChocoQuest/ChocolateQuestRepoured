package team.cqr.cqrepoured.structuregen.structurefile;

import io.netty.buffer.ByteBuf;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.util.BlockPlacingHelper;

public class BlockInfoForceFieldNexus extends AbstractBlockInfo {

	public BlockInfoForceFieldNexus(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoForceFieldNexus(BlockPos pos) {
		super(pos);
	}

	@Override
	public void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos) {
		if (protectedRegion == null) {
			BlockPlacingHelper.setBlockState(world, pos, Blocks.AIR.getDefaultState(), 18, false);
			return;
		}

		BlockPlacingHelper.setBlockState(world, pos, CQRBlocks.FORCE_FIELD_NEXUS.getDefaultState(), 18, false);

		if (world.getBlockState(pos).getBlock() == CQRBlocks.FORCE_FIELD_NEXUS) {
			protectedRegion.addBlockDependency(pos);
		} else {
			CQRMain.logger.warn("Failed to place force field nexus at {}", pos);
		}
	}

	@Override
	public byte getId() {
		return NEXUS_INFO_ID;
	}

	@Override
	protected void writeToByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {

	}

	@Override
	protected void readFromByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {

	}

}
