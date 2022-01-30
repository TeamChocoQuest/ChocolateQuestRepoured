package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import java.util.EnumSet;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class BossAISummonMinions extends AbstractBossAIEnderCalamity {

	private int minionSpawnTick = 0;
	private int borderMinion = 20;
	private float borderHPForMinions = 0.75F;

	public BossAISummonMinions(EntityCQREnderCalamity entity) {
		super(entity);
		//this.setMutexBits(0);
		this.setFlags(EnumSet.noneOf(Flag.class));
	}

	/*@Override
	public int getMutexBits() {
		return 0;
	}*/

	@Override
	public boolean canUse() {
		if (this.entity.hasAttackTarget() && super.canUse()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.canUse()) {
			if (this.entity.getHealth() <= (this.borderHPForMinions * this.entity.getMaxHealth())) {
				return true;// (this.minionSpawnTick > this.borderMinion);
			}
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.minionSpawnTick < this.borderMinion) {
			this.minionSpawnTick++;
			return;
		}

		this.minionSpawnTick = 0;
		if (this.entity.getSummonedEntities().size() >= this.getMaxMinionsPerTime()) {
			this.borderMinion = 30;
			// Check list
			if (this.entity.filterSummonLists()) {
				this.borderMinion = 10;
			}
		} else {
			this.borderMinion = 60;

			double seed = 1 - this.entity.getHealth() / this.entity.getMaxHealth();
			seed *= 4;

			AbstractEntityCQR minion = this.getNewMinion((int) seed, this.world);
			BlockPos pos = this.entity.hasHomePositionCQR() ? this.entity.getHomePositionCQR() : this.entity.blockPosition();
			pos = pos.offset(-2 + this.entity.getRandom().nextInt(3), 0, -2 + this.entity.getRandom().nextInt(3));
			minion.setPos(pos.getX(), pos.getY(), pos.getZ());
			this.entity.setSummonedEntityFaction(minion);
			minion.onInitialSpawn(this.world.getCurrentDifficultyAt(new BlockPos(minion)), null);
			this.entity.addSummonedEntityToList(minion);
			this.entity.tryEquipSummon(minion, this.world.random);
			this.world.addFreshEntity(minion);
		}
	}

	private int getMaxMinionsPerTime() {
		int absoluteMax = 5;
		absoluteMax += this.world.getDifficulty().getId();

		float hpPercentage = this.entity.getHealth() / this.entity.getMaxHealth();
		hpPercentage = 1F - hpPercentage;

		return Math.round(absoluteMax * hpPercentage);
	}

	private AbstractEntityCQR getNewMinion(int seed, World world) {
		AbstractEntityCQR entity = new EntityCQREnderman(world);
		switch (seed) {
		case 4:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			entity.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(CQRItems.SHIELD_SKELETON_FRIENDS));
			entity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.DIAMOND_HELMET));
			entity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
			entity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
			entity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
			break;
		case 3:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			entity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
			entity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			entity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
			entity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
			break;
		case 2:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
			entity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
			entity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			break;
		case 1:
			entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
			break;
		}

		if (DungeonGenUtils.percentageRandom(0.33, world.random)) {
			entity.setItemStackToExtraSlot(EntityEquipmentExtraSlot.BADGE, this.generateBadgeWithPotion());
		}

		return entity;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase.getPhaseObject().canSummonAlliesDuringPhase();
	}

	private ItemStack generateBadgeWithPotion() {
		ItemStack stack = new ItemStack(CQRItems.BADGE, 1);

		LazyOptional<IItemHandler> lOpCap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(lOpCap.isPresent()) {
			IItemHandler inventory = lOpCap.resolve().get();
			inventory.insertItem(0, new ItemStack(CQRItems.POTION_HEALING, 1), false);
		}

		return stack;
	}

}
