package team.cqr.cqrepoured.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRBlocks;

public class BlockNull extends GlassBlock {

	public static final BooleanProperty PASSABLE = BooleanProperty.create("passable");

	public BlockNull() {
		super(Properties.of(Material.GLASS)
				.sound(SoundType.GLASS)
				.strength(-1.0F, 3600000.0F)
				.noDrops()
				.noOcclusion()
				.isValidSpawn(CQRBlocks::never)
				.isRedstoneConductor(CQRBlocks::never)
				.isSuffocating(CQRBlocks::never)
				.isViewBlocking(CQRBlocks::never)
		);
		this.registerDefaultState(this.stateDefinition.any().setValue(PASSABLE, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		pBuilder.add(PASSABLE);
	}

	@Override
	public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
		if (pPlayer.isCreative() && pPlayer.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
			pLevel.setBlock(pPos, pState.setValue(PASSABLE, !pState.getValue(PASSABLE)), 3);
			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.PASS;
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
		return pState.getValue(PASSABLE) ? VoxelShapes.empty() : VoxelShapes.block();
	}

}
