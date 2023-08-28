package team.cqr.cqrepoured.faction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.TextureSetNew;
import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.util.registration.AbstractRegistratableObject;

public class Faction extends AbstractRegistratableObject {
	
	private boolean repuMayChange = true;
	private Map<ResourceLocation, EReputationState> factionRelations;
	private Optional<TextureSetNew> textureSet = Optional.empty();
	private ReputationSettings reputationSettings;

	public static record ReputationSettings(EReputationState defaultReputation, int onKillMember, int onKillAlly, int onKillEnemy) {
		public static final Codec<ReputationSettings> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					EReputationState.CODEC.fieldOf("defaultReputation").forGetter(ReputationSettings::defaultReputation),
					Codec.INT.fieldOf("reputationChangeOnMemberKill").forGetter(ReputationSettings::onKillMember),
					Codec.INT.fieldOf("reputationChangeOnAllyKill").forGetter(ReputationSettings::onKillAlly),
					Codec.INT.fieldOf("reputationChangeOnEnemyKill").forGetter(ReputationSettings::onKillEnemy)
			).apply(instance, ReputationSettings::new);
		});
	}
	
	public static final Codec<Faction> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.unboundedMap(ResourceLocation.CODEC, EReputationState.CODEC).fieldOf("relations").forGetter(Faction::getRelations),
				Codec.BOOL.fieldOf("changeableReputation").forGetter(Faction::canRepuChange),
				ReputationSettings.CODEC.fieldOf("reputation").forGetter(Faction::getReputationSettings),
				CQRDatapackLoaders.TEXTURE_SETS.byNameCodec().optionalFieldOf("textureSet").forGetter(Faction::getTextureSet)
		).apply(instance, Faction::new);
	});
	
	public Faction(Map<ResourceLocation, EReputationState> factionRelations, boolean repuCanChange, ReputationSettings repuSettings, Optional<TextureSetNew> optTextureSet) {
		this.factionRelations = factionRelations;
		this.repuMayChange = repuCanChange;
		this.reputationSettings = repuSettings;
		this.textureSet = optTextureSet;
	}
	
	public ReputationSettings getReputationSettings() {
		return this.reputationSettings;
	}
	
	public Optional<TextureSetNew> getTextureSet() {
		return this.textureSet;
	}
	
	public int getRepuMemberKill() {
		return this.reputationSettings.onKillMember();
	}

	public int getRepuAllyKill() {
		return this.reputationSettings.onKillAlly();
	}

	public int getRepuEnemyKill() {
		return this.reputationSettings.onKillEnemy();
	}

	public EReputationState getDefaultReputation() {
		return this.reputationSettings.defaultReputation();
	}

	@Nullable
	public ResourceLocation getRandomTextureFor(Entity entity) {
		if (this.textureSet != null && this.textureSet.isPresent()) {
			return this.textureSet.get().getRandomTextureFor(entity);
		}
		// Debug
		// System.out.println("No texture set defined for faction: " + this.name);
		return null;
	}

	// DONE: Special case for player faction!!
	public boolean isEnemy(Entity ent) {
		if (CQRConfig.SERVER_CONFIG.advanced.enableOldFactionMemberTeams.get()) {
			if (ent.getTeam() != null && ent.getTeam().getName().equalsIgnoreCase(this.getId().toString())) {
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
	
	public EReputationState getReputationTowards(Faction faction) {
		return this.factionRelations.getOrDefault(faction.getId(), EReputationState.NEUTRAL);
	}
	
	public EReputationStateRough getRoughReputationTowards(Faction faction) {
		return EReputationStateRough.getByRepuScore(this.getReputationTowards(faction).getValue());
	}

	public boolean isEnemy(Faction faction) {
		if (faction == null) {
			return false;
		}
		if (faction == this) {
			return false;
		}
		return this.getRoughReputationTowards(faction).isEnemy();
	}

	// DONE: Special case for player faction!!
	public boolean isAlly(Entity ent) {
		if (CQRConfig.SERVER_CONFIG.advanced.enableOldFactionMemberTeams.get()) {
			if (ent.getTeam() != null && ent.getTeam().getName().equalsIgnoreCase(this.getId().toString())) {
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
		if (faction == null) {
			return false;
		}
		if (faction == this) {
			return true;
		}
		return this.getRoughReputationTowards(faction).isAlly();
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
	
	public Map<ResourceLocation, EReputationState> getRelations() {
		return this.factionRelations;
	}

}
