package team.cqr.cqrepoured.entity.bases;

import java.util.List;
import java.util.Optional;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import team.cqr.cqrepoured.capability.faction.IFactionRelationCapability;
import team.cqr.cqrepoured.entity.ai.sensor.NearbyAlliesSensor;
import team.cqr.cqrepoured.faction.EntityFactionInformation;
import team.cqr.cqrepoured.faction.IFactionRelatedEntity;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.util.WeakReferenceLazyLoadField;

public class FactionEntity<T extends LivingEntity & IFactionRelatedEntity & SmartBrainOwner<T>> extends VariantEntity implements IFactionRelatedEntity, SmartBrainOwner<T>{
	
	private final WeakReferenceLazyLoadField<EntityFactionInformation> FACTION_INFORMATION = new WeakReferenceLazyLoadField<>(this::getEntityFactionInformation);

	public FactionEntity(EntityType<? extends VariantEntity> pEntityType, Level pLevel) {
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
	
	public final List<? extends ExtendedSensor<? extends T>> FACTION_ENTITY_SENSORS = createSensorList((T) this);
	protected List<? extends ExtendedSensor<? extends T>> createSensorList(T entity) {
		return ObjectArrayList.of(
				new NearbyPlayersSensor<T>()
					.setPredicate((player, self) -> !self.isAlliedTo(player) && self.isEnemy(player)),
				new NearbyLivingEntitySensor<T>()
					.setPredicate((target, self) -> !self.isAlliedTo(target) && self.isEnemy(target)),
				new NearbyAlliesSensor<T>()
					.setPredicate((ally, self) -> self.isAlliedTo(ally) || self.isAlly(ally))
		);
	}

	@Override
	public List<? extends ExtendedSensor<? extends T>> getSensors() {
		return this.FACTION_ENTITY_SENSORS;
	}
	
	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		
		tickBrain((T)this);
	}
	
	@Override
	protected Provider<?> brainProvider() {
		return new SmartBrainProvider(this, true, false);
	}

}
