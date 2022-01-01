package team.cqr.cqrepoured.world.structure.generation.generation.generatable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

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
		BlockState state = blockStorage.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
		int light = state.getLightValue(world, pos);
		if (light > 0) {
			dungeon.markRemovedLight(pos.getX(), pos.getY(), pos.getZ(), light);
		}
		boolean flag = BlockPlacingHelper.setBlockState(world, chunk, blockStorage, pos, Blocks.AIR.getDefaultState(), null, 16);
		world.spawnEntity(this.entity);
		return flag;
	}

	public Entity getEntity() {
		return this.entity;
	}

}
