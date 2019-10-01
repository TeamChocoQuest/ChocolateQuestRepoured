package com.teamcqr.chocolatequestrepoured.objects.factories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * A static utility class for generating CQR/vanilla spawners and converting them to/from the other
 * @author DerToaster, Meldexun, jdawg3636
 * @version 1 October 2019
 */
public abstract class SpawnerFactory {

	/*
	 * Creation/Modification
	 */

	/**
	 * Places a spawner in the provided world at the provided position. Spawner type (CQR/vanilla) is determined
	 * dynamically based upon the requested capabilities.
	 * @param entities Entities for spawner to spawn
	 * @param multiUseSpawner Determines spawner type. Vanilla = true; CQR = false.
	 * @param spawnerSettings Settings to be applied if generating vanilla spawner (can be null if CQR spawner)
	 * @param world World in which to place spawner
	 * @param pos Position at which to place spawner
	 */
	public static void placeSpawnerForMobs(Entity[] entities, boolean multiUseSpawner, @Nullable MultiUseSpawnerSettings spawnerSettings, World world, BlockPos pos) {
		// Generate Vanilla Spawner
		if(multiUseSpawner == true && spawnerSettings != null) {
			//NYI
			world.setBlockToAir(pos);
			
			world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState());
			
			TileEntity tile0 = world.getTileEntity(pos);
			if(tile0 != null && tile0 instanceof TileEntityMobSpawner) {
				TileEntityMobSpawner spawnerTile = (TileEntityMobSpawner) tile0;
				
				//TODO: Change spawner to conditions and apply the settings
				
				spawnerTile.updateContainingBlockInfo();
				spawnerTile.update();
				spawnerTile.markDirty();
				applySpawnerSettingsToSpawner(spawnerTile, spawnerSettings);
			}
			return;
		}
		// Generate CQR Spawner
		else {
			world.setBlockToAir(pos);
			
			world.setBlockState(pos, ModBlocks.SPAWNER.getDefaultState());
			
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile instanceof TileEntitySpawner) {
				TileEntitySpawner spawner = (TileEntitySpawner)tile;
				
				for(int i = 0; i < entities.length && i < 9; i++) {
					spawner.inventory.setStackInSlot(i, getSoulBottleItemStackForEntity(entities[i]));
				}
				
				spawner.updateContainingBlockInfo();
				spawner.update();
				spawner.markDirty();
			}
		}
	}

	/**
	 * Places a vanilla spawner in the provided world at the provided position using the provided ResourceLocation for
	 * the entity that it should spawn.
	 */
	public static void createSimpleMultiUseSpawner(World world, BlockPos pos, ResourceLocation entityResLoc) {
		world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState());
		TileEntityMobSpawner spawner = (TileEntityMobSpawner)world.getTileEntity(pos);

		spawner.getSpawnerBaseLogic().setEntityId(entityResLoc);

		spawner.updateContainingBlockInfo();
		spawner.update();
	}

	/**
	 * Overloaded variant of normal createSimpleMultiUseSpawner method that accepts an Entity object rather than a
	 * resource location in its parameter
	 */
	public static void createSimpleMultiUseSpawner(World world, BlockPos pos, Entity entity) {
		createSimpleMultiUseSpawner(world, pos, EntityList.getKey(entity));
	}

	/*
	 * CQR/Vanilla Conversion
	 */

	/**
	 * Converts the CQR spawner at the provided World/BlockPos to a vanilla spawner
	 * @param spawnerSettings
	 */
	public static void convertCQSpawnerToVanillaSpawner(World world, BlockPos pos, MultiUseSpawnerSettings spawnerSettings) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileEntitySpawner) {
			TileEntitySpawner spawner = (TileEntitySpawner)tile;
			
			Entity[] entities = new Entity[spawner.inventory.getSlots()];
			Random rand = new Random();
			
			for(int i = 0; i < entities.length; i++) {
				ItemStack stack = spawner.inventory.getStackInSlot(i);
	    		if(!stack.isEmpty() && stack.getCount() >= 1) {
	    			try {
	        			NBTTagCompound tag = stack.getTagCompound();
	            		
	        			NBTTagCompound entityTag = (NBTTagCompound)tag.getTag("EntityIn");
	        			
	        			entities[i] = createEntityFromNBTWithoutSpawningIt(entityTag, world);
	        			
	        			entities[i].setUniqueId(MathHelper.getRandomUUID(rand));
        				
	            		stack.shrink(1);
	    			} catch(NullPointerException npe) {
	    			}
	    			
	    		}
			}
			world.setBlockToAir(pos);
			
			placeSpawnerForMobs(entities, true, spawnerSettings, world, pos);
		}
	}

	/**
	 * Converts the vanilla spawner at the provided World/BlockPos to a CQR spawner
	 */
	public static void convertVanillaSpawnerToCQSpawner(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileEntityMobSpawner) {
			TileEntityMobSpawner spawnerMultiUseTile = (TileEntityMobSpawner) tile;
			
			List<WeightedSpawnerEntity> spawnerEntries = new ArrayList<WeightedSpawnerEntity>();
			spawnerEntries = ObfuscationReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, spawnerMultiUseTile.getSpawnerBaseLogic(), 1 /* It is an array index of getDeclaredFields()*/);
			if(spawnerEntries != null && !spawnerEntries.isEmpty()) {
				Iterator<WeightedSpawnerEntity> iterator = spawnerEntries.iterator();
				
				Entity[] entities = new Entity[9];
				
				int entriesRead = 0;
				while(entriesRead < 9 && iterator.hasNext()) {
					Entity entity = createEntityFromNBTWithoutSpawningIt(iterator.next().getNbt(), world);
					entities[entriesRead] = entity;
					entriesRead++;
				}
				placeSpawnerForMobs(entities, false, null, world, pos);
			}
		}
	}

	/**
	 * Applies the provided settings to the provided vanilla spawner
	 */
	public static void applySpawnerSettingsToSpawner(TileEntityMobSpawner spawner, MultiUseSpawnerSettings settings) {
		MobSpawnerBaseLogic spawnerLogic = spawner.getSpawnerBaseLogic();

		//TODO Exchange values

		spawner.markDirty();
	}

	/*
	 * Miscellaneous
	 */

	/**
	 * Converts provided NBT data into an entity in the provided world without actually spawning it
	 * @return Generated entity object
	 */
	public static Entity createEntityFromNBTWithoutSpawningIt(NBTTagCompound tag, World worldIn) {
		Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
		entity.readFromNBT(tag);

		return entity;
	}

	/**
	 * Used internally for the placeSpawnerForMobs method
	 */
	public static ItemStack getSoulBottleItemStackForEntity(Entity entity) {
		ItemStack bottle = new ItemStack(ModItems.SOUL_BOTTLE);
		bottle.setCount(1);
		NBTTagCompound mobToSpawnerItem = new NBTTagCompound();
		
		NBTTagCompound entityTag = new NBTTagCompound();
		if(entity.writeToNBTOptional(entityTag)) {
			mobToSpawnerItem.setTag("EntityIn", entityTag);
			bottle.setTagCompound(mobToSpawnerItem);
			return bottle;
		}
		return null;
	}
	
}
