package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.item.BannerItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.block.banner.BannerHelper;
import team.cqr.cqrepoured.util.ByteBufUtil;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableBannerInfo extends PreparableBlockInfo {

	public PreparableBannerInfo(BlockState state, @Nullable CompoundNBT tileEntityData) {
		super(state, tileEntityData);
	}

	@Override
	protected void blockEntityCallback(BlockPos pos, BlockState state, TileEntity blockEntity, DungeonPlacement placement) {
		super.blockEntityCallback(pos, state, blockEntity, placement);

		if (blockEntity instanceof BannerTileEntity && placement.getInhabitant().getBanner() != null) {
			ItemStack stack = placement.getInhabitant().getBanner().getBanner();
			Item item = stack.getItem();
			DyeColor color = item instanceof BannerItem ? ((BannerItem) item).getColor() : DyeColor.BLACK;
			((BannerTileEntity) blockEntity).fromItem(stack, color);
		}
	}

	public static class Factory implements IFactory<BannerTileEntity> {

		@Override
		public PreparablePosInfo create(World level, BlockPos pos, BlockState state, LazyOptional<BannerTileEntity> blockEntityLazy) {
			BannerTileEntity blockEntity = blockEntityLazy.orElseThrow(NullPointerException::new);
			if (BannerHelper.isCQBanner(blockEntity)) {
				return new PreparableBannerInfo(state, IFactory.writeTileEntityToNBT(blockEntity));
			}
			return new PreparableBlockInfo(state, IFactory.writeTileEntityToNBT(blockEntity));
		}

	}

	public static class Serializer implements ISerializer<PreparableBannerInfo> {

		@Override
		public void write(PreparableBannerInfo preparable, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			int data = (palette.idFor(preparable.getState()) << 1) | (preparable.getTileEntityData() != null ? 1 : 0);
			ByteBufUtil.writeVarInt(buf, data, 5);
			if (preparable.getTileEntityData() != null) {
				ByteBufUtil.writeVarInt(buf, nbtList.size(), 5);
				nbtList.add(preparable.getTileEntityData());
			}
		}

		@Override
		public PreparableBannerInfo read(ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			int data = ByteBufUtil.readVarInt(buf, 5);
			BlockState state = palette.stateFor(data >>> 1);
			CompoundNBT tileEntityData = null;
			if ((data & 1) == 1) {
				tileEntityData = nbtList.getCompound(ByteBufUtil.readVarInt(buf, 5));
			}
			return new PreparableBannerInfo(state, tileEntityData);
		}

	}

}
