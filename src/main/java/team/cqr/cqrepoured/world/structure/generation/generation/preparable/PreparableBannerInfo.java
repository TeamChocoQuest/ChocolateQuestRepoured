package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
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

public class PreparableBannerInfo extends PreparableBlockInfo {

	public PreparableBannerInfo(BlockPos pos, IBlockState state, @Nullable NBTTagCompound tileEntityData) {
		this(pos.getX(), pos.getY(), pos.getZ(), state, tileEntityData);
	}

	public PreparableBannerInfo(int x, int y, int z, IBlockState state, @Nullable NBTTagCompound tileEntityData) {
		super(x, y, z, state, tileEntityData);
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos, IBlockState state, @Nullable TileEntity tileEntity) {
		if (placement.getInhabitant().getBanner() != null && tileEntity instanceof TileEntityBanner) {
			((TileEntityBanner) tileEntity).setItemValues(placement.getInhabitant().getBanner().getBanner(), false);
		}

		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	public static class Factory implements IFactory<TileEntityBanner> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, IBlockState state, Supplier<TileEntityBanner> tileEntitySupplier) {
			TileEntityBanner tileEntity = tileEntitySupplier.get();
			if (BannerHelper.isCQBanner(tileEntity)) {
				return new PreparableBannerInfo(x, y, z, state, IFactory.writeTileEntityToNBT(tileEntity));
			}
			return new PreparableBlockInfo(x, y, z, state, IFactory.writeTileEntityToNBT(tileEntitySupplier.get()));
		}

	}

	public static class Serializer implements ISerializer<PreparableBannerInfo> {

		@Override
		public void write(PreparableBannerInfo preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			int data = (palette.idFor(preparable.getState()) << 1) | (preparable.getTileEntityData() != null ? 1 : 0);
			ByteBufUtils.writeVarInt(buf, data, 5);
			if (preparable.getTileEntityData() != null) {
				ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
				nbtList.appendTag(preparable.getTileEntityData());
			}
		}

		@Override
		public PreparableBannerInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			int data = ByteBufUtils.readVarInt(buf, 5);
			IBlockState state = palette.stateFor(data >>> 1);
			NBTTagCompound tileEntityData = null;
			if ((data & 1) == 1) {
				tileEntityData = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
			}
			return new PreparableBannerInfo(x, y, z, state, tileEntityData);
		}

		@Override
		@Deprecated
		public PreparableBannerInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList) {
			int[] intArray = nbtIntArray.getIntArray();
			IBlockState state = palette.stateFor(intArray[1]);
			NBTTagCompound tileEntityData = null;
			if (intArray.length > 2) {
				tileEntityData = nbtList.getCompoundTagAt(intArray[2]);
			}
			return new PreparableBannerInfo(x, y, z, state, tileEntityData);
		}

	}

}
