package team.cqr.cqrepoured.factions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.customtextures.TextureSet;
import team.cqr.cqrepoured.factions.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.data.FileIOUtil;

public class CQRFaction {

	private boolean savedGlobally = true;
	private boolean repuMayChange = true;
	private String name;
	private List<CQRFaction> allies = Collections.synchronizedList(new ArrayList<CQRFaction>());
	private List<CQRFaction> enemies = Collections.synchronizedList(new ArrayList<CQRFaction>());
	private EReputationState defaultRelation;
	private TextureSet textureSet = null;

	private int repuChangeOnMemberKill = 5;
	private int repuChangeOnAllyKill = 2;
	private int repuChangeOnEnemyKill = 1;

	public CQRFaction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, boolean canRepuChange) {
		this(name, defaultReputationState, canRepuChange, Optional.empty(), Optional.empty(), Optional.empty());
	}

	public CQRFaction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, boolean canRepuChange, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
		this(name, null, defaultReputationState, true, canRepuChange, repuChangeOnMemberKill, repuChangeOnAllyKill, repuChangeOnEnemyKill);
	}

	public CQRFaction(@Nonnull String name, TextureSet ctSet, @Nonnull EReputationState defaultReputationState, boolean saveGlobally, boolean canRepuChange, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
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

	public List<CQRFaction> getEnemies() {
		return this.enemies;
	}

	public List<CQRFaction> getAllies() {
		return this.allies;
	}

	public void addAlly(CQRFaction ally) {
		if (ally != null) {
			this.allies.add(ally);
		}
	}

	public void addEnemy(CQRFaction enemy) {
		if (enemy != null) {
			this.enemies.add(enemy);
		}
	}

	@Nullable
	public ResourceLocation getRandomTextureFor(Entity entity) {
		if (this.textureSet != null) {
			return this.textureSet.getRandomTextureFor(entity);
		}
		System.out.println("No texture set defined for faction: " + this.name);
		return null;
	}

	// DONE: Special case for player faction!!
	public boolean isEnemy(Entity ent) {
		if (CQRConfig.advanced.enableOldFactionMemberTeams) {
			if (ent.getTeam() != null && ent.getTeam().getName().equalsIgnoreCase(this.getName())) {
				return false;
			}
		}
		if (ent.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL)) {
			return false;
		}
		if (ent instanceof EntityPlayer) {
			// Special case for player
			return FactionRegistry.instance().getReputationOf(ent.getPersistentID(), this) == EReputationStateRough.ENEMY;
		}
		return this.isEnemy(FactionRegistry.instance().getFactionOf(ent));
	}

	public boolean isEnemy(AbstractEntityCQR ent) {
		if (ent.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL)) {
			return false;
		}
		return this.isEnemy(ent.getFaction());
	}

	public boolean isEnemy(CQRFaction faction) {
		if (faction == this || (faction != null && faction.getName().equalsIgnoreCase("ALL_ALLY"))) {
			return false;
		}
		if (faction != null) {
			for (CQRFaction str : this.enemies) {
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
			return FactionRegistry.instance().getReputationOf(ent.getPersistentID(), this) == EReputationStateRough.ALLY;
		}
		return this.isAlly(FactionRegistry.instance().getFactionOf(ent));
	}

	public boolean isAlly(AbstractEntityCQR ent) {
		return this.isAlly(ent.getFaction());
	}

	public boolean isAlly(CQRFaction faction) {
		if (faction == this || (faction != null && faction.getName().equalsIgnoreCase("ALL_ALLY"))) {
			return true;
		}
		if (faction != null) {
			for (CQRFaction str : this.allies) {
				if (str != null && faction.getName().equalsIgnoreCase(str.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public void decrementReputation(EntityPlayer player, int score) {
		if (this.repuMayChange) {
			FactionRegistry.instance().decrementRepuOf(player, this.name, score);
		}
	}

	public void incrementReputation(EntityPlayer player, int score) {
		if (this.repuMayChange) {
			FactionRegistry.instance().incrementRepuOf(player, this.name, score);
		}
	}

	public boolean canRepuChange() {
		return this.repuMayChange;
	}

	public void saveToFile(File folder) {
		if (this.savedGlobally) {
			// DONE: SAVE DATA
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					File file = FileIOUtil.getOrCreateFile(folder.getAbsolutePath(), CQRFaction.this.getName() + ".properties");
					Properties prop = new Properties();
					try (InputStream inputStream = new FileInputStream(file)) {
						prop.load(inputStream);
					} catch (IOException e) {
						CQRMain.logger.error("Failed to read file" + file.getName(), e);
						return;
					}
					prop.setProperty(ConfigKeys.FACTION_NAME_KEY, CQRFaction.this.name);
					prop.setProperty(ConfigKeys.FACTION_STATIC_REPUTATION_KEY, Boolean.toString(!CQRFaction.this.canRepuChange()));
					prop.setProperty(ConfigKeys.FACTION_REPU_DEFAULT, CQRFaction.this.getDefaultReputation().toString());
					prop.setProperty(ConfigKeys.FACTION_REPU_CHANGE_KILL_ALLY, Integer.toString(CQRFaction.this.getRepuAllyKill()));
					prop.setProperty(ConfigKeys.FACTION_REPU_CHANGE_KILL_MEMBER, Integer.toString(CQRFaction.this.getRepuMemberKill()));
					prop.setProperty(ConfigKeys.FACTION_REPU_CHANGE_KILL_ENEMY, Integer.toString(CQRFaction.this.getRepuEnemyKill()));
					String allies = "";
					for (CQRFaction af : CQRFaction.this.allies) {
						if (!allies.isEmpty()) {
							allies += ", ";
						}
						allies += af.getName();
					}
					prop.setProperty(ConfigKeys.FACTION_ALLIES_KEY, allies);
					String enemies = "";
					for (CQRFaction ef : CQRFaction.this.enemies) {
						if (!enemies.isEmpty()) {
							enemies += ", ";
						}
						enemies += ef.getName();
					}
					prop.setProperty(ConfigKeys.FACTION_ENEMIES_KEY, enemies);

					// Save file
					try {
						OutputStream out = new FileOutputStream(file);
						prop.store(out, "saved faction data");
					} catch (IOException ex) {
						CQRMain.logger.error("Failed to write to file" + file.getName(), ex);
						return;
					}
				}
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
		CQRFaction that = (CQRFaction) o;
		return this.name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
