package team.cqr.cqrepoured.entity.ai.sensor;

import java.util.function.BiPredicate;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.GenericAttackTargetSensor;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.IHasFaction;
import team.cqr.cqrepoured.init.CQRSensors;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.ISupportWeapon;

public class FactionBasedAttackTargetSensor<E extends AbstractEntityCQR & IHasFaction> extends GenericAttackTargetSensor<E> {

	@Override
	protected BiPredicate<LivingEntity, E> predicate() {
		return FactionBasedAttackTargetSensor::isTargetableUsingFaction;
	}
	
	@Override
	protected LivingEntity testForEntity(E entity) {
		NearestVisibleLivingEntities matcher = (NearestVisibleLivingEntities) BrainUtils.getMemory(entity,
				(MemoryModuleType<NearestVisibleLivingEntities>) SBLMemoryTypes.NEAREST_VISIBLE_LIVING_ENTITIES.get());
		if(matcher == null) {
			return null;
		}
		final E owner = entity;
		matcher.sort((entryA, entryB) -> {
			double distA = owner.distanceToSqr(entryA);
			double distB = owner.distanceToSqr(entryB);
			
			return (int) Math.round(distA - distB);
		});
		return matcher == null ? null : this.findMatches(entity, matcher);
	}
	
	public static <E extends AbstractEntityCQR & IHasFaction> boolean isTargetableUsingFaction(LivingEntity target, E factionHolder) {
		if(FactionBasedAttackTargetSensor.canTargetAlly(factionHolder)) {
			return FactionBasedAttackTargetSensor.isSuitableTargetAlly(target, factionHolder);
		}
		return FactionBasedAttackTargetSensor.isSuitableTargetEnemy(target, factionHolder);
	}
	
	public static <E extends AbstractEntityCQR & IHasFaction> boolean canTargetAlly(E factionHolder) {
		final ItemStack mainHand = factionHolder.getMainHandItem();
		if(mainHand != null && !mainHand.isEmpty()) {
			Item item = mainHand.getItem();
			return item instanceof ISupportWeapon<?> || item instanceof IFakeWeapon<?>;
		}
		return false;
	}
	
	public static <E extends AbstractEntityCQR & IHasFaction> boolean isSuitableTargetAlly(LivingEntity possibleTarget, E factionHolder) {
		Faction faction = factionHolder.getFaction();
		if (faction == null) {
			return false;
		}
		if (!TargetUtil.isAllyCheckingLeaders(factionHolder, possibleTarget)) {
			return false;
		}
		if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
			return false;
		}
		if (!factionHolder.isInSightRange(possibleTarget)) {
			return false;
		}
		return factionHolder.getSensing().canSee(possibleTarget);
	}

	public static <E extends AbstractEntityCQR & IHasFaction> boolean isSuitableTargetEnemy(LivingEntity possibleTarget, E factionHolder) {
		if (!TargetUtil.isEnemyCheckingLeaders(factionHolder, possibleTarget)) {
			return false;
		}
		if (!factionHolder.getSensing().canSee(possibleTarget)) {
			return false;
		}
		if (factionHolder.isInAttackReach(possibleTarget)) {
			return true;
		}
		if (factionHolder.isEntityInFieldOfView(possibleTarget)) {
			return factionHolder.isInSightRange(possibleTarget);
		}
		return !possibleTarget.isCrouching() && factionHolder.distanceToSqr(possibleTarget) < 12.0D * 12.0D;
	}
	
	@Override
	public SensorType<? extends ExtendedSensor<?>> type() {
		return CQRSensors.FACTION_ATTACK_TARGET.get();
	}
	
}
