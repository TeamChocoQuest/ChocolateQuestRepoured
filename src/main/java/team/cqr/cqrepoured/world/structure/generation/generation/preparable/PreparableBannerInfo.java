package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.block.banner.BannerHelper;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PreparableBannerInfo extends PreparableBlockInfo {

	public PreparableBannerInfo(BlockPos pos, BlockState state, @Nullable CompoundNBT tileEntityData) {
		this(pos.getX(), pos.getY(), pos.getZ(), state, tileEntityData);
	}

	public PreparableBannerInfo(int x, int y, int z, BlockState state, @Nullable CompoundNBT tileEntityData) {
		super(x, y, z, state, tileEntityData);
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity) {
		if (placement.getInhabitant().getBanner() != null && tileEntity instanceof BannerTileEntity) {
			((BannerTileEntity) tileEntity).setItemValues(placement.getInhabitant().getBanner().getBanner(), false);
		}

		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	public static class Factory implements IFactory<BannerTileEntity> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, BlockState state, Supplier<BannerTileEntity> tileEntitySupplier) {
			BannerTileEntity tileEntity = tileEntitySupplier.get();
			if (BannerHelper.isCQBanner(tileEntity)) {
				return new PreparableBannerInfo(x, y, z, state, IFactory.writeTileEntityToNBT(tileEntity));
			}
			return new PreparableBlockInfo(x, y, z, state, IFactory.writeTileEntityToNBT(tileEntitySupplier.get()));
		}

	}

	public static class Serializer implements ISerializer<PreparableBannerInfo> {

		@Override
		public void write(PreparableBannerInfo preparable, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			int data = (palette.idFor(preparable.getState()) << 1) | (preparable.getTileEntityData() != null ? 1 : 0);
			ByteBufUtils.writeVarInt(buf, data, 5);
			if (preparable.getTileEntityData() != null) {
				ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
				nbtList.appendTag(preparable.getTileEntityData());
			}
		}

		@Override
		public PreparableBannerInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			int data = ByteBufUtils.readVarInt(buf, 5);
			BlockState state = palette.stateFor(data >>> 1);
			CompoundNBT tileEntityData = null;
			if ((data & 1) == 1) {
				tileEntityData = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
			}
			return new PreparableBannerInfo(x, y, z, state, tileEntityData);
		}

		@Override
		@Deprecated
		public PreparableBannerInfo read(int x, int y, int z, IntArrayNBT nbtIntArray, BlockStatePalette palette, ListNBT nbtList) {
			int[] intArray = nbtIntArray.getIntArray();
			BlockState state = palette.stateFor(intArray[1]);
			CompoundNBT tileEntityData = null;
			if (intArray.length > 2) {
				tileEntityData = nbtList.getCompoundTagAt(intArray[2]);
			}
			return new PreparableBannerInfo(x, y, z, state, tileEntityData);
		}

	}

}
