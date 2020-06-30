package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.HashSet;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemBackpack;
import com.teamcqr.chocolatequestrepoured.util.BlockPosUtil;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
		if (this.cooldown > 0) {
			return false;
		}
		if (this.entity.ticksExisted % 4 == 0) {
			if (!hasBackpack(this.entity)) {
				return false;
			}
			if (!this.hasBackpackSpace()) {
				return false;
			}
			BlockPos pos = new BlockPos(this.entity);
			Vec3d vec = this.entity.getPositionEyes(1.0F);
			int horizontalRadius = CQRConfig.mobs.looterAIChestSearchRange;
			int verticalRadius = horizontalRadius >> 1;
			this.currentTarget = BlockPosUtil.getNearest(this.world, pos.getX(), pos.getY() + (MathHelper.ceil(this.entity.height) >> 1), pos.getZ(), horizontalRadius, verticalRadius, true, true, Blocks.CHEST, (mutablePos, state) -> {
				if (this.visitedChests.contains(mutablePos)) {
					return false;
				}
				TileEntity te = this.world.getTileEntity(mutablePos);
				if (!(te instanceof TileEntityChest) || ((TileEntityChest) te).isEmpty()) {
					return false;
				}
				RayTraceResult result = this.world.rayTraceBlocks(vec, new Vec3d(mutablePos.getX() + 0.5D, mutablePos.getY() + 0.5D, mutablePos.getZ() + 0.5D), false, true, false);
				return result == null || result.getBlockPos().equals(mutablePos);
			});
		}

		return this.currentTarget != null;
	}

	private boolean hasBackpackSpace() {
		ItemStack backpack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		IItemHandler inventory = backpack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (inventory != null) {
			for (int i = 0; i < inventory.getSlots(); i++) {
				if (inventory.getStackInSlot(i).isEmpty()) {
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

		if (entity.getNavigator().getPathToPos(currentTarget) == null) {
			this.visitedChests.add(currentTarget);
			currentTarget = null;
			return;
		}

		if (isInLootingRange()) {
			entity.getNavigator().clearPath();
			TileEntityChest tile = (TileEntityChest) world.getTileEntity(currentTarget);
			// TODO: let it stay open
			if (currentLootingTime >= 0) {
				currentLootingTime--;
				if (currentLootingTime % (LOOTING_DURATION / CQRConfig.mobs.looterAIStealableItems) == 0) {
					ItemStack stolenItem = null;
					for (int i = 0; i < tile.getSizeInventory(); i++) {
						if (!tile.getStackInSlot(i).isEmpty()) {
							stolenItem = tile.getStackInSlot(i).copy();
							tile.removeStackFromSlot(i);
							break;
						}
					}
					if (stolenItem != null) {
						tile.markDirty();
						entity.swingArm(EnumHand.MAIN_HAND);
						entity.swingArm(EnumHand.OFF_HAND);
						ItemStack backpack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
						IItemHandler inventory = backpack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
						if (inventory != null) {
							for (int i = 0; i < inventory.getSlots(); i++) {
								if (inventory.getStackInSlot(i).isEmpty()) {
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

	protected boolean isChestStillThere() {
		if (currentTarget == null) {
			return false;
		}
		if (visitedChests.contains(currentTarget)) {
			return false;
		}
		TileEntity tile = world.getTileEntity(currentTarget);
		return tile != null && tile instanceof TileEntityChest && !((TileEntityChest) tile).isEmpty();
	}

}
