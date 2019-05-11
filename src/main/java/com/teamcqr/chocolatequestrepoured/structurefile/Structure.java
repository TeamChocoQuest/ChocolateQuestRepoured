package com.teamcqr.chocolatequestrepoured.structurefile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.teamcqr.chocolatequestrepoured.dungeongen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockSpawner;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class Structure extends Template {
	
	private List<BannerInfo> banners = new ArrayList<BannerInfo>();
	private List<SpawnerInfo> spawners = new ArrayList<SpawnerInfo>();
	private List<LootChestInfo> chests = new ArrayList<LootChestInfo>();
	
	private int part_id;
	
	public Structure() {
		super();
	}
	
	public Structure(int part_id) {
		super();
		this.setPart_id(part_id);
	}
	
	//CONFIRMED WORKING
	@SuppressWarnings("unchecked")
	@Override
	public void takeBlocksFromWorld(World worldIn, BlockPos startPos, BlockPos endPos, boolean takeEntities, Block toIgnore) {
		//System.out.println("Scanning blocks...");
		this.banners.clear();
		this.chests.clear();
		this.spawners.clear();
		//System.out.println("Super class scan....");
		super.takeBlocksFromWorld(worldIn, startPos, endPos.subtract(startPos), takeEntities, toIgnore);
		//System.out.println("Filling special lists...");
		List<Template.BlockInfo> blocks = Lists.<Template.BlockInfo>newArrayList();
		Field superBlockField;
		try {
			superBlockField = Template.class.getDeclaredField("blocks");
			
			superBlockField.setAccessible(true);
			try {
				blocks = (List<BlockInfo>) superBlockField.get(this);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			//TODO: Scan blocks for: Nullblocks, CQ-Spawners, CQ-Chests and banners with CQ-designs, store their indexes in the right lists. NOTE: All Indexes are also present in the removeEntries Array
			//after filtering, remove the entries and add them into their currect lists
			List<Template.BlockInfo> removeEntries = new ArrayList<Template.BlockInfo>();
			for(int i = 0; i < blocks.size(); i++) {
				Template.BlockInfo bi = blocks.get(i);
				Block currentBlock = bi.blockState.getBlock();
				//Banner - Floor
				if(Block.isEqualTo(currentBlock, Blocks.STANDING_BANNER)) {
					//TODO: Check if banner has a CQ pattern, if yes, add it to the list, it only needs the location
					if(DungeonGenUtils.isCQBanner()) {
						BannerInfo bai = new BannerInfo(bi.pos);
						this.banners.add(bai);
					}
				}
				//Wallbanner
				if(Block.isEqualTo(currentBlock, Blocks.WALL_BANNER)) {
					//TODO: Check if banner has a CQ pattern, if yes, add it to the list, it only needs the location
					if(DungeonGenUtils.isCQBanner()) {
						BannerInfo bai = new BannerInfo(bi.pos);
						this.banners.add(bai);
					}
				}
				
				//NULL Block
				if(Block.isEqualTo(currentBlock, ModBlocks.NULL_BLOCK)) {
					//DONE: remove the block entry, so that blocks don't get replaced when pasting
					removeEntries.add(bi);
				}
				
				//CQ-Spawners
				//DONE: Wait for spawner block and tileentity
				if(Block.isEqualTo(currentBlock, ModBlocks.SPAWNER)) {
					SpawnerInfo si = new SpawnerInfo((BlockSpawner) currentBlock, bi.pos, worldIn, bi.tileentityData);
					this.spawners.add(si);
					removeEntries.add(bi);
				}
				
				//Chests
				if(DungeonGenUtils.isLootChest(currentBlock)) {
					ELootTable elt = ELootTable.valueOf(currentBlock);
					if(elt != null) {
						LootChestInfo lci = new LootChestInfo(currentBlock, bi.pos, elt.getID());
						this.chests.add(lci);
						removeEntries.add(bi);
					}
				}
			}
			//And now: remove all the entries we want to be gone.... 
			for(int i = 0; i < removeEntries.size(); i++) {
				//int index = removeEntries.get(i);
				blocks.remove(removeEntries.get(i));
			}
			//exchange the field values
			try {
				superBlockField.set(this, blocks);
				superBlockField.setAccessible(false);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		//System.out.println("Blocks scanned!");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound tag =  super.writeToNBT(nbt);
		
		NBTTagList bannerTags = new NBTTagList();
		for(BannerInfo bi : this.banners) {
			bannerTags.appendTag(bi.getAsNBTTag());
		}
		
		NBTTagList chestTags = new NBTTagList();
		for(LootChestInfo lci : this.chests) {
			chestTags.appendTag(lci.getAsNBTTag());
		}
		
		NBTTagList spawnerTags = new NBTTagList();
		for(SpawnerInfo si : this.spawners) {
			spawnerTags.appendTag(si.getAsNBTTag());
		}
		
		tag.setTag("banners", bannerTags);
		tag.setTag("chests", chestTags);
		tag.setTag("spawners", spawnerTags);
		
		return tag;
	}
	
	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);
		
		this.banners.clear();
		NBTTagList bannerTag = compound.getTagList("banners", 10);
		for(int i = 0; i < bannerTag.tagCount(); i++) {
			NBTTagCompound tag = bannerTag.getCompoundTagAt(i);
			this.banners.add(new BannerInfo(tag));
		}
		
		this.spawners.clear();
		NBTTagList spawnerTag = compound.getTagList("spawners", 10);
		for(int i = 0; i < spawnerTag.tagCount(); i++) {
			NBTTagCompound tag = spawnerTag.getCompoundTagAt(i);
			//DONE: Add functionaliy to spawner info
			this.spawners.add(new SpawnerInfo(tag));
		}
		
		this.chests.clear();
		NBTTagList chestTag = compound.getTagList("chests", 10);
		for(int i = 0; i < chestTag.tagCount(); i++) {
			NBTTagCompound tag = chestTag.getCompoundTagAt(i);
			this.chests.add(new LootChestInfo(tag));
		}
	}
	
	@Override
	public void addBlocksToWorld(World worldIn, BlockPos pos, ITemplateProcessor templateProcessor, PlacementSettings placementIn, int flags) {
		super.addBlocksToWorld(worldIn, pos, templateProcessor, placementIn, flags);
		
		//Now we want to place the banners first......
		//TODO: Wait for banner patterns, then do this
		if(this.banners != null && !this.banners.isEmpty()) {
			for(BannerInfo bi : this.banners) {
				if(bi != null) {
					BlockPos bannerPos = transformedBlockPos(placementIn, bi.getPos()).add(pos);
					try {
						@SuppressWarnings("unused")
						TileEntityBanner banner = (TileEntityBanner) worldIn.getTileEntity(bannerPos);
						//TODO: Set banner design
						//banner.writeToNBT(compound)
					} catch(ClassCastException ex) {
					
					}
				}
			}
		}
		//Then, we place the chests and set their loot tables.....
		if(this.chests != null && !this.chests.isEmpty()) {
			for(LootChestInfo lci : this.chests) {
				if(lci != null) {
					BlockPos chestPos = transformedBlockPos(placementIn, lci.getPos()).add(pos);
					Block chestBlock = lci.isRedstoneChest() ? Blocks.TRAPPED_CHEST : Blocks.CHEST;
					worldIn.setBlockState(chestPos, chestBlock.getDefaultState());
					TileEntityChest chest = (TileEntityChest) worldIn.getTileEntity(chestPos);
					//DONE: Wait for loot tables to be finished, get the right one and add it below
					long seed = WorldDungeonGenerator.getSeed(worldIn, chestPos.getX(), chestPos.getZ());
					chest.setLootTable(ELootTable.valueOf(lci.getLootType()).getLootTable(), seed);
				}
			}
		}
		
		
		//And at last, we place the spawners....
		if(this.spawners != null && !this.spawners.isEmpty()) {
			for(SpawnerInfo si : this.spawners) {
				//DONE: Place spawners
				BlockPos spawnerPos = transformedBlockPos(placementIn, si.getPos().add(pos));
				worldIn.setBlockState(spawnerPos, ModBlocks.SPAWNER.getDefaultState());
				TileEntity te = worldIn.getTileEntity(spawnerPos);
				
				//Problem is here: it somehow fails to properly "set" the data to the tile entity .... >:( --> solved
				NBTTagCompound tileData = si.getSpawnerData();
				tileData.setInteger("x", spawnerPos.getX());
				tileData.setInteger("y", spawnerPos.getY());
				tileData.setInteger("z", spawnerPos.getZ());
				
				te.readFromNBT(tileData);
				te.mirror(placementIn.getMirror());
				te.rotate(placementIn.getRotation());
			}
		}
		//Done :D
	}

	public int getPart_id() {
		return part_id;
	}

	public void setPart_id(int part_id) {
		this.part_id = part_id;
	}

}
