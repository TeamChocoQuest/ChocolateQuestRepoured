package team.cqr.cqrepoured.common.services.interfaces;

public interface ConfigService {
	
	public static interface FactionConfig {
		boolean enableTeamsForFactionAssignment();
	}
	
	FactionConfig factionConfig();

}
