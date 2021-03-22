package team.cqr.cqrepoured.objects.entity.ai.boss.spectrelord;

import team.cqr.cqrepoured.objects.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntitySpectreLordIllusion;

public class EntityAISpectreLordIllusionExplosion extends AbstractEntityAISpell<EntitySpectreLordIllusion> implements IEntityAISpellAnimatedVanilla {

	public EntityAISpectreLordIllusionExplosion(EntitySpectreLordIllusion entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 0);
		this.setup(true, true, true, false);
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		// TODO summon entity which creates a small explosion after a few ticks
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
