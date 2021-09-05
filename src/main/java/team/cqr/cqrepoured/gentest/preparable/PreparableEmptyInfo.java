package team.cqr.cqrepoured.gentest.preparable;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.gentest.DungeonPlacement;
import team.cqr.cqrepoured.gentest.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.gentest.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;

public class PreparableEmptyInfo extends PreparablePosInfo {

	public PreparableEmptyInfo(BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	public PreparableEmptyInfo(int x, int y, int z) {
		super(x, y, z);
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		return null;
	}

	public static class Serializer implements ISerializer<PreparableEmptyInfo> {

		@Override
		public void write(PreparableEmptyInfo preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			// nothing to write
		}

		@Override
		public PreparableEmptyInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			return new PreparableEmptyInfo(x, y, z);
		}

		@Override
		@Deprecated
		public PreparableEmptyInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList) {
			return new PreparableEmptyInfo(x, y, z);
		}

	}

}
