package team.cqr.cqrepoured.entity.ai.boss.spectrelord;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntitySpectreLordIllusion;
import team.cqr.cqrepoured.faction.Faction;

import java.util.List;

public class EntityAISpectreLordIllusionHeal extends AbstractEntityAISpell<EntitySpectreLordIllusion> implements IEntityAISpellAnimatedVanilla {

	private LivingEntity target;

	public EntityAISpectreLordIllusionHeal(EntitySpectreLordIllusion entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 0);
		this.setup(false, false, false, false);
	}

	@Override
	public boolean shouldExecute() {
		if (!super.shouldExecute()) {
			return false;
		}
		AABB aabb = new AABB(this.entity.getX() - 24.0D, this.entity.getY() - 2.0D, this.entity.getZ() - 24.0D, this.entity.getX() + 24.0D, this.entity.getY() + this.entity.getBbHeight() + 2.0D, this.entity.getZ() + 24.0D);
		Faction faction = this.entity.getFaction();
		List<LivingEntity> alliesToHeal = this.world.getEntitiesOfClass(LivingEntity.class, aabb, e -> {
			if (e == this.entity) {
				return false;
			}
			if (e.getHealth() / e.getMaxHealth() > 0.9F) {
				return false;
			}
			if (faction == null || !faction.isAlly(e)) {
				return false;
			}
			return this.entity.hasLineOfSight(e);
		});
		return !alliesToHeal.isEmpty();
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.target = null;
	}

	@Override
	public void startChargingSpell() {
		super.startChargingSpell();
		AABB aabb = new AABB(this.entity.getX() - 24.0D, this.entity.getY() - 2.0D, this.entity.getZ() - 24.0D, this.entity.getX() + 24.0D, this.entity.getY() + this.entity.getBbHeight() + 2.0D, this.entity.getZ() + 24.0D);
		Faction faction = this.entity.getFaction();
		List<LivingEntity> alliesToHeal = this.world.getEntitiesOfClass(LivingEntity.class, aabb, e -> {
			if (e == this.entity) {
				return false;
			}
			if (e.getHealth() / e.getMaxHealth() > 0.9F) {
				return false;
			}
			if (!faction.isAlly(e)) {
				return false;
			}
			return this.entity.hasLineOfSight(e);
		});
		this.target = TargetUtil.getNearestEntity(this.entity, alliesToHeal);
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		this.target.heal(10.0F);
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public boolean ignoreWeight() {
		return false;
	}

	@Override
	public float getRed() {
		return 0.6F;
	}

	@Override
	public float getGreen() {
		return 0.6F;
	}

	@Override
	public float getBlue() {
		return 0.35F;
	}

}
