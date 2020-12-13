package team.cqr.cqrepoured.objects.entity.ai;

import java.util.HashSet;
import java.util.Set;

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
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.items.armor.ItemBackpack;
import team.cqr.cqrepoured.util.BlockPosUtil;
import team.cqr.cqrepoured.util.CQRConfig;

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
			if (!this.hasBackpack(this.entity)) {
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
		ItemStack backpack = this.entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
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
		this.currentLootingTime = this.LOOTING_DURATION;
		this.entity.getNavigator().tryMoveToXYZ(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), 1.125D);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.isChestStillThere() && this.hasBackpack(this.entity) && this.hasBackpackSpace();
	}

	@Override
	public void updateTask() {
		super.updateTask();

		if (this.entity.getNavigator().getPathToPos(this.currentTarget) == null) {
			this.visitedChests.add(this.currentTarget);
			this.currentTarget = null;
			return;
		}

		if (this.isInLootingRange()) {
			this.entity.getNavigator().clearPath();
			TileEntityChest tile = (TileEntityChest) this.world.getTileEntity(this.currentTarget);
			// TODO: let it stay open
			if (this.currentLootingTime >= 0) {
				this.currentLootingTime--;
				if (this.currentLootingTime % (this.LOOTING_DURATION / CQRConfig.mobs.looterAIStealableItems) == 0) {
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
						this.entity.swingArm(EnumHand.MAIN_HAND);
						this.entity.swingArm(EnumHand.OFF_HAND);
						ItemStack backpack = this.entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
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
				this.visitedChests.add(this.currentTarget);
			}
		} else {
			this.entity.getLookHelper().setLookPosition(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), 30, 30);
			if (!this.entity.hasPath()) {
				this.entity.getNavigator().tryMoveToXYZ(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), 1.125D);
			}
		}
	}

	private boolean isInLootingRange() {
		return this.entity.getDistanceSq(this.currentTarget) <= this.REQUIRED_DISTANCE_TO_LOOT;
	}

	protected boolean hasBackpack(EntityLiving living) {
		ItemStack chest = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		return chest != null && chest.getItem() instanceof ItemBackpack;
	}

	protected boolean isChestStillThere() {
		if (this.currentTarget == null) {
			return false;
		}
		if (this.visitedChests.contains(this.currentTarget)) {
			return false;
		}
		TileEntity tile = this.world.getTileEntity(this.currentTarget);
		return tile != null && tile instanceof TileEntityChest && !((TileEntityChest) tile).isEmpty();
	}

}
