package com.teamcqr.chocolatequestrepoured.structuregen;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public enum EDungeonGenerator {

	CAVERNS("caverns"),
	ABANDONED("abandoned"),
	RUIN("ruin"),
	NETHER_CITY("nether_city"),
	FLOATING_NETHER_CITY("floating_nether_city"),
	TEMPLATE_SURFACE("template_surface"),
	TEMPLATE_OCEAN_FLOOR("template_ocean_floor"),
	STRONGHOLD("stronghold"),
	CLASSIC_STRONGHOLD("classic_stronghold"),
	JUNGLE_CAVE("jungle_cave"),
	SWAMP_CAVE("swamp_cave"),
	VILLAGE("village"),
	CASTLE("castle"),
	VOLCANO("volcano");
	
	private String name;
	
	EDungeonGenerator(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static boolean isValidDungeonGenerator(String toTest) {
		
		for(EDungeonGenerator generator : EDungeonGenerator.values()) {
			if(toTest.equalsIgnoreCase(generator.getName())) {
				return true;
			}
		}
		
		return false;
	}
		
}
