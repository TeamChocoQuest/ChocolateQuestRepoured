package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;

public enum EDungeonMobType {
	
	DEFAULT(null, null, null),
	//DONT_REPLACE(null),
	DWARF(new ResourceLocation(Reference.MODID, "dwarf"), null, null),
	SKELETON(new ResourceLocation(Reference.MODID, "skeleton"), new ResourceLocation(Reference.MODID, "necromancer"), EBanners.SKELETON_BANNER),
	ZOMBIE(new ResourceLocation(Reference.MODID, "zombie"), new ResourceLocation(Reference.MODID, "lich"), null),
	PIRATE(new ResourceLocation(Reference.MODID, "pirate"), null, EBanners.PIRATE_BANNER),
	ILLAGER(new ResourceLocation(Reference.MODID, "illager"), null, EBanners.ILLAGER_BANNER),
	WALKER(new ResourceLocation(Reference.MODID, "walker"), null, EBanners.WALKER_ORDO),
	SPECTER(new ResourceLocation(Reference.MODID, "spectre"), null, null),
	ENDERMAN(new ResourceLocation(Reference.MODID, "enderman"), null, EBanners.ENDERMEN_BANNER),
	BOARMAN(new ResourceLocation(Reference.MODID, "boarman"), new ResourceLocation(Reference.MODID, "boar_mage"), EBanners.PIGMAN_BANNER),
	MINOTAUR(new ResourceLocation(Reference.MODID, "minotaur"), null, null),
	ORC(new ResourceLocation(Reference.MODID, "orc"), null, null),
	GOLEM(new ResourceLocation(Reference.MODID, "golem"), null, null),
	GOBLIN(new ResourceLocation(Reference.MODID, "goblin"), null, null),
	MUMMY(new ResourceLocation(Reference.MODID, "mummy"), null, null),
	OGRE(new ResourceLocation(Reference.MODID, "ogre"), null, null),
	TRITON(new ResourceLocation(Reference.MODID, "triton"), null, null)
	;
	
	static final int[] countMapping = new int[] {
			1,
			2,
			1,
			//3,
			1,
			1,
			1
	};
	static final EDungeonMobType[][] mobWheel = new EDungeonMobType[][] {
		new EDungeonMobType[] {SKELETON},
		new EDungeonMobType[] {ZOMBIE, MUMMY},
		new EDungeonMobType[] {ILLAGER},
		//new EDungeonMobType[] {GOBLIN, ORC, OGRE},
		new EDungeonMobType[] {SPECTER},
		new EDungeonMobType[] {MINOTAUR},
		new EDungeonMobType[] {ENDERMAN},
	};
	
	private ResourceLocation resLoc;
	private ResourceLocation bossResLoc;
	private EBanners banner;
	
	private EDungeonMobType(ResourceLocation resLoc, ResourceLocation bossResLoc, EBanners banner) {
		this.resLoc = resLoc;
		this.banner = banner;
		this.bossResLoc = bossResLoc;
	}
	
	public ResourceLocation getEntityResourceLocation() {
		return resLoc;
	}
	public ResourceLocation getBossResourceLocation() {
		return bossResLoc;
	}
	public EBanners getBanner() {
		return banner;
	}
	
	//DONE: Rewrite this to return the mob type and not the mob itself
	
	//X and Z are B L O C K x and z, not chunk x and z!!!
	public static EDungeonMobType getMobTypeDependingOnDistance(int x, int z) {
		//System.out.println("X: " + x);
		//System.out.println("Z: " + z);
		double distToSpawn = Math.sqrt(x * x + z * z);
		//System.out.println("DistToSpawn: " + distToSpawn);
		
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
		//System.out.println("Index: " + index);
		//if the index is larger than array size +1 -> RANDOM
		Random rdm = new Random();
		int indx = -1;
		if(index >= countMapping.length) {
			indx = rdm.nextInt(countMapping.length);
		} else {
			indx = index;
		}
		//System.out.println("Indx: " + indx);
		if(indx >= 0) {
			return mobWheel[indx][rdm.nextInt(countMapping[indx])];
		}
		
		return SKELETON;
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
