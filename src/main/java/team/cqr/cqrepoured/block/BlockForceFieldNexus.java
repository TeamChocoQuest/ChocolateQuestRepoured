package team.cqr.cqrepoured.block;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockRenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import team.cqr.cqrepoured.init.CQRBlockEntities;

public class BlockForceFieldNexus extends Block {

	// TODO make this two blocks

	private static final VoxelShape SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 1.75D, 1.0D);

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
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return CQRBlockEntities.FORCE_FIELD_NEXUS.get().create();
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

}
