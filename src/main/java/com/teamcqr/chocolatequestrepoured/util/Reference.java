package com.teamcqr.chocolatequestrepoured.util;

public class Reference 
{
	public static final String MODID = "cqrepoured";
	public static final String NAME = "Chocolate Quest Repoured";
	public static final String VERSION = "0.1.2";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "com.teamcqr.chocolatequestrepoured.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.teamcqr.chocolatequestrepoured.proxy.CommonProxy";
	public static final String DUNGEON_REGISTRY = "com.teamcqr.chocolatequestrepoured.dungeongen.DungeonRegistry";
	public static final String DUNGEON_TERRAIN_POPULATOIR = "com.teamcqr.chocolatequestrepoured.dungeongen.WorldDungeonGenerator";
	
	public static final int EXPORTER_GUI_ID = 1;
	public static final int SPAWNER_GUI_ID = 2;
	public static final int BADGE_GUI_ID = 3;
	public static final int BACKPACK_GUI_ID = 4;
	public static final int ALCHEMY_BAG_GUI_ID = 5;
	
	public static final int ENTITY_SLIME_PART = 44;
	public static final int TARGET_EFFECT_MESSAGE_ID = 30;
	
	public static final int PROJECTILE_EARTH_QUAKE = 66;
	public static final int PROJECTILE_BULLET = 67;
	public static final int PROJECTILE_SPIDER_BALL = 68;
	public static final int PROJECTILE_CANNON_BALL = 69;
	public static final int PROJECTILE_VAMPIRIC_SPELL = 70;
	public static final int PROJECTILE_POISON_SPELL = 71;

	public static final int ENTITY_CQRZOMBIE = 120;

	public static final double SPAWNER_RANGE = 25.0D;
	
	public static final ConfigFileHelper CONFIG_HELPER = new ConfigFileHelper();
}