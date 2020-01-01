package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.banners.BannerHelper;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockSpawner;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
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
public class CQStructurePart extends Template {

	private List<BannerInfo> banners = new ArrayList<BannerInfo>();
	private List<SpawnerInfo> spawners = new ArrayList<SpawnerInfo>();
	private List<LootChestInfo> chests = new ArrayList<LootChestInfo>();
	private List<ForceFieldNexusInfo> forceFieldCores = new ArrayList<ForceFieldNexusInfo>();

	private EDungeonMobType dungeonMob = EDungeonMobType.DEFAULT;

	private List<BossInfo> bosses = new ArrayList<>();

	private EBanners newBannerPattern = EBanners.WALKER_BANNER;

	@Nullable
	DungeonBase dungeon;
	int dunX, dunZ = 0;

	private int part_id;

	public CQStructurePart(@Nullable DungeonBase dungeon, int posOfDunX, int posOfDunZ, EDungeonMobType mob) {
		super();
		if (dungeon != null) {
			this.dungeon = dungeon;
			this.dungeonMob = mob;
		}
		this.dunX = posOfDunX;
		this.dunZ = posOfDunZ;
	}

	public void setNewBannerPattern(EBanners pattern) {
		this.newBannerPattern = pattern;
	}

	public void setDungeonMob(EDungeonMobType type) {
		this.dungeonMob = type;
	}

	public CQStructurePart(int part_id) {
		super();

		this.setPartId(part_id);
	}

	// CONFIRMED WORKING
	// @SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	@Override
	public void takeBlocksFromWorld(World worldIn, BlockPos startPos, BlockPos endPos, boolean takeEntities, Block toIgnore) {
		// System.out.println("Scanning blocks...");
		this.banners.clear();
		this.chests.clear();
		this.spawners.clear();
		this.forceFieldCores.clear();
		// System.out.println("Super class scan....");
		super.takeBlocksFromWorld(worldIn, startPos, endPos.subtract(startPos), takeEntities, toIgnore);
		// System.out.println("Filling special lists...");
		List<Template.BlockInfo> blocks = Lists.<Template.BlockInfo>newArrayList();
		Field superBlockField;
		try {
			boolean outsideOfDevEnv = false;
			try {
				superBlockField = Template.class.getDeclaredField("blocks");
			} catch (NoSuchFieldException ex) {
				// ex.printStackTrace();
				outsideOfDevEnv = true;
				System.out.println("It seems we're not in the dev environment... Using obfuscated field name...");
				superBlockField = null;
			}
			if (outsideOfDevEnv) {
				try {
					superBlockField = Template.class.getDeclaredField("field_186270_a");
				} catch (NoSuchFieldException ex) {
					ex.printStackTrace();
					superBlockField = null;
				}
			}

			if (superBlockField != null) {
				superBlockField.setAccessible(true);

				try {
					blocks = (List<Template.BlockInfo>) superBlockField.get(this);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			// DONE: Scan blocks for: Nullblocks, CQ-Spawners, CQ-Chests and banners with CQ-designs, store their indexes in the right lists. NOTE: All Indexes are also present in the removeEntries Array
			// after filtering, remove the entries and add them into their currect lists

			List<Template.BlockInfo> removeEntries = new ArrayList<Template.BlockInfo>();
			for (int i = 0; i < blocks.size(); i++) {
				Template.BlockInfo bi = blocks.get(i);
				Block currentBlock = bi.blockState.getBlock();

				// DONE: Fix bug: vanilla containers have no inventory?!?!
				// Problem: Does not even get the data from the super call ?!?!
				// Cause of it: chests item map is empty ???
				// Problem begins here: The chest tiledata's contents only contain air ?!?!

				// Banner - Floor
				if (Block.isEqualTo(currentBlock, Blocks.STANDING_BANNER)) {
					// DONE: Check if banner has a CQ pattern, if yes, add it to the list, it only needs the location
					TileEntity te = worldIn.getTileEntity(startPos.add(bi.pos));
					if (te != null && te instanceof TileEntityBanner) {
						if (DungeonGenUtils.isCQBanner((TileEntityBanner) te)) {
							BannerInfo bai = new BannerInfo(bi.pos);
							this.banners.add(bai);
						}
					}
				}
				// Wallbanner
				if (Block.isEqualTo(currentBlock, Blocks.WALL_BANNER)) {
					// DONE: Check if banner has a CQ pattern, if yes, add it to the list, it only needs the location
					TileEntity te = worldIn.getTileEntity(startPos.add(bi.pos));
					if (te != null && te instanceof TileEntityBanner) {
						if (DungeonGenUtils.isCQBanner((TileEntityBanner) te)) {
							BannerInfo bai = new BannerInfo(bi.pos);
							this.banners.add(bai);
						}
					}
				}

				// NULL Block
				if (Block.isEqualTo(currentBlock, ModBlocks.NULL_BLOCK)) {
					// DONE: remove the block entry, so that blocks don't get replaced when pasting
					removeEntries.add(bi);
				}

				// CQ-Spawners
				// DONE: Wait for spawner block and tileentity
				if (Block.isEqualTo(currentBlock, ModBlocks.SPAWNER)) {
					SpawnerInfo si = new SpawnerInfo((BlockSpawner) currentBlock, bi.pos, worldIn, bi.tileentityData);
					this.spawners.add(si);
					removeEntries.add(bi);
				}

				// Chests
				if (DungeonGenUtils.isLootChest(currentBlock)) {
					ELootTable elt = ELootTable.valueOf(currentBlock);
					if (elt != null) {
						LootChestInfo lci = new LootChestInfo(currentBlock, bi.pos, elt.getID());
						this.chests.add(lci);
						removeEntries.add(bi);
					}
				}

				// Force Field cores
				if (Block.isEqualTo(currentBlock, ModBlocks.FORCE_FIELD_NEXUS)) {
					ForceFieldNexusInfo ffni = new ForceFieldNexusInfo(bi.pos);
					this.forceFieldCores.add(ffni);
					removeEntries.add(bi);
				}

				// Boss blocks
				if (Block.isEqualTo(currentBlock, ModBlocks.BOSS_BLOCK)) {
					BossInfo boi = new BossInfo(bi.pos);
					this.bosses.add(boi);
					removeEntries.add(bi);
				}
			}
			// And now: remove all the entries we want to be gone....
			for (int i = 0; i < removeEntries.size(); i++) {
				// int index = removeEntries.get(i);
				blocks.remove(removeEntries.get(i));
			}
			// exchange the field values
			try {
				if (superBlockField != null) {
					superBlockField.set(this, blocks);
					superBlockField.setAccessible(false);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		// System.out.println("Blocks scanned!");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound tag = super.writeToNBT(nbt);

		NBTTagList bannerTags = new NBTTagList();
		for (BannerInfo bi : this.banners) {
			bannerTags.appendTag(bi.getAsNBTTag());
		}

		NBTTagList chestTags = new NBTTagList();
		for (LootChestInfo lci : this.chests) {
			chestTags.appendTag(lci.getAsNBTTag());
		}

		NBTTagList spawnerTags = new NBTTagList();
		for (SpawnerInfo si : this.spawners) {
			spawnerTags.appendTag(si.getAsNBTTag());
		}

		NBTTagList forcefieldcoreTags = new NBTTagList();
		for (ForceFieldNexusInfo ffni : this.forceFieldCores) {
			forcefieldcoreTags.appendTag(ffni.getAsNBTTag());
		}

		tag.setTag("banners", bannerTags);
		tag.setTag("chests", chestTags);
		tag.setTag("spawners", spawnerTags);
		tag.setTag("forcefieldcores", forcefieldcoreTags);

		return tag;
	}

	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);

		this.banners.clear();
		NBTTagList bannerTag = compound.getTagList("banners", 10);
		for (int i = 0; i < bannerTag.tagCount(); i++) {
			NBTTagCompound tag = bannerTag.getCompoundTagAt(i);
			this.banners.add(new BannerInfo(tag));
		}

		this.spawners.clear();
		NBTTagList spawnerTag = compound.getTagList("spawners", 10);
		for (int i = 0; i < spawnerTag.tagCount(); i++) {
			NBTTagCompound tag = spawnerTag.getCompoundTagAt(i);
			// DONE: Add functionaliy to spawner info
			this.spawners.add(new SpawnerInfo(tag));
		}

		this.chests.clear();
		NBTTagList chestTag = compound.getTagList("chests", 10);
		for (int i = 0; i < chestTag.tagCount(); i++) {
			NBTTagCompound tag = chestTag.getCompoundTagAt(i);
			this.chests.add(new LootChestInfo(tag));
		}

		this.forceFieldCores.clear();
		NBTTagList coresTag = compound.hasKey("forcefieldcores") ? compound.getTagList("forcefieldcores", 10) : new NBTTagList();
		for (int i = 0; i < coresTag.tagCount(); i++) {
			NBTTagCompound tag = coresTag.getCompoundTagAt(i);
			this.forceFieldCores.add(new ForceFieldNexusInfo(tag));
		}
	}

	@Override
	public void addBlocksToWorld(World worldIn, BlockPos pos, ITemplateProcessor templateProcessor, PlacementSettings placementIn, int flags) {
		super.addBlocksToWorld(worldIn, pos, templateProcessor, placementIn, flags);

		// Now we want to place the banners first......
		// DONE: Wait for banner patterns, then do this
		if (this.dungeon.replaceBanners() && this.banners != null && !this.banners.isEmpty() && this.newBannerPattern != null) {
			for (BannerInfo bi : this.banners) {
				if (bi != null) {
					BlockPos bannerPos = transformedBlockPos(placementIn, bi.getPos()).add(pos);
					try {
						TileEntityBanner banner = (TileEntityBanner) worldIn.getTileEntity(bannerPos);
						if (BannerHelper.isCQBanner(banner)) {
							// TODO: Set banners new base color
							// DONE: Place replaced banners
							// DONE: "Clean" the banner
							// DONE: Repaint the banner
							banner.setItemValues(this.newBannerPattern.getBanner(), true);
						}
					} catch (ClassCastException ex) {

					}
				}
			}
		}
		// Then, we place the chests and set their loot tables.....
		if (this.chests != null && !this.chests.isEmpty()) {
			for (LootChestInfo lci : this.chests) {
				if (lci != null) {
					BlockPos chestPos = transformedBlockPos(placementIn, lci.getPos()).add(pos);
					Block chestBlock = lci.isRedstoneChest() ? Blocks.TRAPPED_CHEST : Blocks.CHEST;
					worldIn.setBlockState(chestPos, chestBlock.getDefaultState());
					TileEntityChest chest = (TileEntityChest) worldIn.getTileEntity(chestPos);
					// DONE: Wait for loot tables to be finished, get the right one and add it below
					long seed = WorldDungeonGenerator.getSeed(worldIn, chestPos.getX(), chestPos.getZ());
					chest.setLootTable(ELootTable.valueOf(lci.getLootType()).getResourceLocation(), seed);
				}
			}
		}

		// And at last, we place the spawners....
		if (this.spawners != null && !this.spawners.isEmpty()) {
			for (SpawnerInfo si : this.spawners) {
				// DONE: Place spawners
				BlockPos spawnerPos = transformedBlockPos(placementIn, si.getPos().add(pos));
				worldIn.setBlockState(spawnerPos, ModBlocks.SPAWNER.getDefaultState());
				TileEntity te = worldIn.getTileEntity(spawnerPos);

				// Problem is here: it somehow fails to properly "set" the data to the tile entity .... >:( --> solved
				NBTTagCompound tileData = si.getSpawnerData();
				tileData.setInteger("x", spawnerPos.getX());
				tileData.setInteger("y", spawnerPos.getY());
				tileData.setInteger("z", spawnerPos.getZ());

				((TileEntitySpawner) te).setDungeonSpawner();

				if (this.dungeon != null) {
					((TileEntitySpawner) te).setInDungeon(this.dungeon, this.dunX, this.dunZ, this.dungeonMob);
				}

				te.readFromNBT(tileData);
				te.mirror(placementIn.getMirror());
				te.rotate(placementIn.getRotation());
			}
		}
		// Done :D
	}

	public int getPartId() {
		return this.part_id;
	}

	public void setPartId(int part_id) {
		this.part_id = part_id;
	}

	public List<ForceFieldNexusInfo> getFieldCores() {
		return new ArrayList<ForceFieldNexusInfo>(this.forceFieldCores);
	}

	public List<BossInfo> getBosses() {
		return this.bosses;
	}

}
