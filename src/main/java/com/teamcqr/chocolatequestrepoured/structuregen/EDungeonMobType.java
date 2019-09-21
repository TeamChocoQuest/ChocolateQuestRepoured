package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;

public enum EDungeonMobType {
	
	DEFAULT(null),
	//DONT_REPLACE(null),
	DWARF(new ResourceLocation(Reference.MODID, "dwarf")),
	SKELETON(new ResourceLocation(Reference.MODID, "skeleton")),
	ZOMBIE(new ResourceLocation(Reference.MODID, "zombie")),
	PIRATE(new ResourceLocation(Reference.MODID, "pirate")),
	ILLAGER(new ResourceLocation(Reference.MODID, "illager")),
	WALKER(new ResourceLocation(Reference.MODID, "walker")),
	SPECTER(new ResourceLocation(Reference.MODID, "spectre")),
	ENDERMAN(new ResourceLocation(Reference.MODID, "enderman")),
	BOARMAN(new ResourceLocation(Reference.MODID, "boarman")),
	MINOTAUR(new ResourceLocation(Reference.MODID, "minotaur")),
	ORC(new ResourceLocation(Reference.MODID, "orc")),
	GOLEM(new ResourceLocation(Reference.MODID, "golem")),
	GOBLIN(new ResourceLocation(Reference.MODID, "goblin")),
	MUMMY(new ResourceLocation(Reference.MODID, "mummy")),
	OGRE(new ResourceLocation(Reference.MODID, "ogre")),
	TRITON(new ResourceLocation(Reference.MODID, "triton"))
	;
	
	static final int[] mobCount = new int[] {
			1,
			2,
			1,
			3,
			1,
			1,
			1
	};
	static final ResourceLocation[][] mobWheel = new ResourceLocation[][] {
		new ResourceLocation[] {SKELETON.getEntityResourceLocation()},
		new ResourceLocation[] {ZOMBIE.getEntityResourceLocation(), MUMMY.getEntityResourceLocation()},
		new ResourceLocation[] {ILLAGER.getEntityResourceLocation()},
		new ResourceLocation[] {GOBLIN.getEntityResourceLocation(), ORC.getEntityResourceLocation(), OGRE.getEntityResourceLocation()},
		new ResourceLocation[] {SPECTER.getEntityResourceLocation()},
		new ResourceLocation[] {MINOTAUR.getEntityResourceLocation()},
		new ResourceLocation[] {ENDERMAN.getEntityResourceLocation()},
	};
	
	private ResourceLocation resLoc;
	
	private EDungeonMobType(ResourceLocation resLoc) {
		this.resLoc = resLoc;
	}
	
	public ResourceLocation getEntityResourceLocation() {
		return resLoc;
	}
	
	//X and Z are B L O C K x and z, not chunk x and z!!!
	public static ResourceLocation getMobDependingOnDistance(int x, int z) {
		double distToSpawn = Math.sqrt(x * x + z * z);
		
		/*
		 * Mob spawns depending on distance: Raising from near to far
		 * 
		 * Skeleton
		 * Zombie
		 * Illager
		 * Goblin/Orc/Ogre -> Random
		 * Specter
		 * Minotaur?
		 * Endermen
		 * 
		 * Distances: Config option for delimitter
		 * 
		 */
		
		int index = new Double(distToSpawn / Reference.CONFIG_HELPER_INSTANCE.getMobChangeDistanceDivisor()).intValue();
		//if the index is larger than array size +1 -> RANDOM
		Random rdm = new Random();
		int indx = -1;
		if(index >= mobWheel.length) {
			indx = rdm.nextInt(mobCount.length);
		} else {
			indx = index;
		}
		if(indx >= 0) {
			return mobWheel[indx][rdm.nextInt(mobCount[indx])];
		}
		
		return SKELETON.getEntityResourceLocation();
	}
	
	@Nullable
	public static EDungeonMobType byString(String name) {
		for(EDungeonMobType e : EDungeonMobType.values()) {
			if(e.name().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	}

}
