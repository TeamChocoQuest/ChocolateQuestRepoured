package com.teamcqr.chocolatequestrepoured.factions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.customtextures.TextureSet;
import com.teamcqr.chocolatequestrepoured.customtextures.TextureSetManager;
import com.teamcqr.chocolatequestrepoured.factions.EReputationState.EReputationStateRough;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketInitialFactionInformation;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketUpdatePlayerReputation;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FactionRegistry {

	private static FactionRegistry instance;

	private volatile Map<String, CQRFaction> factions = new ConcurrentHashMap<>();
	private volatile List<UUID> uuidsBeingLoaded = Collections.synchronizedList(new ArrayList<UUID>());
	private Map<UUID, Map<String, Integer>> playerFactionRepuMap = new ConcurrentHashMap<>();
	private Map<ResourceLocation, CQRFaction> entityFactionMap = new ConcurrentHashMap<>();

	public static final int LOWEST_REPU = EReputationState.ARCH_ENEMY.getValue();
	public static final int HIGHEST_REPU = EReputationState.MEMBER.getValue();

	public FactionRegistry() {
		instance = this;
	}

	public void loadFactions() {
		if (!this.factions.isEmpty()) {
			this.factions.clear();
		}
		if (!this.playerFactionRepuMap.isEmpty()) {
			this.playerFactionRepuMap.clear();
		}
		if (!this.entityFactionMap.isEmpty()) {
			this.entityFactionMap.clear();
		}

		this.loadFactionsInConfigFolder();
		this.loadDefaultFactions();

		this.loadEntityFactionRelations();
	}

	@SideOnly(Side.CLIENT)
	public synchronized void addFaction(CQRFaction faction) {
		this.factions.put(faction.getName(), faction);
	}

	@SideOnly(Side.CLIENT)
	public synchronized void setReputation(UUID player, int reputation, CQRFaction faction) {
		if (faction.canRepuChange()) {
			Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.computeIfAbsent(player, key -> new ConcurrentHashMap<>());
			factionsOfPlayer.put(faction.getName(), reputation);
			// System.out.println("Repu of " + player.toString() + " set to " + reputation + " for faction " + faction.getName());
		}
	}

	// Variant on the server, used by the command
	public void changeReputationTo(@Nonnull EntityPlayerMP player, int reputation, @Nonnull CQRFaction faction) {
		Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.computeIfAbsent(player.getPersistentID(), key -> new ConcurrentHashMap<>());
		factionsOfPlayer.put(faction.getName(), reputation);

		this.sendRepuUpdatePacket(player, reputation, faction.getName());
	}

	private void loadEntityFactionRelations() {
		File file = new File(CQRMain.CQ_CONFIG_FOLDER, "entityFactionRelation.properties");
		if (file.exists()) {
			Properties prop = new Properties();
			boolean flag = true;
			try (InputStream inputStream = new FileInputStream(file)) {
				prop.load(inputStream);
				flag = true;
			} catch (IOException e) {
				CQRMain.logger.error("Failed to load file" + file.getName(), e);
				flag = false;
			}
			if (flag) {
				for (String key : prop.stringPropertyNames()) {
					if (key.startsWith("#")) {
						continue;
					}
					String rlkey = key.replace('.', ':');
					ResourceLocation resLoc = new ResourceLocation(rlkey);
					// if(EntityList.isRegistered(resLoc)) {
					String faction = prop.getProperty(key, null);
					if (faction != null && this.factions.containsKey(faction)) {
						this.entityFactionMap.put(resLoc, this.factions.get(faction));
					}
					// }
				}
			}
		}
	}

	private void loadFactionsInConfigFolder() {
		// DONE: Load factions from files
		List<File> files = new ArrayList<>(FileUtils.listFiles(CQRMain.CQ_FACTION_FOLDER, new String[] { "cfg", "prop", "properties" }, true));
		int fileCount = files.size();
		if (fileCount > 0) {
			ArrayList<String> fIDs = new ArrayList<>(fileCount + 1);
			ArrayList<List<String>> allyTmp = new ArrayList<>();
			ArrayList<List<String>> enemyTmp = new ArrayList<>();
			boolean flag = true;
			for (int i = 0; i < fileCount; i++) {
				File file = files.get(i);
				Properties prop = new Properties();
				try (InputStream inputStream = new FileInputStream(file)) {
					prop.load(inputStream);
					flag = true;
				} catch (IOException e) {
					CQRMain.logger.error("Failed to load file" + file.getName(), e);
					flag = false;
					continue;
				}
				if (flag) {
					List<String> fAlly = new ArrayList<>();
					List<String> fEnemy = new ArrayList<>();
					// CQRFaction fTmp =
					String fName = prop.getProperty(ConfigKeys.FACTION_NAME_KEY, "FACTION_NAME");
					int repuChangeAlly = PropertyFileHelper.getIntProperty(prop, ConfigKeys.FACTION_REPU_CHANGE_KILL_ALLY, 2);
					int repuChangeEnemy = PropertyFileHelper.getIntProperty(prop, ConfigKeys.FACTION_REPU_CHANGE_KILL_ENEMY, 1);
					int repuChangeMember = PropertyFileHelper.getIntProperty(prop, ConfigKeys.FACTION_REPU_CHANGE_KILL_MEMBER, 5);
					EReputationState defRepu = EReputationState.valueOf(prop.getProperty(ConfigKeys.FACTION_REPU_DEFAULT, EReputationState.NEUTRAL.toString()));
					boolean staticRepu = PropertyFileHelper.getBooleanProperty(prop, ConfigKeys.FACTION_STATIC_REPUTATION_KEY, false);
					// Reputation lists
					for (String ally : PropertyFileHelper.getStringArrayProperty(prop, ConfigKeys.FACTION_ALLIES_KEY, new String[0], true)) {
						fAlly.add(ally);
					}
					for (String enemy : PropertyFileHelper.getStringArrayProperty(prop, ConfigKeys.FACTION_ENEMIES_KEY, new String[0], true)) {
						fEnemy.add(enemy);
					}
					fIDs.add(fName);
					allyTmp.add(fAlly);
					enemyTmp.add(fEnemy);

					Optional<Integer> optionMember = Optional.of(repuChangeMember);
					Optional<Integer> optionAlly = Optional.of(repuChangeAlly);
					Optional<Integer> optionEnemy = Optional.of(repuChangeEnemy);

					// Custom textures start
					String textureSetName = prop.getProperty(ConfigKeys.TEXTURE_SET_KEY, "");
					TextureSet ts = TextureSetManager.getInstance().getTextureSet(textureSetName);
					// Custom textures end

					CQRFaction f = new CQRFaction(fName, ts, defRepu, true, !staticRepu, optionMember, optionAlly, optionEnemy);
					this.factions.put(fName, f);
				}
			}
			for (int i = 0; i < fIDs.size(); i++) {
				String name = fIDs.get(i);
				CQRFaction fac = this.factions.get(name);
				for (String s : allyTmp.get(i)) {
					fac.addAlly(this.factions.get(s));
				}
				for (String s : enemyTmp.get(i)) {
					fac.addEnemy(this.factions.get(s));
				}
			}
		}
	}

	private void loadDefaultFactions() {
		String[][] allies = new String[EDefaultFaction.values().length][];
		String[][] enemies = new String[EDefaultFaction.values().length][];
		List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < EDefaultFaction.values().length; i++) {
			EDefaultFaction edf = EDefaultFaction.values()[i];
			if (this.factions.containsKey(edf.name())) {
				continue;
			}
			indices.add(i);
			allies[i] = edf.getAllies();
			enemies[i] = edf.getEnemies();

			Optional<Integer> optionMember = Optional.empty();
			Optional<Integer> optionAlly = Optional.empty();
			Optional<Integer> optionEnemy = Optional.empty();

			CQRFaction fac = new CQRFaction(edf.name(), null, edf.getDefaultReputation(), false, edf.canRepuChange(), optionMember, optionAlly, optionEnemy);
			this.factions.put(edf.name(), fac);
		}

		for (int i : indices) {
			String name = EDefaultFaction.values()[i].name();
			CQRFaction fac = this.factions.get(name);
			for (int j = 0; j < allies[i].length; j++) {
				fac.addAlly(this.factions.get(allies[i][j]));
			}
			for (int j = 0; j < enemies[i].length; j++) {
				fac.addEnemy(this.factions.get(enemies[i][j]));
			}
		}

		CQRMain.logger.info("Default factions loaded and initialized!");
	}

	@Nullable
	public CQRFaction getFactionOf(Entity entity) {
		if (entity == null) {
			return null;
		}

		if (CQRConfig.advanced.enableOldFactionMemberTeams) {
			if (entity.getTeam() != null && factions.containsKey(entity.getTeam().getName()) && factions.get(entity.getTeam().getName()) != null) {
				return factions.get(entity.getTeam().getName());
			}
		}

		if (entity instanceof EntityPlayer) {
			return this.factions.get(EDefaultFaction.PLAYERS.name());
		}

		if (entity instanceof MultiPartEntityPart && ((MultiPartEntityPart) entity).parent instanceof Entity) {
			return this.getFactionOf((Entity) ((MultiPartEntityPart) entity).parent);
		}
		if (entity.getControllingPassenger() != null) {
			return this.getFactionOf(entity.getControllingPassenger());
		}
		if (entity instanceof EntityTameable && ((EntityTameable) entity).getOwner() != null) {
			return this.getFactionOf(((EntityTameable) entity).getOwner());
		}

		if (entity instanceof IEntityOwnable && ((IEntityOwnable) entity).getOwner() != null) {
			return this.getFactionOf(((IEntityOwnable) entity).getOwner());
		}

		if (entity instanceof AbstractEntityCQR) {
			return ((AbstractEntityCQR) entity).getFaction();
		}

		// Faction overriding
		ResourceLocation registryName = EntityList.getKey(entity);
		if (registryName != null && this.entityFactionMap.containsKey(registryName)) {
			return this.entityFactionMap.get(registryName);
		}
		// Overriding end

		if (entity instanceof EntityArmorStand) {
			return this.factions.get(EDefaultFaction.ALL_ALLY.name());
		}

		if (entity instanceof EntityVillager || entity instanceof EntityGolem) {
			return this.factions.get(EDefaultFaction.VILLAGERS.name());
		}

		if (entity instanceof AbstractIllager || entity instanceof EntityVex) {
			return this.factions.get(EDefaultFaction.ILLAGERS.name());
		}

		if (entity instanceof EntityEnderman || entity instanceof EntityEndermite || entity instanceof EntityDragon) {
			return this.factions.get(EDefaultFaction.ENDERMEN.name());
		}

		if (entity instanceof EntityAnimal) {
			return this.factions.get(EDefaultFaction.NEUTRAL.name());
		}

		if (entity instanceof EntityMob) {
			return this.factions.get(EDefaultFaction.UNDEAD.name());
		}

		if (entity instanceof EntityWaterMob) {
			return this.factions.get(EDefaultFaction.TRITONS.name());
		}

		return null;
	}

	public FactionRegistry get() {
		return this;
	}

	public static FactionRegistry instance() {
		try {
			return instance.get();
		} catch (NullPointerException npe) {
			// Will also set the instance field
			new FactionRegistry();
			return instance.get();
		}
	}

	public CQRFaction getFactionInstance(String factionName) {
		if (this.factions.containsKey(factionName)) {
			return this.factions.get(factionName);
		}
		return this.factions.get(EDefaultFaction.NEUTRAL.name());
	}

	public EReputationStateRough getReputationOf(UUID playerID, CQRFaction faction) {
		return EReputationStateRough.getByRepuScore(this.getExactReputationOf(playerID, faction));
	}

	public int getExactReputationOf(UUID playerID, CQRFaction faction) {
		if (!faction.canRepuChange()) {
			return faction.getDefaultReputation().getValue();
		}
		if (playerID != null && this.playerFactionRepuMap.containsKey(playerID)) {
			if (this.playerFactionRepuMap.get(playerID).containsKey(faction.getName())) {
				return this.playerFactionRepuMap.get(playerID).get(faction.getName());
			}
		}
		return faction.getDefaultReputation().getValue();
	}

	void incrementRepuOf(EntityPlayer player, String faction, int score) {
		this.changeRepuOf(player, faction, Math.abs(score));
	}

	void decrementRepuOf(EntityPlayer player, String faction, int score) {
		this.changeRepuOf(player, faction, -Math.abs(score));
	}

	private void changeRepuOf(EntityPlayer player, String faction, int score) {
		// System.out.println("Changing repu...");

		/*
		 * boolean flag = false;
		 * if (this.canRepuChange(player)) {
		 * if (score < 0) {
		 * flag = this.canDecrementRepu(player, faction);
		 * } else {
		 * flag = this.canIncrementRepu(player, faction);
		 * }
		 * }
		 */
		if (this.canDecrementRepu(player, faction) || this.canIncrementRepu(player, faction)) {
			Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.computeIfAbsent(player.getPersistentID(), key -> new ConcurrentHashMap<>());
			int oldScore = factionsOfPlayer.getOrDefault(faction, this.factions.get(faction).getDefaultReputation().getValue());
			factionsOfPlayer.put(faction, oldScore + score);
			// CQRMain.logger.info("Repu changed!");

			// send packet to player
			if (player instanceof EntityPlayerMP) {
				this.sendRepuUpdatePacket((EntityPlayerMP) player, score + oldScore, faction);
			}
		}
		// System.out.println("Repu of " + player.getDisplayNameString() + " is " + this.getExactReputationOf(player.getPersistentID(), getFactionInstance(faction)));
	}

	private void sendRepuUpdatePacket(EntityPlayerMP player, int reputation, String faction) {
		// System.out.println("Sending update packet...");
		IMessage packet = new SPacketUpdatePlayerReputation(player, faction, reputation);
		CQRMain.NETWORK.sendTo(packet, player);
	}

	private boolean canDecrementRepu(EntityPlayer player, String faction) {
		if (this.canRepuChange(player)) {
			Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.getOrDefault(player.getPersistentID(), new ConcurrentHashMap<>());
			if (factionsOfPlayer != null) {
				if (factionsOfPlayer.containsKey(faction)) {
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
		if (this.canRepuChange(player)) {
			Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.getOrDefault(player.getPersistentID(), new ConcurrentHashMap<>());
			if (factionsOfPlayer != null) {
				if (factionsOfPlayer.containsKey(faction)) {
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
		return !(player.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL) || player.isCreative() || player.isSpectator() || this.uuidsBeingLoaded.contains(player.getPersistentID()));
	}

	public void handlePlayerLogin(EntityPlayerMP player) {
		String path = FileIOUtil.getAbsoluteWorldPath() + "/data/CQR/reputation/";
		File f = new File(path, player.getPersistentID() + ".nbt");
		CQRMain.logger.info("Loading player reputation...");
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				final UUID uuid = player.getPersistentID();
				if (f.exists()) {
					NBTTagCompound root = FileIOUtil.getRootNBTTagOfFile(f);
					// NBTTagList repuDataList = FileIOUtil.getOrCreateTagList(root, "reputationdata", Constants.NBT.TAG_COMPOUND);
					if (!root.isEmpty()) {
						while (FactionRegistry.this.uuidsBeingLoaded.contains(uuid)) {
							// Wait until the uuid isnt active
						}
						FactionRegistry.this.uuidsBeingLoaded.add(uuid);
						try {
							Map<String, Integer> mapping = FactionRegistry.this.playerFactionRepuMap.computeIfAbsent(player.getPersistentID(), key -> new ConcurrentHashMap<>());
							/*
							 * repuDataList.forEach(new Consumer<NBTBase>() {
							 * 
							 * @Override public void accept(NBTBase t) { NBTTagCompound tag = (NBTTagCompound) t; String fac = tag.getString("factionName"); if
							 * (FactionRegistry.this.factions.containsKey(fac)) { int reputation =
							 * tag.getInteger("reputation"); mapping.put(fac, reputation); } } });
							 */
							for (String key : root.getKeySet()) {
								try {
									int value = root.getInteger(key);
									mapping.put(key, value);
								} catch (Exception ex) {
									// Ignore
								}
							}
						} finally {
							FactionRegistry.this.uuidsBeingLoaded.remove(uuid);
						}
					}
				}

				// Send over factions and reputations
				IMessage packet = new SPacketInitialFactionInformation(uuid);
				CQRMain.NETWORK.sendTo(packet, player);
			}
		});
		t.setDaemon(true);
		t.start();

	}

	public void handlePlayerLogout(EntityPlayerMP player) {
		if (this.playerFactionRepuMap.containsKey(player.getPersistentID())) {
			CQRMain.logger.info("Saving player reputation...");
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					savePlayerReputation(player.getPersistentID(), true);
				}
			});
			t.setName("CQR-Reputation-Data-Saver");
			t.setDaemon(true);
			t.start();
		}
	}

	public void saveAllReputationData(final boolean removeMapsFromMemory) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				for (UUID playerID : FactionRegistry.this.playerFactionRepuMap.keySet()) {
					try {
						savePlayerReputation(playerID, removeMapsFromMemory);
					} catch (Exception ex) {
						System.out.println("Unable to save reputation data of " + playerID + "!");
						ex.printStackTrace();
					}
				}
			}
		});
		t.setName("CQR-Reputation-Data-Saver");
		t.setDaemon(true);
		t.start();
	}

	public boolean savePlayerReputation(final UUID playerID, final boolean removeFromMap) {
		Map<String, Integer> mapping = FactionRegistry.this.playerFactionRepuMap.get(playerID);
		// Map<String, Integer> entryMapping = new HashMap<>();
		final UUID uuid = playerID;
		String path = FileIOUtil.getAbsoluteWorldPath() + "/data/CQR/reputation/";
		File f = FileIOUtil.getOrCreateFile(path, uuid + ".nbt");
		if (f != null) {
			while (FactionRegistry.this.uuidsBeingLoaded.contains(uuid)) {
				// Wait until the uuid isnt active
			}
			FactionRegistry.this.uuidsBeingLoaded.add(uuid);
			try {
				NBTTagCompound root = FileIOUtil.getRootNBTTagOfFile(f);
				/*
				 * NBTTagList repuDataList = FileIOUtil.getOrCreateTagList(root, "reputationdata", Constants.NBT.TAG_COMPOUND); for (int i = 0; i < repuDataList.tagCount();
				 * i++) { NBTTagCompound tag = repuDataList.getCompoundTagAt(i); if
				 * (mapping.containsKey(tag.getString("factionName"))) { entryMapping.put(tag.getString("factionName"), i); } } for (Map.Entry<String, Integer> entry :
				 * mapping.entrySet()) { if (entryMapping.containsKey(entry.getKey())) {
				 * repuDataList.removeTag(entryMapping.get(entry.getKey())); } NBTTagCompound tag = new NBTTagCompound(); tag.setString("factionName", entry.getKey());
				 * tag.setInteger("reputation", entry.getValue());
				 * repuDataList.appendTag(tag); } root.removeTag("reputationdata"); root.setTag("reputationdata", repuDataList);
				 */
				for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
					root.setInteger(entry.getKey(), entry.getValue());
				}

				FileIOUtil.saveNBTCompoundToFile(root, f);
			} finally {
				if (removeFromMap) {
					FactionRegistry.this.playerFactionRepuMap.remove(playerID);
				}
				FactionRegistry.this.uuidsBeingLoaded.remove(uuid);
			}
		}
		return true;
	}

	public List<CQRFaction> getLoadedFactions() {
		return new ArrayList<>(this.factions.values());
	}

}
