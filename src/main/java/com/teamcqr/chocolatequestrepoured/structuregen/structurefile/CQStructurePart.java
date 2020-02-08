package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.banners.BannerHelper;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockExporterChest;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class CQStructurePart extends Template {

	private final List<BlockPos> banners = new ArrayList<BlockPos>();
	private final List<BlockPos> spawners = new ArrayList<BlockPos>();
	private final List<LootChestInfo> chests = new ArrayList<LootChestInfo>();
	private final List<BlockPos> forceFieldCores = new ArrayList<BlockPos>();
	private final List<BlockPos> bosses = new ArrayList<BlockPos>();

	public void takeBlocksFromWorld(World worldIn, BlockPos startPos, BlockPos size) {
		this.takeBlocksFromWorld(worldIn, startPos, size, true, Blocks.STRUCTURE_VOID);

		this.banners.clear();
		this.chests.clear();
		this.spawners.clear();
		this.forceFieldCores.clear();
		this.bosses.clear();

		List<Template.BlockInfo> blocks = this.getBlockInfoList();
		List<Integer> removeEntries = new ArrayList<Integer>();

		for (int i = 0; i < blocks.size(); i++) {
			Template.BlockInfo blockInfo = blocks.get(i);
			Block currentBlock = blockInfo.blockState.getBlock();

			// Removing null blocks
			if (Block.isEqualTo(currentBlock, ModBlocks.NULL_BLOCK)) {
				removeEntries.add(i);
			}

			// Saving banner info
			if (Block.isEqualTo(currentBlock, Blocks.STANDING_BANNER) || Block.isEqualTo(currentBlock, Blocks.WALL_BANNER)) {
				TileEntity tileEntity = worldIn.getTileEntity(startPos.add(blockInfo.pos));

				if (tileEntity instanceof TileEntityBanner && BannerHelper.isCQBanner((TileEntityBanner) tileEntity)) {
					this.banners.add(blockInfo.pos);
				}
			}

			// Saving spawner info
			if (Block.isEqualTo(currentBlock, ModBlocks.SPAWNER)) {
				this.spawners.add(blockInfo.pos);
			}

			// Saving loot chest info
			if (currentBlock instanceof BlockExporterChest) {
				LootChestInfo lootChestInfo = new LootChestInfo(blockInfo.pos, blockInfo.blockState.getValue(BlockHorizontal.FACING), ((BlockExporterChest) currentBlock).lootTable);
				this.chests.add(lootChestInfo);
				removeEntries.add(i);
			}

			// Saving force field nexus info
			if (Block.isEqualTo(currentBlock, ModBlocks.FORCE_FIELD_NEXUS)) {
				this.forceFieldCores.add(blockInfo.pos);
				removeEntries.add(i);
			}

			// Saving boss info
			if (Block.isEqualTo(currentBlock, ModBlocks.BOSS_BLOCK)) {
				this.bosses.add(blockInfo.pos);
				removeEntries.add(i);
			}
		}

		for (int i = 0; i < removeEntries.size(); i++) {
			blocks.remove(removeEntries.get(i) - i);
		}
	}

	private List<Template.BlockInfo> getBlockInfoList() {
		try {
			Field field = Template.class.getDeclaredField("blocks");
			field.setAccessible(true);
			return (List<BlockInfo>) field.get(this);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error("Error while taking blocks from world", e);
		}
		return Collections.emptyList();
	}

	public void addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn, int dungeonChunkX, int dungeonChunkZ, EDungeonMobType dungeonMob, boolean replaceBanners, EBanners dungeonBanner, boolean hasShield) {
		this.addBlocksToWorld(worldIn, pos, placementIn);

		if (replaceBanners) {
			for (BlockPos bannerPos : this.banners) {
				BlockPos transformedPos = transformedBlockPos(placementIn, bannerPos).add(pos);
				TileEntity tileEntity = worldIn.getTileEntity(transformedPos);

				if (tileEntity instanceof TileEntityBanner) {
					((TileEntityBanner) tileEntity).setItemValues(dungeonBanner.getBanner(), true);
				} else {
					CQRMain.logger.warn("Failed to place banner at " + transformedPos);
				}
			}
		}

		for (BlockPos spawnerPos : this.spawners) {
			BlockPos transformedPos = transformedBlockPos(placementIn, spawnerPos).add(pos);
			TileEntity tileEntity = worldIn.getTileEntity(transformedPos);

			if (tileEntity instanceof TileEntitySpawner) {
				((TileEntitySpawner) tileEntity).setInDungeon(dungeonChunkX, dungeonChunkZ, dungeonMob);
			} else {
				CQRMain.logger.warn("Failed to place spawner at " + transformedPos);
			}
		}

		for (LootChestInfo lootChestInfo : this.chests) {
			BlockPos transformedPos = transformedBlockPos(placementIn, lootChestInfo.getPosition()).add(pos);

			if (!worldIn.isOutsideBuildHeight(transformedPos)) {
				worldIn.setBlockState(transformedPos, Blocks.CHEST.getDefaultState().withRotation(placementIn.getRotation()).withProperty(BlockHorizontal.FACING, lootChestInfo.getFacing()), 2);
				TileEntityChest tileEntityChest = (TileEntityChest) worldIn.getTileEntity(transformedPos);

				long seed = WorldDungeonGenerator.getSeed(worldIn, transformedPos.getX(), transformedPos.getZ());
				tileEntityChest.setLootTable(lootChestInfo.getLootTable().getResourceLocation(), seed);
			} else {
				CQRMain.logger.warn("Failed to place loot chest at " + transformedPos);
			}
		}

		for (BlockPos nexusPos : this.forceFieldCores) {
			BlockPos transformedPos = transformedBlockPos(placementIn, nexusPos).add(pos);

			if (!worldIn.isOutsideBuildHeight(transformedPos)) {
				if (hasShield) {
					worldIn.setBlockState(transformedPos, ModBlocks.FORCE_FIELD_NEXUS.getDefaultState().withRotation(placementIn.getRotation()), 2);

					// TODO add nexus to protection system
				} else {
					worldIn.setBlockState(transformedPos, Blocks.AIR.getDefaultState().withRotation(placementIn.getRotation()), 2);
				}
			} else {
				CQRMain.logger.warn("Failed to place force field nexus at " + transformedPos);
			}
		}

		for (BlockPos bossPos : this.bosses) {
			BlockPos transformedPos = transformedBlockPos(placementIn, bossPos).add(pos);

			if (!worldIn.isOutsideBuildHeight(transformedPos)) {
				worldIn.setBlockState(transformedPos, Blocks.AIR.getDefaultState().withRotation(placementIn.getRotation()), 2);

				if (dungeonMob.getBossResourceLocation() != null) {
					Entity entity = EntityList.createEntityByIDFromName(dungeonMob.getBossResourceLocation(), worldIn);

					entity.setPosition(transformedPos.getX() + 0.5D, transformedPos.getY(), transformedPos.getZ() + 0.5D);
					if (entity instanceof EntityLiving) {
						((EntityLiving) entity).enablePersistence();
					}
					if (entity instanceof AbstractEntityCQRBoss) {
						((AbstractEntityCQRBoss) entity).onSpawnFromCQRSpawnerInDungeon();
						((AbstractEntityCQRBoss) entity).setHealingPotions(CQRConfig.mobs.defaultHealingPotionCount);
						((AbstractEntityCQRBoss) entity).equipDefaultEquipment(worldIn, transformedPos);
					}
					worldIn.spawnEntity(entity);

					// TODO add boss to protection system
				} else {
					EntityArmorStand indicator = new EntityArmorStand(worldIn);
					indicator.setCustomNameTag("Oops! We haven't added this boss yet! Treat yourself to some free loot!");
					indicator.setPosition(transformedPos.getX() + 0.5D, transformedPos.getY(), transformedPos.getZ() + 0.5D);
					indicator.setEntityInvulnerable(true);
					indicator.setInvisible(true);
					indicator.setAlwaysRenderNameTag(true);
					indicator.setSilent(true);
					indicator.setNoGravity(true);

					worldIn.spawnEntity(indicator);
				}
			} else {
				CQRMain.logger.warn("Failed to place boss at " + transformedPos);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound tag = super.writeToNBT(nbt);

		tag.removeTag("author");

		NBTTagList bannerTagList = new NBTTagList();
		for (BlockPos pos : this.banners) {
			bannerTagList.appendTag(NBTUtil.createPosTag(pos));
		}

		NBTTagList spawnerTagList = new NBTTagList();
		for (BlockPos pos : this.spawners) {
			spawnerTagList.appendTag(NBTUtil.createPosTag(pos));
		}

		NBTTagList chestTagList = new NBTTagList();
		for (LootChestInfo lootChestInfo : this.chests) {
			chestTagList.appendTag(lootChestInfo.getAsNBTTag());
		}

		NBTTagList forceFieldNexusTagList = new NBTTagList();
		for (BlockPos pos : this.forceFieldCores) {
			forceFieldNexusTagList.appendTag(NBTUtil.createPosTag(pos));
		}

		NBTTagList bossTagList = new NBTTagList();
		for (BlockPos pos : this.bosses) {
			bossTagList.appendTag(NBTUtil.createPosTag(pos));
		}

		tag.setTag("banners", bannerTagList);
		tag.setTag("spawners", spawnerTagList);
		tag.setTag("chests", chestTagList);
		tag.setTag("forcefieldcores", forceFieldNexusTagList);
		tag.setTag("bosses", bossTagList);

		return tag;
	}

	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);

		this.banners.clear();
		this.spawners.clear();
		this.chests.clear();
		this.forceFieldCores.clear();
		this.bosses.clear();

		NBTTagList bannerTagList = compound.getTagList("banners", 10);
		for (int i = 0; i < bannerTagList.tagCount(); i++) {
			NBTTagCompound tag = bannerTagList.getCompoundTagAt(i);
			this.banners.add(NBTUtil.getPosFromTag(tag));
		}

		NBTTagList spawnerTagList = compound.getTagList("spawners", 10);
		for (int i = 0; i < spawnerTagList.tagCount(); i++) {
			NBTTagCompound tag = spawnerTagList.getCompoundTagAt(i);
			this.spawners.add(NBTUtil.getPosFromTag(tag));
		}

		NBTTagList chestTagList = compound.getTagList("chests", 10);
		for (int i = 0; i < chestTagList.tagCount(); i++) {
			NBTTagCompound tag = chestTagList.getCompoundTagAt(i);
			this.chests.add(new LootChestInfo(tag));
		}

		NBTTagList coresTagList = compound.getTagList("forcefieldcores", 10);
		for (int i = 0; i < coresTagList.tagCount(); i++) {
			NBTTagCompound tag = coresTagList.getCompoundTagAt(i);
			this.forceFieldCores.add(NBTUtil.getPosFromTag(tag));
		}

		NBTTagList bossTagList = compound.getTagList("bosses", 10);
		for (int i = 0; i < bossTagList.tagCount(); i++) {
			NBTTagCompound tag = bossTagList.getCompoundTagAt(i);
			this.bosses.add(NBTUtil.getPosFromTag(tag));
		}
	}

}
