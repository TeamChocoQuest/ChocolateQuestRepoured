package com.teamcqr.chocolatequestrepoured.objects.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockUnlitTorch extends BlockTorch {

	public BlockUnlitTorch() {
		this.setTickRandomly(false);
		this.setLightLevel(0.0F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		Block block = Block.getBlockFromItem(stack.getItem());

		if (stack.getItem() instanceof ItemFlintAndSteel || block.getLightValue(block.getDefaultState(), worldIn, pos) > 0.0F) {
			if (!worldIn.isRemote) {
				this.lightUp(worldIn, pos, state.getValue(FACING));
			}
			return true;
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote && entityIn.isBurning()) {
			this.lightUp(worldIn, pos, state.getValue(FACING));
		}
	}

	private void lightUp(World world, BlockPos pos, EnumFacing facing) {
		if (!world.isRemote) {
			world.setBlockState(pos, Blocks.TORCH.getDefaultState().withProperty(FACING, facing), 3);
			world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 3.0F, 1.0F + world.rand.nextFloat());
			((WorldServer) world).spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, 15, 0.25D, 0.25D, 0.25D, 0.00125);
		}
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {

	}

}
