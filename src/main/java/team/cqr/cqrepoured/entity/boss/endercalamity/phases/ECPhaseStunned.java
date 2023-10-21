package team.cqr.cqrepoured.entity.boss.endercalamity.phases;

import java.util.Optional;

import team.cqr.cqrepoured.entity.boss.endercalamity.IEnderCalamityPhase;
import team.cqr.cqrepoured.util.DungeonGenUtils;

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
		//return new IEnderCalamityPhase[] { EEnderCalamityPhase.PHASE_TELEPORT_LASER.getPhaseObject(), EEnderCalamityPhase.PHASE_IDLE.getPhaseObject()/* , EEnderCalamityPhase.PHASE_LASERING.getPhaseObject() */ };
		return EEnderCalamityPhase.PHASE_IDLE.getPhaseObject().getPossibleSuccessors();
	}

	@Override
	public boolean isPhaseTimed() {
		return true;
	}
	
	private static final int MIN_EXECUTION_TIME = 100; //5s
	private static final int MAX_EXECUTION_TIME = 300; //15s

	@Override
	public Optional<Integer> getRandomExecutionTime() {
		return Optional.of(DungeonGenUtils.randomBetween(MIN_EXECUTION_TIME, MAX_EXECUTION_TIME));
	}

}
