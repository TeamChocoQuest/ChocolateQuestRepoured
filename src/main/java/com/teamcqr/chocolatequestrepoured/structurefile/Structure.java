package com.teamcqr.chocolatequestrepoured.structurefile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.teamcqr.chocolatequestrepoured.dungeongen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

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
			superBlockField = this.getClass().getSuperclass().getDeclaredField("blocks");
			
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
			List<Integer> removeEntries = new ArrayList<Integer>();
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
					removeEntries.add(i);
				}
				
				//CQ-Spawners
				//TODO: Wait for spawner block and tileentity
				
				//Chests
				if(DungeonGenUtils.isLootChest(currentBlock)) {
					ELootTable elt = ELootTable.valueOf(currentBlock);
					if(elt != null) {
						removeEntries.add(i);
						LootChestInfo lci = new LootChestInfo(currentBlock, bi.pos, elt.getID());
						this.chests.add(lci);
						removeEntries.add(i);
					}
				}
			}
			//And now: remove all the entries we want to be gone.... 
			for(int i = 0; i < removeEntries.size(); i++) {
				blocks.remove(i);
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
		for(@SuppressWarnings("unused") SpawnerInfo si : this.spawners) {
			
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
			@SuppressWarnings("unused")
			NBTTagCompound tag = spawnerTag.getCompoundTagAt(i);
			//TODO: Add functionaliy to spawner info
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
			for(@SuppressWarnings("unused") SpawnerInfo si : this.spawners) {
				//TODO: Place spawners
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
