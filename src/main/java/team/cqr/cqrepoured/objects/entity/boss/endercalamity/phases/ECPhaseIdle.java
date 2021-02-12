package team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases;

import team.cqr.cqrepoured.objects.entity.boss.endercalamity.IEnderCalamityPhase;

public final class ECPhaseIdle implements IEnderCalamityPhase {

	
	ECPhaseIdle() {
	}

	@Override
	public boolean canRandomTeleportDuringPhase() {
		return true;
	}

	@Override
	public boolean canSummonAlliesDuringPhase() {
		return true;
	}

	@Override
	public boolean canPickUpBlocksDuringPhase() {
		return true;
	}

	@Override
	public boolean canThrowBlocksDuringPhase() {
		return true;
	}

	@Override
	public IEnderCalamityPhase[] getPossibleSuccessors() {
		return new IEnderCalamityPhase[] {EEnderCalamityPhases.PHASE_LASERING.getPhaseObject(), EEnderCalamityPhases.PHASE_ENERGY_TENNIS.getPhaseObject(), EEnderCalamityPhases.PHASE_TELEPORT_EYE_THROWER.getPhaseObject(), EEnderCalamityPhases.PHASE_TELEPORT_LASER.getPhaseObject()};
	}

}
