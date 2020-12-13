package team.cqr.cqrepoured.objects.entity.ai.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.util.EntityUtil;

public class EntityAIAntiAirSpellWalker extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAIAntiAirSpellWalker(AbstractEntityCQR entity) {
		super(entity, 300, 20, 1);
		this.setup(true, true, true, false);
	}

	@Override
	public boolean shouldExecute() {
		if (this.random.nextBoolean()) {
			return false;
		}
		if (!super.shouldExecute()) {
			return false;
		}
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		CQRFaction faction = this.entity.getFaction();
		if ((faction != null && faction.isAlly(attackTarget)) || this.entity.getLeader() == attackTarget) {
			return false;
		}
		if (attackTarget.isRiding()) {
			Entity entity = attackTarget.getLowestRidingEntity();
			if (entity instanceof EntityLivingBase) {
				attackTarget = (EntityLivingBase) entity;
			}
		}
		return EntityUtil.isEntityFlying(attackTarget);
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		EntityColoredLightningBolt coloredLightningBolt = new EntityColoredLightningBolt(this.world, attackTarget.posX, attackTarget.posY, attackTarget.posZ, true, false, 0.8F, 0.35F, 0.1F, 0.3F);
		this.world.spawnEntity(coloredLightningBolt);
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
		return 0.1F;
	}

	@Override
	public float getGreen() {
		return 0.9F;
	}

	@Override
	public float getBlue() {
		return 0.8F;
	}

}
