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
	
	private int counter = 0;
	
	private EReputationState(int index) {
		counter = index;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public enum EReputationStateRough {
		NEUTRAL(250, -250),
		ENEMY(-251, -1000),
		ALLY(251, 1000);
		
		private EReputationStateRough(int highBound, int lowBound) {
		}
	}

}
