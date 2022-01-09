package team.cqr.cqrepoured.world.structure.generation.thewall.wallparts;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorDyable;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.SpawnerFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.part.BlockDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableSpawnerInfo;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class WallPartRailingWall implements IWallPart {

	@Override
	public int getTopY() {
		return CQRConfig.wall.topY - 12;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, ChunkGenerator cg, GeneratableDungeon.Builder dungeonBuilder, ServerWorld sw) {
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;
		int startY = this.getTopY();

		BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
		BlockState stateBlock = Blocks.SMOOTH_STONE.defaultBlockState();

		int[] zValues = new int[] { 2, 3, 12, 13 };
		for (int y = 0; y < 8; y++) {
			for (int z : zValues) {
				for (int x = 0; x < 8; x++) {
					if (this.isBiggerPart(x)) {
						if (y >= 3 || z == 3 || z == 12) {
							partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2, y, z), stateBlock, null));
							partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2 + 1, y, z), stateBlock, null));
						}
					} else if (y >= 4 && y <= 6 && (z == 3 || z == 12)) {
						partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2, y, z), stateBlock, null));
						partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2 + 1, y, z), stateBlock, null));
					}
				}
			}
		}

		// Spawner
		this.placeSpawner(new BlockPos(4, 6, 7), sw, partBuilder);

		dungeonBuilder.add(partBuilder, dungeonBuilder.getPlacement(new BlockPos(startX, startY, startZ)));
	}

	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		return (xAsChunkRelativeCoord & 1) == 0;
	}

	private void placeSpawner(BlockPos spawnerPos, World world, BlockDungeonPart.Builder partBuilder) {
		Entity spawnerEnt = EntityList.createEntityByIDFromName(new ResourceLocation(CQRConfig.wall.mob), world);

		if (spawnerEnt instanceof LivingEntity) {
			Difficulty difficulty = world.getDifficulty();
			this.equipWeaponBasedOnDifficulty((LivingEntity) spawnerEnt, difficulty);
			this.equipArmorBasedOnDifficulty((LivingEntity) spawnerEnt, difficulty);

			if (spawnerEnt instanceof AbstractEntityCQR) {
				((AbstractEntityCQR) spawnerEnt).setHealingPotions(1);
			}

			BlockState state2 = CQRBlocks.SPAWNER.defaultBlockState();
			TileEntitySpawner tileSpawner = (TileEntitySpawner) CQRBlocks.SPAWNER.createTileEntity(state2, world);
			tileSpawner.inventory.setStackInSlot(0, SpawnerFactory.getSoulBottleItemStackForEntity(spawnerEnt));

			partBuilder.add(new PreparableSpawnerInfo(spawnerPos, tileSpawner.save(new CompoundNBT())));
		}
	}

	private void equipWeaponBasedOnDifficulty(LivingEntity entity, Difficulty difficulty) {
		switch (entity.getRandom().nextInt(5)) {
		case 0:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			entity.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(CQRItems.SHIELD_SPECTER, 1));
			break;
		case 1:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_AXE, 1));
			entity.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(CQRItems.SHIELD_SPECTER, 1));
			break;
		case 2:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW, 1));
			break;
		case 3:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW, 1));
			entity.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(CQRItems.SHIELD_SPECTER, 1));
			break;
		case 4:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			entity.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			break;
		}
	}

	private void equipArmorBasedOnDifficulty(LivingEntity entity, Difficulty difficulty) {
		ItemStack helm = new ItemStack(CQRItems.HELMET_IRON_DYABLE);
		ItemStack chest = new ItemStack(CQRItems.CHESTPLATE_IRON_DYABLE);
		ItemStack legs = new ItemStack(CQRItems.LEGGINGS_IRON_DYABLE);
		ItemStack feet = new ItemStack(CQRItems.BOOTS_IRON_DYABLE);

		if (difficulty == Difficulty.HARD) {
			if (entity.getRandom().nextDouble() < 0.35D) {
				helm = new ItemStack(CQRItems.HELMET_DIAMOND_DYABLE);
				chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE);
				legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE);
				feet = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE);
			}
		} else if (difficulty == Difficulty.NORMAL) {
			if (entity.getRandom().nextDouble() < 0.25D) {
				helm = new ItemStack(CQRItems.HELMET_DIAMOND_DYABLE);
				chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE);
				legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE);
				feet = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE);
			}
		} else {
			if (entity.getRandom().nextDouble() < 0.2D) {
				helm = new ItemStack(CQRItems.HELMET_DIAMOND_DYABLE);
				chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE);
				legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE);
				feet = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE);
			}
		}

		if (entity.getRandom().nextDouble() < 0.005D) {
			((ItemArmorDyable) helm.getItem()).setColor(helm, 0x1008FFFF);
			((ItemArmorDyable) helm.getItem()).setColor(chest, 0x1008FFFF);
			((ItemArmorDyable) helm.getItem()).setColor(legs, 0x1008FFFF);
			((ItemArmorDyable) helm.getItem()).setColor(feet, 0x1008FFFF);
		} else {
			((ItemArmorDyable) helm.getItem()).setColor(helm, 0);
			((ItemArmorDyable) helm.getItem()).setColor(chest, 0);
			((ItemArmorDyable) helm.getItem()).setColor(legs, 0);
			((ItemArmorDyable) helm.getItem()).setColor(feet, 0);
		}

		entity.setItemSlot(EquipmentSlotType.HEAD, helm);
		entity.setItemSlot(EquipmentSlotType.CHEST, chest);
		entity.setItemSlot(EquipmentSlotType.LEGS, legs);
		entity.setItemSlot(EquipmentSlotType.FEET, feet);
	}

}
