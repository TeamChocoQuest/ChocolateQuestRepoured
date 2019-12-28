package com.teamcqr.chocolatequestrepoured.factions;

import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumDifficulty;

public class CQRFaction {

	private boolean savedGlobally = true;
	private String name;
	//private CQRFaction[] allies = new CQRFaction[12];
	private ArrayList<CQRFaction> allies = new ArrayList<>();
	//private CQRFaction[] enemies = new CQRFaction[12];
	private ArrayList<CQRFaction> enemies = new ArrayList<>();
	private EReputationState defaultRelation;
	
	private int repuChangeOnMemberKill = 5;
	private int repuChangeOnAllyKill = 2;
	private int repuChangeOnEnemyKill = 1;
	
	public CQRFaction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
		this(name, defaultReputationState, true, repuChangeOnMemberKill, repuChangeOnAllyKill, repuChangeOnEnemyKill);
	}
	
	public CQRFaction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, boolean saveGlobally, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
		this.savedGlobally = saveGlobally;
		this.name = name;
		this.defaultRelation = defaultReputationState;
		
		this.repuChangeOnMemberKill = repuChangeOnMemberKill.isPresent() ? repuChangeOnMemberKill.get() : 5;
		this.repuChangeOnAllyKill = repuChangeOnAllyKill.isPresent() ? repuChangeOnAllyKill.get() : 2;
		this.repuChangeOnEnemyKill = repuChangeOnEnemyKill.isPresent() ? repuChangeOnEnemyKill.get() : 1;
	}
	
	public int getRepuMemberKill() {return repuChangeOnMemberKill;}
	public int getRepuAllyKill() {return repuChangeOnAllyKill;}
	public int getRepuEnemyKill() {return repuChangeOnEnemyKill;}
	
	public String getName() {
		return this.name;
	}
	public boolean isSavedGlobally() {
		return savedGlobally;
	}
	public EReputationState getDefaultReputation() {
		return defaultRelation;
	}

	public void addAlly(CQRFaction ally) {
		//allies = addFactionToRelationArray(ally, allies);
		if(ally != null) {
			allies.add(ally);
		}
	}
	
	public void addEnemy(CQRFaction enemy) {
		//enemies = addFactionToRelationArray(enemy, enemies);
		if(enemy != null) {
			enemies.add(enemy);
		}
	}
	
	/*private CQRFaction[] addFactionToRelationArray(CQRFaction f, CQRFaction[] a) {
		if(a[a.length -1] != null) {
			a = (CQRFaction[]) ArrayManipulationUtil.enlargeFactionArray(a, 1);
		}
		a[a.length -1] = f;
		
		return a;
	}*/

	public boolean isEnemy(CQRFaction faction) {
		if(faction == this || (faction != null && faction.getName().equalsIgnoreCase("ALL_ALLY"))) {
			return false;
		}
		if(faction != null) {
			for(CQRFaction str : this.enemies) {
				if(str != null && faction.getName().equalsIgnoreCase(str.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isAlly(CQRFaction faction) {
		if(faction == this || (faction != null && faction.getName().equalsIgnoreCase("ALL_ALLY"))) {
			return true;
		}
		if(faction != null) {
			for(CQRFaction str : this.allies) {
				if(str != null && faction.getName().equalsIgnoreCase(str.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isAlly(AbstractEntityCQR ent) {
		return isAlly(ent.getFaction());
	}
	public boolean isEnemy(AbstractEntityCQR ent) {
		if(ent.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL)) {
			return false;
		}
		return isEnemy(ent.getFaction());
	}
	public boolean isAlly(Entity ent) {
		return isAlly(FactionRegistry.instance().getFactionOf(ent));
	}
	public boolean isEnemy(Entity ent) {
		if(ent.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL)) {
			return false;
		}
		return isEnemy(FactionRegistry.instance().getFactionOf(ent));
	}

	public void decrementReputation(EntityPlayer player, int score) {
		FactionRegistry.instance().decrementRepuOf(player, name, score);
	}

	public void incrementReputation(EntityPlayer player, int score) {
		FactionRegistry.instance().incrementRepuOf(player, name, score);
	}

}
