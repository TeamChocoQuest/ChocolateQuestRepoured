package com.teamcqr.chocolatequestrepoured.factions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.factions.EReputationState.EReputationStateRough;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRNPC;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class FactionRegistry {

	private static FactionRegistry instance;
	
	private Map<String, CQRFaction> factions = new ConcurrentHashMap<>();
	private List<UUID> uuidsBeingLoaded = Collections.synchronizedList(new ArrayList<UUID>());
	private Map<UUID, Map<String, Integer>> playerFactionRepuMap = new ConcurrentHashMap<>();
	
	public static final int LOWEST_REPU = EReputationState.ARCH_ENEMY.getValue();
	public static final int HIGHEST_REPU = EReputationState.MEMBER.getValue(); 
	
	public FactionRegistry() {
		instance = this;
	}
	
	public void loadFactions() {
		loadDefaultFactions();
		loadFactionsInConfigFolder();
	}
	
	private void loadFactionsInConfigFolder() {
		//DONE: Load factions from files
		File[] files = CQRMain.CQ_FACTION_FOLDER.listFiles(FileIOUtil.getNBTFileFilter());
		int fileCount = files.length;
		if(fileCount > 0) {
			ArrayList<String> fIDs = new ArrayList<>(); 
			ArrayList<List<String>> allyTmp = new ArrayList<>();
			ArrayList<List<String>> enemyTmp = new ArrayList<>();
			for(int i = 0; i < fileCount; i++) {
				File faction = files[i];
				NBTTagCompound root = FileIOUtil.getRootNBTTagOfFile(faction);
				if(root.getString("type").equalsIgnoreCase("FACTION")) {
					List<String> fAlly = new ArrayList<>();
					List<String> fEnemy = new ArrayList<>();
					//CQRFaction fTmp = 
					String fName = root.getString("name");
					int repuChangeAlly = root.getInteger("repuchangekillally");
					int repuChangeEnemy = root.getInteger("repuchangekillenemy");
					int repuChangeMember = root.getInteger("repuchangekillmember");
					EReputationState defRepu = EReputationState.valueOf(root.getString("defaultrelation"));
					boolean staticRepu = root.getBoolean("staticreputation");
					//Reputation lists
					NBTTagCompound repuTag = root.getCompoundTag("relations");
					if(!repuTag.hasNoTags()) {
						NBTTagList allyTag = FileIOUtil.getOrCreateTagList(repuTag, "allies", Constants.NBT.TAG_STRING);
						if(!allyTag.hasNoTags()) {
							for(int j = 0; j < allyTag.tagCount(); j++) {
								fAlly.add(allyTag.getStringTagAt(j));
							}
						}
						NBTTagList enemyTag = FileIOUtil.getOrCreateTagList(repuTag, "enemies", Constants.NBT.TAG_STRING);
						if(!enemyTag.hasNoTags()) {
							for(int j = 0; j < enemyTag.tagCount(); j++) {
								fEnemy.add(enemyTag.getStringTagAt(j));
							}
						}
					}
					fIDs.set(i, fName);
					allyTmp.set(i, fAlly);
					enemyTmp.set(i, fEnemy);
					
					Optional<Integer> optionMember = Optional.of(repuChangeMember);
					Optional<Integer> optionAlly = Optional.of(repuChangeAlly);
					Optional<Integer> optionEnemy = Optional.of(repuChangeEnemy);
					
					CQRFaction f = new CQRFaction(fName, defRepu, !staticRepu, optionMember, optionAlly, optionEnemy);
					factions.put(fName, f);
				}
			}
			for(int i = 0; i < fIDs.size(); i++) {
				String name = fIDs.get(i);
				CQRFaction fac = factions.get(name);
				for(String s : allyTmp.get(i)) {
					fac.addAlly(factions.get(s));
				}
				for(String s : enemyTmp.get(i)) {
					fac.addEnemy(factions.get(s));
				}
			}
		}
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
			
			CQRFaction fac = new CQRFaction(edf.name(), edf.getDefaultReputation(), false, edf.canRepuChange(), optionMember, optionAlly, optionEnemy);
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
		
		System.out.println("Default factions loaded and initialized!");
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
	
	public EReputationStateRough getReputationOf(UUID playerID, CQRFaction faction) {
		if(faction.isRepuStatic()) {
			return EReputationStateRough.getByRepuScore(faction.getDefaultReputation().getValue());
		}
		if(playerFactionRepuMap.containsKey(playerID)) {
			if(playerFactionRepuMap.get(playerID).containsKey(faction.getName())) {
				return EReputationStateRough.getByRepuScore(playerFactionRepuMap.get(playerID).get(faction.getName()));
			}
		}
		return EReputationStateRough.getByRepuScore(faction.getDefaultReputation().getValue());
	}
	
	public void incrementRepuOf(EntityPlayer player, String faction, int score) {
		changeRepuOf(player, faction, Math.abs(score));
	}
	
	public void decrementRepuOf(EntityPlayer player, String faction, int score) {
		changeRepuOf(player, faction, -Math.abs(score));
	}
	
	public void changeRepuOf(EntityPlayer player, String faction, int score) {
		boolean flag = false;
		if(canRepuChange(player)) {
			if(score < 0) {
				flag = canDecrementRepu(player, faction);
			} else {
				flag = canIncrementRepu(player, faction);
			}
		}
		if(flag) {
			Map<String, Integer> factionsOfPlayer = playerFactionRepuMap.getOrDefault(player.getPersistentID(), new ConcurrentHashMap<>());
			int oldScore = factionsOfPlayer.getOrDefault(faction, factions.get(faction).getDefaultReputation().getValue());
			factionsOfPlayer.put(faction, oldScore + score);
			playerFactionRepuMap.put(player.getPersistentID(), factionsOfPlayer);
			System.out.println("Repu changed!");
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
					return factionsOfPlayer.get(faction) <= HIGHEST_REPU;
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
		return !(player.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL) || player.isCreative() || player.isSpectator() || uuidsBeingLoaded.contains(player.getPersistentID()));
	}
	
	public void handlePlayerLogin(PlayerLoggedInEvent event) {
		String path = FileIOUtil.getAbsoluteWorldPath() + "/data/CQR/reputation/";
		File f = new File(path, event.player.getPersistentID() + ".nbt");
		if(f.exists()) {
			System.out.println("Loading player reputation...");
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					NBTTagCompound root = FileIOUtil.getRootNBTTagOfFile(f);
					NBTTagList repuDataList = FileIOUtil.getOrCreateTagList(root, "reputationdata", Constants.NBT.TAG_COMPOUND);
					if(!repuDataList.hasNoTags()) {
						uuidsBeingLoaded.add(event.player.getPersistentID());
						try {
							Map<String, Integer> mapping = playerFactionRepuMap.get(event.player.getPersistentID());
							repuDataList.forEach(new Consumer<NBTBase>() {

								@Override
								public void accept(NBTBase t) {
									NBTTagCompound tag = (NBTTagCompound)t;
									String fac = tag.getString("factionName");
									if(factions.containsKey(fac)) {
										int reputation = tag.getInteger("reputation");
										mapping.put(fac, reputation);
									}
								}
							});
						} finally {
							uuidsBeingLoaded.remove(event.player.getPersistentID());
						}
					}
				}
			});
			t.start();
		}
	}
	
	public void handlePlayerLogout(PlayerLoggedOutEvent event) {
		if(playerFactionRepuMap.containsKey(event.player.getPersistentID())) {
			System.out.println("Saving player reputation...");
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					Map<String, Integer> mapping = playerFactionRepuMap.get(event.player.getPersistentID());
					Map<String, Integer> entryMapping = new HashMap<>();
					String path = FileIOUtil.getAbsoluteWorldPath() + "/data/CQR/reputation/";
					File f = FileIOUtil.getOrCreateFile(path, event.player.getPersistentID() +".nbt");
					if(f != null) {
						uuidsBeingLoaded.add(event.player.getPersistentID());
						try {
							NBTTagCompound root = FileIOUtil.getRootNBTTagOfFile(f);
							NBTTagList repuDataList = FileIOUtil.getOrCreateTagList(root, "reputationdata", Constants.NBT.TAG_COMPOUND);
							for(int i = 0; i < repuDataList.tagCount(); i++) {
								NBTTagCompound tag = repuDataList.getCompoundTagAt(i);
								if(mapping.containsKey(tag.getString("factionName"))) {
									entryMapping.put(tag.getString("factionName"), i);
								}
							}
							for(Map.Entry<String, Integer> entry : mapping.entrySet()) {
								if(entryMapping.containsKey(entry.getKey())) {
									repuDataList.removeTag(entryMapping.get(entry.getKey()));
								}
								NBTTagCompound tag = new NBTTagCompound();
								tag.setString("factionName", entry.getKey());
								tag.setInteger("reputation", entry.getValue());
								repuDataList.appendTag(tag);
							}
							root.removeTag("reputationdata");
							root.setTag("reputationdata", repuDataList);
							
							FileIOUtil.saveNBTCompoundToFile(root, f);
						} finally {
							uuidsBeingLoaded.remove(event.player.getPersistentID());
						}
					}
				}
			});
			t.start();
		}
	}

}
