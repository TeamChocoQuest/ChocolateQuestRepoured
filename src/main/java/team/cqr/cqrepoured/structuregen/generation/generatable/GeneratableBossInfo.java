package team.cqr.cqrepoured.structuregen.generation.generatable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;
import team.cqr.cqrepoured.structuregen.generation.generatable.GeneratablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;
import team.cqr.cqrepoured.util.BlockPlacingHelper;

public class GeneratableBossInfo extends GeneratablePosInfo {

	private final Entity entity;

	public GeneratableBossInfo(int x, int y, int z, Entity entity) {
		super(x, y, z);
		this.entity = entity;
	}

	public GeneratableBossInfo(BlockPos pos, Entity entity) {
		this(pos.getX(), pos.getY(), pos.getZ(), entity);
	}

	@Override
	protected boolean place(World world, Chunk chunk, ExtendedBlockStorage blockStorage, BlockPos pos, GeneratableDungeon dungeon) {
		IBlockState state = blockStorage.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
		int light = state.getLightValue(world, pos);
		if (light > 0) {
			dungeon.markRemovedLight(pos.getX(), pos.getY(), pos.getZ(), light);
		}
		boolean flag = BlockPlacingHelper.setBlockState(world, chunk, blockStorage, pos, Blocks.AIR.getDefaultState(), null, 16);
		world.spawnEntity(this.entity);
		return flag;
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public boolean hasSpecialShape() {
		return true;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public static class Serializer implements ISerializer<GeneratableBossInfo> {

		@Override
		public void write(GeneratableBossInfo generatable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
			NBTTagCompound compound = new NBTTagCompound();
			generatable.entity.writeToNBTAtomically(compound);
			nbtList.appendTag(compound);
		}

		@Override
		public GeneratableBossInfo read(World world, int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			NBTTagCompound compound = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
			Entity entity = EntityList.createEntityFromNBT(compound, world);
			return new GeneratableBossInfo(x, y, z, entity);
		}

	}

}
