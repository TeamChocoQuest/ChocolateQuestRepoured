package team.cqr.cqrepoured.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

public class EntityAIPanicElectrocute extends PanicGoal {

	public EntityAIPanicElectrocute(CreatureEntity creature, double speedIn) {
		super(creature, speedIn);
	}

	@Override
	public boolean canUse() {
		if (this.mob instanceof IMechanical || this.mob.getMobType() == CQRCreatureAttributes.MECHANICAL) {
			return false;
		}

		if (this.mob.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).isPresent()) {
			return this.mob.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).resolve().get().isElectrocutionActive() && this.findRandomPosition();
		}
		return false;
	}

}
