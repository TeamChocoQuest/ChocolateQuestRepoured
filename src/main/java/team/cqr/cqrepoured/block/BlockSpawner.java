package team.cqr.cqrepoured.block;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.GuiHandler;

public class BlockSpawner extends ContainerBlock {

	public BlockSpawner() {
		super(Material.IRON);

		this.setSoundType(SoundType.METAL);
		this.setBlockUnbreakable();
		this.setResistance(Float.MAX_VALUE);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && playerIn.isCreative()) {
			playerIn.openGui(CQRMain.INSTANCE, GuiHandler.SPAWNER_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, BlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntitySpawner) {
			IItemHandler itemHandler = ((TileEntitySpawner) tileEntity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

			for (int i = 0; i < itemHandler.getSlots(); i++) {
				ItemStack stack = itemHandler.getStackInSlot(i);

				if (!stack.isEmpty()) {
					ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
					world.spawnEntity(item);
				}
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Deprecated
	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Deprecated
	@Override
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	@Override
	public Item getItemDropped(BlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySpawner();
	}

	@Deprecated
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Deprecated
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}

	@Dist(OnlyIn.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Deprecated
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
		return BlockFaceShape.UNDEFINED;
	}

}
