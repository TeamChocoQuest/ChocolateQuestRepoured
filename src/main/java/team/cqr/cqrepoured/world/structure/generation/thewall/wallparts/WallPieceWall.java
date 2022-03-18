package team.cqr.cqrepoured.world.structure.generation.thewall.wallparts;

import java.util.Random;

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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRStructurePieces;
import team.cqr.cqrepoured.item.armor.ItemArmorDyable;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.SpawnerFactory;

public class WallPieceWall extends AbstractWallPiece {

	public WallPieceWall() {
		super(CQRStructurePieces.WALL_PIECE_WALL, 1);
	}
	
	public WallPieceWall(TemplateManager tm, CompoundNBT nbt) {
		super(CQRStructurePieces.WALL_PIECE_WALL, nbt, EWallPieceType.WALL);
	}
	
	@Override
	public int getTopY() {
		return CQRConfig.wall.topY - 7;
	}
	
	protected int getTopYForRailing() {
		return getTopY() - 5;
	}

	@Override
	public boolean postProcess(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
		int chunkX = pChunkPos.x;
		int chunkZ = pChunkPos.z;
		
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;
		int startY = this.getBottomY(pChunkGenerator, startX, startZ);
		
		int startYRailing = this.getTopYForRailing();
		BlockState stateBlockRailing = Blocks.SMOOTH_STONE.defaultBlockState();
		int[] zValues = new int[] { 2, 3, 12, 13 };
		for (int y = 0; y < (startYRailing + 8); y++) {
			for (int z : zValues) {
				for (int x = 0; x < 8; x++) {
					if ((this.isBiggerPart(x) && (y >= 3 || z == 3 || z == 12)) ^ (y >= 4 && y <= 6 && (z == 3 || z == 12))) {
							//partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2, y, z), stateBlock, null));
							//partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2 + 1, y, z), stateBlock, null));
							this.generateBox(
									pLevel, 
									pBox, 
									startX + (x * 2), 
									y + startYRailing, 
									startZ + z, 
									startX + (x * 2) + 1, 
									y + startYRailing, 
									startZ + z, 
									stateBlockRailing, 
									stateBlockRailing, 
									false
							);
					}
				}
			}
		}

		// Spawner
		this.placeSpawner(new BlockPos(4, 6, 7).offset(startX, startYRailing, startZ), pLevel);

		if (this.getTopY() > startY) {
			// All the calculated block positions are stored within this map
			//BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
			BlockState stateBrick = Blocks.STONE_BRICKS.defaultBlockState();
			BlockState stateObsidian = CQRConfig.wall.obsidianCore ? Blocks.OBSIDIAN.defaultBlockState() : stateBrick;

			int height = this.getTopY() - startY;
			// Calculates all the block positions
			//TODO: Figure out what kind of coordinates these are => relative to the center of the structure or world coordinates?
			this.generateBox(pLevel, pBox, startX + 0, startY + 0, startZ + 4, startX + 15, startY + height, startZ + 11, stateBrick, stateObsidian, false);
			/*for (BlockPos pos : BlockPos.betweenClosed(0, 0, 4, 15, height, 11)) {
				if (pos.getY() < height && pos.getZ() >= 6 && pos.getZ() <= 9) {
					partBuilder.add(new PreparableBlockInfo(pos, stateObsidian, null));
				} else {
					partBuilder.add(new PreparableBlockInfo(pos, stateBrick, null));
				}
			}
			

			dungeonBuilder.add(partBuilder, dungeonBuilder.getPlacement(new BlockPos(startX, startY, startZ)));*/
		}
		return true;
	}

	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		return (xAsChunkRelativeCoord & 1) == 0;
	}

	private void placeSpawner(BlockPos spawnerPos, ISeedReader iSeedReader) {
		Entity spawnerEnt = EntityList.createEntityByIDFromName(new ResourceLocation(CQRConfig.wall.mob), iSeedReader.getLevel());

		if (spawnerEnt instanceof LivingEntity) {
			Difficulty difficulty = iSeedReader.getDifficulty();
			this.equipWeaponBasedOnDifficulty((LivingEntity) spawnerEnt, difficulty);
			this.equipArmorBasedOnDifficulty((LivingEntity) spawnerEnt, difficulty);

			if (spawnerEnt instanceof AbstractEntityCQR) {
				((AbstractEntityCQR) spawnerEnt).setHealingPotions(1);
			}

			BlockState state2 = CQRBlocks.SPAWNER.get().defaultBlockState();
			TileEntitySpawner tileSpawner = (TileEntitySpawner) CQRBlocks.SPAWNER.get().createTileEntity(state2, iSeedReader);
			tileSpawner.getInventory().setItem(0, SpawnerFactory.getSoulBottleItemStackForEntity(spawnerEnt));

			//partBuilder.add(new PreparableSpawnerInfo(spawnerPos, tileSpawner.save(new CompoundNBT())));
			this.placeBlock(iSeedReader, state2, spawnerPos.getX(), spawnerPos.getY(), spawnerPos.getZ(), this.boundingBox);
		}
	}

	private void equipWeaponBasedOnDifficulty(LivingEntity entity, Difficulty difficulty) {
		switch (entity.getRandom().nextInt(5)) {
		case 0:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			entity.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(CQRItems.SHIELD_SPECTER.get(), 1));
			break;
		case 1:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_AXE, 1));
			entity.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(CQRItems.SHIELD_SPECTER.get(), 1));
			break;
		case 2:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW, 1));
			break;
		case 3:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW, 1));
			entity.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(CQRItems.SHIELD_SPECTER.get(), 1));
			break;
		case 4:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			entity.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			break;
		}
	}

	private void equipArmorBasedOnDifficulty(LivingEntity entity, Difficulty difficulty) {
		ItemStack helm = new ItemStack(CQRItems.HELMET_IRON_DYABLE.get());
		ItemStack chest = new ItemStack(CQRItems.CHESTPLATE_IRON_DYABLE.get());
		ItemStack legs = new ItemStack(CQRItems.LEGGINGS_IRON_DYABLE.get());
		ItemStack feet = new ItemStack(CQRItems.BOOTS_IRON_DYABLE.get());

		if (difficulty == Difficulty.HARD) {
			if (entity.getRandom().nextDouble() < 0.35D) {
				helm = new ItemStack(CQRItems.HELMET_DIAMOND_DYABLE.get());
				chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE.get());
				legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE.get());
				feet = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE.get());
			}
		} else if (difficulty == Difficulty.NORMAL) {
			if (entity.getRandom().nextDouble() < 0.25D) {
				helm = new ItemStack(CQRItems.HELMET_DIAMOND_DYABLE.get());
				chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE.get());
				legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE.get());
				feet = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE.get());
			}
		} else {
			if (entity.getRandom().nextDouble() < 0.2D) {
				helm = new ItemStack(CQRItems.HELMET_DIAMOND_DYABLE.get());
				chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE.get());
				legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE.get());
				feet = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE.get());
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
