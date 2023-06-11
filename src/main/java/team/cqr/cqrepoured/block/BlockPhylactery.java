package team.cqr.cqrepoured.block;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.cqr.cqrepoured.init.CQRBlocks;

public class BlockPhylactery extends Block {

	// TODO make Phylactery active when protecting an entity and spawn particle when active
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	private static final VoxelShape SHAPE = VoxelShapes.box(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);

	public BlockPhylactery() {
		super(Properties.of(Material.GLASS)
				.sound(SoundType.GLASS)
				.strength(0.5F, 0.5F)
				.noDrops()
				.noOcclusion()
				.lightLevel(state -> 3)
				.isViewBlocking(CQRBlocks::never)
				.isValidSpawn(CQRBlocks::never)
		);
	}

	@Override
	public void playerDestroy(World pLevel, PlayerEntity pPlayer, BlockPos pPos, BlockState pState, TileEntity pTe, ItemStack pStack) {
		super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
		pLevel.explode(null, pPos.getX(), pPos.getY(), pPos.getZ(), 1.5F, Mode.BREAK);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
		return SHAPE;
	}
	
	@Override
	public void onProjectileHit(World pLevel, BlockState pState, BlockRayTraceResult pHit, ProjectileEntity pProjectile) {
		pProjectile.kill();
		pLevel.removeBlock(pHit.getBlockPos(), false);
		pLevel.explode(null, pHit.getBlockPos().getX(), pHit.getBlockPos().getY(), pHit.getBlockPos().getZ(), 1.5F, Mode.BREAK);
	}

}
