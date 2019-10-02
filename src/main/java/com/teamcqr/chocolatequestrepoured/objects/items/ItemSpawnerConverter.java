package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpawnerConverter extends Item {

	public ItemSpawnerConverter() {
		setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			Block block = worldIn.getBlockState(pos).getBlock();
			if(block == null) {
				return EnumActionResult.PASS;
			}
			player.getCooldownTracker().setCooldown(this, 10);
			if(block == Blocks.MOB_SPAWNER || block == ModBlocks.SPAWNER) {
				if(block == Blocks.MOB_SPAWNER) {
					SpawnerFactory.convertVanillaSpawnerToCQSpawner(worldIn, pos);
					return EnumActionResult.SUCCESS;
				}
				if(block == ModBlocks.SPAWNER) {
					SpawnerFactory.convertCQSpawnerToVanillaSpawner(worldIn, pos, null);
					return EnumActionResult.SUCCESS;
				}
			} 
		}
		return EnumActionResult.PASS;
	}

}
