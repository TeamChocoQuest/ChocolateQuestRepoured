package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.block.banner.BannerHelper;
import team.cqr.cqrepoured.common.io.DataIOUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;

public class PreparableBannerInfo extends BlockInfo {

	public PreparableBannerInfo(BlockState state, @Nullable CompoundTag tileEntityData) {
		super(state, tileEntityData);
	}

	@Override
	protected void blockEntityCallback(BlockPos pos, BlockState state, BlockEntity blockEntity, DungeonPlacement placement) {
		super.blockEntityCallback(pos, state, blockEntity, placement);

		if (blockEntity instanceof BannerBlockEntity bannerBE) {
			placement.inhabitant().prepare(bannerBE);
		}
	}

	public static class Factory implements IBlockInfoFactory<BannerBlockEntity> {

		@Override
		public IBlockInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<BannerBlockEntity> blockEntitySupplier) {
			BannerBlockEntity blockEntity = blockEntitySupplier.orElseThrow(NullPointerException::new);
			if (BannerHelper.isCQBanner(blockEntity)) {
				return new PreparableBannerInfo(state, IBlockInfoFactory.writeBlockEntityToNBT(blockEntity));
			}
			return new BlockInfo(state, IBlockInfoFactory.writeBlockEntityToNBT(blockEntity));
		}

	}

	public static class Serializer implements IBlockInfoSerializer<PreparableBannerInfo> {

		@Override
		public void write(PreparableBannerInfo bannerInfo, DataOutput out, SimplePalette palette) throws IOException {
			int data = (palette.idFor(bannerInfo.getBlockState()) << 1) | (bannerInfo.getBlockEntityTag() != null ? 1 : 0);
			DataIOUtil.writeVarInt(out, data);
			if (bannerInfo.getBlockEntityTag() != null) {
				NbtIo.write(bannerInfo.getBlockEntityTag(), out);
			}
		}

		@Override
		public PreparableBannerInfo read(DataInput in, SimplePalette palette) throws IOException {
			int data = DataIOUtil.readVarInt(in);
			BlockState blockState = palette.stateFor(data >>> 1);
			CompoundTag blockEntityTag = null;
			if ((data & 1) == 1) {
				blockEntityTag = NbtIo.read(in);
			}
			return new PreparableBannerInfo(blockState, blockEntityTag);
		}

	}

}
