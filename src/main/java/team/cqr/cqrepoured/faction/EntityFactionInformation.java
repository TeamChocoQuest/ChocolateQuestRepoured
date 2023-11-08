package team.cqr.cqrepoured.faction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.entity.Entity;
import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.util.registration.AbstractRegistratableObject;

public class EntityFactionInformation extends AbstractRegistratableObject implements IFactionRelated {
	
	private final Map<Faction, EReputationState> reputationMapping;
	private final Optional<List<Faction>> memberFactions; 
	private final EReputationState fallbackReputation;
	
	public static final Codec<EntityFactionInformation> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.unboundedMap(CQRDatapackLoaders.FACTIONS.byNameCodec(), EReputationState.CODEC).fieldOf("relations").forGetter((efi) -> {
					return efi.reputationMapping;
				}),
				EReputationState.CODEC.fieldOf("defaultReputation").forGetter((efi) -> {
					return efi.fallbackReputation;
				}),
				CQRDatapackLoaders.FACTIONS.byNameCodec().listOf().optionalFieldOf("memberships").forGetter((efi) -> {
					return efi.memberFactions;
				})
		).apply(instance, EntityFactionInformation::new);
	});
	
	EntityFactionInformation(Map<Faction, EReputationState> reputationMapping, EReputationState fallbackReputation, Optional<List<Faction>> memberFactions) {
		this.reputationMapping = reputationMapping;
		this.fallbackReputation = fallbackReputation;
		this.memberFactions = memberFactions;
	}
	
	@Override
	public boolean isMemberOf(Faction faction) {
		return this.memberFactions.isPresent() && this.memberFactions.get().contains(faction);
	}

	@Override
	public EReputationState getRelationTowards(Faction faction) {
		return this.reputationMapping.getOrDefault(faction, fallbackReputation);
	}

	@Override
	public int getExactRelationTowards(Faction faction) {
		return this.getRelationTowards(faction).getValue();
	}
	
	public EReputationStateRough getRoughReputationOf(Entity entity, Map<Faction, EReputationState> reputationMap) {
		if (entity instanceof IFactionRelated other) {
			return this.getRoughReputationOf(other, reputationMap);
		} else {
			EntityFactionInformation efi = CQRDatapackLoaders.getEntityFactionInformation(entity.getType());
			if (efi != null) {
				return this.getRoughReputationOf(efi, reputationMap);
			}
			return EReputationStateRough.NEUTRAL;
		}
	}
	
	public boolean hasInformationFor(Faction faction) {
		return (!this.memberFactions.isEmpty() && this.memberFactions.get().contains(faction)) || this.reputationMapping.containsKey(faction);
	}

	@Override
	public EReputationStateRough getRoughReputationOf(Entity entity) {
		return getRoughReputationOf(entity, Map.of());
	}

	private EReputationStateRough getRoughReputationOf(IFactionRelated other, Map<Faction, EReputationState> reputationMap) {
		int score = 0;
		if (this.memberFactions.isPresent()) {
			for (Faction faction : this.memberFactions.get()) {
				if (other.isAllyOf(faction)) {
					score++;
				} else if (other.isEnemyOf(faction)) {
					score--;
				}
			}
			score *= 2;
		}
		// Now the factions that we have a reputation towards...
		for (Map.Entry<Faction, EReputationState> entry : this.reputationMapping.entrySet()) {
			if (!reputationMap.containsKey(entry.getKey()) || (reputationMap.containsKey(entry.getKey()) && reputationMap.get(entry.getKey()) == null)) {
				reputationMap.put(entry.getKey(), entry.getValue());
			}
		}
		for (Map.Entry<Faction, EReputationState> entry : reputationMap.entrySet()) {
			EReputationStateRough roughReputation = EReputationStateRough.getByRepuScore(entry.getValue().getValue());
			int scoreTmp = 0;
			if (other.isAllyOf(entry.getKey())) {
				scoreTmp++;
			} else if(other.isEnemyOf(entry.getKey())) {
				scoreTmp--;
			}
			if (roughReputation.isEnemy()) {
				scoreTmp *= -1;
			}
			score += scoreTmp;
		}
		
		if (score == 0) {
			return EReputationStateRough.NEUTRAL;
		} else if (score > 0) {
			return EReputationStateRough.ALLY;
		} else {
			return EReputationStateRough.ENEMY;
		}
	}

}
