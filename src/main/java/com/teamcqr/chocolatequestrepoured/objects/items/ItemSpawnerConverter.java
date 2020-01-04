package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpawnerConverter extends Item {

	public ItemSpawnerConverter() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
		Block block = world.getBlockState(pos).getBlock();
		return block != ModBlocks.SPAWNER && block != Blocks.MOB_SPAWNER;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (player.isCreative()) {
			Block block = world.getBlockState(pos).getBlock();
			if (block == ModBlocks.SPAWNER || block == Blocks.MOB_SPAWNER) {
				if (!world.isRemote) {
					if (block == ModBlocks.SPAWNER) {
						CQRMain.logger.info("Converting: CQR -> Vanilla");
						SpawnerFactory.convertCQSpawnerToVanillaSpawner(world, pos, null);
					}
					if (block == Blocks.MOB_SPAWNER) {
						CQRMain.logger.info("Converting: Vanilla -> CQR");
						SpawnerFactory.convertVanillaSpawnerToCQSpawner(world, pos);
					}
					player.getCooldownTracker().setCooldown(this, 10);
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}

}
