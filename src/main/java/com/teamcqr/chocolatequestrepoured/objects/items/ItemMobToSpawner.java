package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.base.ItemBase;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMobToSpawner extends ItemBase {
	
	public ItemMobToSpawner(String name) {
		super(name);
		setMaxStackSize(1);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		ItemStack bottleStack = new ItemStack(ModItems.SOUL_BOTTLE);
		bottleStack.setCount(1);
		
		NBTTagCompound mobToSpawnerItem = new NBTTagCompound();
		
		NBTTagCompound entityTag = new NBTTagCompound();
		
		if(entity.writeToNBTOptional(entityTag))
		{
			mobToSpawnerItem.setTag("EntityIn", entityTag);
			
			this.spawnAdditions(entity.world, entity);
			
			bottleStack.setTagCompound(mobToSpawnerItem);
			
			World world = entity.getEntityWorld();
			
			BlockPos pos = entity.getPosition();
			
			entity.setDead();
			
			world.setBlockState(pos, ModBlocks.SPAWNER.getDefaultState());
			TileEntity spawnerTile = world.getTileEntity(pos);
			if(spawnerTile != null) {
				TileEntitySpawner spawner = (TileEntitySpawner)spawnerTile;
				
				for(int i = 0; i < spawner.inventory.getSlots(); i++) {
					ItemStack currStack = spawner.inventory.getStackInSlot(i);
					if(currStack == null || currStack.getItem() == Items.AIR) {
						spawner.inventory.insertItem(i, bottleStack, false);
						spawner.markDirty();
						player.getCooldownTracker().setCooldown(this, 5);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void spawnAdditions(World worldIn, Entity entity)
	{
		for(int x = 0; x < 5; x++)
    	{
    		worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, entity.posX + itemRand.nextFloat() - 0.5D, entity.posY + 0.5D + itemRand.nextFloat(), entity.posZ + itemRand.nextFloat() - 0.5D, 0, 0, 0);
    		worldIn.spawnParticle(EnumParticleTypes.FLAME, entity.posX + itemRand.nextFloat() - 0.5D, entity.posY + 0.5D + itemRand.nextFloat(), entity.posZ + itemRand.nextFloat() - 0.5D, 0, 0, 0);
    	}
		
		worldIn.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.MASTER, 4.0F, (1.0F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F) * 0.7F, false);
	}
	
	

}
