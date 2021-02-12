package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import java.util.Optional;

import javax.annotation.Nullable;

public interface IEnderCalamityPhase {
	
	public boolean canRandomTeleportDuringPhase();
	public boolean canSummonAlliesDuringPhase();
	public boolean canPickUpBlocksDuringPhase();
	public boolean canThrowBlocksDuringPhase();
	@Nullable
	IEnderCalamityPhase[] getPossibleSuccessors();
	
	public default Optional<IEnderCalamityPhase> getNextPhase(EntityCQREnderCalamity boss) {
		IEnderCalamityPhase[] successors = this.getPossibleSuccessors();
		if(successors != null && successors.length > 0) {
			return Optional.of(successors[boss.getRNG().nextInt(successors.length)]);
		}
		return Optional.empty();
	}

}
