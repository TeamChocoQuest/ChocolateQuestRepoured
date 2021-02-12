package team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases;

import team.cqr.cqrepoured.objects.entity.boss.endercalamity.IEnderCalamityPhase;

public enum EEnderCalamityPhases {
	
	PHASE_NO_TARGET(new ECPhaseNoTarget()),
	PHASE_IDLE(new ECPhaseIdle()),
	PHASE_TELEPORT_LASER(new ECPhaseTeleportLaser()),
	PHASE_TELEPORT_EYE_THROWER(new ECPhaseTeleportEyeThrower()),
	PHASE_ENERGY_TENNIS(new ECPhaseEnergyTennis()),
	PHASE_LASERING(new ECPhaseLasering()),
	PHASE_STUNNED(new ECPhaseStunned()),
	PHASE_DYING(new ECPhaseDying());
	
	private IEnderCalamityPhase phaseObject;
	
	EEnderCalamityPhases(final IEnderCalamityPhase phaseObject) {
		this.phaseObject = phaseObject;
	}
	
	public final IEnderCalamityPhase getPhaseObject() {
		return this.phaseObject;
	}

}
