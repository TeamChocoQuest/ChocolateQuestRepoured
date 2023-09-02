package team.cqr.cqrepoured.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.cqr.cqrepoured.init.CQRBlockEntities;

public class BlockForceFieldNexus extends Block implements SimpleWaterloggedBlock, EntityBlock {

	// TODO make this two blocks => Why again?

	private static final VoxelShape SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 1.75D, 1.0D);

	public BlockForceFieldNexus() {
		super(Properties.of()
				.sound(SoundType.METAL)
				.strength(45.0F, 10.0F)
				.noOcclusion()
			);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return CQRBlockEntities.FORCE_FIELD_NEXUS.get().create(pPos, pState);
	}

}
