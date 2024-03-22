package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.block.banner.BannerHelper;
import team.cqr.cqrepoured.common.buffer.ByteBufUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.ISerializer;

public class PreparableBannerInfo extends PreparableBlockInfo {

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

	public static class Factory implements IFactory<BannerBlockEntity> {

		@Override
		public PreparablePosInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<BannerBlockEntity> blockEntityLazy) {
			BannerBlockEntity blockEntity = blockEntityLazy.orElseThrow(NullPointerException::new);
			if (BannerHelper.isCQBanner(blockEntity)) {
				return new PreparableBannerInfo(state, IFactory.writeTileEntityToNBT(blockEntity));
			}
			return new PreparableBlockInfo(state, IFactory.writeTileEntityToNBT(blockEntity));
		}

	}

	public static class Serializer implements ISerializer<PreparableBannerInfo> {

		@Override
		public void write(PreparableBannerInfo preparable, ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			int data = (palette.idFor(preparable.getState()) << 1) | (preparable.getTileEntityData() != null ? 1 : 0);
			ByteBufUtil.writeVarInt(buf, data, 5);
			if (preparable.getTileEntityData() != null) {
				ByteBufUtil.writeVarInt(buf, nbtList.size(), 5);
				nbtList.add(preparable.getTileEntityData());
			}
		}

		@Override
		public PreparableBannerInfo read(ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			int data = ByteBufUtil.readVarInt(buf, 5);
			BlockState state = palette.stateFor(data >>> 1);
			CompoundTag tileEntityData = null;
			if ((data & 1) == 1) {
				tileEntityData = nbtList.getCompound(ByteBufUtil.readVarInt(buf, 5));
			}
			return new PreparableBannerInfo(state, tileEntityData);
		}

	}

}
