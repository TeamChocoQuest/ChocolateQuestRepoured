package team.cqr.cqrepoured.blocks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.cqr.cqrepoured.blocks.block.entity.BossBlockEntity;
import team.cqr.cqrepoured.blocks.init.CQRBlocksBlockEntityTypes;

public class BossBlock extends Block implements EntityBlock {
	
	public BossBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return pBlockEntityType == CQRBlocksBlockEntityTypes.BOSS_BLOCK.get() ? BossBlockEntity::onTick : null;
	}

}
