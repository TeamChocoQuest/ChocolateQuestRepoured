package team.cqr.cqrepoured.faction.services.impl;

import java.util.Optional;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.common.services.interfaces.FactionService;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.capability.IFactionRelationCapability;
import team.cqr.cqrepoured.faction.init.FactionCapabilities;
import team.cqr.cqrepoured.faction.init.FactionDatapackLoaders;

public class FactionServiceImpl implements FactionService {
	
	protected Optional<IFactionRelationCapability> getFactionRelationOf(Entity entity) {
		LazyOptional<IFactionRelationCapability> lOpCap = entity.getCapability(FactionCapabilities.FACTION_RELATION);
		if (lOpCap.isPresent()) {
			return lOpCap.resolve();
		}
		return Optional.empty();
	}

	@Override
	public void setReputation(Entity entity, ResourceLocation faction, int value) {
			Optional<IFactionRelationCapability> opCap = this.getFactionRelationOf(entity);
			if (opCap.isPresent()) {
				IFactionRelationCapability relationCap = opCap.get();
				relationCap.setReputationTowards(faction, value);
			}
	}
	
	@Override
	public Optional<Integer> getReputation(Entity entity, ResourceLocation faction) {
		Optional<IFactionRelationCapability> opCap = this.getFactionRelationOf(entity);
		if (opCap.isPresent()) {
			IFactionRelationCapability relationCap = opCap.get();
			Optional<Faction> optFactionObj = FactionDatapackLoaders.getFaction(faction);
			if (optFactionObj.isPresent() && relationCap.hasInformationFor(optFactionObj.get())) {
				return Optional.of(relationCap.getExactRelationTowards(optFactionObj.get()));
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean hasFactionCapabiltiy(Entity entity) {
		return this.getFactionRelationOf(entity).isPresent();
	}

}
