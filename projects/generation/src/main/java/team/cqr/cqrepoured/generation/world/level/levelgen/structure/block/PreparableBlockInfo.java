package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class PreparableBlockInfo extends PreparablePosInfo {

	private final BlockState state;
	@Nullable
	private final CompoundTag tileEntityData;

	public PreparableBlockInfo(BlockState state, @Nullable CompoundTag tileEntityData) {
		this.state = state;
		this.tileEntityData = tileEntityData;
	}

	@Override
	protected void prepareNormal(CQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedState = placement.transform(this.state);

		level.setBlockState(transformedPos, transformedState, blockEntity -> this.blockEntityCallback(transformedPos, transformedState, blockEntity, placement));
	}

	protected void blockEntityCallback(BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, DungeonPlacement placement) {
		if (blockEntity != null && this.tileEntityData != null) {
			this.tileEntityData.putInt("x", pos.getX());
			this.tileEntityData.putInt("y", pos.getY());
			this.tileEntityData.putInt("z", pos.getZ());
			blockEntity.load(this.tileEntityData);
			placement.transform(blockEntity);
			this.tileEntityData.remove("x");
			this.tileEntityData.remove("y");
			this.tileEntityData.remove("z");
		}
	}

	@Override
	protected void prepareDebug(CQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedState = placement.transform(this.state);

		level.setBlockState(transformedPos, transformedState, blockEntity -> this.blockEntityCallbackDebug(transformedPos, transformedState, blockEntity, placement));
	}

	protected void blockEntityCallbackDebug(BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, DungeonPlacement placement) {
		if (blockEntity != null && this.tileEntityData != null) {
			this.tileEntityData.putInt("x", pos.getX());
			this.tileEntityData.putInt("y", pos.getY());
			this.tileEntityData.putInt("z", pos.getZ());
			blockEntity.load(this.tileEntityData);
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
	public CompoundTag getTileEntityData() {
		return this.tileEntityData;
	}

	public static class Factory implements IFactory<BlockEntity> {

		@Override
		public PreparablePosInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<BlockEntity> blockEntityLazy) {
			return new PreparableBlockInfo(state, blockEntityLazy.map(IFactory::writeTileEntityToNBT).orElse(null));
		}

	}

	public static class Serializer implements ISerializer<PreparableBlockInfo> {

		@Override
		public void write(PreparableBlockInfo preparable, ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			int data = (palette.idFor(preparable.state) << 1) | (preparable.tileEntityData != null ? 1 : 0);
			ByteBufUtil.writeVarInt(buf, data, 5);
			if (preparable.tileEntityData != null) {
				ByteBufUtil.writeVarInt(buf, nbtList.size(), 5);
				nbtList.add(preparable.tileEntityData);
			}
		}

		@Override
		public PreparableBlockInfo read(ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			int data = ByteBufUtil.readVarInt(buf, 5);
			BlockState state = palette.stateFor(data >>> 1);
			CompoundTag tileEntityData = null;
			if ((data & 1) == 1) {
				tileEntityData = nbtList.getCompound(ByteBufUtil.readVarInt(buf, 5));
			}
			return new PreparableBlockInfo(state, tileEntityData);
		}

	}

}
