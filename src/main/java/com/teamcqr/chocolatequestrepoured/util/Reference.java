package com.teamcqr.chocolatequestrepoured.util;

import de.DerToaster.SimpleThreading.MultiThreadController;

public class Reference {
	public static final String MODID = "cqrepoured";
	public static final String NAME = "Chocolate Quest Repoured";
	public static final String VERSION = "1.11.0A";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "com.teamcqr.chocolatequestrepoured.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.teamcqr.chocolatequestrepoured.proxy.ServerProxy";
	public static final String DUNGEON_REGISTRY = "com.teamcqr.chocolatequestrepoured.structuregen.DungeonRegistry";
	public static final String DUNGEON_TERRAIN_POPULATOR = "com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator";
	public static final String WALL_TERRAIN_POPULATOR = "com.teamcqr.chocolatequestrepoured.structuregen.thewall.WorldWallGenerator";

	public static final int EXPORTER_GUI_ID = 1;
	public static final int SPAWNER_GUI_ID = 2;
	public static final int BADGE_GUI_ID = 3;
	public static final int BACKPACK_GUI_ID = 4;
	public static final int ALCHEMY_BAG_GUI_ID = 5;
	public static final int CQR_ENTITY_GUI_ID = 6;
	public static final int REPUTATION_GUI_ID = 7;

	public static final int TARGET_EFFECT_MESSAGE_ID = 30;
	public static final int SAVE_STRUCUTRE_REQUEST_MESSAGE_ID = 31;

	// Moved to CONFIG_HELPER
	// public static final double SPAWNER_RANGE = 25.0D;

	// public static final ConfigFileHelper CONFIG_HELPER_INSTANCE = new ConfigFileHelper();

	public static final MultiThreadController BLOCK_PLACING_THREADS_INSTANCE = new MultiThreadController(0);
}
