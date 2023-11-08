package team.cqr.cqrepoured.mixin.entity;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.capability.faction.IFactionRelationCapability;
import team.cqr.cqrepoured.faction.EntityFactionInformation;
import team.cqr.cqrepoured.faction.IFactionRelatedEntity;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.util.WeakReferenceLazyLoadField;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IFactionRelatedEntity {
	
	@Unique
	private final WeakReferenceLazyLoadField<EntityFactionInformation> FACTION_INFORMATION = new WeakReferenceLazyLoadField<>(this::getEntityFactionInformation);

	public MixinLivingEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Override
	public EntityFactionInformation getEntityFactionInformation() {
		return CQRDatapackLoaders.getEntityFactionInformation(this.getType());
	}

	
	@Override
	public Optional<IFactionRelationCapability> getFactionOverrides() {
		LazyOptional<IFactionRelationCapability> opCap = this.getCapability(CQRCapabilities.FACTION_RELATION);
		if (opCap.isPresent()) {
			return opCap.resolve();
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<EntityFactionInformation> getEntityFactionSettings() {
		return Optional.ofNullable(this.FACTION_INFORMATION.get());
	}
	
}
