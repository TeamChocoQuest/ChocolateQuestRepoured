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
import net.minecraft.nbt.NBTTagList;
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
	 * @param spawnerSettingsOverrides Settings to be applied if generating vanilla spawner (can be null if CQR spawner)
	 * @param world World in which to place spawner
	 * @param pos Position at which to place spawner
	 */
	public static void placeSpawner(Entity[] entities, boolean multiUseSpawner, @Nullable NBTTagCompound spawnerSettingsOverrides, World world, BlockPos pos) {

		world.setBlockToAir(pos);

		world.setBlockState(pos, (multiUseSpawner == true /*&& spawnerSettingsOverrides != null*/) ? Blocks.MOB_SPAWNER.getDefaultState() : ModBlocks.SPAWNER.getDefaultState());

		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityMobSpawner) {

			// Vars
			TileEntityMobSpawner spawner = (TileEntityMobSpawner) tile;
			NBTTagCompound spawnerData = new NBTTagCompound();
			NBTTagList spawnerEntities = spawnerData.getTagList("SpawnPotentials", 10);

			// Store entity ids into NBT tag
			for (int i = 0; i < entities.length; i++) {
				if(entities[i] != null) {
					NBTTagCompound entityToAddAsNBT = new NBTTagCompound();
					entityToAddAsNBT.setString("id", EntityList.getEntityString(entities[i]));
					spawnerEntities.set(i, entityToAddAsNBT);
				}
			}
			spawnerData.setTag("SpawnPotentials", spawnerEntities);

			// Store default settings into NBT
			spawnerData.setInteger("MinSpawnDelay", 200);
			spawnerData.setInteger("MaxSpawnDelay", 800);
			spawnerData.setInteger("SpawnCount", 4);
			spawnerData.setInteger("MaxNearbyEntities", 6);
			spawnerData.setInteger("SpawnRange", 4);
			spawnerData.setInteger("RequiredPlayerRange", 16);

			// Call spawner obj to read data from newly created NBT
			spawner.readFromNBT(spawnerData);
			if(spawnerSettingsOverrides != null) spawner.readFromNBT(spawnerSettingsOverrides);
			spawner.updateContainingBlockInfo();
			spawner.update();
			spawner.markDirty();
		}
		if(tile instanceof TileEntitySpawner) {
			TileEntitySpawner spawner = (TileEntitySpawner)tile;

			for(int i = 0; i < entities.length && i < 9; i++) {
				spawner.inventory.setStackInSlot(i, getSoulBottleItemStackForEntity(entities[i]));
			}

			spawner.updateContainingBlockInfo();
			spawner.update();
			spawner.markDirty();
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
	public static void convertCQSpawnerToVanillaSpawner(World world, BlockPos pos, @Nullable NBTTagCompound spawnerSettings) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntitySpawner) {
			TileEntitySpawner spawner = (TileEntitySpawner)tile;
			
			Entity[] entities = new Entity[spawner.inventory.getSlots()];
			Random rand = new Random();
			
			for(int i = 0; i < entities.length; i++) {
				ItemStack stack = spawner.inventory.getStackInSlot(i);
	    		if(stack != null && !stack.isEmpty() && stack.getCount() >= 1) {
	    			try {
	        			NBTTagCompound tag = stack.getTagCompound();
	            		
	        			NBTTagCompound entityTag = (NBTTagCompound)tag.getTag("EntityIn");
	        			
	        			entities[i] = createEntityFromNBTWithoutSpawningIt(entityTag, world);
	        			
	        			entities[i].setUniqueId(MathHelper.getRandomUUID(rand));
	    			}
	    			catch(NullPointerException ignored) {}
	    		} else {
	    			entities[i] = null;
	    		}
			}
			world.setBlockToAir(pos);
			
			placeSpawner(entities, true, spawnerSettings, world, pos);
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
				placeSpawner(entities, false, null, world, pos);
			}
		}
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
	 * Used internally for the placeSpawner method
	 */
	public static ItemStack getSoulBottleItemStackForEntity(Entity entity) {
		if(entity == null) {
			return null;
		}
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
