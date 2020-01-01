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
	
	public static EReputationState getByInt(int amount) {
		if(amount < -750) {
			return ARCH_ENEMY;
		} else if(amount < -375) {
			return ENEMY;
		} else if(amount < -187) {
			return HATED;
		} else if(amount < -62) {
			return AVOIDED;
		} else if(amount < 62) {
			return NEUTRAL;
		} else if(amount < 187) {
			return ACCEPTED;
		} else if(amount < 375) {
			return FRIEND;
		} else if(amount < 750) {
			return ALLY;
		} else {
			return MEMBER;
		}
	}
	
	public enum EReputationStateRough {
		NEUTRAL(250, -250),
		ENEMY(-251, -10000),
		ALLY(251, 10000);
		
		private final int high; 
		private final int low;
		
		private EReputationStateRough(int highBound, int lowBound) {
			high = highBound;
			low = lowBound;
		}
		
		public int getHighBound() {
			return high;
		}
		public int getLowBound() {
			return low;
		}
		
		public static EReputationStateRough getByRepuScore(int score) {
			if(score <= NEUTRAL.getHighBound() && score >= NEUTRAL.getLowBound()) {
				return NEUTRAL;
			}
			if(score < NEUTRAL.getLowBound()) {
				return ENEMY;
			}
			if(score > NEUTRAL.getHighBound()) {
				return ALLY;
			}
			return ENEMY;
		}
	}

}
