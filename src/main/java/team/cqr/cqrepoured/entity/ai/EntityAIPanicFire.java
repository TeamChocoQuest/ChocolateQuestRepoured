package team.cqr.cqrepoured.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import team.cqr.cqrepoured.config.CQRConfig;

public class EntityAIPanicFire extends EntityAIPanic {

	public EntityAIPanicFire(EntityCreature creature, double speedIn) {
		super(creature, speedIn);
	}

	@Override
	public boolean shouldExecute() {
		if (CQRConfig.mobs.disableFirePanicAI) {
			return false;
		}
		if (this.creature.isImmuneToFire()) {
			return false;
		}
		if (this.creature.isInLava() || this.creature.isBurning()) {
			return this.findRandomPosition();
		}
		return false;
	}

}
