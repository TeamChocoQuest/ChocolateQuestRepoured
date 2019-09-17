package com.teamcqr.chocolatequestrepoured.factions;

import com.teamcqr.chocolatequestrepoured.factions.EReputationState.EReputationStateRough;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumDifficulty;

public enum EFaction {
	
	UNDEAD(new String[] {"WALKERS", "VILLAGERS", "PLAYERS", "TRITONS"}, new String[] {"ILLAGERS", "ENDERMEN"}, EReputationState.ENEMY),
	PIRATE(new String[] {"WALKERS", "VILLAGERS", "INQUISITION", "PLAYERS", "TRITONS"}, new String[] {"ILLAGERS"}, EReputationState.ENEMY),
	WALKERS(new String[] {"UNDEAD", "PIRATE", "DWARVES_AND_GOLEMS", "GOBLINS", "ENDERMEN", "PLAYERS", "OGRES_AND_GREMLINS", "INQUISITION", "ILLAGERS", "VILLAGERS", "NPC"}, new String[] {}, EReputationState.ARCH_ENEMY),
	DWARVES_AND_GOLEMS(new String[] {"WALKERS", "ENDERMEN", "ILLAGERS"}, new String[] {"VILLAGERS", "NPC", "INQUISITION"}, EReputationState.ACCEPTED),
	GOBLINS(new String[] {"OGRES_AND_GREMLINS", "WALKERS", "VILLAGERS", "INQUISITION", "PLAYERS"}, new String[] {"ENDERMEN", "ILLAGERS"}, EReputationState.ENEMY),
	ENDERMEN(new String[] {"WALKERS", "PLAYERS", "DWARVES_AND_GOLEMS", "VILLAGERS", "NPCS", "PIRATE", "TRITONS"}, new String[] {"ILLAGERS", "UNDEAD"}, EReputationState.NEUTRAL),
	//OGRES_AND_GREMLINS(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	INQUISITION(new String[] {"WALKERS", "ILLAGERS", "UNDEAD", "GOBLINS"}, new String[] {"DWARVES_AND_GOLEMS", "NPC", "VILLAGERS"}, EReputationState.NEUTRAL),
	ILLAGERS(new String[] {"WALKERS", "PLAYERS", "VILLAGERS", "NPC", "TRITONS"}, new String[] {"ENDERMEN", "UNDEAD", "PIRATE"}, EReputationState.NEUTRAL),
	VILLAGERS(new String[] {"WALKERS", "UNDEAD", "ILLAGERS"}, new String[] {"NPC", "TRITONS", "PLAYERS"}, EReputationState.NEUTRAL),
	NEUTRAL(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	TRITONS(new String[] {"WALKERS", "UNDEAD", "PIRATE", "ENDERMEN"}, new String[] {"NPC", "VILLAGERS"}, EReputationState.NEUTRAL),
	PLAYERS(new String[] {}, new String[] {"VILLAGERS", "NPC"}, EReputationState.NEUTRAL),
	;

	public static final int REPU_DECREMENT_ON_MEMBER_KILL = 50;
	public static final int REPU_DECREMENT_ON_ENEMY_KILL = 10;
	public static final int REPU_DECREMENT_ON_ALLY_KILL = 20;
	
	private String[] enemies;
	private String[] allies;
	private EReputationState defaultState;
	
	private EFaction(String[] enemies, String[] allies, EReputationState startState) {
		this.enemies = enemies;
		this.allies = allies;
		this.defaultState = startState;
	}
	
	public EReputationState getDefaultReputation() {
		return this.defaultState;
	}
	
	public boolean isEnemy(EFaction otherFac) {
		for(String str : this.enemies) {
			if(otherFac.toString().toUpperCase().equals(str)) {
				return true;
			}
		}
		return false;
	}
	public boolean isAlly(EFaction otherFac) {
		for(String str : this.allies) {
			if(otherFac.toString().toUpperCase().equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	//DONE: Methods to check wether a faction is an ally or an enemy
	public EReputationStateRough getRelation(AbstractEntityCQR e1, AbstractEntityCQR e2) {
		
		EFaction e1Fac = e1.getFaction();
		EFaction e2Fac = e2.getFaction();
		
		if(e1Fac.isAlly(e2Fac) || e2Fac.isAlly(e1Fac)) {
			return EReputationStateRough.ALLY;
		}
		
		if(e1Fac.isEnemy(e2Fac) || e2Fac.isEnemy(e1Fac)) {
			return EReputationStateRough.ENEMY;
		}
		
		return EReputationStateRough.NEUTRAL;
	}
	
	public boolean isEntityEnemy(Entity entity) {
		if(entity == null) {
			return false;
		}
		if(entity.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL)) {
			return false;
		}
		if(getFactionOfEntity(entity) != null) {
			return isEnemy(getFactionOfEntity(entity));
		}
		return false;
	}
	
	public boolean isEntityAlly(Entity entity) {
		if(entity == null) {
			return false;
		}
		if(getFactionOfEntity(entity) != null) {
			return isAlly(getFactionOfEntity(entity));
		}
		return false;
	}
	
	public static EFaction getFactionOfEntity(Entity entity) {
		if(entity instanceof EntityTameable) {
			return getFactionOfEntity(((EntityTameable)entity).getOwner());
		}
		
		if(entity instanceof AbstractEntityCQR) {
			return ((AbstractEntityCQR)entity).getFaction();
		}
		
		if(entity instanceof EntityVillager || entity instanceof EntityGolem) {
			return VILLAGERS;
		}
		if(entity instanceof EntityIllusionIllager || entity instanceof EntityVex || entity instanceof EntityVindicator || entity instanceof EntityEvoker) {
			return ILLAGERS;
		}
		
		if(entity instanceof EntityEnderman || entity instanceof EntityEndermite || entity instanceof EntityDragon) {
			return ENDERMEN;
		}
		
		if(entity instanceof EntityAnimal) {
			return NEUTRAL;
		}
		
		if(entity instanceof EntityMob) {
			return UNDEAD;
		}
		
		if(entity instanceof EntityWaterMob) {
			return TRITONS;
		}
		
		if(entity instanceof EntityPlayer) {
			return PLAYERS;
		}
		
		return null;
		
	}
	
	public void decrementReputation(EntityPlayer player, int amount) {
		
	}
	
	public void incrementReputation(EntityPlayer player, int amount) {
		
	}

}
