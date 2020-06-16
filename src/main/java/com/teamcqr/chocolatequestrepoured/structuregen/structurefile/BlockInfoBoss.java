package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacingHelper;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class BlockInfoBoss extends AbstractBlockInfo {

	public BlockInfoBoss(BlockPos pos) {
		super(pos);
	}

	public BlockInfoBoss(BlockPos pos, NBTTagIntArray nbtTagIntArray) {
		super(pos);
		this.readFromNBT(nbtTagIntArray, null, null);
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, String dungeonMob, ProtectedRegion protectedRegion) {
		BlockPos transformedPos = dungeonPartPos.add(Template.transformedBlockPos(settings, this.pos));

		if (!world.isOutsideBuildHeight(transformedPos)) {
			BlockPlacingHelper.setBlockState(world, transformedPos, Blocks.AIR.getDefaultState(), 18, CQRConfig.advanced.instantLightUpdates);

			DungeonInhabitant inha = DungeonInhabitantManager.getInhabitantByName(dungeonMob);

			if (inha != null && inha.getBossID() != null) {
				Entity entity = EntityList.createEntityByIDFromName(inha.getBossID(), world);

				entity.setPosition(transformedPos.getX() + 0.5D, transformedPos.getY(), transformedPos.getZ() + 0.5D);
				if (entity instanceof AbstractEntityCQRBoss) {
					((AbstractEntityCQRBoss) entity).onSpawnFromCQRSpawnerInDungeon(settings, inha);
					((AbstractEntityCQRBoss) entity).setHealingPotions(1);
					if (inha.getFactionOverride() != null && !inha.getFactionOverride().isEmpty()) {
						((AbstractEntityCQRBoss) entity).setFaction(inha.getFactionOverride());
					}
				}
				if (entity instanceof EntityLiving) {
					((EntityLiving) entity).enablePersistence();
					((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(transformedPos), (IEntityLivingData) null);
				}
				world.spawnEntity(entity);

				if (protectedRegion != null) {
					protectedRegion.addEntityDependency(entity.getPersistentID());
				}
			} else {
				/*
				 * EntityArmorStand indicator = new EntityArmorStand(world); indicator.setCustomNameTag("Oops! We haven't added this boss yet! Treat yourself to some free loot!"); indicator.setPosition(transformedPos.getX() + 0.5D,
				 * transformedPos.getY(), transformedPos.getZ() + 0.5D); indicator.setEntityInvulnerable(true); indicator.setInvisible(true); indicator.setAlwaysRenderNameTag(true); indicator.setSilent(true); indicator.setNoGravity(true);
				 */
				Entity indicator = EntityList.createEntityByIDFromName(inha.getEntityID(), world);
				if (indicator instanceof EntityLiving) {
					if (indicator instanceof AbstractEntityCQR) {
						((AbstractEntityCQR) indicator).onSpawnFromCQRSpawnerInDungeon(settings, inha);
						((AbstractEntityCQR) indicator).setHealingPotions(1);
						((AbstractEntityCQR) indicator).resize(1.5F, 1.5F);
						if (inha.getFactionOverride() != null && !inha.getFactionOverride().isEmpty()) {
							((AbstractEntityCQR) indicator).setFaction(inha.getFactionOverride());
						}
					}
					((EntityLiving) indicator).onInitialSpawn(world.getDifficultyForLocation(transformedPos), (IEntityLivingData) null);

					((EntityLiving) indicator).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
					((EntityLiving) indicator).setHealth(((EntityLiving) indicator).getMaxHealth());

					// Some gear
					((EntityLiving) indicator).setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.GREAT_SWORD_DIAMOND, 1));
					((EntityLiving) indicator).setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(inha.getShieldReplacement(), 1));

					((EntityLiving) indicator).setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.HELMET_HEAVY_DIAMOND, 1));
					((EntityLiving) indicator).setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(ModItems.CHESTPLATE_HEAVY_DIAMOND, 1));
					((EntityLiving) indicator).setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(ModItems.LEGGINGS_HEAVY_DIAMOND, 1));
					((EntityLiving) indicator).setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(ModItems.BOOTS_HEAVY_DIAMOND, 1));

					indicator.setPosition(transformedPos.getX() + 0.5D, transformedPos.getY(), transformedPos.getZ() + 0.5D);
					((EntityLiving) indicator).enablePersistence();
					indicator.setCustomNameTag("Temporary Boss");
				}

				world.spawnEntity(indicator);
				if (protectedRegion != null) {
					protectedRegion.addEntityDependency(indicator.getPersistentID());
				}
			}
		} else {
			CQRMain.logger.warn("Failed to place boss at {}", transformedPos);
		}
	}

	@Override
	public int getId() {
		return BOSS_INFO_ID;
	}

}
