package team.cqr.cqrepoured.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.cqr.cqrepoured.init.CQRBlockEntities;

public class BlockPhylactery extends Block implements SimpleWaterloggedBlock, EntityBlock {

	// TODO make Phylactery active when protecting an entity and spawn particle when active
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	private static final VoxelShape SHAPE = Shapes.box(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);

	public BlockPhylactery() {
		super(Properties.of()
				.sound(SoundType.GLASS)
				.strength(0.5F, 0.5F)
				//.noDrops()
				.noOcclusion()
				.lightLevel(state -> 3)
				.isViewBlocking((bs, bg, p) -> false)
				.isValidSpawn((bs, bg, p, e) -> false)
		);
	}

	@Override
	public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, BlockEntity pTe, ItemStack pStack) {
		super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
		pLevel.explode(null, pPos.getX(), pPos.getY(), pPos.getZ(), 1.5F, ExplosionInteraction.MOB);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}
	
	@Override
	public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
		pProjectile.kill();
		pLevel.removeBlock(pHit.getBlockPos(), false);
		pLevel.explode(null, pHit.getBlockPos().getX(), pHit.getBlockPos().getY(), pHit.getBlockPos().getZ(), 1.5F, ExplosionInteraction.MOB);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return CQRBlockEntities.PHYLACTERY.get().create(pPos, pState);
	}

}
