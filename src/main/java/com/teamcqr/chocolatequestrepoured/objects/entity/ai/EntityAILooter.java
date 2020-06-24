package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemBackpack;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EntityAILooter extends AbstractCQREntityAI<AbstractEntityCQR> {
	
	protected Set<BlockPos> visitedChests = new HashSet<>();
	protected BlockPos currentTarget = null;
	
	protected int currentLootingTime = 0;
	protected final int LOOTING_DURATION = 100;
	protected int cooldown = 100;
	protected final int MAX_COOLDOWN = 100;
	
	protected final double REQUIRED_DISTANCE_TO_LOOT = 4; // = 2 * 2

	public EntityAILooter(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(2);
	}

	@Override
	public boolean shouldExecute() {
		if (this.cooldown > 0) {
			this.cooldown--;
		}

		if (this.cooldown <= 0 && this.entity.ticksExisted % 4 == 0) {
			if (!hasBackpack(this.entity)) {
				return false;
			}
			if (!this.hasBackpackSpace()) {
				return false;
			}
			BlockPos pos = new BlockPos(this.entity);
			int radius = CQRConfig.mobs.looterAIChestSearchRange;
			this.currentTarget = this.getNearestUnvisitedChest(this.world, pos.getX(), pos.getY(), pos.getZ(), radius, radius / 2);
		}

		return this.currentTarget != null;
	}
	
	private boolean hasBackpackSpace() {
		ItemStack backpack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		IItemHandler inventory = backpack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(inventory != null) {
			for(int i = 0; i < inventory.getSlots(); i++) {
				if(inventory.getStackInSlot(i).isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		currentLootingTime = LOOTING_DURATION;
		this.entity.getNavigator().tryMoveToXYZ(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), 1.125D);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && isChestStillThere() && hasBackpack(entity) && hasBackpackSpace();
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		
		if(entity.getNavigator().getPathToPos(currentTarget) == null) {
			this.visitedChests.add(currentTarget);
			currentTarget = null;
			return;
		}
		
		if(isInLootingRange()) {
			entity.getNavigator().clearPath();
			TileEntityChest tile = (TileEntityChest) world.getTileEntity(currentTarget);
			//TODO: let it stay open
			if(currentLootingTime >= 0) {
				currentLootingTime--;
				if(currentLootingTime % (LOOTING_DURATION / CQRConfig.mobs.looterAIStealableItems) == 0) {
					ItemStack stolenItem = null;
					for(int i = 0; i < tile.getSizeInventory(); i++) {
						if(!tile.getStackInSlot(i).isEmpty()) {
							stolenItem = tile.getStackInSlot(i).copy();
							tile.removeStackFromSlot(i);
							break;
						}
					}
					if(stolenItem != null) {
						tile.markDirty();
						entity.swingArm(EnumHand.MAIN_HAND);
						entity.swingArm(EnumHand.OFF_HAND);
						ItemStack backpack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
						IItemHandler inventory = backpack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
						if(inventory != null) {
							for(int i = 0; i < inventory.getSlots(); i++) {
								if(inventory.getStackInSlot(i).isEmpty()) {
									inventory.insertItem(i, stolenItem, false);
									break;
								}
							}
						}
					}
				}
			} else {
				this.visitedChests.add(currentTarget);
			}
		} else {
			entity.getLookHelper().setLookPosition(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), 30, 30);
			if (!this.entity.hasPath()) {
				this.entity.getNavigator().tryMoveToXYZ(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), 1.125D);
			}
		}
	}
	
	private boolean isInLootingRange() {
		return entity.getDistanceSq(currentTarget) <= REQUIRED_DISTANCE_TO_LOOT;
	}

	protected boolean hasBackpack(EntityLiving living) {
		ItemStack chest = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		return chest != null && chest.getItem() instanceof ItemBackpack;
	}
	

	@Nullable
	protected BlockPos getNearestUnvisitedChest(World world, int x, int y, int z, int horizontalRadius, int vertialRadius) {
		if (world.getWorldType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			return null;
		}
		int x1 = Math.max(x - horizontalRadius, -30000000);
		int y1 = Math.max(y - vertialRadius, 1);
		int z1 = Math.max(z - horizontalRadius, -30000000);
		int x2 = Math.min(x + horizontalRadius, 30000000);
		int y2 = Math.min(y + vertialRadius, 255);
		int z2 = Math.min(z + horizontalRadius, 30000000);
		BlockPos.MutableBlockPos pos1 = null;
		BlockPos.MutableBlockPos pos2 = new BlockPos.MutableBlockPos();
		double min = Double.MAX_VALUE;
		int oldChunkX = x1 >> 4;
		int oldChunkY = y1 >> 4;
		int oldChunkZ = z1 >> 4;
		boolean isLoaded = world.isBlockLoaded(pos2.setPos(x1, 0, z1));
		Chunk chunk = null;
		ExtendedBlockStorage extendedBlockStorage = Chunk.NULL_BLOCK_STORAGE;
		if (isLoaded) {
			chunk = world.getChunkFromChunkCoords(oldChunkX, oldChunkZ);
			extendedBlockStorage = chunk.getBlockStorageArray()[oldChunkY];
		}
		for (int x3 = x1; x3 <= x2; x3++) {
			int chunkX = x3 >> 4;

			if (chunkX != oldChunkX) {
				oldChunkX = chunkX;
				oldChunkY = y1 >> 4;
				oldChunkZ = z1 >> 4;
				isLoaded = world.isBlockLoaded(pos2.setPos(x3, 0, z1));
				if (isLoaded) {
					chunk = world.getChunkFromChunkCoords(chunkX, z1 >> 4);
					extendedBlockStorage = chunk.getBlockStorageArray()[y1 >> 4];
				}
			}

			for (int z3 = z1; z3 <= z2; z3++) {
				int chunkZ = z3 >> 4;

				if (chunkZ != oldChunkZ) {
					oldChunkX = chunkX;
					oldChunkY = y1 >> 4;
					oldChunkZ = chunkZ;
					isLoaded = world.isBlockLoaded(pos2.setPos(x3, 0, z3));
					if (isLoaded) {
						chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
						extendedBlockStorage = chunk.getBlockStorageArray()[y1 >> 4];
					}
				}

				if (isLoaded) {
					for (int y3 = y1; y3 <= y2; y3++) {
						int chunkY = y3 >> 4;

						if (chunkY != oldChunkY) {
							oldChunkY = chunkY;
							extendedBlockStorage = chunk.getBlockStorageArray()[chunkY];
						}

						if (extendedBlockStorage != Chunk.NULL_BLOCK_STORAGE) {
							IBlockState state1 = extendedBlockStorage.get(x3 & 15, y3 & 15, z3 & 15);

							if (state1.getBlock() == Blocks.CHEST) {
								Vec3d vec3d1 = this.entity.getPositionEyes(1.0F);
								Vec3d vec3d2 = new Vec3d(x3 + 0.5D, y3 + 0.5D, z3 + 0.5D);
								RayTraceResult rayTraceResult = this.entity.world.rayTraceBlocks(vec3d1, vec3d2, false, true, false);
								if (rayTraceResult != null && !rayTraceResult.getBlockPos().equals(pos2.setPos(x3, y3, z3))) {
									continue;
								}
								TileEntity te = this.world.getTileEntity(pos2);
								if (te instanceof TileEntityChest && !this.visitedChests.contains(pos2) && !((TileEntityChest) te).isEmpty()) {
									double distance = this.entity.getDistanceSqToCenter(pos2);

									if (distance < min) {
										pos1 = pos1 != null ? pos1.setPos(x3, y3, z3) : new BlockPos.MutableBlockPos(x3, y3, z3);
										min = distance;
									}
								}
							}

						} else {
							y3 += 15 - (y3 & 15);
						}
					}
				} else {
					z3 += 15 - (z3 & 15);
				}
			}
		}

		return pos1 != null ? pos1.toImmutable() : null;
	}
	
	protected boolean isChestStillThere() {
		if(currentTarget == null) {
			return false;
		}
		if(visitedChests.contains(currentTarget)) {
			return false;
		}
		TileEntity tile = world.getTileEntity(currentTarget);
		return tile != null && tile instanceof TileEntityChest && !((TileEntityChest)tile).isEmpty();
	}

}
