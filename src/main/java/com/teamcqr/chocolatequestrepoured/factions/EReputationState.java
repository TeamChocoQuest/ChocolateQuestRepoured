package com.teamcqr.chocolatequestrepoured.factions;

public enum EReputationState {
	
	ARCH_ENEMY(-1000),
	ENEMY(-500),
	HATED(-250),
	AVOIDED(-125),
	NEUTRAL(0),
	ACCEPTED(125),
	FRIEND(250),
	ALLY(500),
	MEMBER(1000);
	
	private int value = 0;
	
	private EReputationState(int index) {
		value = index;
	}
	
	public int getValue() {
		return value;
	}
	
	public enum EReputationStateRough {
		NEUTRAL(250, -250),
		ENEMY(-251, -1000),
		ALLY(251, 1000);
		
		private final int high; 
		private final int low;
		
		private EReputationStateRough(int highBound, int lowBound) {
			high = highBound;
			low = lowBound;
		}
		
		public static EReputationStateRough getByRepuScore(int score) {
			for(EReputationStateRough ersr : values()) {
				if(score <= ersr.high && score >= ersr.low) {
					return ersr;
				}
			}
			return NEUTRAL;
		}
	}

}
