package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import java.util.Optional;

import javax.annotation.Nullable;

public interface IEnderCalamityPhase {

	boolean canRandomTeleportDuringPhase();

	boolean canSummonAlliesDuringPhase();

	boolean canPickUpBlocksDuringPhase();

	boolean canThrowBlocksDuringPhase();

	boolean isPhaseTimed();

	// Implement this for all timed phases
	default Optional<Integer> getRandomExecutionTime() {
		return Optional.empty();
	}

	@Nullable
	IEnderCalamityPhase[] getPossibleSuccessors();

	default Optional<IEnderCalamityPhase> getNextPhase(EntityCQREnderCalamity boss) {
		IEnderCalamityPhase[] successors = this.getPossibleSuccessors();
		if (successors != null && successors.length > 0) {
			return Optional.of(successors[boss.getRNG().nextInt(successors.length)]);
		}
		return Optional.empty();
	}

}
