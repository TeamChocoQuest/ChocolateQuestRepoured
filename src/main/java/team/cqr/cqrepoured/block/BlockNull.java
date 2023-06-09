package team.cqr.cqrepoured.block;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
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
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pPlayer.isCreative() && pPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
			pLevel.setBlock(pPos, pState.setValue(PASSABLE, !pState.getValue(PASSABLE)), 3);
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return pState.getValue(PASSABLE) ? Shapes.empty() : Shapes.block();
	}

}
