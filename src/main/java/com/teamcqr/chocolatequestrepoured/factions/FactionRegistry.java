package com.teamcqr.chocolatequestrepoured.factions;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRNPC;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
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
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class FactionRegistry {

	private static FactionRegistry instance;
	
	private Map<String, CQRFaction> factions = new ConcurrentHashMap<>();
	
	private Map<UUID, Map<String, Integer>> playerFactionRepuMap = new ConcurrentHashMap<>();
	
	public static final int LOWEST_REPU = EReputationState.ARCH_ENEMY.getValue();
	public static final int HIGHEST_REPU = EReputationState.MEMBER.getValue(); 
	
	public FactionRegistry() {
		instance = this;
	}
	
	public void loadFactions() {
		loadDefaultFactions();
		
	}
	
	private void loadDefaultFactions() {
		String[][] allies = new String[EDefaultFaction.values().length][];
		String[][] enemies = new String[EDefaultFaction.values().length][];
		for(int i = 0; i < EDefaultFaction.values().length; i++) {
			EDefaultFaction edf = EDefaultFaction.values()[i];
			allies[i] = edf.getAllies();
			enemies[i] = edf.getEnemies();
			
			Optional<Integer> optionMember = Optional.empty();
			Optional<Integer> optionAlly = Optional.empty();
			Optional<Integer> optionEnemy = Optional.empty();
			
			CQRFaction fac = new CQRFaction(edf.name(), edf.getDefaultReputation(), false, optionMember, optionAlly, optionEnemy);
			factions.put(edf.name(), fac);
		}
		
		for(int i = 0; i < EDefaultFaction.values().length; i++) {
			String name = EDefaultFaction.values()[i].name();
			CQRFaction fac = factions.get(name);
			for(int j = 0; j < allies[i].length; j++) {
				fac.addAlly(factions.get(allies[i][j]));
			}
			for(int j = 0; j < enemies[i].length; j++) {
				fac.addEnemy(factions.get(enemies[i][j]));
			}
		}
		
		System.out.println("Defaunt factions loaded and initialized!");
	}
	
	public CQRFaction getFactionOf(Entity entity) {
		if(entity instanceof EntityTameable) {
			return getFactionOf(((EntityTameable)entity).getOwner());
		}
		if(entity instanceof EntityArmorStand) {
			return factions.get(EDefaultFaction.ALL_ALLY.name());
		}
		
		if(entity instanceof EntityVillager || entity instanceof EntityGolem || entity instanceof EntityCQRNPC) {
			return factions.get(EDefaultFaction.VILLAGERS.name());
		}
		
		if(entity instanceof AbstractEntityCQR) {
			return ((AbstractEntityCQR)entity).getFaction();
		}
		
		
		if(entity instanceof EntityIllusionIllager || entity instanceof EntityVex || entity instanceof EntityVindicator || entity instanceof EntityEvoker) {
			return factions.get(EDefaultFaction.BEASTS.name());
		}
		
		if(entity instanceof EntityEnderman || entity instanceof EntityEndermite || entity instanceof EntityDragon) {
			return factions.get(EDefaultFaction.ENDERMEN.name());
		}
		
		if(entity instanceof EntityAnimal) {
			return factions.get(EDefaultFaction.NEUTRAL.name());
		}
		
		if(entity instanceof EntityMob) {
			return factions.get(EDefaultFaction.UNDEAD.name());
		}
		
		if(entity instanceof EntityWaterMob) {
			return factions.get(EDefaultFaction.TRITONS.name());
		}
		
		return null;
	}
	
	public static FactionRegistry instance() {
		if(instance == null) {
			instance = new FactionRegistry();
		}
		return instance;
	}

	public CQRFaction getFactionInstance(String factionName) {
		if(factions.containsKey(factionName)) {
			return factions.get(factionName);
		}
		return factions.get(EDefaultFaction.UNDEAD.name());
	}
	
	public void incrementRepuOf(EntityPlayer player, String faction, int score) {
		changeRepuOf(player, faction, Math.abs(score));
	}
	
	public void decrementRepuOf(EntityPlayer player, String faction, int score) {
		changeRepuOf(player, faction, -Math.abs(score));
	}
	
	public void changeRepuOf(EntityPlayer player, String faction, int score) {
		boolean flag = false;
		if(score < 0) {
			flag = canDecrementRepu(player, faction);
		} else {
			flag = canIncrementRepu(player, faction);
		}
		if(flag) {
			Map<String, Integer> factionsOfPlayer = playerFactionRepuMap.getOrDefault(player.getPersistentID(), new ConcurrentHashMap<>());
			int oldScore = factionsOfPlayer.getOrDefault(faction, factions.get(faction).getDefaultReputation().getValue());
			factionsOfPlayer.put(faction, oldScore + score);
		}
	}
	
	public boolean canDecrementRepu(EntityPlayer player, String faction) {
		if(canRepuChange(player)) {
			Map<String, Integer> factionsOfPlayer = playerFactionRepuMap.getOrDefault(player.getPersistentID(), new ConcurrentHashMap<>());
			if(factionsOfPlayer != null) {
				if(factionsOfPlayer.containsKey(faction)) {
					return factionsOfPlayer.get(faction) >= LOWEST_REPU;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	public boolean canIncrementRepu(EntityPlayer player, String faction) {
		if(canRepuChange(player)) {
			Map<String, Integer> factionsOfPlayer = playerFactionRepuMap.getOrDefault(player.getPersistentID(), new ConcurrentHashMap<>());
			if(factionsOfPlayer != null) {
				if(factionsOfPlayer.containsKey(faction)) {
					return factionsOfPlayer.get(faction) >= HIGHEST_REPU;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	private boolean canRepuChange(EntityPlayer player) {
		return !(player.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL) || player.isCreative() || player.isSpectator());
	}
	
	public void handlePlayerLogin(PlayerLoggedInEvent event) {
		
	}
	
	public void handlePlayerLogout(PlayerLoggedOutEvent event) {
		
	}

}
