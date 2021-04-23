package team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases;

import java.util.Optional;

import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.IEnderCalamityPhase;
import team.cqr.cqrepoured.util.DungeonGenUtils;

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
		return new IEnderCalamityPhase[] { /*EEnderCalamityPhase.PHASE_LASERING.getPhaseObject(),*/ EEnderCalamityPhase.PHASE_BUILDING.getPhaseObject(), EEnderCalamityPhase.PHASE_TELEPORT_EYE_THROWER.getPhaseObject(), EEnderCalamityPhase.PHASE_TELEPORT_LASER.getPhaseObject() };
	}

	@Override
	public boolean isPhaseTimed() {
		return true;
	}

	private static final int MIN_EXECUTION_TIME = 50;
	private static final int MAX_EXECUTION_TIME = 150;

	@Override
	public Optional<Integer> getRandomExecutionTime() {
		return Optional.of(DungeonGenUtils.randomBetween(MIN_EXECUTION_TIME, MAX_EXECUTION_TIME));
	}

	@Override
	public Optional<IEnderCalamityPhase> getNextPhase(EntityCQREnderCalamity boss) {
		if (!boss.hasAttackTarget()) {
			return Optional.of(EEnderCalamityPhase.PHASE_NO_TARGET.getPhaseObject());
		}
		IEnderCalamityPhase[] successors = this.getPossibleSuccessors();
		if (successors != null && successors.length > 0) {
			return Optional.of(successors[boss.getRNG().nextInt(successors.length)]);
		}
		return Optional.empty();
	}

}
