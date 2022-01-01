package team.cqr.cqrepoured.block;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.*;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;

public class BlockUnlitTorch extends TorchBlock {

	public BlockUnlitTorch() {
		this.setTickRandomly(false);
		this.setLightLevel(0.0F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		Block block = Block.getBlockFromItem(stack.getItem());

		if (stack.getItem() instanceof FlintAndSteelItem || block.getLightValue(block.getDefaultState(), worldIn, pos) > 0.0F) {
			if (!worldIn.isRemote) {
				lightUp(worldIn, pos, state.getValue(FACING));
			}
			return true;
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, BlockState state, Entity entityIn) {
		if (!worldIn.isRemote && entityIn.isBurning()) {
			lightUp(worldIn, pos, state.getValue(FACING));
		}
	}

	public static void lightUp(World world, BlockPos pos, Direction facing) {
		if (!world.isRemote) {
			world.setBlockState(pos, Blocks.TORCH.getDefaultState().withProperty(FACING, facing), 3);
			world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F + world.rand.nextFloat());
			((ServerWorld) world).spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, 15, 0.25D, 0.25D, 0.25D, 0.00125);
		}
	}

	@Override
	public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

	}

}
