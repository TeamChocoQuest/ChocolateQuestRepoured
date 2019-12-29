package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMobToSpawner extends Item {

	public ItemMobToSpawner() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		IBlockState state = player.getEntityWorld().getBlockState(pos);
		return !this.canHarvestBlock(state);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!player.world.isRemote) {
			SpawnerFactory.placeSpawner(new Entity[] { entity }, false, null, player.world, new BlockPos(entity));
			entity.setDead();
		} else {
			this.spawnAdditions(entity.world, entity);
		}
		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return blockIn.getBlock() != ModBlocks.SPAWNER && blockIn.getBlock() != Blocks.MOB_SPAWNER;
	}

	private void spawnAdditions(World worldIn, Entity entity) {
		for (int i = 0; i < 5; i++) {
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entity.posX + itemRand.nextFloat() - 0.5D, entity.posY + 0.5D + itemRand.nextFloat(), entity.posZ + itemRand.nextFloat() - 0.5D, 0, 0, 0);
		}
		for (int i = 0; i < 10; i++) {
			worldIn.spawnParticle(EnumParticleTypes.LAVA, entity.posX + itemRand.nextFloat() - 0.5D, entity.posY + 0.5D + itemRand.nextFloat(), entity.posZ + itemRand.nextFloat() - 0.5D, 0, 0, 0);
		}

		worldIn.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.MASTER, 4.0F, 0.6F + itemRand.nextFloat() * 0.2F, false);
	}

}
