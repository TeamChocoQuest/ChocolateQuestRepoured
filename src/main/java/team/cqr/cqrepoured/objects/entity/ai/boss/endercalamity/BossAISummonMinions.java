package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQREnderman;

public class BossAISummonMinions extends AbstractCQREntityAI<EntityCQREnderCalamity> {

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
		if (this.entity.hasAttackTarget() && this.entity.getCurrentPhase().getPhaseObject().canSummonAlliesDuringPhase()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (super.shouldContinueExecuting() && this.shouldExecute()) {
			if (this.entity.getHealth() <= (borderHPForMinions * this.entity.getMaxHealth())) {
				this.minionSpawnTick++;
				return (this.minionSpawnTick > this.borderMinion);
			}
		}
		return false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (this.minionSpawnTick < this.borderMinion) {
			return;
		}

		this.minionSpawnTick = 0;
		if (this.entity.getSummonedEntities().size() >= this.getMaxMinionsPerTime()) {
			this.borderMinion = 80;
			// Check list
			if (this.entity.filterSummonLists()) {
				this.borderMinion = 50;
			}
		} else {
			this.borderMinion = 160;

			double seed = 1 - this.entity.getHealth() / this.entity.getMaxHealth();
			seed *= 4;

			AbstractEntityCQR minion = this.getNewMinion((int) seed, this.world);
			BlockPos pos = this.entity.hasHomePositionCQR() ? this.entity.getHomePositionCQR() : this.entity.getPosition();
			pos = pos.add(-2 + this.entity.getRNG().nextInt(3), 0, -2 + this.entity.getRNG().nextInt(3));
			minion.setPosition(pos.getX(), pos.getY(), pos.getZ());
			this.entity.setSummonedEntityFaction(minion);
			minion.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(minion)), null);
			this.entity.addSummonedEntityToList(minion);
			world.spawnEntity(minion);
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

}
