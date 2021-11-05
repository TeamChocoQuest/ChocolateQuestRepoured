package team.cqr.cqrepoured.objects.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.bases.ISummoner;
import team.cqr.cqrepoured.objects.items.ItemCursedBone;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

// TODO: Make entity strafe backwards (like with the bow) while they are casting
public class EntityAICursedBoneSummoner extends AbstractCQREntityAI<AbstractEntityCQR> implements ISummoner {

	private List<Entity> summonedEntities = new ArrayList<>();

	private static final int MAX_COOLDOWN = 90;
	private static final int MIN_COOLDOWN = 20;

	private int cooldown = 20;
	private int chargingTicks = 20;

	private final EnumHand boneHand;

	public EntityAICursedBoneSummoner(AbstractEntityCQR entity, EnumHand boneHand) {
		super(entity);
		this.boneHand = boneHand;
		this.setMutexBits(0);
	}

	private boolean hasCursedBone() {
		return (this.entity.getHeldItem(this.boneHand).getItem() instanceof ItemCursedBone);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.hasAttackTarget()) {
			return false;
		}
		if (hasCursedBone()) {
			if (this.cooldown > 0) {
				this.cooldown--;

				return false;
			}
			this.filterSummons();

			return this.summonedEntities.size() < this.getMaxSummonedEntities();
		}
		return false;
	}

	private int getMaxSummonedEntities() {
		switch (this.world.getDifficulty()) {
		case HARD:
			return 12;
		case NORMAL:
			return 9;
		default:
			return 6;
		}
	}

	private void filterSummons() {
		if (this.summonedEntities.isEmpty()) {
			return;
		}
		this.summonedEntities.removeIf(new Predicate<Entity>() {

			@Override
			public boolean test(Entity t) {
				return t == null || (t != null && (t.isDead || !t.isEntityAlive()));
			}
		});
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.chargingTicks >= 0;
	}

	// TODO: Add some magic sounds...
	@Override
	public void updateTask() {
		this.chargingTicks--;
		super.updateTask();

		this.entity.swingArm(this.boneHand);

		if (this.chargingTicks < 0) {
			int mobCount = (int) Math.ceil(this.getMaxSummonedEntities() / 2D);
			int remainingEntitySlots = this.getMaxSummonedEntities() - this.summonedEntities.size();

			mobCount = Math.min(mobCount, remainingEntitySlots);

			if (mobCount > 0) {
				Vec3d vector = this.entity.getLookVec().normalize().scale(3);
				Item cbTmp = this.entity.getHeldItem(this.boneHand).getItem();
				if (cbTmp != null && cbTmp instanceof ItemCursedBone) {
					ItemCursedBone cursedBone = (ItemCursedBone) cbTmp;
					for (int i = 0; i < mobCount; i++) {
						Vec3d posV = this.entity.getPositionVector().add(vector);
						BlockPos pos = new BlockPos(posV.x, posV.y, posV.z);
						Optional<Entity> circle = cursedBone.spawnEntity(pos, this.world, this.entity.getHeldItem(this.boneHand), this.entity, this);
						if (circle.isPresent()) {
							this.summonedEntities.add(circle.get());
							vector = VectorUtil.rotateVectorAroundY(vector, 360 / mobCount);
						}
					}
				}
			}
		}
	}

	@Override
	public void resetTask() {
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRNG());
		this.chargingTicks = 20;
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

	// TODO: Integrate with looter, so when he summons something, he will equip the entity with gear from his backpack
	@Override
	public void addSummonedEntityToList(Entity summoned) {
		this.summonedEntities.add(summoned);
	}

}
