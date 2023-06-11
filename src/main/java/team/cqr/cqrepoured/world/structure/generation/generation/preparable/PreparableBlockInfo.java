package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.util.ByteBufUtil;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.ICQRLevel;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableBlockInfo extends PreparablePosInfo {

	private final BlockState state;
	@Nullable
	private final CompoundNBT tileEntityData;

	public PreparableBlockInfo(BlockState state, @Nullable CompoundNBT tileEntityData) {
		this.state = state;
		this.tileEntityData = tileEntityData;
	}

	@Override
	protected void prepareNormal(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedState = placement.transform(this.state);

		level.setBlockState(transformedPos, transformedState, blockEntity -> this.blockEntityCallback(transformedPos, transformedState, blockEntity, placement));
	}

	protected void blockEntityCallback(BlockPos pos, BlockState state, @Nullable TileEntity blockEntity, DungeonPlacement placement) {
		if (blockEntity != null && this.tileEntityData != null) {
			this.tileEntityData.putInt("x", pos.getX());
			this.tileEntityData.putInt("y", pos.getY());
			this.tileEntityData.putInt("z", pos.getZ());
			blockEntity.load(state, this.tileEntityData);
			placement.transform(blockEntity);
			this.tileEntityData.remove("x");
			this.tileEntityData.remove("y");
			this.tileEntityData.remove("z");
		}
	}

	@Override
	protected void prepareDebug(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedState = placement.transform(this.state);

		level.setBlockState(transformedPos, transformedState, blockEntity -> this.blockEntityCallbackDebug(transformedPos, transformedState, blockEntity, placement));
	}

	protected void blockEntityCallbackDebug(BlockPos pos, BlockState state, @Nullable TileEntity blockEntity, DungeonPlacement placement) {
		if (blockEntity != null && this.tileEntityData != null) {
			this.tileEntityData.putInt("x", pos.getX());
			this.tileEntityData.putInt("y", pos.getY());
			this.tileEntityData.putInt("z", pos.getZ());
			blockEntity.load(state, this.tileEntityData);
			placement.transform(blockEntity);
			this.tileEntityData.remove("x");
			this.tileEntityData.remove("y");
			this.tileEntityData.remove("z");
		}
	}

	public BlockState getState() {
		return this.state;
	}

	@Nullable
	public CompoundNBT getTileEntityData() {
		return this.tileEntityData;
	}

	public static class Factory implements IFactory<TileEntity> {

		@Override
		public PreparablePosInfo create(World level, BlockPos pos, BlockState state, LazyOptional<TileEntity> blockEntityLazy) {
			return new PreparableBlockInfo(state, blockEntityLazy.map(IFactory::writeTileEntityToNBT).orElse(null));
		}

	}

	public static class Serializer implements ISerializer<PreparableBlockInfo> {

		@Override
		public void write(PreparableBlockInfo preparable, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			int data = (palette.idFor(preparable.state) << 1) | (preparable.tileEntityData != null ? 1 : 0);
			ByteBufUtil.writeVarInt(buf, data, 5);
			if (preparable.tileEntityData != null) {
				ByteBufUtil.writeVarInt(buf, nbtList.size(), 5);
				nbtList.add(preparable.tileEntityData);
			}
		}

		@Override
		public PreparableBlockInfo read(ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			int data = ByteBufUtil.readVarInt(buf, 5);
			BlockState state = palette.stateFor(data >>> 1);
			CompoundNBT tileEntityData = null;
			if ((data & 1) == 1) {
				tileEntityData = nbtList.getCompound(ByteBufUtil.readVarInt(buf, 5));
			}
			return new PreparableBlockInfo(state, tileEntityData);
		}

	}

}
