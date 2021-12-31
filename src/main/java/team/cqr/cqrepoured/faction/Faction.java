package team.cqr.cqrepoured.faction;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.TextureSet;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.util.data.FileIOUtil;

public class Faction {

	private boolean savedGlobally = true;
	private boolean repuMayChange = true;
	private String name;
	private List<Faction> allies = Collections.synchronizedList(new ArrayList<Faction>());
	private List<Faction> enemies = Collections.synchronizedList(new ArrayList<Faction>());
	private EReputationState defaultRelation;
	private TextureSet textureSet = null;

	private int repuChangeOnMemberKill = 5;
	private int repuChangeOnAllyKill = 2;
	private int repuChangeOnEnemyKill = 1;

	public Faction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, boolean canRepuChange) {
		this(name, defaultReputationState, canRepuChange, Optional.empty(), Optional.empty(), Optional.empty());
	}

	public Faction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, boolean canRepuChange, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
		this(name, null, defaultReputationState, true, canRepuChange, repuChangeOnMemberKill, repuChangeOnAllyKill, repuChangeOnEnemyKill);
	}

	public Faction(@Nonnull String name, TextureSet ctSet, @Nonnull EReputationState defaultReputationState, boolean saveGlobally, boolean canRepuChange, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
		this.savedGlobally = saveGlobally;
		this.name = name;
		this.textureSet = ctSet;
		this.defaultRelation = defaultReputationState;
		this.repuMayChange = canRepuChange;
		this.repuChangeOnMemberKill = repuChangeOnMemberKill.isPresent() ? repuChangeOnMemberKill.get() : 5;
		this.repuChangeOnAllyKill = repuChangeOnAllyKill.isPresent() ? repuChangeOnAllyKill.get() : 2;
		this.repuChangeOnEnemyKill = repuChangeOnEnemyKill.isPresent() ? repuChangeOnEnemyKill.get() : 1;
	}

	public int getRepuMemberKill() {
		return this.repuChangeOnMemberKill;
	}

	public int getRepuAllyKill() {
		return this.repuChangeOnAllyKill;
	}

	public int getRepuEnemyKill() {
		return this.repuChangeOnEnemyKill;
	}

	public String getName() {
		return this.name;
	}

	public boolean isSavedGlobally() {
		return this.savedGlobally;
	}

	public EReputationState getDefaultReputation() {
		return this.defaultRelation;
	}

	public List<Faction> getEnemies() {
		return this.enemies;
	}

	public List<Faction> getAllies() {
		return this.allies;
	}

	public void addAlly(Faction ally) {
		if (ally != null) {
			this.allies.add(ally);
		}
	}

	public void addEnemy(Faction enemy) {
		if (enemy != null) {
			this.enemies.add(enemy);
		}
	}

	@Nullable
	public ResourceLocation getRandomTextureFor(Entity entity) {
		if (this.textureSet != null) {
			return this.textureSet.getRandomTextureFor(entity);
		}
		// Debug
		// System.out.println("No texture set defined for faction: " + this.name);
		return null;
	}

	// DONE: Special case for player faction!!
	public boolean isEnemy(Entity ent) {
		if (CQRConfig.advanced.enableOldFactionMemberTeams) {
			if (ent.getTeam() != null && ent.getTeam().getName().equalsIgnoreCase(this.getName())) {
				return false;
			}
		}
		if (ent.getEntityWorld().getDifficulty() == EnumDifficulty.PEACEFUL) {
			return false;
		}
		if (ent instanceof EntityPlayer) {
			// Special case for player
			return FactionRegistry.instance(ent).getReputationOf(ent.getPersistentID(), this) == EReputationStateRough.ENEMY;
		}
		return this.isEnemy(FactionRegistry.instance(ent).getFactionOf(ent));
	}

	public boolean isEnemy(AbstractEntityCQR ent) {
		if (ent.getEntityWorld().getDifficulty() == EnumDifficulty.PEACEFUL) {
			return false;
		}
		return this.isEnemy(ent.getFaction());
	}

	public boolean isEnemy(Faction faction) {
		if (faction == this || (faction != null && faction.getName().equalsIgnoreCase("ALL_ALLY"))) {
			return false;
		}
		if (faction != null && faction.getName().equalsIgnoreCase("ALL_ENEMY")) {
			return true;
		}
		if (faction != null) {
			for (Faction str : this.enemies) {
				if (str != null && faction.getName().equalsIgnoreCase(str.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	// DONE: Special case for player faction!!
	public boolean isAlly(Entity ent) {
		if (CQRConfig.advanced.enableOldFactionMemberTeams) {
			if (ent.getTeam() != null && ent.getTeam().getName().equalsIgnoreCase(this.getName())) {
				return true;
			}
		}
		if (ent instanceof EntityPlayer) {
			// Special case for player
			return FactionRegistry.instance(ent).getReputationOf(ent.getPersistentID(), this) == EReputationStateRough.ALLY;
		}
		return this.isAlly(FactionRegistry.instance(ent).getFactionOf(ent));
	}

	public boolean isAlly(AbstractEntityCQR ent) {
		return this.isAlly(ent.getFaction());
	}

	public boolean isAlly(Faction faction) {
		if (faction == this || (faction != null && faction.getName().equalsIgnoreCase("ALL_ALLY"))) {
			return true;
		}
		if (faction != null) {
			for (Faction str : this.allies) {
				if (str != null && faction.getName().equalsIgnoreCase(str.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public void decrementReputation(EntityPlayer player, int score) {
		if (this.repuMayChange) {
			FactionRegistry.instance(player).decrementRepuOf(player, this.name, score);
		}
	}

	public void incrementReputation(EntityPlayer player, int score) {
		if (this.repuMayChange) {
			FactionRegistry.instance(player).incrementRepuOf(player, this.name, score);
		}
	}

	public boolean canRepuChange() {
		return this.repuMayChange;
	}

	public void saveToFile(File folder) {
		if (this.savedGlobally) {
			// DONE: SAVE DATA
			Thread t = new Thread(() -> {
				Properties prop = new Properties();
				prop.setProperty(ConfigKeys.FACTION_NAME_KEY, this.name);
				prop.setProperty(ConfigKeys.FACTION_STATIC_REPUTATION_KEY, Boolean.toString(!this.canRepuChange()));
				prop.setProperty(ConfigKeys.FACTION_REPU_DEFAULT, this.getDefaultReputation().toString());
				prop.setProperty(ConfigKeys.FACTION_REPU_CHANGE_KILL_ALLY, Integer.toString(this.getRepuAllyKill()));
				prop.setProperty(ConfigKeys.FACTION_REPU_CHANGE_KILL_MEMBER, Integer.toString(this.getRepuMemberKill()));
				prop.setProperty(ConfigKeys.FACTION_REPU_CHANGE_KILL_ENEMY, Integer.toString(this.getRepuEnemyKill()));
				prop.setProperty(ConfigKeys.FACTION_ALLIES_KEY, this.allies.stream().map(Faction::getName).collect(Collectors.joining(", ")));
				prop.setProperty(ConfigKeys.FACTION_ENEMIES_KEY, this.enemies.stream().map(Faction::getName).collect(Collectors.joining(", ")));

				// Save file
				File file = new File(folder, this.getName() + ".properties");
				FileIOUtil.writePropToFile(prop, file);
			});
			t.setDaemon(true);
			t.start();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		Faction that = (Faction) o;
		return this.name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
