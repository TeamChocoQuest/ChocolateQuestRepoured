package team.cqr.cqrepoured.faction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.TextureSet;
import team.cqr.cqrepoured.customtextures.TextureSetManager;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.network.server.packet.SPacketInitialFactionInformation;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdatePlayerReputation;
import team.cqr.cqrepoured.util.PropertyFileHelper;
import team.cqr.cqrepoured.util.data.FileIOUtil;

public class FactionRegistry {

	// TODO create IFactionRegistry interface to not have client/server methods available on the other side
	private static final FactionRegistry CLIENT_INSTANCE = new FactionRegistry();
	private static final FactionRegistry SERVER_INSTANCE = new FactionRegistry();

	public static final DummyFaction DUMMY_FACTION = new DummyFaction();
	public static final int LOWEST_REPU = EReputationState.ARCH_ENEMY.getValue();
	public static final int HIGHEST_REPU = EReputationState.MEMBER.getValue();

	private final Map<String, Faction> factions = new HashMap<>();
	private final Map<UUID, Object2IntMap<String>> playerFactionRepuMap = new HashMap<>();
	private final Map<Class<? extends Entity>, Faction> entityFactionMap = new HashMap<>();

	public static FactionRegistry getClientInstance() {
		return CLIENT_INSTANCE;
	}

	public static FactionRegistry getServerInstance() {
		return SERVER_INSTANCE;
	}

	public static FactionRegistry instance(World world) {
		return world.isRemote ? CLIENT_INSTANCE : SERVER_INSTANCE;
	}

	public static FactionRegistry instance(Entity entity) {
		return instance(entity.world);
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

		this.factions.put(DUMMY_FACTION.getName(), DUMMY_FACTION);
		this.entityFactionMap.put(Entity.class, DUMMY_FACTION);

		this.loadFactionsInConfigFolder();
		this.loadDefaultFactions();

		this.loadEntityFactionRelations();
	}

	@SideOnly(Side.CLIENT)
	public void addFaction(Faction faction) {
		this.factions.put(faction.getName(), faction);
	}

	@SideOnly(Side.CLIENT)
	public void setReputation(UUID player, int reputation, Faction faction) {
		if (faction.canRepuChange()) {
			Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.computeIfAbsent(player, key -> new Object2IntOpenHashMap<>());
			factionsOfPlayer.put(faction.getName(), reputation);
			// System.out.println("Repu of " + player.toString() + " set to " + reputation + " for faction " + faction.getName());
		}
	}

	// Variant on the server, used by the command
	public void changeReputationTo(@Nonnull EntityPlayerMP player, int reputation, @Nonnull Faction faction) {
		Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.computeIfAbsent(player.getPersistentID(), key -> new Object2IntOpenHashMap<>());
		factionsOfPlayer.put(faction.getName(), reputation);

		this.sendRepuUpdatePacket(player, reputation, faction.getName());
	}

	private void loadEntityFactionRelations() {
		for (String s : CQRConfig.general.entityFactionRelation) {
			int i = s.indexOf('=');
			if (i == -1) {
				CQRMain.logger.warn("Invalid entity-faction relation \"{}\"! Format is incorrect!", s);
				continue;
			}
			ResourceLocation registryName = new ResourceLocation(s.substring(0, i).trim());
			EntityEntry entry = ForgeRegistries.ENTITIES.getValue(registryName);
			if (entry == null) {
				CQRMain.logger.warn("Invalid entity-faction relation \"{}\"! Entity does not exists!", s);
				continue;
			}
			if (this.entityFactionMap.containsKey(entry.getClass())) {
				CQRMain.logger.warn("Invalid entity-faction relation \"{}\"! Entity already has an assigned faction!", s);
				continue;
			}
			Faction faction = this.factions.get(s.substring(i + 1).trim());
			if (faction == null) {
				CQRMain.logger.warn("Invalid entity-faction relation \"{}\"! Faction does not exists!", s);
				continue;
			}
			this.entityFactionMap.put(entry.getEntityClass(), faction);
		}
	}

	private void loadFactionsInConfigFolder() {
		// DONE: Load factions from files
		List<File> files = new ArrayList<>(FileUtils.listFiles(CQRMain.CQ_FACTION_FOLDER, new String[] { "cfg", "prop", "properties" }, true));
		int fileCount = files.size();
		if (fileCount > 0) {
			List<String> fIDs = new ArrayList<>(fileCount + 1);
			List<List<String>> allyTmp = new ArrayList<>();
			List<List<String>> enemyTmp = new ArrayList<>();
			boolean flag = true;
			for (int i = 0; i < fileCount; i++) {
				File file = files.get(i);
				Properties prop = new Properties();
				try (InputStream inputStream = new FileInputStream(file)) {
					prop.load(inputStream);
					flag = true;
				} catch (IOException e) {
					CQRMain.logger.error("Failed to load file {}", file.getName(), e);
					flag = false;
					continue;
				}
				if (flag) {
					List<String> fAlly = new ArrayList<>();
					List<String> fEnemy = new ArrayList<>();
					// CQRFaction fTmp =
					String fName = prop.getProperty(ConfigKeys.FACTION_NAME_KEY);
					if (fName == null || this.factions.containsKey(fName)) {
						continue;
					}
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

					Faction f = new Faction(fName, ts, defRepu, true, !staticRepu, optionMember, optionAlly, optionEnemy);
					this.factions.put(fName, f);
				}
			}
			for (int i = 0; i < fIDs.size(); i++) {
				String name = fIDs.get(i);
				Faction fac = this.factions.get(name);
				for (String s : allyTmp.get(i)) {
					fac.addAlly(this.factions.getOrDefault(s, null));
				}
				for (String s : enemyTmp.get(i)) {
					fac.addEnemy(this.factions.getOrDefault(s, null));
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

			Faction fac = new Faction(edf.name(), null, edf.getDefaultReputation(), false, edf.canRepuChange(), optionMember, optionAlly, optionEnemy);
			this.factions.put(edf.name(), fac);
		}

		for (int i : indices) {
			String name = EDefaultFaction.values()[i].name();
			Faction fac = this.factions.get(name);
			for (int j = 0; j < allies[i].length; j++) {
				fac.addAlly(this.factions.get(allies[i][j]));
			}
			for (int j = 0; j < enemies[i].length; j++) {
				fac.addEnemy(this.factions.get(enemies[i][j]));
			}
		}

		CQRMain.logger.info("Default factions loaded and initialized!");
	}

	public Faction getFactionOf(@Nullable Entity entity) {
		if (entity == null) {
			return FactionRegistry.DUMMY_FACTION;
		}

		if (CQRConfig.advanced.enableOldFactionMemberTeams) {
			if (entity.getTeam() != null) {
				Faction teamFaction = this.factions.get(entity.getTeam().getName());
				if (teamFaction != null) {
					return teamFaction;
				}
			}
		}

		if (entity instanceof MultiPartEntityPart && ((MultiPartEntityPart) entity).parent instanceof Entity) {
			return this.getFactionOf((Entity) ((MultiPartEntityPart) entity).parent);
		}

		if (entity instanceof AbstractEntityCQR) {
			return ((AbstractEntityCQR) entity).getFaction();
		}

		return this.getFactionOf(entity.getClass());
	}

	@SuppressWarnings("unchecked")
	private Faction getFactionOf(Class<? extends Entity> entityClass) {
		if(entityClass == null) {
			CQRMain.logger.error("Class of entity is null! This should never happen!");
			return null;
		}
		
		Faction faction = this.entityFactionMap.get(entityClass);
		if (faction == null && entityClass != Entity.class) {
			faction = this.getFactionOf((Class<? extends Entity>) entityClass.getSuperclass());
			this.entityFactionMap.put(entityClass, faction);
		}
		return faction;
	}

	@Nullable
	public Faction getFactionInstance(String factionName) {
		return this.factions.get(factionName);
	}

	public EReputationStateRough getReputationOf(UUID playerID, Faction faction) {
		return EReputationStateRough.getByRepuScore(this.getExactReputationOf(playerID, faction));
	}

	public int getExactReputationOf(UUID playerID, Faction faction) {
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
		 * boolean flag = false; if (this.canRepuChange(player)) { if (score < 0) { flag = this.canDecrementRepu(player,
		 * faction); } else { flag =
		 * this.canIncrementRepu(player, faction); } }
		 */
		if (this.canDecrementRepu(player, faction) || this.canIncrementRepu(player, faction)) {
			Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.computeIfAbsent(player.getPersistentID(), key -> new Object2IntOpenHashMap<>());
			int oldScore = factionsOfPlayer.getOrDefault(faction, this.factions.get(faction).getDefaultReputation().getValue());
			factionsOfPlayer.put(faction, oldScore + score);
			// CQRMain.logger.info("Repu changed!");

			// send packet to player
			if (player instanceof EntityPlayerMP) {
				this.sendRepuUpdatePacket((EntityPlayerMP) player, score + oldScore, faction);
			}
		}
		// System.out.println("Repu of " + player.getDisplayNameString() + " is " +
		// this.getExactReputationOf(player.getPersistentID(), getFactionInstance(faction)));
	}

	private void sendRepuUpdatePacket(EntityPlayerMP player, int reputation, String faction) {
		// System.out.println("Sending update packet...");
		IMessage packet = new SPacketUpdatePlayerReputation(player, faction, reputation);
		CQRMain.NETWORK.sendTo(packet, player);
	}

	private boolean canDecrementRepu(EntityPlayer player, String faction) {
		if (this.canRepuChange(player)) {
			Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.getOrDefault(player.getPersistentID(), new Object2IntOpenHashMap<>());
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
			Map<String, Integer> factionsOfPlayer = this.playerFactionRepuMap.getOrDefault(player.getPersistentID(), new Object2IntOpenHashMap<>());
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
		return player.getEntityWorld().getDifficulty() != EnumDifficulty.PEACEFUL && !player.isCreative() && !player.isSpectator();
	}

	public void loadPlayerReputationData(EntityPlayer player) {
		CQRMain.logger.info("Loading player reputation...");

		UUID uuid = player.getPersistentID();
		File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), "data/CQR/reputation");
		File file = new File(folder, uuid + ".nbt");
		if (file.exists()) {
			NBTTagCompound root = FileIOUtil.readNBTFromFile(file);
			Map<String, Integer> mapping = this.playerFactionRepuMap.computeIfAbsent(uuid, key -> new Object2IntOpenHashMap<>());
			for (String factionName : root.getKeySet()) {
				int value = root.getInteger(factionName);
				mapping.put(factionName, value);
			}
		}
	}

	public void syncPlayerReputationData(EntityPlayerMP player) {
		// Send over factions and reputations
		CQRMain.NETWORK.sendTo(new SPacketInitialFactionInformation(player.getPersistentID(), this.getLoadedFactions(), this.playerFactionRepuMap.getOrDefault(player.getPersistentID(), Object2IntMaps.emptyMap())), player);
	}

	public void savePlayerReputationData(EntityPlayerMP player) {
		if (this.playerFactionRepuMap.containsKey(player.getPersistentID())) {
			CQRMain.logger.info("Saving player reputation...");
			this.savePlayerReputation(player.getPersistentID(), true);
		}
	}

	public void saveAllReputationData(final boolean removeMapsFromMemory) {
		for (UUID playerID : this.playerFactionRepuMap.keySet()) {
			this.savePlayerReputation(playerID, removeMapsFromMemory);
		}
	}

	public void savePlayerReputation(final UUID playerID, final boolean removeFromMap) {
		Map<String, Integer> mapping = this.playerFactionRepuMap.get(playerID);
		NBTTagCompound root = new NBTTagCompound();
		for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
			root.setInteger(entry.getKey(), entry.getValue());
		}

		File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), "data/CQR/reputation");
		File file = new File(folder, playerID + ".nbt");
		FileIOUtil.writeNBTToFile(root, file);

		if (removeFromMap) {
			this.playerFactionRepuMap.remove(playerID);
		}
	}

	public Collection<Faction> getLoadedFactions() {
		return Collections.unmodifiableCollection(this.factions.values());
	}

}
