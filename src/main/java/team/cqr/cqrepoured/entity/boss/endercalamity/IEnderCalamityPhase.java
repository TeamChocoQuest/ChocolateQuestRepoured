package team.cqr.cqrepoured.entity.boss.endercalamity;

import javax.annotation.Nullable;
import java.util.Optional;

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
			return Optional.of(successors[boss.getRandom().nextInt(successors.length)]);
		}
		return Optional.empty();
	}

}
