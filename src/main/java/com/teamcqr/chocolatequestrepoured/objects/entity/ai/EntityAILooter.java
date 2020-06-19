package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.HashSet;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemBackpack;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
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
		if(cooldown <= 0) {
			return hasBackpack(entity) && hasBackpackSpace() && getNearestUnvisitedChest() != null;
		}
		cooldown--;
		return false;
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
		currentTarget = getNearestUnvisitedChest();
		currentLootingTime = LOOTING_DURATION;
		this.entity.getNavigator().tryMoveToXYZ(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), 1.125D);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && isChestStillThere();
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
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
	
	
	protected BlockPos getNearestUnvisitedChest() {
		Iterable<MutableBlockPos> list = BlockPos.getAllInBoxMutable(entity.getPosition().add(CQRConfig.mobs.looterAIChestSearchRange, CQRConfig.mobs.looterAIChestSearchRange /2 , CQRConfig.mobs.looterAIChestSearchRange),
				entity.getPosition().add(-CQRConfig.mobs.looterAIChestSearchRange, -(CQRConfig.mobs.looterAIChestSearchRange / 2), -CQRConfig.mobs.looterAIChestSearchRange));
		for(BlockPos pos : list) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile instanceof TileEntityChest && !((TileEntityChest)tile).isEmpty()) {
				if(currentTarget != pos && !visitedChests.contains(pos)) {
					return pos;
				}
			}
		}
		return null;
	}
	
	protected boolean isChestStillThere() {
		if(visitedChests.contains(currentTarget)) {
			return false;
		}
		TileEntity tile = world.getTileEntity(currentTarget);
		return tile != null && tile instanceof TileEntityChest && !((TileEntityChest)tile).isEmpty();
	}

}
