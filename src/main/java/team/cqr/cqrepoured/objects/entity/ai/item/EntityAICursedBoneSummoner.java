package team.cqr.cqrepoured.objects.entity.ai.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.bases.ISummoner;
import team.cqr.cqrepoured.objects.items.ItemCursedBone;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

// TODO: Make entity strafe backwards (like with the bow) while they are casting
public class EntityAICursedBoneSummoner extends AbstractCQREntityAI<AbstractEntityCQR> implements ISummoner {

	private List<Entity> summonedEntities = new ArrayList<>();

	private static final int SUMMONS_PER_CAST = 2;
	private static final int MAX_COOLDOWN = 300;
	private static final int MIN_COOLDOWN = 200;

	private int prevTimeUsed;
	private int cooldown = 20;
	private int chargingTicks = 20;

	public EntityAICursedBoneSummoner(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(0);
	}

	private boolean hasCursedBone() {
		return this.entity.getHeldItemMainhand().getItem() instanceof ItemCursedBone || this.entity.getHeldItemOffhand().getItem() instanceof ItemCursedBone;
	}

	@Override
	public boolean shouldExecute() {
		this.filterSummons();
		if (!this.hasCursedBone()) {
			return false;
		}
		if (!this.entity.hasAttackTarget()) {
			return false;
		}
		if (this.entity.ticksExisted - this.prevTimeUsed < this.cooldown) {
			return false;
		}
		return this.summonedEntities.size() < this.getMaxSummonedEntities();
	}

	private int getMaxSummonedEntities() {
		switch (this.world.getDifficulty()) {
		case HARD:
			return 6;
		case NORMAL:
			return 5;
		default:
			return 4;
		}
	}

	private void filterSummons() {
		if (this.summonedEntities.isEmpty()) {
			return;
		}
		this.summonedEntities.removeIf(e -> !e.isEntityAlive());
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.hasCursedBone()) {
			return false;
		}
		return this.chargingTicks >= 0;
	}

	// TODO: Add some magic sounds...
	@Override
	public void updateTask() {
		this.chargingTicks--;
		super.updateTask();

		ItemStack stack = this.entity.getHeldItemMainhand();
		if (!(stack.getItem() instanceof ItemCursedBone)) {
			stack = this.entity.getHeldItemOffhand();
			if (!(stack.getItem() instanceof ItemCursedBone)) {
				return;
			} else {
				this.entity.swingArm(EnumHand.OFF_HAND);
			}
		} else {
			this.entity.swingArm(EnumHand.MAIN_HAND);
		}

		if (this.chargingTicks < 0) {
			int remainingEntitySlots = this.getMaxSummonedEntities() - this.summonedEntities.size();
			int mobCount = Math.min(SUMMONS_PER_CAST, remainingEntitySlots);

			if (mobCount > 0) {
				Vec3d vector = this.entity.getLookVec().normalize().scale(3);
				ItemCursedBone cursedBone = (ItemCursedBone) stack.getItem();
				for (int i = 0; i < mobCount; i++) {
					Vec3d posV = this.entity.getPositionVector().add(vector);
					BlockPos pos = new BlockPos(posV.x, posV.y, posV.z);
					Optional<Entity> circle = cursedBone.spawnEntity(pos, this.world, stack, this.entity, this);
					if (circle.isPresent()) {
						this.summonedEntities.add(circle.get());
						vector = VectorUtil.rotateVectorAroundY(vector, 360 / mobCount);
					}
				}
			}
		}
	}

	@Override
	public void resetTask() {
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRNG());
		this.chargingTicks = 20;
		this.prevTimeUsed = this.entity.ticksExisted;
		super.resetTask();
	}

	@Override
	public CQRFaction getSummonerFaction() {
		return this.entity.getFaction();
	}

	@Override
	public List<Entity> getSummonedEntities() {
		return this.summonedEntities;
	}

	@Override
	public EntityLivingBase getSummoner() {
		return this.entity;
	}

	// TODO: Integrate with looter, so when he summons something, he will equip the
	// entity with gear from his backpack
	@Override
	public void addSummonedEntityToList(Entity summoned) {
		this.summonedEntities.add(summoned);
		if (summoned instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) summoned;

			int material = this.world.rand.nextInt(3); // wood, stone, iron
			int weapon = this.world.rand.nextInt(4); // sword, pickaxe, axe, shovel
			ItemStack stack = ItemStack.EMPTY;
			if (material == 0) {
				if (weapon == 0) {
					stack = new ItemStack(Items.WOODEN_SWORD);
				} else if (weapon == 1) {
					stack = new ItemStack(Items.WOODEN_PICKAXE);
				} else if (weapon == 2) {
					stack = new ItemStack(Items.WOODEN_AXE);
				} else if (weapon == 3) {
					stack = new ItemStack(Items.WOODEN_SHOVEL);
				}
			} else if (material == 1) {
				if (weapon == 0) {
					stack = new ItemStack(Items.STONE_SWORD);
				} else if (weapon == 1) {
					stack = new ItemStack(Items.STONE_PICKAXE);
				} else if (weapon == 2) {
					stack = new ItemStack(Items.STONE_AXE);
				} else if (weapon == 3) {
					stack = new ItemStack(Items.STONE_SHOVEL);
				}
			} else if (material == 2) {
				if (weapon == 0) {
					stack = new ItemStack(Items.IRON_SWORD);
				} else if (weapon == 1) {
					stack = new ItemStack(Items.IRON_PICKAXE);
				} else if (weapon == 2) {
					stack = new ItemStack(Items.IRON_AXE);
				} else if (weapon == 3) {
					stack = new ItemStack(Items.IRON_SHOVEL);
				}
			}
			living.setHeldItem(EnumHand.MAIN_HAND, stack);
		}
	}

}
