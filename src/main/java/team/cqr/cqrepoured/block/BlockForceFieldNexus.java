package team.cqr.cqrepoured.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.cqr.cqrepoured.init.CQRBlockEntities;

public class BlockForceFieldNexus extends Block {

	// TODO make this two blocks

	private static final VoxelShape SHAPE = VoxelShapes.box(0.0D, 0.0D, 0.0D, 1.0D, 1.75D, 1.0D);

	public BlockForceFieldNexus() {
		super(Properties.of(Material.METAL)
				.sound(SoundType.METAL)
				.strength(45.0F, 10.0F)
				.noDrops()
				.noOcclusion()
			);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return CQRBlockEntities.FORCE_FIELD_NEXUS.get().create();
	}

	@Override
	public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
		return SHAPE;
	}

}
