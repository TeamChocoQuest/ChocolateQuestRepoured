package team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases;

import team.cqr.cqrepoured.objects.entity.boss.endercalamity.IEnderCalamityPhase;

public class ECPhaseStunned implements IEnderCalamityPhase {
	
	ECPhaseStunned() {
	}

	@Override
	public boolean canRandomTeleportDuringPhase() {
		return false;
	}

	@Override
	public boolean canSummonAlliesDuringPhase() {
		return false;
	}

	@Override
	public boolean canPickUpBlocksDuringPhase() {
		return false;
	}

	@Override
	public boolean canThrowBlocksDuringPhase() {
		return false;
	}

	@Override
	public IEnderCalamityPhase[] getPossibleSuccessors() {
		return new IEnderCalamityPhase[] {EEnderCalamityPhases.PHASE_TELEPORT_LASER.getPhaseObject(), EEnderCalamityPhases.PHASE_IDLE.getPhaseObject(), EEnderCalamityPhases.PHASE_LASERING.getPhaseObject()};
	}

}
