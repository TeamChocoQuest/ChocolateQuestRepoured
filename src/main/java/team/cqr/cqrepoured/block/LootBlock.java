package team.cqr.cqrepoured.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class LootBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public LootBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.UP)
				.setValue(WATERLOGGED, false)
		);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		// TODO Auto-generated method stub
		return null;
	}

}
