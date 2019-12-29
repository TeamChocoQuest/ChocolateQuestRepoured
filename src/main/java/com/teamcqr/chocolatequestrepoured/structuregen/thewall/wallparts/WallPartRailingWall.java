package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSpectre;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.BlockStoneSlab;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub:
 * https://github.com/DerToaster98
 */
public class WallPartRailingWall implements IWallPart {

	public WallPartRailingWall() {
		// Does not really need a constructor too....
	}

	@Override
	public int getTopY() {
		return Reference.CONFIG_HELPER_INSTANCE.getWallTopY() - 12;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk) {
		int startX = chunkX *16;
		int startZ = chunkZ *16;
		
		int[] zValues = new int[] {2,3,12,13};
		
		List<BlockPos> railingBlocks = new ArrayList<BlockPos>();
		
		//Spawner
		BlockPos spawnerPos = new BlockPos(startX + 4, getTopY() +12 +1 -7, startZ + 6);
		placeSpawner(spawnerPos, world);
		
		for(int y = 0; y <= 7; y++) {
			for(int z : zValues) {
				for(int x = 0; x < 8; x++) {
					if(isBiggerPart(x)) {
						if(y < 3 && z == (z > 3? 12:3)) {
							railingBlocks.add(new BlockPos(startX + x*2, getTopY() +y, startZ +z));
							railingBlocks.add(new BlockPos(startX + x*2 +1, getTopY() +y, startZ +z));
						} else if(y >= 3) {							
							railingBlocks.add(new BlockPos(startX + x*2, getTopY() +y, startZ +z));
							railingBlocks.add(new BlockPos(startX + x*2 +1, getTopY() +y, startZ +z));
						}
					} else if(y >= 4 && z == (z > 3? 12:3) && y <= 6) {
						railingBlocks.add(new BlockPos(startX + x*2, getTopY() +y, startZ +z));
						railingBlocks.add(new BlockPos(startX + x*2 +1, getTopY() +y, startZ +z));
					}
				}
			}
		}
		
		
		
		if(!railingBlocks.isEmpty()) {
			/*for(BlockPos pos : railingBlocks) {
				world.setBlockState(pos, Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE));
			}*/
			final List<BlockPos> posL = new ArrayList<BlockPos>(railingBlocks);
			railingBlocks.clear();
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
				
				@Override
				public void run() {
					
					for(BlockPos p : posL) {
						world.setBlockState(p, Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE).withProperty(BlockStoneSlab.SEAMLESS, true));
					}
					
				}
			});
		}
	}
	
	private void placeSpawner(BlockPos spawnerPos, World world) {
		AbstractEntityCQR spawnerEnt = new EntityCQRSpectre(world);
		switch(spawnerEnt.getRNG().nextInt(5)) {
		case 0:
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.SHIELD_SPECTER, 1));
			break;
		case 1:
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE, 1));
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.SHIELD_SPECTER, 1));
			break;
		case 2:
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW, 1));
			break;
		case 3:
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW, 1));
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.SHIELD_SPECTER, 1));
			break;
		case 4:
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			break;
		}
		spawnerEnt.setHealingPotions(3);
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

		if (!nbttagcompound.hasKey("display", 10)) {
			nbttagcompound.setTag("display", nbttagcompound1);
		}

		nbttagcompound1.setInteger("color", 000000);
		
		ItemStack helmet = new ItemStack(ModItems.HELMET_IRON_DYABLE, 1/*, 0, nbttagcompound*/);
		//((ItemArmorDyable)ModItems.HELMET_DYABLE_IRON).setColor(helmet, 000000);
		spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.HEAD, helmet);
		
		ItemStack chest = new ItemStack(ModItems.CHESTPLATE_IRON_DYABLE, 1/*, 0, nbttagcompound*/);
		//((ItemArmorDyable)ModItems.CHESTPLATE_DYABLE_IRON).setColor(chest, 000000);
		spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.CHEST, chest);
		
		ItemStack legs = new ItemStack(ModItems.LEGGINGS_IRON_DYABLE, 1/*, 0, nbttagcompound*/);
		//((ItemArmorDyable)ModItems.LEGGINGS_DYABLE_IRON).setColor(legs, 000000);
		spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.LEGS, legs);
		
		ItemStack boots = new ItemStack(ModItems.BOOTS_IRON_DYABLE, 1/*, 0, nbttagcompound*/);
		//((ItemArmorDyable)ModItems.BOOTS_DYABLE_IRON).setColor(boots, 000000);
		spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.FEET, boots);
		
		SpawnerFactory.placeSpawner(new Entity[] {spawnerEnt}, false, null, world, spawnerPos);
	}

	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		if(xAsChunkRelativeCoord %2 == 0 || xAsChunkRelativeCoord == 0) {
			return true;
		}
		return false;
	}

}
