package team.cqr.cqrepoured.entity.boss.endercalamity.phases;

import team.cqr.cqrepoured.entity.boss.endercalamity.IEnderCalamityPhase;

import javax.annotation.Nullable;

public enum EEnderCalamityPhase {

	PHASE_NO_TARGET(new ECPhaseNoTarget()),
	PHASE_IDLE(new ECPhaseIdle()),
	PHASE_TELEPORT_LASER(new ECPhaseTeleportLaser()),
	PHASE_TELEPORT_EYE_THROWER(new ECPhaseTeleportEyeThrower()),
	PHASE_ENERGY_TENNIS(new ECPhaseEnergyTennis()),
	PHASE_LASERING(new ECPhaseLasering()),
	PHASE_STUNNED(new ECPhaseStunned()),
	PHASE_DYING(new ECPhaseDying()),
	PHASE_BUILDING(new ECPhaseBuilding());

	private IEnderCalamityPhase phaseObject;

	EEnderCalamityPhase(final IEnderCalamityPhase phaseObject) {
		this.phaseObject = phaseObject;
	}

	public final IEnderCalamityPhase getPhaseObject() {
		return this.phaseObject;
	}

	@Nullable
	public static EEnderCalamityPhase getByPhaseObject(final IEnderCalamityPhase phase) {
		for (EEnderCalamityPhase ephase : values()) {
			if (ephase.getPhaseObject() == phase) {
				return ephase;
			}
		}
		return null;
	}

}
