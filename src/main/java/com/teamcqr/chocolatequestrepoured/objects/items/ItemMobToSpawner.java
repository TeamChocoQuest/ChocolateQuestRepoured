package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.base.ItemBase;
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
		setMaxStackSize(1);
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		IBlockState state = player.getEntityWorld().getBlockState(pos);
		return !canHarvestBlock(state);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		this.spawnAdditions(entity.world, entity);
		SpawnerFactory.placeSpawnerForMob(entity, false, null, player.getEntityWorld(), entity.getPosition());
		entity.setDead();
		return false;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return blockIn.getBlock() != ModBlocks.SPAWNER && blockIn.getBlock() != Blocks.MOB_SPAWNER;
	}
	
	private void spawnAdditions(World worldIn, Entity entity)
	{
		for(int x = 0; x < 5; x++)
    	{
    		worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entity.posX + itemRand.nextFloat() - 0.5D, entity.posY + 0.5D + itemRand.nextFloat(), entity.posZ + itemRand.nextFloat() - 0.5D, 0, 0, 0);
    	}
		for(int x = 0; x < 10; x++) {
			worldIn.spawnParticle(EnumParticleTypes.LAVA, entity.posX + itemRand.nextFloat() - 0.5D, entity.posY + 0.5D + itemRand.nextFloat(), entity.posZ + itemRand.nextFloat() - 0.5D, 0, 0, 0);
		}
		
		worldIn.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.MASTER, 4.0F, (1.0F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F) * 0.7F, false);
	}
	
	

}
