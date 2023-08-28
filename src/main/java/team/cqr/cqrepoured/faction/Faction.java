package team.cqr.cqrepoured.faction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.TextureSetNew;
import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.util.registration.AbstractRegistratableObject;

public class Faction /*extends AbstractRegistratableObject */{
	
	private boolean repuMayChange = true;
	private List<Faction> allies = Collections.synchronizedList(new ArrayList<Faction>());
	private List<Faction> enemies = Collections.synchronizedList(new ArrayList<Faction>());
	private EReputationState defaultRelation;
	private TextureSetNew textureSet = null;

	private int repuChangeOnMemberKill = 5;
	private int repuChangeOnAllyKill = 2;
	private int repuChangeOnEnemyKill = 1;

	public Faction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, boolean canRepuChange) {
		this(name, defaultReputationState, canRepuChange, Optional.empty(), Optional.empty(), Optional.empty());
	}

	public Faction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, boolean canRepuChange, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
		this(name, null, defaultReputationState, true, canRepuChange, repuChangeOnMemberKill, repuChangeOnAllyKill, repuChangeOnEnemyKill);
	}

	public Faction(@Nonnull String name, TextureSetNew ctSet, @Nonnull EReputationState defaultReputationState, boolean saveGlobally, boolean canRepuChange, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
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

	public String getId() {
		return null;
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
		if (CQRConfig.SERVER_CONFIG.advanced.enableOldFactionMemberTeams.get()) {
			if (ent.getTeam() != null && ent.getTeam().getName().equalsIgnoreCase(this.getId())) {
				return false;
			}
		}
		if (ent.level().getDifficulty() == Difficulty.PEACEFUL) {
			return false;
		}
		if (ent instanceof Player) {
			// Special case for player
			return FactionRegistry.instance(ent).getReputationOf(ent.getUUID(), this) == EReputationStateRough.ENEMY;
		}
		return this.isEnemy(FactionRegistry.instance(ent).getFactionOf(ent));
	}

	public boolean isEnemy(IHasFaction ent) {
		if (ent.getLevel().getDifficulty() == Difficulty.PEACEFUL) {
			return false;
		}
		return this.isEnemy(ent.getFaction());
	}

	public boolean isEnemy(Faction faction) {
		if (faction == this || (faction != null && faction.getId().equalsIgnoreCase("ALL_ALLY"))) {
			return false;
		}
		if (faction != null && faction.getId().equalsIgnoreCase("ALL_ENEMY")) {
			return true;
		}
		if (faction != null) {
			for (Faction str : this.enemies) {
				if (str != null && faction.getId().equalsIgnoreCase(str.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	// DONE: Special case for player faction!!
	public boolean isAlly(Entity ent) {
		if (CQRConfig.SERVER_CONFIG.advanced.enableOldFactionMemberTeams.get()) {
			if (ent.getTeam() != null && ent.getTeam().getName().equalsIgnoreCase(this.getId())) {
				return true;
			}
		}
		if (ent instanceof Player) {
			// Special case for player
			return FactionRegistry.instance(ent).getReputationOf(ent.getUUID(), this) == EReputationStateRough.ALLY;
		}
		return this.isAlly(FactionRegistry.instance(ent).getFactionOf(ent));
	}

	public boolean isAlly(IHasFaction ent) {
		return this.isAlly(ent.getFaction());
	}

	public boolean isAlly(Faction faction) {
		if (faction == this || (faction != null && faction.getId().equalsIgnoreCase("ALL_ALLY"))) {
			return true;
		}
		if (faction != null) {
			for (Faction str : this.allies) {
				if (str != null && faction.getId().equalsIgnoreCase(str.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	public void decrementReputation(Player player, int score) {
		if (this.repuMayChange) {
			FactionRegistry.instance(player).decrementRepuOf(player, this.getId(), score);
		}
	}

	public void incrementReputation(Player player, int score) {
		if (this.repuMayChange) {
			FactionRegistry.instance(player).incrementRepuOf(player, this.getId(), score);
		}
	}

	public boolean canRepuChange() {
		return this.repuMayChange;
	}

}
