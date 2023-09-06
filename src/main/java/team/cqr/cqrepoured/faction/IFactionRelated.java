package team.cqr.cqrepoured.faction;

import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;

public interface IFactionRelated {
	
	public static final IFactionRelated _FALLBACK = new IFactionRelated() {
		
		@Override
		public int getExactRelationTowards(Faction faction) {
			return 0;
		}
	};
	
	public default boolean isAllyOf(Faction faction) {
		return this.getRoughRelationTowards(faction).isAlly() || this.getRoughRelationTowards(faction).isNeutral();
	}

	public default boolean isEnemyOf(Faction faction) {
		return !this.isAllyOf(faction);
	}

	public default boolean isMemberOf(Faction faction) {
		return this.getRelationTowards(faction).equals(EReputationState.MEMBER);
	}
	
	public int getExactRelationTowards(Faction faction);
	
	public default EReputationState getRelationTowards(Faction faction) {
		return EReputationState.getByInt(this.getExactRelationTowards(faction));
	}
	
	public default EReputationStateRough getRoughRelationTowards(Faction faction) {
		return EReputationStateRough.getByRepuScore(this.getRelationTowards(faction).getValue());
	}

}
