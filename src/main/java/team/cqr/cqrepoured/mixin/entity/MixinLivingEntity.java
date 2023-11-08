package team.cqr.cqrepoured.mixin.entity;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.capability.faction.IFactionRelationCapability;
import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.faction.EntityFactionInformation;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.IFactionRelated;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.util.WeakReferenceLazyLoadField;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IFactionRelated {
	
	@Unique
	private final WeakReferenceLazyLoadField<EntityFactionInformation> FACTION_INFORMATION = new WeakReferenceLazyLoadField<>(this::getEntityFactionInformation);

	public MixinLivingEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Unique
	private EntityFactionInformation getEntityFactionInformation() {
		return CQRDatapackLoaders.getEntityFactionInformation(this.getType());
	}

	@Override
	public boolean isAllyOf(Faction faction) {
		return this.getRelevantFactionObject(faction).isAllyOf(faction);
	}

	@Override
	public boolean isEnemyOf(Faction faction) {
		return this.getRelevantFactionObject(faction).isEnemyOf(faction);
	}

	@Override
	public boolean isMemberOf(Faction faction) {
		return this.getRelevantFactionObject(faction).isMemberOf(faction);
	}
	
	@Unique
	private Optional<IFactionRelationCapability> getFactionOverrides() {
		LazyOptional<IFactionRelationCapability> opCap = this.getCapability(CQRCapabilities.FACTION_RELATION);
		if (opCap.isPresent()) {
			return opCap.resolve();
		}
		return Optional.empty();
	}
	
	@Unique
	private Optional<EntityFactionInformation> getEntityFactionSettings() {
		return Optional.ofNullable(this.FACTION_INFORMATION.get());
	}
	
	private IFactionRelated getRelevantFactionObject(Faction faction) {
		List<Optional<? extends IFactionRelated>> candidates = List.of(this.getFactionOverrides(), this.getEntityFactionSettings());
		candidates.removeIf(opt -> {
			if (opt.isEmpty()) {
				return true;
			}
			if (opt.get() instanceof IFactionRelationCapability ifrc) {
				return !ifrc.hasInformationFor(faction);
			}
			if (opt.get() instanceof EntityFactionInformation efi) {
				return !efi.hasInformationFor(faction);
			}
			return false;
		});
		if (candidates.isEmpty()) {
			return _FALLBACK;
		} else {
			for (int i = 0; i < candidates.size(); i++) {
				if (candidates.get(i).isPresent()) {
					return candidates.get(i).get();
				}
			}
		}
		return _FALLBACK;
	}

	@Override
	public int getExactRelationTowards(Faction faction) {
		return this.getRelevantFactionObject(faction).getExactRelationTowards(faction);
	}
	
	public EReputationStateRough getRoughReputationOf(Entity entity) {
		if (this.getFactionOverrides().isPresent()) {
			return this.getFactionOverrides().get().getRoughReputationOf(entity);
		} else if (this.getEntityFactionSettings().isPresent()) {
			return this.getEntityFactionSettings().get().getRoughReputationOf(entity);
		} else {
			return _FALLBACK.getRoughReputationOf(entity);
		}
	}

}
