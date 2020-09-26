package com.teamcqr.chocolatequestrepoured.objects.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/*
 * Copied from vanilla FrostedIce block
 */
public class BlockTemporaryWeb extends BlockWeb {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

	public BlockTemporaryWeb() {
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
		this.setTickRandomly(true);
	}

	@Override
	public int getMetaFromState(IBlockState p_176201_1_) {
		return (Integer) p_176201_1_.getValue(AGE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AGE, MathHelper.clamp(meta, 0, 3));
	}

	@Override
	public boolean requiresUpdates() {
		return true;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState blockstate, Random random) {
		if ((random.nextInt(3) == 0 || this.countNeighbors(world, pos) < 4)) {
			this.slightlyDecay(world, pos, blockstate, random, true);
		} else {
			world.scheduleUpdate(pos, this, MathHelper.getInt(random, 10, 30));
		}

	}

	@Override
	public void neighborChanged(IBlockState blockstate, World world, BlockPos pos, Block block, BlockPos p_189540_5_) {
		if (block == this) {
			int lvt_6_1_ = this.countNeighbors(world, pos);
			if (lvt_6_1_ < 2) {
				world.setBlockToAir(pos);
			}
		}

	}

	private int countNeighbors(World world, BlockPos pos) {
		int lvt_3_1_ = 0;
		EnumFacing[] var4 = EnumFacing.values();
		int var5 = var4.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			EnumFacing lvt_7_1_ = var4[var6];
			if (world.getBlockState(pos.offset(lvt_7_1_)).getBlock() == this) {
				++lvt_3_1_;
				if (lvt_3_1_ >= 4) {
					return lvt_3_1_;
				}
			}
		}

		return lvt_3_1_;
	}

	protected void slightlyDecay(World world, BlockPos pos, IBlockState blockstate, Random random, boolean updateNeighbors) {
		int age = (Integer) blockstate.getValue(AGE);
		if (age < 3) {
			world.setBlockState(pos, blockstate.withProperty(AGE, age + 1), 2);
			world.scheduleUpdate(pos, this, MathHelper.getInt(random, 20, 40));
		} else {
			world.setBlockToAir(pos);
			if (updateNeighbors) {
				EnumFacing[] var7 = EnumFacing.values();
				int var8 = var7.length;

				for (int var9 = 0; var9 < var8; ++var9) {
					EnumFacing lvt_10_1_ = var7[var9];
					BlockPos lvt_11_1_ = pos.offset(lvt_10_1_);
					IBlockState lvt_12_1_ = world.getBlockState(lvt_11_1_);
					if (lvt_12_1_.getBlock() == this) {
						this.slightlyDecay(world, lvt_11_1_, lvt_12_1_, random, false);
					}
				}
			}
		}

	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState blockstate) {
		return ItemStack.EMPTY;
	}

}
