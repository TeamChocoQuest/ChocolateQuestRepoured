package team.cqr.cqrepoured.common.services.interfaces;

public interface ConfigService {
	
	public static interface FactionConfig {
		boolean enableTeamsForFactionAssignment();
	}
	
	public static interface ProtectionConfig {
		boolean protectionSystemEnabled();
		boolean preventEntitySpawningInActiveRegions();
		boolean preventFireSpreadingInActiveRegions();
		boolean preventTNTExplosionsInActiveRegions();
		boolean preventOtherExplosionsInActiveRegions();
		boolean preventBlockPlacingInActiveRegions();
		boolean preventBlockBreakingInActiveRegions();
	}
	
	FactionConfig factionConfig();
	ProtectionConfig protectionConfig();

}
