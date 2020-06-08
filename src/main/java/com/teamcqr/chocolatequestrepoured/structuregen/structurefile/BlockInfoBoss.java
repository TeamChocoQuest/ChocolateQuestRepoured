package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacingHelper;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class BlockInfoBoss extends AbstractBlockInfo {

	public static final int ID = 2;

	public BlockInfoBoss(BlockPos pos) {
		super(pos);
	}

	public BlockInfoBoss(BlockPos pos, NBTTagIntArray nbtTagIntArray) {
		super(pos);
		this.readFromNBT(nbtTagIntArray, null, null);
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, EDungeonMobType dungeonMob, ProtectedRegion protectedRegion) {
		BlockPos transformedPos = dungeonPartPos.add(Template.transformedBlockPos(settings, this.pos));

		if (!world.isOutsideBuildHeight(transformedPos)) {
			BlockPlacingHelper.setBlockState(world, transformedPos, Blocks.AIR.getDefaultState(), 18, CQRConfig.advanced.instantLightUpdates);

			if (dungeonMob.getBossResourceLocation() != null) {
				Entity entity = EntityList.createEntityByIDFromName(dungeonMob.getBossResourceLocation(), world);

				entity.setPosition(transformedPos.getX() + 0.5D, transformedPos.getY(), transformedPos.getZ() + 0.5D);
				if (entity instanceof AbstractEntityCQRBoss) {
					((AbstractEntityCQRBoss) entity).onSpawnFromCQRSpawnerInDungeon(settings, dungeonMob);
					((AbstractEntityCQRBoss) entity).setHealingPotions(3);
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
				EntityArmorStand indicator = new EntityArmorStand(world);
				indicator.setCustomNameTag("Oops! We haven't added this boss yet! Treat yourself to some free loot!");
				indicator.setPosition(transformedPos.getX() + 0.5D, transformedPos.getY(), transformedPos.getZ() + 0.5D);
				indicator.setEntityInvulnerable(true);
				indicator.setInvisible(true);
				indicator.setAlwaysRenderNameTag(true);
				indicator.setSilent(true);
				indicator.setNoGravity(true);

				world.spawnEntity(indicator);
			}
		} else {
			CQRMain.logger.warn("Failed to place boss at {}", transformedPos);
		}
	}

	@Override
	public int getId() {
		return ID;
	}

}
