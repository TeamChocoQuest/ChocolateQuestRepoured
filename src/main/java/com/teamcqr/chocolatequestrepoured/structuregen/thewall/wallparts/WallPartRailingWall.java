package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorDyable;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart.ExtendedBlockState;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
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
		return CQRConfig.wall.topY - 12;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, List<List<? extends IStructure>> lists) {
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;

		int[] zValues = new int[] { 2, 3, 12, 13 };

		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		ExtendedBlockStatePart.ExtendedBlockState stateBlock = new ExtendedBlockState(Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE).withProperty(BlockStoneSlab.SEAMLESS, true), null);

		// Spawner
		BlockPos spawnerPos = new BlockPos(startX + 4, this.getTopY() + 12 + 1 - 7, startZ + 7);
		this.placeSpawner(spawnerPos, world, stateMap);

		for (int y = 0; y <= 7; y++) {
			for (int z : zValues) {
				for (int x = 0; x < 8; x++) {
					if (this.isBiggerPart(x)) {
						if (y < 3 && z == (z > 3 ? 12 : 3)) {
							stateMap.put(new BlockPos(startX + x * 2, this.getTopY() + y, startZ + z), stateBlock);
							stateMap.put(new BlockPos(startX + x * 2 + 1, this.getTopY() + y, startZ + z), stateBlock);
						} else if (y >= 3) {
							stateMap.put(new BlockPos(startX + x * 2, this.getTopY() + y, startZ + z), stateBlock);
							stateMap.put(new BlockPos(startX + x * 2 + 1, this.getTopY() + y, startZ + z), stateBlock);
						}
					} else if (y >= 4 && z == (z > 3 ? 12 : 3) && y <= 6) {
						stateMap.put(new BlockPos(startX + x * 2, this.getTopY() + y, startZ + z), stateBlock);
						stateMap.put(new BlockPos(startX + x * 2 + 1, this.getTopY() + y, startZ + z), stateBlock);
					}
				}
			}
		}

		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

	private void placeSpawner(BlockPos spawnerPos, World world, Map<BlockPos, ExtendedBlockState> stateMap) {
		Entity spawnerEnt = EntityList.createEntityByIDFromName(new ResourceLocation(CQRConfig.wall.mob), world);
		if (spawnerEnt instanceof EntityLiving) {
			switch (((EntityLiving) spawnerEnt).getRNG().nextInt(5)) {
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
			if (spawnerEnt instanceof AbstractEntityCQR) {
				((AbstractEntityCQR) spawnerEnt).setHealingPotions(3);
			}
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (!nbttagcompound.hasKey("display", 10)) {
				nbttagcompound.setTag("display", nbttagcompound1);
			}

			nbttagcompound1.setInteger("color", 000000);

			ItemStack helmet = new ItemStack(ModItems.HELMET_IRON_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) ModItems.HELMET_IRON_DYABLE).setColor(helmet, 000000);
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.HEAD, helmet);

			ItemStack chest = new ItemStack(ModItems.CHESTPLATE_IRON_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) ModItems.CHESTPLATE_IRON_DYABLE).setColor(chest, 000000);
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.CHEST, chest);

			ItemStack legs = new ItemStack(ModItems.LEGGINGS_IRON_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) ModItems.LEGGINGS_IRON_DYABLE).setColor(legs, 000000);
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.LEGS, legs);

			ItemStack boots = new ItemStack(ModItems.BOOTS_IRON_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) ModItems.BOOTS_IRON_DYABLE).setColor(boots, 000000);
			spawnerEnt.setItemStackToSlot(EntityEquipmentSlot.FEET, boots);

			IBlockState state2 = ModBlocks.SPAWNER.getDefaultState();
			TileEntitySpawner tileSpawner = (TileEntitySpawner) ModBlocks.SPAWNER.createTileEntity(world, state2);
			tileSpawner.inventory.setStackInSlot(0, SpawnerFactory.getSoulBottleItemStackForEntity(spawnerEnt));

			stateMap.put(spawnerPos, new ExtendedBlockStatePart.ExtendedBlockState(state2, tileSpawner.writeToNBT(new NBTTagCompound())));
		}
	}

	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		if (xAsChunkRelativeCoord % 2 == 0 || xAsChunkRelativeCoord == 0) {
			return true;
		}
		return false;
	}

}
