package team.cqr.cqrepoured.faction;

import java.util.List;
import java.util.Optional;

import net.minecraft.world.entity.Entity;
import team.cqr.cqrepoured.capability.faction.IFactionRelationCapability;
import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;

public interface IFactionRelatedEntity extends IFactionRelated {
	
	public Optional<IFactionRelationCapability> getFactionOverrides();
	public Optional<EntityFactionInformation> getEntityFactionSettings();
	public EntityFactionInformation getEntityFactionInformation();

	@Override
	public default boolean isAllyOf(Faction faction) {
		return this.getRelevantFactionObject(faction).isAllyOf(faction);
	}

	@Override
	public default boolean isEnemyOf(Faction faction) {
		return this.getRelevantFactionObject(faction).isEnemyOf(faction);
	}

	@Override
	public default boolean isMemberOf(Faction faction) {
		return this.getRelevantFactionObject(faction).isMemberOf(faction);
	}
	
	private IFactionRelated getRelevantFactionObject(Faction faction) {
		List<Optional<? extends IFactionRelated>> candidates = List.of(this.getFactionOverrides(), this.getEntityFactionSettings());
		candidates.removeIf(opt -> {
			if (opt.isEmpty()) {
				return true;
			}
			if (opt.get() instanceof IFactionRelationCapability ifrc) {
				return !ifrc.hasInformationFor(faction);
			}
			if (opt.get() instanceof EntityFactionInformation efi) {
				return !efi.hasInformationFor(faction);
			}
			return false;
		});
		if (candidates.isEmpty()) {
			return _FALLBACK;
		} else {
			for (int i = 0; i < candidates.size(); i++) {
				if (candidates.get(i).isPresent()) {
					return candidates.get(i).get();
				}
			}
		}
		return _FALLBACK;
	}

	@Override
	public default int getExactRelationTowards(Faction faction) {
		return this.getRelevantFactionObject(faction).getExactRelationTowards(faction);
	}
	
	@Override
	public default EReputationStateRough getRoughReputationOf(Entity entity) {
		if (this.getFactionOverrides().isPresent()) {
			return this.getFactionOverrides().get().getRoughReputationOf(entity);
		} else if (this.getEntityFactionSettings().isPresent()) {
			return this.getEntityFactionSettings().get().getRoughReputationOf(entity);
		} else {
			return _FALLBACK.getRoughReputationOf(entity);
		}
	}

}
