package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	private int borderMinion = 80;
	private float borderHPForMinions = 0.75F;

	public BossAISummonMinions(EntityCQREnderCalamity entity) {
		super(entity);
		this.setMutexBits(0);
	}

	@Override
	public int getMutexBits() {
		return 0;
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.hasAttackTarget() && super.shouldExecute()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.shouldExecute()) {
			if (this.entity.getHealth() <= (this.borderHPForMinions * this.entity.getMaxHealth())) {
				return true;// (this.minionSpawnTick > this.borderMinion);
			}
		}
		return false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (this.minionSpawnTick < this.borderMinion) {
			this.minionSpawnTick++;
			return;
		}

		this.minionSpawnTick = 0;
		if (this.entity.getSummonedEntities().size() >= this.getMaxMinionsPerTime()) {
			this.borderMinion = 100;
			// Check list
			//Returns true if there were (dead) entities removed from the list
			if (this.entity.filterSummonLists()) {
				this.borderMinion = 50;
			}
		} else {
			this.borderMinion = 80;

			double seed = 1 - this.entity.getHealth() / this.entity.getMaxHealth();
			seed *= DungeonGenUtils.percentageRandom(0.2, world.rand) ? 4 : 3;

			AbstractEntityCQR minion = this.getNewMinion((int) seed, this.world);
			BlockPos pos = this.entity.hasHomePositionCQR() ? this.entity.getHomePositionCQR() : this.entity.getPosition();
			pos = pos.add(-2 + this.entity.getRNG().nextInt(3), 0, -2 + this.entity.getRNG().nextInt(3));
			minion.setPosition(pos.getX(), pos.getY(), pos.getZ());
			this.entity.setSummonedEntityFaction(minion);
			
			if (DungeonGenUtils.percentageRandom(0.33, world.rand)) {
				minion.setItemStackToExtraSlot(EntityEquipmentExtraSlot.BADGE, this.generateBadgeWithPotion());
			}
			minion.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, ItemStack.EMPTY);
			
			if(minion instanceof EntityCQREnderman) {
				((EntityCQREnderman)minion).setMayTeleport(false);
			}
			this.entity.addSummonedEntityToList(minion);
			this.entity.tryEquipSummon(minion, this.world.rand);
			this.world.spawnEntity(minion);
		}
	}

	private int getMaxMinionsPerTime() {
		int absoluteMax = 3;
		absoluteMax += this.world.getDifficulty().getId();

		float hpPercentage = this.entity.getHealth() / this.entity.getMaxHealth();
		hpPercentage = 1F - hpPercentage;

		return Math.round(absoluteMax * hpPercentage);
	}

	private AbstractEntityCQR getNewMinion(int seed, World world) {
		AbstractEntityCQR entity = new EntityCQREnderman(world);
		entity.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entity)), null);
		switch (seed) {
		case 4:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(CQRItems.SHIELD_SKELETON_FRIENDS));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
			entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
			break;
		case 3:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
			entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
			break;
		case 2:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			break;
		case 1:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			break;
		}

		return entity;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase.getPhaseObject().canSummonAlliesDuringPhase();
	}

	private ItemStack generateBadgeWithPotion() {
		ItemStack stack = new ItemStack(CQRItems.BADGE, 1);

		IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		inventory.insertItem(0, new ItemStack(CQRItems.POTION_HEALING, 1), false);

		return stack;
	}

}
