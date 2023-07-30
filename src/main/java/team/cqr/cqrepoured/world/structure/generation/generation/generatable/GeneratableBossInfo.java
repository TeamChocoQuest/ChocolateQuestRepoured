package team.cqr.cqrepoured.world.structure.generation.generation.generatable;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
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
		boolean flag = BlockPlacingHelper.setBlockState(world, chunk, blockStorage, pos, Blocks.AIR.getDefaultState(), null, 16, dungeon);
		world.spawnEntity(this.entity);
		return flag;
	}

	public Entity getEntity() {
		return this.entity;
	}

}
