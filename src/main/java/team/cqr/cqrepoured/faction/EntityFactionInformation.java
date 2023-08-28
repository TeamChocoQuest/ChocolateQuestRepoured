package team.cqr.cqrepoured.faction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.util.registration.AbstractRegistratableObject;

public class EntityFactionInformation extends AbstractRegistratableObject implements IFactionRelated {
	
	private final Map<Faction, EReputationState> reputationMapping;
	private final Optional<List<Faction>> memberFactions; 
	private final EReputationState fallbackReputation;
	private final boolean canChangeReputation;
	
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
				}), 
				Codec.BOOL.fieldOf("canChangeReputation").forGetter(EntityFactionInformation::canChangeReputation)
		).apply(instance, EntityFactionInformation::new);
	});
	
	EntityFactionInformation(Map<Faction, EReputationState> reputationMapping, EReputationState fallbackReputation, Optional<List<Faction>> memberFactions, boolean canChangeReputation) {
		this.reputationMapping = reputationMapping;
		this.fallbackReputation = fallbackReputation;
		this.memberFactions = memberFactions;
		this.canChangeReputation = canChangeReputation;
	}
	
	public boolean canChangeReputation() {
		return this.canChangeReputation;
	}

	@Override
	public boolean isMemberOf(Faction faction) {
		return this.memberFactions.isPresent() && this.memberFactions.get().contains(faction);
	}

	@Override
	public EReputationState getRelationTowards(Faction faction) {
		return this.reputationMapping.getOrDefault(faction, fallbackReputation);
	}

}
