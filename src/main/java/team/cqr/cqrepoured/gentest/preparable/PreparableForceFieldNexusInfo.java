package team.cqr.cqrepoured.gentest.preparable;

import io.netty.buffer.ByteBuf;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.gentest.DungeonPlacement;
import team.cqr.cqrepoured.gentest.generatable.GeneratableBlockInfo;
import team.cqr.cqrepoured.gentest.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.gentest.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;

public class PreparableForceFieldNexusInfo extends PreparablePosInfo {

	public PreparableForceFieldNexusInfo(BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	public PreparableForceFieldNexusInfo(int x, int y, int z) {
		super(x, y, z);
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		if (placement.getProtectedRegionBuilder() == null) {
			return new GeneratableBlockInfo(pos, Blocks.AIR.getDefaultState(), null);
		}

		placement.getProtectedRegionBuilder().addBlock(pos);
		return new GeneratableBlockInfo(pos, CQRBlocks.FORCE_FIELD_NEXUS.getDefaultState(), null);
	}

	@Override
	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos) {
		return new GeneratableBlockInfo(pos, CQRBlocks.FORCE_FIELD_NEXUS.getDefaultState(), null);
	}

	public static class Serializer implements ISerializer<PreparableForceFieldNexusInfo> {

		@Override
		public void write(PreparableForceFieldNexusInfo preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			// nothing to write
		}

		@Override
		public PreparableForceFieldNexusInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			return new PreparableForceFieldNexusInfo(x, y, z);
		}

		@Override
		@Deprecated
		public PreparableForceFieldNexusInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList) {
			return new PreparableForceFieldNexusInfo(x, y, z);
		}

	}

}
