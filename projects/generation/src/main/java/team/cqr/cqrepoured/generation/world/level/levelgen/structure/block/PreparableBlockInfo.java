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
import team.cqr.cqrepoured.common.buffer.ByteBufUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.ISerializer;

public class PreparableBlockInfo extends PreparablePosInfo {

	private final BlockState blockState;
	@Nullable
	private final CompoundTag blockEntityTag;

	public PreparableBlockInfo(BlockState blockState, @Nullable CompoundTag blockEntityTag) {
		this.blockState = blockState;
		this.blockEntityTag = blockEntityTag;
	}

	@Override
	protected void prepareNormal(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedBlockState = placement.transform(this.blockState);

		level.setBlockState(transformedPos, transformedBlockState, this.blockEntityTag);
	}

	@Override
	protected void prepareDebug(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedBlockState = placement.transform(this.blockState);

		level.setBlockState(transformedPos, transformedBlockState, this.blockEntityTag);
	}

	public BlockState getBlockState() {
		return this.blockState;
	}

	@Nullable
	public CompoundTag getBlockEntityTag() {
		return this.blockEntityTag;
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
			int data = (palette.idFor(preparable.blockState) << 1) | (preparable.blockEntityTag != null ? 1 : 0);
			ByteBufUtil.writeVarInt(buf, data, 5);
			if (preparable.blockEntityTag != null) {
				ByteBufUtil.writeVarInt(buf, nbtList.size(), 5);
				nbtList.add(preparable.blockEntityTag);
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
