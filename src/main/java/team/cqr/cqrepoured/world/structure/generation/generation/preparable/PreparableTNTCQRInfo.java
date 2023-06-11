package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.block.BlockTNTCQR;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.ICQRLevel;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableTNTCQRInfo extends PreparablePosInfo {

	@Override
	protected void prepareNormal(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		level.setBlockState(transformedPos, CQRBlocks.TNT.get().defaultBlockState().setValue(BlockTNTCQR.HIDDEN, true));
	}

	@Override
	protected void prepareDebug(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		level.setBlockState(transformedPos, CQRBlocks.TNT.get().defaultBlockState().setValue(BlockTNTCQR.HIDDEN, false));
	}

	public static class Factory implements IFactory<TileEntity> {

		@Override
		public PreparablePosInfo create(World level, BlockPos pos, BlockState state, LazyOptional<TileEntity> blockEntityLazy) {
			return new PreparableTNTCQRInfo();
		}

	}

	public static class Serializer implements ISerializer<PreparableTNTCQRInfo> {

		@Override
		public void write(PreparableTNTCQRInfo preparable, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			// nothing to write
		}

		@Override
		public PreparableTNTCQRInfo read(ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			return new PreparableTNTCQRInfo();
		}

	}

}
