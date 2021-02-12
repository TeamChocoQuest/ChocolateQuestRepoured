package team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases;

import team.cqr.cqrepoured.objects.entity.boss.endercalamity.IEnderCalamityPhase;

public class ECPhaseLasering implements IEnderCalamityPhase {

	ECPhaseLasering() {
	}

	@Override
	public boolean canRandomTeleportDuringPhase() {
		return false;
	}

	@Override
	public boolean canSummonAlliesDuringPhase() {
		return true;
	}

	@Override
	public boolean canPickUpBlocksDuringPhase() {
		return false;
	}

	@Override
	public boolean canThrowBlocksDuringPhase() {
		return true;
	}

	@Override
	public IEnderCalamityPhase[] getPossibleSuccessors() {
		return new IEnderCalamityPhase[] {EEnderCalamityPhases.PHASE_IDLE.getPhaseObject(), EEnderCalamityPhases.PHASE_ENERGY_TENNIS.getPhaseObject()};
	}

}
