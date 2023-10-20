package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class BossAISummonMinions extends AbstractBossAIEnderCalamity {
	
	//TODO: Change it to the following
	// - minions spawn in multiple waves, waves begin once the previous one has been cleared
	// - wave count = 3 + 3 * (1- boss HP percent)
	// - entity count per wave = difficultyID * nearbyPlayers * wave
	// - equipment loadout: Gets better with wave count

	private int minionSpawnTick = 0;
	private int borderMinion = 80;
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
			this.borderMinion = 100;
			// Check list
			//Returns true if there were (dead) entities removed from the list
			if (this.entity.filterSummonLists()) {
				this.borderMinion = 50;
			}
		} else {
			this.borderMinion = 80;

			double seed = 1 - this.entity.getHealth() / this.entity.getMaxHealth();
			seed *= DungeonGenUtils.percentageRandom(0.2, world.random) ? 4 : 3;

			AbstractEntityCQR minion = this.getNewMinion((int) seed, this.world);
			BlockPos pos = this.entity.hasHomePositionCQR() ? this.entity.getHomePositionCQR() : this.entity.blockPosition();
			pos = pos.offset(-2 + this.entity.getRandom().nextInt(3), 0, -2 + this.entity.getRandom().nextInt(3));
			minion.setPos(pos.getX(), pos.getY(), pos.getZ());
			this.entity.setSummonedEntityFaction(minion);
			
			if (DungeonGenUtils.percentageRandom(0.33, world.random)) {
				minion.setItemStackToExtraSlot(EntityEquipmentExtraSlot.BADGE, this.generateBadgeWithPotion());
			}
			minion.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, ItemStack.EMPTY);
			
			if(minion instanceof EntityCQREnderman) {
				((EntityCQREnderman)minion).setMayTeleport(false);
			}

			this.entity.addSummonedEntityToList(minion);
			this.entity.tryEquipSummon(minion, this.world.random);
			this.world.addFreshEntity(minion);
		}
	}

	private int getMaxMinionsPerTime() {
		int absoluteMax = 3;
		absoluteMax += this.world.getDifficulty().getId();

		float hpPercentage = this.entity.getHealth() / this.entity.getMaxHealth();
		hpPercentage = 1F - hpPercentage;

		return Math.round(absoluteMax * hpPercentage);
	}

	private AbstractEntityCQR getNewMinion(int seed, Level world) {
		AbstractEntityCQR entity = new EntityCQREnderman(world);
		entity.finalizeSpawn((ServerLevel) this.world, this.world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.REINFORCEMENT, null, null);
		switch (seed) {
		case 4:
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			entity.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(CQRItems.SHIELD_SKELETON_FRIENDS.get()));
			entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
			entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
			entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
			break;
		case 3:
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
			entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
			break;
		case 2:
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			break;
		case 1:
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			break;
		}

		return entity;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase.getPhaseObject().canSummonAlliesDuringPhase();
	}

	private ItemStack generateBadgeWithPotion() {
		ItemStack stack = new ItemStack(CQRItems.BADGE.get(), 1);

		LazyOptional<IItemHandler> lOpCap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
		if(lOpCap.isPresent()) {
			IItemHandler inventory = lOpCap.resolve().get();
			inventory.insertItem(0, new ItemStack(CQRItems.POTION_HEALING.get(), 1), false);
		}

		return stack;
	}

}
