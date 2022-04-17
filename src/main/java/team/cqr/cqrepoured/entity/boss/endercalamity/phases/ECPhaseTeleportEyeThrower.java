package team.cqr.cqrepoured.entity.boss.endercalamity.phases;

import team.cqr.cqrepoured.entity.boss.endercalamity.IEnderCalamityPhase;
import team.cqr.cqrepoured.util.DungeonGenUtils;

import java.util.Optional;

public class ECPhaseTeleportEyeThrower implements IEnderCalamityPhase {

	ECPhaseTeleportEyeThrower() {
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
		return true;
	}

	@Override
	public boolean canThrowBlocksDuringPhase() {
		return true;
	}

	@Override
	public IEnderCalamityPhase[] getPossibleSuccessors() {
		return new IEnderCalamityPhase[] { EEnderCalamityPhase.PHASE_BUILDING.getPhaseObject(), EEnderCalamityPhase.PHASE_IDLE.getPhaseObject() };
	}

	@Override
	public boolean isPhaseTimed() {
		return true;
	}

	private static final int MIN_EXECUTION_TIME = 300;
	private static final int MAX_EXECUTION_TIME = 600;

	@Override
	public Optional<Integer> getRandomExecutionTime() {
		return Optional.of(DungeonGenUtils.randomBetween(MIN_EXECUTION_TIME, MAX_EXECUTION_TIME));
	}

}
