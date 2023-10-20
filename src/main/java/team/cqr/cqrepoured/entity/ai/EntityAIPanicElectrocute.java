package team.cqr.cqrepoured.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRPotions;

public class EntityAIPanicElectrocute extends PanicGoal {

	public EntityAIPanicElectrocute(PathfinderMob creature, double speedIn) {
		super(creature, speedIn);
	}

	@Override
	public boolean canUse() {
		if (this.mob instanceof IMechanical || this.mob.getMobType() == CQRCreatureAttributes.MECHANICAL) {
			return false;
		}

		if (this.mob.hasEffect(CQRPotions.ELECTROCUTION.get())) {
			return this.findRandomPosition();
		}
		
		return false;
	}

}
