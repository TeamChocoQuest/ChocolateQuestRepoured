package team.cqr.cqrepoured.objects.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;

public class EntityAIPanicFire extends EntityAIPanic {

	public EntityAIPanicFire(EntityCreature creature, double speedIn) {
		super(creature, speedIn);
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.creature.isImmuneToFire()) {
			return false;
		}
		if(this.creature.isInLava() || this.creature.isBurning()) {
			return this.findRandomPosition();
		}
		return false;
	}

}
