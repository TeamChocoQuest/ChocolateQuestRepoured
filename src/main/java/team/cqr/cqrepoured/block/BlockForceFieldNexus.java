package team.cqr.cqrepoured.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

public class BlockForceFieldNexus extends Block {

	// TODO make this two blocks

	private static final VoxelShape SHAPE = VoxelShapes.box(0.0D, 0.0D, 0.0D, 1.0D, 1.75D, 1.0D);

	public BlockForceFieldNexus() {
		super(Properties.of(Material.METAL)
				.sound(SoundType.METAL)
				.strength(45.0F, 10.0F)
				.noDrops()
				.noOcclusion()
				.lightLevel(state -> 15));
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
