package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.banners.BannerHelper;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockExporterChest;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacingHelper;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class CQStructurePart extends Template {

	private static final Set<Block> SPECIAL_BLOCKS = new HashSet<>();
	private static final Set<String> SPECIAL_ENTITIES = new HashSet<>();

	private final List<BlockPos> banners = new ArrayList<>();
	private final List<BlockPos> spawners = new ArrayList<>();
	private final List<LootChestInfo> chests = new ArrayList<>();
	private final List<BlockPos> forceFieldCores = new ArrayList<>();
	private final List<BlockPos> bosses = new ArrayList<>();

	public void takeBlocksFromWorld(World worldIn, BlockPos startPos, BlockPos size, boolean takeSpecialBlocks, boolean ignoreEntities) {
		if (size.getX() >= 1 && size.getY() >= 1 && size.getZ() >= 1) {
			BlockPos blockpos = startPos.add(size).add(-1, -1, -1);
			List<Template.BlockInfo> list = new ArrayList<>();
			List<Template.BlockInfo> list1 = new ArrayList<>();
			List<Template.BlockInfo> list2 = new ArrayList<>();
			BlockPos blockpos1 = DungeonGenUtils.getMinPos(startPos, blockpos);
			BlockPos blockpos2 = DungeonGenUtils.getMaxPos(startPos, blockpos);
			this.setSize(size);

			for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos1, blockpos2)) {
				BlockPos blockpos3 = blockpos$mutableblockpos.subtract(blockpos1);
				IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);
				Block block = iblockstate.getBlock();

				if (block != Blocks.STRUCTURE_VOID && block != ModBlocks.NULL_BLOCK && takeSpecialBlocks == SPECIAL_BLOCKS.contains(block)) {
					TileEntity tileentity = worldIn.getTileEntity(blockpos$mutableblockpos);

					// Saving banner info
					if (Block.isEqualTo(block, Blocks.STANDING_BANNER) || Block.isEqualTo(block, Blocks.WALL_BANNER)) {
						TileEntity tileEntity = worldIn.getTileEntity(startPos.add(blockpos3));

						if (tileEntity instanceof TileEntityBanner && BannerHelper.isCQBanner((TileEntityBanner) tileEntity)) {
							this.banners.add(blockpos3);
						}
					}

					// Saving spawner info
					if (Block.isEqualTo(block, ModBlocks.SPAWNER)) {
						this.spawners.add(blockpos3);
					}

					// Saving loot chest info
					if (block instanceof BlockExporterChest) {
						LootChestInfo lootChestInfo = new LootChestInfo(blockpos3, iblockstate.getValue(BlockHorizontal.FACING), ((BlockExporterChest) block).lootTable);
						this.chests.add(lootChestInfo);
						continue;
					}

					// Saving force field nexus info
					if (Block.isEqualTo(block, ModBlocks.FORCE_FIELD_NEXUS)) {
						this.forceFieldCores.add(blockpos3);
						continue;
					}

					// Saving boss info
					if (Block.isEqualTo(block, ModBlocks.BOSS_BLOCK)) {
						this.bosses.add(blockpos3);
						continue;
					}

					if (tileentity != null) {
						NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
						nbttagcompound.removeTag("x");
						nbttagcompound.removeTag("y");
						nbttagcompound.removeTag("z");
						list1.add(new Template.BlockInfo(blockpos3, iblockstate, nbttagcompound));
					} else if (!iblockstate.isFullBlock() && !iblockstate.isFullCube()) {
						list2.add(new Template.BlockInfo(blockpos3, iblockstate, (NBTTagCompound) null));
					} else {
						list.add(new Template.BlockInfo(blockpos3, iblockstate, (NBTTagCompound) null));
					}
				}
			}

			List<Template.BlockInfo> blocks = this.getBlockInfoList();
			blocks.clear();
			blocks.addAll(list);
			blocks.addAll(list1);
			blocks.addAll(list2);

			if (takeSpecialBlocks) {
				this.takeEntitiesFromWorld(worldIn, blockpos1, blockpos2.add(1, 1, 1));
				if (ignoreEntities) {
					List<Template.EntityInfo> entities = this.getEntityInfoList();
					List<Integer> toRemove = new ArrayList<>();
					for (int i = 0; i < entities.size(); i++) {
						if (!SPECIAL_ENTITIES.contains(entities.get(i).entityData.getString("id"))) {
							toRemove.add(i);
						}
					}
					for (int i = 0; i < toRemove.size(); i++) {
						entities.remove(toRemove.get(i) - i);
					}
				}
			} else {
				List<Template.EntityInfo> entities = this.getEntityInfoList();
				entities.clear();
			}
		}
	}

	private static Field blocksField = null;

	private List<Template.BlockInfo> getBlockInfoList() {
		try {
			if (blocksField == null) {
				try {
					blocksField = Template.class.getDeclaredField("field_186270_a");
				} catch (NoSuchFieldException e) {
					blocksField = Template.class.getDeclaredField("blocks");
				}
				blocksField.setAccessible(true);
			}
			return (List<BlockInfo>) blocksField.get(this);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error("Failed to get value of Template.blocks field", e);
		}
		return Collections.emptyList();
	}

	private static Field entitiesField = null;

	private List<Template.EntityInfo> getEntityInfoList() {
		try {
			if (entitiesField == null) {
				try {
					entitiesField = Template.class.getDeclaredField("field_186271_b");
				} catch (NoSuchFieldException e) {
					entitiesField = Template.class.getDeclaredField("entities");
				}
				entitiesField.setAccessible(true);
			}
			return (List<Template.EntityInfo>) entitiesField.get(this);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error("Failed to get value of Template.entities field", e);
		}
		return Collections.emptyList();
	}

	private static Field sizeField = null;

	private void setSize(BlockPos size) {
		try {
			if (sizeField == null) {
				try {
					sizeField = Template.class.getDeclaredField("field_186272_c");
				} catch (NoSuchFieldException e) {
					sizeField = Template.class.getDeclaredField("size");
				}
				sizeField.setAccessible(true);
			}
			sizeField.set(this, size);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error("Failed to set value of Template.size field", e);
		}
	}

	private static Method takeEntitiesFromWorldMethod = null;

	private void takeEntitiesFromWorld(World worldIn, BlockPos startPos, BlockPos endPos) {
		try {
			if (takeEntitiesFromWorldMethod == null) {
				try {
					takeEntitiesFromWorldMethod = Template.class.getDeclaredMethod("func_186255_a", World.class, BlockPos.class, BlockPos.class);
				} catch (NoSuchMethodException e) {
					takeEntitiesFromWorldMethod = Template.class.getDeclaredMethod("takeEntitiesFromWorld", World.class, BlockPos.class, BlockPos.class);
				}
				takeEntitiesFromWorldMethod.setAccessible(true);
			}
			takeEntitiesFromWorldMethod.invoke(this, worldIn, startPos, endPos);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to invoke Template.takeEntitiesFromWorld method", e);
		}
	}

	private static Method addEntitiesToWorldMethod = null;

	private void addEntitiesToWorld(World worldIn, BlockPos pos, Mirror mirrorIn, Rotation rotationIn, @Nullable StructureBoundingBox aabb) {
		try {
			if (addEntitiesToWorldMethod == null) {
				try {
					addEntitiesToWorldMethod = Template.class.getDeclaredMethod("func_186255_a", World.class, BlockPos.class, Mirror.class, Rotation.class, StructureBoundingBox.class);
				} catch (NoSuchMethodException e) {
					addEntitiesToWorldMethod = Template.class.getDeclaredMethod("addEntitiesToWorld", World.class, BlockPos.class, Mirror.class, Rotation.class, StructureBoundingBox.class);
				}
				addEntitiesToWorldMethod.setAccessible(true);
			}
			addEntitiesToWorldMethod.invoke(this, worldIn, pos, mirrorIn, rotationIn, aabb);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to invoke Template.addEntitiesToWorld method", e);
		}
	}

	private static Method transformedVec3d = null;

	private Vec3d transformedVec3d(Vec3d vec, Mirror mirror, Rotation rotation) {
		try {
			if (transformedVec3d == null) {
				try {
					transformedVec3d = Template.class.getDeclaredMethod("func_186269_a", Vec3d.class, Mirror.class, Rotation.class);
				} catch (NoSuchMethodException e) {
					transformedVec3d = Template.class.getDeclaredMethod("transformedVec3d", Vec3d.class, Mirror.class, Rotation.class);
				}
				transformedVec3d.setAccessible(true);
			}
			return (Vec3d) transformedVec3d.invoke(this, vec, mirror, rotation);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to invoke Template.transformedVec3d method", e);
		}
		return vec;
	}

	private static Method transformedBlockPos = null;

	private BlockPos transformedBlockPos(BlockPos pos, Mirror mirror, Rotation rotation) {
		try {
			if (transformedBlockPos == null) {
				try {
					transformedBlockPos = Template.class.getDeclaredMethod("func_186268_a", BlockPos.class, Mirror.class, Rotation.class);
				} catch (NoSuchMethodException e) {
					transformedBlockPos = Template.class.getDeclaredMethod("transformedBlockPos", BlockPos.class, Mirror.class, Rotation.class);
				}
				transformedBlockPos.setAccessible(true);
			}
			return (BlockPos) transformedBlockPos.invoke(this, pos, mirror, rotation);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to invoke Template.transformedBlockPos method", e);
		}
		return pos;
	}

	private void addEntitiesToWorld2(World worldIn, BlockPos pos, Mirror mirrorIn, Rotation rotationIn, @Nullable StructureBoundingBox aabb) {
		for (Template.EntityInfo template$entityinfo : this.getEntityInfoList()) {
			BlockPos blockpos = transformedBlockPos(template$entityinfo.blockPos, mirrorIn, rotationIn).add(pos);

			if (aabb == null || aabb.isVecInside(blockpos)) {
				NBTTagCompound nbttagcompound = template$entityinfo.entityData;
				Vec3d vec3d = transformedVec3d(template$entityinfo.pos, mirrorIn, rotationIn);
				Vec3d vec3d1 = vec3d.addVector((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
				NBTTagList nbttaglist = new NBTTagList();
				nbttaglist.appendTag(new NBTTagDouble(vec3d1.x));
				nbttaglist.appendTag(new NBTTagDouble(vec3d1.y));
				nbttaglist.appendTag(new NBTTagDouble(vec3d1.z));
				nbttagcompound.setTag("Pos", nbttaglist);
				nbttagcompound.setUniqueId("UUID", UUID.randomUUID());
				Entity entity;

				try {
					entity = EntityList.createEntityFromNBT(nbttagcompound, worldIn);
				} catch (Exception var15) {
					entity = null;
				}

				if (entity != null) {
					float f = entity.getMirroredYaw(mirrorIn);
					f = f + (entity.rotationYaw - entity.getRotatedYaw(rotationIn));
					if (entity instanceof EntityPainting) {
						entity.setLocationAndAngles(blockpos.getX(), blockpos.getY(), blockpos.getZ(), f, entity.rotationPitch);
					} else {
						entity.setLocationAndAngles(vec3d1.x, vec3d1.y, vec3d1.z, f, entity.rotationPitch);
					}
					worldIn.spawnEntity(entity);
				}
			}
		}
	}

	public void addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn, int dungeonChunkX, int dungeonChunkZ, EDungeonMobType dungeonMob, boolean replaceBanners, EBanners dungeonBanner, ProtectedRegion protectedRegion) {
		// this.addBlocksToWorld(worldIn, pos, placementIn);
		BlockPlacingHelper.setBlockStates(worldIn, pos, this.getBlockInfoList(), placementIn, 2);
		this.addEntitiesToWorld2(worldIn, pos, placementIn.getMirror(), placementIn.getRotation(), placementIn.getBoundingBox());

		if (replaceBanners && dungeonBanner != null) {
			for (BlockPos bannerPos : this.banners) {
				BlockPos transformedPos = transformedBlockPos(placementIn, bannerPos).add(pos);
				TileEntity tileEntity = worldIn.getTileEntity(transformedPos);

				if (tileEntity instanceof TileEntityBanner) {
					((TileEntityBanner) tileEntity).setItemValues(dungeonBanner.getBanner(), true);
				} else {
					CQRMain.logger.warn("Failed to place banner at {}", transformedPos);
				}
			}
		}

		for (BlockPos spawnerPos : this.spawners) {
			BlockPos transformedPos = transformedBlockPos(placementIn, spawnerPos).add(pos);
			TileEntity tileEntity = worldIn.getTileEntity(transformedPos);

			if (tileEntity instanceof TileEntitySpawner) {
				((TileEntitySpawner) tileEntity).setInDungeon(dungeonChunkX, dungeonChunkZ, dungeonMob);
				((TileEntitySpawner) tileEntity).setMirrorAndRotation(placementIn.getMirror(), placementIn.getRotation());
			} else {
				CQRMain.logger.warn("Failed to place spawner at {}", transformedPos);
			}
		}

		for (LootChestInfo lootChestInfo : this.chests) {
			BlockPos transformedPos = transformedBlockPos(placementIn, lootChestInfo.getPosition()).add(pos);

			if (!worldIn.isOutsideBuildHeight(transformedPos)) {
				worldIn.setBlockState(transformedPos, Blocks.CHEST.getDefaultState().withMirror(placementIn.getMirror()).withRotation(placementIn.getRotation()).withProperty(BlockHorizontal.FACING, lootChestInfo.getFacing()), 2);
				TileEntityChest tileEntityChest = (TileEntityChest) worldIn.getTileEntity(transformedPos);

				long seed = WorldDungeonGenerator.getSeed(worldIn, transformedPos.getX(), transformedPos.getZ());
				tileEntityChest.setLootTable(lootChestInfo.getLootTable().getResourceLocation(), seed);
			} else {
				CQRMain.logger.warn("Failed to place loot chest at {}", transformedPos);
			}
		}

		for (BlockPos nexusPos : this.forceFieldCores) {
			BlockPos transformedPos = transformedBlockPos(placementIn, nexusPos).add(pos);

			if (!worldIn.isOutsideBuildHeight(transformedPos)) {
				if (protectedRegion != null) {
					worldIn.setBlockState(transformedPos, ModBlocks.FORCE_FIELD_NEXUS.getDefaultState().withMirror(placementIn.getMirror()).withRotation(placementIn.getRotation()), 2);
					protectedRegion.addBlockDependency(transformedPos);
				} else {
					worldIn.setBlockState(transformedPos, Blocks.AIR.getDefaultState(), 2);
				}
			} else {
				CQRMain.logger.warn("Failed to place force field nexus at {}", transformedPos);
			}
		}

		for (BlockPos bossPos : this.bosses) {
			BlockPos transformedPos = transformedBlockPos(placementIn, bossPos).add(pos);

			if (!worldIn.isOutsideBuildHeight(transformedPos)) {
				worldIn.setBlockState(transformedPos, Blocks.AIR.getDefaultState(), 2);

				if (dungeonMob.getBossResourceLocation() != null) {
					Entity entity = EntityList.createEntityByIDFromName(dungeonMob.getBossResourceLocation(), worldIn);

					entity.setPosition(transformedPos.getX() + 0.5D, transformedPos.getY(), transformedPos.getZ() + 0.5D);
					if (entity instanceof AbstractEntityCQRBoss) {
						((AbstractEntityCQRBoss) entity).onSpawnFromCQRSpawnerInDungeon(placementIn);
						((AbstractEntityCQRBoss) entity).setHealingPotions(CQRConfig.mobs.defaultHealingPotionCount);
					}
					if (entity instanceof EntityLiving) {
						((EntityLiving) entity).enablePersistence();
						((EntityLiving) entity).onInitialSpawn(worldIn.getDifficultyForLocation(transformedPos), (IEntityLivingData) null);
					}
					worldIn.spawnEntity(entity);

					if (protectedRegion != null) {
						protectedRegion.addEntityDependency(entity.getPersistentID());
					}
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
				CQRMain.logger.warn("Failed to place boss at {}", transformedPos);
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

	public static void updateSpecialBlocks() {
		CQStructurePart.SPECIAL_BLOCKS.clear();
		for (String s : CQRConfig.advanced.specialBlocks) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				CQStructurePart.SPECIAL_BLOCKS.add(b);
			}
		}
	}

	public static void updateSpecialEntities() {
		CQStructurePart.SPECIAL_ENTITIES.clear();
		for (String s : CQRConfig.advanced.specialEntities) {
			CQStructurePart.SPECIAL_ENTITIES.add(s);
		}
	}

}
