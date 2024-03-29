package team.cqr.cqrepoured.entity.boss.endercalamity.phases;

import team.cqr.cqrepoured.entity.boss.endercalamity.IEnderCalamityPhase;

public class ECPhaseEnergyTennis implements IEnderCalamityPhase {

	ECPhaseEnergyTennis() {
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
		return new IEnderCalamityPhase[] { EEnderCalamityPhase.PHASE_STUNNED.getPhaseObject() };
	}

	@Override
	public boolean isPhaseTimed() {
		return false;
	}

}
