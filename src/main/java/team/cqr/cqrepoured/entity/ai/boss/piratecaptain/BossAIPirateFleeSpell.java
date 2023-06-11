package team.cqr.cqrepoured.entity.ai.boss.piratecaptain;

import java.util.List;

import org.joml.Vector3d;

import com.google.common.base.Predicate;

import net.minecraft.entity.MobEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateCaptain;

public class BossAIPirateFleeSpell extends AbstractEntityAISpell<EntityCQRPirateCaptain> implements IEntityAISpellAnimatedVanilla {

	protected final Predicate<MobEntity> predicateAlly = input -> {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(input)) {
			return false;
		}
		if (!EntityPredicates.LIVING_ENTITY_STILL_ALIVE.test(input)) {
			return false;
		}
		return BossAIPirateFleeSpell.this.isSuitableAlly(input);
	};

	public BossAIPirateFleeSpell(EntityCQRPirateCaptain entity, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, cooldown, chargingTicks, castingTicks);
		this.setup(true, true, true, false);
	}

	@Override
	public boolean shouldExecute() {
		if (super.shouldExecute() && this.entity.getHealingPotions() >= 1 && this.entity.getHealth() / this.entity.getMaxHealth() <= 0.2) {
			return this.hasNearbyAllies();
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.hasNearbyAllies();
	}

	private boolean hasNearbyAllies() {
		Vector3d vec = new Vector3d(CQRConfig.SERVER_CONFIG.bosses.pirateCaptainFleeCheckRadius.get(), 0.5 * CQRConfig.SERVER_CONFIG.bosses.pirateCaptainFleeCheckRadius.get(), CQRConfig.SERVER_CONFIG.bosses.pirateCaptainFleeCheckRadius.get());
		Vector3d v1 = this.entity.position().add(vec);
		Vector3d v2 = this.entity.position().subtract(vec);
		AxisAlignedBB aabb = new AxisAlignedBB(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);

		List<MobEntity> allies = this.entity.level.getEntitiesOfClass(MobEntity.class, aabb, this.predicateAlly);
		return !allies.isEmpty();
	}

	private int getNearbyAllies(MobEntity o1) {
		Vector3d vec = new Vector3d(4, 2, 4);
		Vector3d v1 = o1.position().add(vec);
		Vector3d v2 = o1.position().subtract(vec);
		AxisAlignedBB aabb = new AxisAlignedBB(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
		return o1.level.getEntitiesOfClass(MobEntity.class, aabb, this.predicateAlly).size();
	}

	@Override
	public void castSpell() {
		super.castSpell();

		Vector3d vec = new Vector3d(CQRConfig.SERVER_CONFIG.bosses.pirateCaptainFleeCheckRadius.get(), 0.5 * CQRConfig.SERVER_CONFIG.bosses.pirateCaptainFleeCheckRadius.get(), CQRConfig.SERVER_CONFIG.bosses.pirateCaptainFleeCheckRadius.get());
		Vector3d v1 = this.entity.position().add(vec);
		Vector3d v2 = this.entity.position().subtract(vec);
		AxisAlignedBB aabb = new AxisAlignedBB(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);

		List<MobEntity> allies = this.entity.level.getEntitiesOfClass(MobEntity.class, aabb, this.predicateAlly);
		allies.sort((o1, o2) -> {
			int entCount1 = BossAIPirateFleeSpell.this.getNearbyAllies(o1);
			int entCount2 = BossAIPirateFleeSpell.this.getNearbyAllies(o2);
			return entCount2 - entCount1;
		});
		Vector3d p = allies.get(0).position();
		this.entity.randomTeleport(p.x, p.y, p.z, true); //OLD: attemptTeleport
	}

	@Override
	public int getWeight() {
		return 2;
	}

	@Override
	public boolean ignoreWeight() {
		return true;
	}

	@Override
	public float getRed() {
		return 0;
	}

	@Override
	public float getGreen() {
		return 1;
	}

	@Override
	public float getBlue() {
		return 0;
	}

	protected boolean isSuitableAlly(MobEntity possibleAlly) {
		if (possibleAlly == this.entity) {
			return false;
		}
		if (!this.entity.getFaction().isAlly(possibleAlly)) {
			return false;
		}
		Path path = possibleAlly.getNavigation().createPath(this.entity, 1 /* accuracy */);
		return path != null && path.getNodeCount() <= 10;
	}

}
