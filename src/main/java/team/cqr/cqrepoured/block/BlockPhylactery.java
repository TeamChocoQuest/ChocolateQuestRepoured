package team.cqr.cqrepoured.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.entity.boss.EntityCQRLich;

public class BlockPhylactery extends Block {

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
	private static final VoxelShape VOXEL_SHAPE = VoxelShapes.create(BOUNDING_BOX);

	public BlockPhylactery() {
		super(Properties
				.of(Material.GLASS)
				.strength(0.5F, 0.5F)
				.sound(SoundType.GLASS)
				.randomTicks()
				.noDrops()
				.lightLevel((state) -> 3)
		);
	}

	@Override
	public void playerDestroy(World pLevel, PlayerEntity pPlayer, BlockPos pPos, BlockState pState, TileEntity pTe, ItemStack pStack) {
		super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
		pLevel.explode(null, pPos.getX(), pPos.getY(), pPos.getZ(), 1.5F, Mode.BREAK);
	}
	
	@Override
	public VoxelShape getVisualShape(BlockState pState, IBlockReader pReader, BlockPos pPos, ISelectionContext pContext) {
		return VoxelShapes.empty();
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState pState, IBlockReader pReader, BlockPos pPos) {
		return true;
	}
	
	@Override
	public float getShadeBrightness(BlockState pState, IBlockReader pLevel, BlockPos pPos) {
		return 1.0F;
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
		return VOXEL_SHAPE;
	}
	
	@Override
	public void tick(BlockState pState, ServerWorld worldIn, BlockPos pos, Random pRand) {
		super.tick(pState, worldIn, pos, pRand);
		
		AxisAlignedBB aabb = new AxisAlignedBB(pos.offset(3, 2, 3), pos.offset(-3, -2, -3));
		List<EntityCQRLich> lichesInRange = worldIn.getEntitiesOfClass(EntityCQRLich.class, aabb);
		if (!lichesInRange.isEmpty()) {
			int i = 0;
			while (i < lichesInRange.size()) {
				EntityCQRLich lich = lichesInRange.get(i);
				if (lich != null && !lich.isDeadOrDying()) {
					if (!lich.hasPhylactery()) {
						lich.setCurrentPhylacteryBlock(pos);
						i = lichesInRange.size();
					} else {
						i++;
					}
				}
			}
		}
	}
	
	@Override
	public void onProjectileHit(World pLevel, BlockState pState, BlockRayTraceResult pHit, ProjectileEntity pProjectile) {
		this.destroy(pLevel, pHit.getBlockPos(), pState);
		pLevel.explode(null, pHit.getBlockPos().getX(), pHit.getBlockPos().getY(), pHit.getBlockPos().getZ(), 1.5F, Mode.BREAK);
	}

}
