package team.cqr.cqrepoured.block;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockExporterChest extends ChestBlock {

	public BlockExporterChest() {
		super(Properties.of(Material.WOOD)
				.sound(SoundType.WOOD)
				.strength(-1.0F, 3600000.0F)
				.noDrops()
				.noOcclusion(),
				() -> TileEntityType.CHEST);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

	@Override
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult result) {
		return ActionResultType.PASS;
	}

	@Override
	public boolean is(Block block) {
		return block.getClass() == ChestBlock.class || block instanceof BlockExporterChest;
	}

}
