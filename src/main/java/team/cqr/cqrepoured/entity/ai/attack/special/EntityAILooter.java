package team.cqr.cqrepoured.entity.ai.attack.special;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.armor.ItemBackpack;
import team.cqr.cqrepoured.util.BlockPosUtil;

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
		//this.setMutexBits(2);
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.cooldown > 0) {
			this.cooldown--;
		}
		if (this.cooldown > 0) {
			return false;
		}
		if (this.entity.tickCount % 4 == 0) {
			if (!this.hasBackpack(this.entity)) {
				return false;
			}
			if (!this.hasBackpackSpace()) {
				return false;
			}
			BlockPos pos = this.entity.blockPosition();
			Vec3 vec = this.entity.getEyePosition(1.0F);
			int horizontalRadius = CQRConfig.SERVER_CONFIG.mobs.looterAIChestSearchRange.get();
			int verticalRadius = horizontalRadius >> 1;
			this.currentTarget = BlockPosUtil.getNearest(this.world, pos.getX(), pos.getY() + (Mth.ceil(this.entity.getBbHeight()) >> 1), pos.getZ(), horizontalRadius, verticalRadius, true, true, Blocks.CHEST, (mutablePos, state) -> {
				if (this.visitedChests.contains(mutablePos)) {
					return false;
				}
				BlockEntity te = this.world.getBlockEntity(mutablePos);
				if (!(te instanceof ChestBlockEntity) || ((ChestBlockEntity) te).isEmpty()) {
					return false;
				}
				ClipContext rtc = new ClipContext(vec, new Vec3(mutablePos.getX() + 0.5D, mutablePos.getY() + 0.5D, mutablePos.getZ() + 0.5D), Block.COLLIDER, Fluid.ANY, null);
				HitResult result = this.world.clip(rtc);//this.world.rayTraceBlocks(vec, new Vector3d(mutablePos.getX() + 0.5D, mutablePos.getY() + 0.5D, mutablePos.getZ() + 0.5D), false, true, false);
				MutableBlockPos bp = BlockPos.containing(result.getLocation()).mutable();
				return result == null || bp.equals(mutablePos);
			});
		}

		return this.currentTarget != null;
	}

	private boolean hasBackpackSpace() {
		ItemStack backpack = this.entity.getItemBySlot(EquipmentSlot.CHEST);
		LazyOptional<IItemHandler> lOpCap = backpack.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
		if(lOpCap.isPresent()) {
			IItemHandler inventory = lOpCap.resolve().get();
			for (int i = 0; i < inventory.getSlots(); i++) {
				if (inventory.getStackInSlot(i).isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void start() {
		super.start();
		this.currentLootingTime = this.LOOTING_DURATION;
		this.entity.getNavigation().moveTo(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), 1.125D);
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.isChestStillThere() && this.hasBackpack(this.entity) && this.hasBackpackSpace();
	}

	@Override
	public void tick() {
		super.tick();

		if (this.entity.getNavigation().createPath(this.currentTarget, 1) == null) {
			this.visitedChests.add(this.currentTarget);
			this.currentTarget = null;
			return;
		}

		if (this.isInLootingRange()) {
			this.entity.getNavigation().stop();
			ChestBlockEntity tile = (ChestBlockEntity) this.world.getBlockEntity(this.currentTarget);
			// TODO: let it stay open
			if (this.currentLootingTime >= 0) {
				this.currentLootingTime--;
				if (this.currentLootingTime % (this.LOOTING_DURATION / CQRConfig.SERVER_CONFIG.mobs.looterAIStealableItems.get()) == 0) {
					ItemStack stolenItem = null;
					for (int i = 0; i < tile.getContainerSize(); i++) {
						if (!tile.getItem(i).isEmpty() && tile.getItem(i).getItem() != Items.BREAD) {
							stolenItem = tile.removeItemNoUpdate(i);
							//Suggestion from slayer and i liked it: Leave behind bread
							ItemStack bread = Items.BREAD.getDefaultInstance();
							bread.setCount(1);
							tile.setItem(i, bread);
							break;
						}
					}
					if (stolenItem != null) {
						tile.setChanged();
						this.entity.swing(InteractionHand.MAIN_HAND);
						this.entity.swing(InteractionHand.OFF_HAND);
						ItemStack backpack = this.entity.getItemBySlot(EquipmentSlot.CHEST);
						LazyOptional<IItemHandler> lOpCap = backpack.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
						if(lOpCap.isPresent()) {
							IItemHandler inventory = lOpCap.resolve().get();
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
			this.entity.getLookControl().setLookAt(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), 30, 30);
			if (!this.entity.isPathFinding()) {
				this.entity.getNavigation().moveTo(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), 1.125D);
			}
		}
	}

	private boolean isInLootingRange() {
		return this.entity.blockPosition().distSqr(this.currentTarget) <= this.REQUIRED_DISTANCE_TO_LOOT;
	}

	protected boolean hasBackpack(Mob living) {
		ItemStack chest = living.getItemBySlot(EquipmentSlot.CHEST);
		return chest != null && chest.getItem() instanceof ItemBackpack;
	}

	protected boolean isChestStillThere() {
		if (this.currentTarget == null) {
			return false;
		}
		if (this.visitedChests.contains(this.currentTarget)) {
			return false;
		}
		BlockEntity tile = this.world.getBlockEntity(this.currentTarget);
		return tile != null && tile instanceof ChestBlockEntity && !((ChestBlockEntity) tile).isEmpty();
	}

}
