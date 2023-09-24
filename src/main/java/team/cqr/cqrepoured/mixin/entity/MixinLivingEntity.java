package team.cqr.cqrepoured.mixin.entity;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.capability.faction.IFactionRelationCapability;
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
		return this.getRelevantObjectForFactionHandling().isAllyOf(faction);
	}

	@Override
	public boolean isEnemyOf(Faction faction) {
		return this.getRelevantObjectForFactionHandling().isEnemyOf(faction);
	}

	@Override
	public boolean isMemberOf(Faction faction) {
		return this.getRelevantObjectForFactionHandling().isMemberOf(faction);
	}
	
	@Unique
	private IFactionRelated getRelevantObjectForFactionHandling() {
		IFactionRelated result = null;
		if (this.FACTION_INFORMATION.get() != null && !this.FACTION_INFORMATION.get().canChangeReputation()) {
			return this.FACTION_INFORMATION.get();
		}
		if (this.getCapability(CQRCapabilities.FACTION_RELATION).isPresent()) {
			Optional<IFactionRelationCapability> opCap = this.getCapability(CQRCapabilities.FACTION_RELATION).resolve();
			if (opCap.isPresent()) {
				result = opCap.get();
			}
		}
		if (result == null) {
			result = this.FACTION_INFORMATION.get();
		}
		if (result == null) {
			// RETURN A FALLBACK
			result = IFactionRelated._FALLBACK;
		}
		return result;
	}

	@Override
	public int getExactRelationTowards(Faction faction) {
		return this.getRelevantObjectForFactionHandling().getExactRelationTowards(faction);
	}

}
