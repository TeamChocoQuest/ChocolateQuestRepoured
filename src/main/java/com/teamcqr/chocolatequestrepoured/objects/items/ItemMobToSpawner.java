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
import net.minecraft.world.WorldServer;

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
			for (Entity passenger : entity.getPassengers()) {
				passenger.setDead();
			}
			this.spawnAdditions(entity.world, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ);
		}
		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return blockIn.getBlock() != ModBlocks.SPAWNER && blockIn.getBlock() != Blocks.MOB_SPAWNER;
	}

	private void spawnAdditions(World world, double x, double y, double z) {
		if (!world.isRemote) {
			((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			((WorldServer) world).spawnParticle(EnumParticleTypes.LAVA, x, y, z, 8, 0.25D, 0.25D, 0.25D, 0.0D);
			world.playSound(null, x, y, z, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.8F, 0.6F + itemRand.nextFloat() * 0.2F);
		}
	}

}
