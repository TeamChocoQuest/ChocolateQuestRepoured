package team.cqr.cqrepoured.event.capability;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.electric.IDontSpreadElectrocution;
import team.cqr.cqrepoured.capability.electric.IElectricShockCapability;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;

@EventBusSubscriber(modid = CQRConstants.MODID)
public class ElectricEventHandler {

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		Entity entity = event.getTarget();
		if (!(entity instanceof LivingEntity) || !checkForCapabilityAndServerSide((LivingEntity) entity) || !(entity instanceof ServerPlayer)) {
			return;
		}
		CQRMain.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SPacketUpdateElectrocuteCapability((LivingEntity) entity));
	}

	@SubscribeEvent
	public static void onLogIn(PlayerLoggedInEvent event) {
		LivingEntity entity = event.getEntity();
		if (!checkForCapabilityAndServerSide(entity) || !(entity instanceof ServerPlayer)) {
			return;
		}
		CQRMain.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SPacketUpdateElectrocuteCapability(entity));
	}

	@SubscribeEvent
	public static void onRespawn(PlayerRespawnEvent event) {
		LivingEntity entity = event.getEntity();
		if (!checkForCapabilityAndServerSide(entity) || !(entity instanceof ServerPlayer)) {
			return;
		}
		// First, reduce the ticks
		LazyOptional<IElectricShockCapability> icapability = entity.getCapability(CQRCapabilities.ELECTRIC_SPREAD, null);

		icapability.ifPresent(cap -> cap.setRemainingTicks(-1));
		// We don't need to send the update ourselves, the capability handles it itself in the setter
		// CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability(entity), (EntityPlayerMP) entity);
	}

	@SubscribeEvent
	public static void onChangeDimension(PlayerChangedDimensionEvent event) {
		LivingEntity entity = event.getEntity();
		if (!checkForCapabilityAndServerSide(entity) || !(entity instanceof ServerPlayer)) {
			return;
		}
		CQRMain.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SPacketUpdateElectrocuteCapability(entity));
	}

	private static boolean checkForCapabilityAndServerSide(LivingEntity entity) {
		if (entity.level().isClientSide()) {
			// If we are on the remote end, we don't do anything
			return false;
		}
		if (!entity.getCapability(CQRCapabilities.ELECTRIC_SPREAD, null).isPresent()) {
			return false;
		}
		return true;
	}

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (!checkForCapabilityAndServerSide(entity)) {
			return;
		}

		// First, reduce the ticks
		LazyOptional<IElectricShockCapability> lOp = entity.getCapability(CQRCapabilities.ELECTRIC_SPREAD, null);
		lOp.ifPresent(currentCap -> {
			currentCap.reduceRemainingTicks();

			// Mechanicals can get electrocuted but don't take damage
			if (entity instanceof IMechanical || entity.getMobType() == CQRCreatureAttributes.MECHANICAL) {
				// But, if we are wet, we get damage from beign electrocuted
				if (((IMechanical) entity).canReceiveElectricDamageCurrently()) {
					currentCap.setRemainingTicks(100);
					entity.hurt(entity.level().damageSources().lightningBolt(), 2);
				}
			} else if (currentCap.isElectrocutionActive()) {
				entity.hurt(entity.level().damageSources().lightningBolt(), 1);
			}
			if (!entity.isAlive()) {
				currentCap.setTarget(null);
			}
			// Maybe you could spread to other entities?
			if (!(entity instanceof IDontSpreadElectrocution) && currentCap.canSpread() && currentCap.getTarget() == null && currentCap.getRemainignSpreads() > 0) {
				spreadElectrocute(entity, currentCap);
			} else if (currentCap.getTarget() != null) {
				if (!currentCap.getTarget().isAlive() || !entity.hasLineOfSight(currentCap.getTarget()) || entity.distanceTo(currentCap.getTarget()) > 16) {
					currentCap.setTarget(null);
				} else {
					LazyOptional<IElectricShockCapability> targetCap = currentCap.getTarget().getCapability(CQRCapabilities.ELECTRIC_SPREAD, null);
					targetCap.ifPresent(cap -> cap.setRemainingTicks(100));
				}
			}
		});
	}

	private static void spreadElectrocute(LivingEntity spreader, IElectricShockCapability sourceCap) {
		// First, get all applicable entities in range
		List<LivingEntity> entities = spreader.level().getEntitiesOfClass(LivingEntity.class, spreader.getBoundingBox().inflate(12), Predicates.and(TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED, entityLiving -> {
			if (entityLiving.getUUID().equals(sourceCap.getCasterID())) {
				return false;
			}
			if (!spreader.hasLineOfSight(entityLiving)) {
				return false;
			}
			if (spreader.distanceTo(entityLiving) > CQRConfig.SERVER_CONFIG.general.electricFieldEffectSpreadRange.get()) {
				return false;
			}
			return !TargetUtil.isAllyCheckingLeaders(spreader, entityLiving);
		}));
		if (entities.isEmpty()) {
			return;
		}
		LivingEntity chosen = entities.get(spreader.level().random.nextInt(entities.size()));
		sourceCap.setTarget(chosen);
		sourceCap.reduceSpreads();

		LazyOptional<IElectricShockCapability> lOp = chosen.getCapability(CQRCapabilities.ELECTRIC_SPREAD, null);
		lOp.ifPresent(targetCap -> {
			targetCap.setRemainingTicks(100);
			targetCap.setCasterID(sourceCap.getCasterID());
			if (targetCap.getRemainignSpreads() < 0 || targetCap.getRemainignSpreads() >= sourceCap.getRemainignSpreads()) {
				targetCap.setRemainingSpreads(sourceCap.getRemainignSpreads() - 1);
			}
		});
	}

}
